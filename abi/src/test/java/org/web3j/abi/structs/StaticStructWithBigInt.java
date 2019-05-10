package org.web3j.abi.structs;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.StaticStructType;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

public class StaticStructWithBigInt extends StaticStructType {

    public StaticStructWithBigInt(BigInteger data) {
        this(new Uint256(data));
    }

    public StaticStructWithBigInt(Uint256 data) {
        super(Collections.singletonList(data));
    }

    public static List<TypeReference> getTypes() {
        return Collections.singletonList(new TypeReference<Uint256>() {
        });
    }

    public Uint256 getData() {
        return (Uint256) super.getValue().get(0);
    }

    public void setData(BigInteger data) {
        final Class<Type> expectedType = super.itemTypes.get(0);

        if (!expectedType.isAssignableFrom(Uint256.class)) {
            throw new IllegalArgumentException("Attempting to change types?");
        }

        this.getValue().set(0, new Uint256(data));
    }
}