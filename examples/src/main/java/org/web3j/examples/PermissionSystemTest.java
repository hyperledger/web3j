package org.web3j.examples;

import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.CitaTransactionManager;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PermissionSystemTest {
    private static Web3j service;
    private static Random random;
    private static BigInteger quota = BigInteger.valueOf(1000000);

    static {
        service = Web3j.build(new HttpService("http://127.0.0.1:1337"));
        random = new Random(System.currentTimeMillis());
    }

    static long currentBlockNumber() throws Exception {
        return service.ethBlockNumber().send().getBlockNumber().longValue();
    }

    static BigInteger getValidUntilBlock() throws Exception {
        return BigInteger.valueOf(currentBlockNumber() + 50);
    }

    static BigInteger newNonce() {
        return BigInteger.valueOf(Math.abs(random.nextLong()));
    }

    private static byte[] asciiToHex(String asciiValue)
    {
        char[] chars = asciiValue.toCharArray();
        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < chars.length; i++)
        {
            hex.append(Integer.toHexString((int) chars[i]));
        }

        String hexStr = hex.toString() + "".join("", Collections.nCopies(32 - (hex.length()/2), "00"));
        System.out.println("hex str: " + hexStr);
        return Numeric.hexStringToByteArray(hexStr);
    }

    public static void main(String[] args) throws Exception {
        String adminPrivKey = "b7eb833f2ac19d3dabf4375146a7ce0e983b9997a02557be05f224e22797419e";
        Credentials admin = Credentials.create(adminPrivKey);
        CitaTransactionManager adminManager = new CitaTransactionManager(service, admin);

        String senderPrivKey = "866c936ff332228948bdefc15b1877c88e0effce703ee6de898cffcafe9bbe25";
        Credentials sender = Credentials.create(senderPrivKey);
        CitaTransactionManager senderManager = new CitaTransactionManager(service, sender);

        // permission system contract already deployed when startup
        String permissionSystemContractAddress = "0x00000000000000000000000000000000013241a5";
        String permissionManagerContractAddress = "0x00000000000000000000000000000000013241a4";
        PermissionSystem permissionSystem = PermissionSystem.load(permissionSystemContractAddress, service, adminManager);
        PermissionManager permissionManager = PermissionManager.load(permissionManagerContractAddress, service, senderManager);

        // set send transaction permission
        System.out.println("set send transaction permission");
        TransactionReceipt tx1 = permissionManager.grantPermission(admin.getAddress(), BigInteger.valueOf(1), quota, newNonce(), getValidUntilBlock()).send();
        if (tx1.getErrorMessage() != null) {
            System.out.println("set send transaction permission failed because of " + tx1.getErrorMessage());
            System.exit(1);
        }

        BigInteger permission = permissionManager.queryPermission(admin.getAddress()).send();
        System.out.println("we have permission " + permission.longValue());

        // new group test
        System.out.println("add new group test_group");
        ArrayList<String> newUsers = new ArrayList<String>(3);
        newUsers.add("0x1a702a25c6bca72b67987968f0bfb3a3213c5600");
        newUsers.add("0x1a702a25c6bca72b67987968f0bfb3a3213c5601");
        newUsers.add("0x1a702a25c6bca72b67987968f0bfb3a3213c5602");
        TransactionReceipt tx2 = permissionSystem.newGroup(
                asciiToHex("zz"), asciiToHex("test_group"), newUsers, true, BigInteger.valueOf(0),
                asciiToHex(""), "This is a test group", quota, newNonce(), getValidUntilBlock()).send();

        if (tx2.getErrorMessage() != null) {
            System.out.println("create new group failed because of " + tx2.getErrorMessage());
            System.exit(1);
        }

        List<Bytes32> groups = permissionSystem.queryGroups(newUsers.get(0)).send();
        System.out.println("group: " + Numeric.toHexString(groups.get(0).getValue()));
    }
}
