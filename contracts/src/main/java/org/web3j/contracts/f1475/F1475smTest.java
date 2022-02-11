package org.web3j.contracts.f1475;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Numeric;

public class F1475smTest {
//    public static void main(String[] args) {
//        Web3j web3j =
//                Web3j.build(
//                        new HttpService(
//                                "https://ropsten.infura.io/v3/007b2b584fc44030bb3ee8470ff04dd1"));
//        Credentials credentials =
//                Credentials.create("ebf575f95890c2140f3189433328574033304f542a2b9eff71e1901caa3f46b1");
//
//        F1475sm test = new F1475sm("0x2f9f0701EB1335F523005655E3150481BCfda005", web3j, credentials, new DefaultGasProvider());
//
//        System.out.println(test);
//0x3132333435363738393000000000000000000000000000000000000000000000
//    }


    public static void main(String[] args) {
        Web3j web3j =
                Web3j.build(
                        new HttpService(
                                "HTTP://127.0.0.1:7545"));
        Credentials credentials =
                Credentials.create("936cdb173619a9e8bc3ea5d68f56cbea7121758698ed2bcf6c7608068a01f0de");

        F1475sm test = new F1475sm("0xFE60B1F61AD4a8f579D9332Dbf36D88be0DF7756", web3j, credentials, new DefaultGasProvider());
        try {
            String result = Numeric.toHexStringNoPrefix(test.encodeMixFunc1().send());
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(test);

    }
}
