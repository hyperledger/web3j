package org.web3j.tx;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;

/*
 * One should consider to make this class <code>final</code> such that the thread-safety is not damaged by a subclass.
 */
public final class NonblockingFastRawTransactionManager extends RawTransactionManager {

	private final AtomicReference<BigInteger> nonceHolder = new AtomicReference<>(BigInteger.valueOf(-1));

	public NonblockingFastRawTransactionManager(Web3j web3j, Credentials credentials, byte chainId) {
		super(web3j, credentials, chainId);
	}

	public NonblockingFastRawTransactionManager(Web3j web3j, Credentials credentials) {
		super(web3j, credentials);
	}

	@Override
	BigInteger getNonce() throws IOException {
		/*
		 * According to JavaDoc, <code>BigInteger</code> is immutable:
		 * "Immutable arbitrary-precision integers."
		 * 
		 * @see
		 * https://docs.oracle.com/javase/7/docs/api/java/math/BigInteger.html
		 * 
		 * The algorithm is copied from this page:
		 * https://stackoverflow.com/questions/14006495/possible-to-safely-
		 * increment-biginteger-in-a-thread-safe-way-perhaps-with-atomi
		 * 
		 * See the code of incrementAndGet() in <code>AtomicLong</code>:
		 * @see http://www.docjar.com/html/api/java/util/concurrent/atomic/AtomicLong.java.html 
		 * 
		 */

		for (;;) {
			final BigInteger currentNonce = nonceHolder.get();

			final BigInteger nextNonce;
			if (currentNonce.signum() == -1) {

				/*
				 * Are we sure that super.getNonce() returns the correct value
				 * in a thread-safe manner?
				 */
				nextNonce = super.getNonce();
			} else {
				nextNonce = currentNonce.add(BigInteger.ONE);
			}

			/*
			 * Compete with other threads to set the value to
			 * <code>nextNonce</code> if the current value is
			 * <code>currentNonce</code>. If not successful, try it in the next
			 * iteration of the loop.
			 */
			if (nonceHolder.compareAndSet(currentNonce, nextNonce)) {
				return nextNonce;
			}
		}

	}

	public BigInteger getCurrentNonce() {
		return nonceHolder.get();
	}

	public void resetNonce() throws IOException {
		/*
		 * Are we sure that super.getNonce() returns the correct value in a
		 * thread-safe manner?
		 */
		nonceHolder.set(super.getNonce());
	}

	public void setNonce(BigInteger value) {
		/*
		 * This brutally sets a value. Perhaps, there should be some kind of
		 * compareAndSet or accumulateAndSet?
		 */
		nonceHolder.set(value);
	}
}
