package org.web3j.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.account.Account;
import org.web3j.protocol.account.CompiledContract;
import org.web3j.protocol.core.methods.response.AbiDefinition;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;
import org.web3j.utils.Strings;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PermissionSystemTest {
    private static Random random;
    private static BigInteger quota;
    private final static Web3j service = Web3j.build(new HttpService("http://127.0.0.1:1337"));

    private Account superAdmin;
    private Account sender;
    private CompiledContract permissionSystem;
    private CompiledContract permissionManager;
    private String permissionManagerContractAddress;
    private String permissionSystemContractAddress;

    static {
        random = new Random(System.currentTimeMillis());
        quota = BigInteger.valueOf(1000000);
    }

    private static BigInteger randomNonce() {
        return BigInteger.valueOf(Math.abs(random.nextLong()));
    }

    private static TransactionReceipt waitToGetReceipt(String hash) throws Exception {
        Thread.sleep(10_000);
        return service.ethGetTransactionReceipt(hash).send().getTransactionReceipt().get();
    }

    public PermissionSystemTest(Config config)
            throws Exception{
        this.sender = new Account(config.senderPrivateKey, service);
        this.superAdmin = new Account(config.superAdminPrivateKey, service);
        this.permissionSystem = new CompiledContract(new File(config.permissionSystemPath));
        this.permissionManager = new CompiledContract(new File(config.permissionManagerPath));
        this.permissionManagerContractAddress = config.permissionManagerContractAddress;
        this.permissionSystemContractAddress = config.permissionSystemContractAddress;
    }

    public void grantPermission(String address, BigInteger permission) throws Exception {
        System.out.println("grant " + permission.longValue() + " permission to " + address);
        AbiDefinition grantPermissionAbi = permissionManager.getFunctionAbi("grantPermission", 2);
        EthSendTransaction ethSendTransaction = (EthSendTransaction) sender.callContract(permissionManagerContractAddress,
                grantPermissionAbi, randomNonce(), quota, address, permission);
        TransactionReceipt receipt = waitToGetReceipt(ethSendTransaction.getSendTransactionResult().getHash());
        if (receipt.getErrorMessage() != null) {
            System.out.println("grant permission failed because of " + receipt.getErrorMessage());
            System.exit(1);
        }
        System.out.println(address + " have " + queryPermission(address) + " permissions");
    }

    public BigInteger queryPermission(String address) throws Exception {
        AbiDefinition queryPermissionAbi = permissionManager.getFunctionAbi("queryPermission", 1);
        return (BigInteger) sender.callContract(permissionManagerContractAddress, queryPermissionAbi, null, null, address);
    }

    public void revokePermission(String address, BigInteger permission) throws Exception {
        System.out.println("revoke " + permission.longValue() + " permission from " + address);
        AbiDefinition revokePermissionAbi = permissionManager.getFunctionAbi("revokePermission", 2);
        EthSendTransaction ethSendTransaction = (EthSendTransaction) sender.callContract(permissionManagerContractAddress,
                revokePermissionAbi, randomNonce(), quota, address, permission);
        TransactionReceipt receipt = waitToGetReceipt(ethSendTransaction.getSendTransactionResult().getHash());
        if (receipt.getErrorMessage() != null) {
            System.out.println("revoke permission failed because of " + receipt.getErrorMessage());
            System.exit(1);
        }
        System.out.println(address + " have " + queryPermission(address) + " permissions");
    }

    public void querySuperAdmin() throws Exception {
        AbiDefinition querySuperAdminAbi = permissionSystem.getFunctionAbi("querySuperAdmin", 0);
        String address = (String) sender.callContract(permissionSystemContractAddress, querySuperAdminAbi, null, null);
        System.out.println("superAdmin is " + address);
    }

    public void newGroup(byte[] group, byte[] name, String[] users, boolean subSwitch, BigInteger op, byte[] role, String profile)
            throws Exception {
        AbiDefinition newGroupAbi = permissionSystem.getFunctionAbi("newGroup", 7);
        EthSendTransaction ethSendTransaction = (EthSendTransaction) superAdmin.callContract(permissionSystemContractAddress,
                newGroupAbi, randomNonce(), quota, group, name, users,
                subSwitch, op, role, profile);
        TransactionReceipt receipt = waitToGetReceipt(ethSendTransaction.getSendTransactionResult().getHash());
        if (receipt.getErrorMessage() != null) {
            System.out.println("create new group failed because of " + receipt.getErrorMessage());
            System.exit(1);
        }
        System.out.println("create new group success");
    }

    public void queryGroup(String address) throws Exception {
        AbiDefinition queryGroupsAbi = permissionSystem.getFunctionAbi("queryGroups", 1);
        Object queryResult = superAdmin.callContract(permissionSystemContractAddress,
                queryGroupsAbi, null, null, address);
        List<Object> groups = (List<Object>) queryResult;
        for (Object group: groups) {
            byte[] g = (byte[]) group;
            System.out.println(address + " belong to " + Strings.hexStringToAscii(Numeric.toHexString(g).substring(2)));
        }
    }

    public void queryUsers(byte[] group) throws Exception {
        AbiDefinition queryUsersAbi = permissionSystem.getFunctionAbi("queryUsers", 1);
        Object queryResult = superAdmin.callContract(permissionSystemContractAddress,
                queryUsersAbi, null, null, group);
        List<String> users = (List<String>) queryResult;
        System.out.println("this group have " + users.size() + " users:");
        for (String address: users) {
            System.out.println(address);
        }
    }

    public void querySubSwitch(byte[] group) throws Exception {
        AbiDefinition querySubSwitchAbi = permissionSystem.getFunctionAbi("querySubSwitch", 1);
        Boolean queryResult = (Boolean) superAdmin.callContract(permissionSystemContractAddress,
                querySubSwitchAbi, null, null, group);
        if (queryResult) {
            System.out.println("this group have sub switch");
        } else {
            System.out.println("this group have no sub switch");
        }
    }

    public void queryProfile(byte[] group) throws Exception {
        AbiDefinition queryProfileAbi = permissionSystem.getFunctionAbi("queryProfile", 1);
        String queryResult = (String) superAdmin.callContract(permissionSystemContractAddress,
                queryProfileAbi, null, null, group);
        System.out.println("the profile of this group is: " + queryResult);
    }

    public void modifySubSwitch(byte[] group, byte[] resource, byte[] role, boolean newSubSwitch) throws Exception {
        System.out.println("modify sub switch to " + newSubSwitch);
        AbiDefinition modifySubSwitchAbi = permissionSystem.getFunctionAbi("modifySubSwitch", 4);
        EthSendTransaction ethSendTransaction = (EthSendTransaction) superAdmin.callContract(permissionSystemContractAddress,
                modifySubSwitchAbi, randomNonce(), quota, group, resource, role, newSubSwitch);
        TransactionReceipt receipt = waitToGetReceipt(ethSendTransaction.getSendTransactionResult().getHash());
        if (receipt.getErrorMessage() != null) {
            System.out.println("modify sub switch failed because of " + receipt.getErrorMessage());
            System.exit(1);
        }
    }

    public void modifyGroupName(byte[] group, byte[] newGroup, byte[] resource, byte[] role) throws Exception {
        System.out.println("modify group name");
        AbiDefinition modifyGroupNameAbi = permissionSystem.getFunctionAbi("modifyGroupName", 4);
        EthSendTransaction ethSendTransaction = (EthSendTransaction) superAdmin.callContract(permissionSystemContractAddress,
                modifyGroupNameAbi, randomNonce(), quota, group, newGroup, resource, role);
        TransactionReceipt receipt = waitToGetReceipt(ethSendTransaction.getSendTransactionResult().getHash());
        if (receipt.getErrorMessage() != null) {
            System.out.println("modify group name failed because of " + receipt.getErrorMessage());
            System.exit(1);
        }
    }

    public void deleteGroup(byte[] group, byte[] name, byte[] role) throws Exception {
        System.out.println("delete group");
        AbiDefinition deleteGroupAbi = permissionSystem.getFunctionAbi("deleteGroup", 3);
        EthSendTransaction ethSendTransaction = (EthSendTransaction) superAdmin.callContract(permissionSystemContractAddress,
                deleteGroupAbi, randomNonce(), quota, group, name, role);
        TransactionReceipt receipt = waitToGetReceipt(ethSendTransaction.getSendTransactionResult().getHash());
        if (receipt.getErrorMessage() != null) {
            System.out.println("delete group failed because of " + receipt.getErrorMessage());
            System.exit(1);
        }
    }

    public void queryAllGroups() throws Exception {
        AbiDefinition queryAllGroupsAbi = permissionSystem.getFunctionAbi("queryAllGroups", 0);
        Object queryResult = superAdmin.callContract(permissionSystemContractAddress, queryAllGroupsAbi, null, null);
        List<byte[]> groups = (List<byte[]>) queryResult;
        System.out.println("we have " + groups.size() + " groups");
        for (byte[] group: groups) {
            System.out.println(Strings.hexStringToAscii(Numeric.toHexString(group).substring(2)));
        }
    }

    public void newRole(byte[] group, byte[] name, byte[] role, List<byte[]> newPermissions, BigInteger op) throws Exception {
        System.out.println("create new role");
        AbiDefinition newRoleAbi = permissionSystem.getFunctionAbi("newRole", 5);
        EthSendTransaction ethSendTransaction = (EthSendTransaction) superAdmin.callContract(permissionSystemContractAddress,
                newRoleAbi, randomNonce(), quota, group, name, role, newPermissions, op);
        TransactionReceipt receipt = waitToGetReceipt(ethSendTransaction.getSendTransactionResult().getHash());
        if (receipt.getErrorMessage() != null) {
            System.out.println("create new role failed because of " + receipt.getErrorMessage());
            System.exit(1);
        }
    }

    public void queryPermissions(byte[] role) throws Exception {
        AbiDefinition queryPermissionAbi = permissionSystem.getFunctionAbi("queryPermissions", 1);
        Object queryResult = superAdmin.callContract(permissionSystemContractAddress, queryPermissionAbi, null, null, role);
        List<byte[]> permissions = (List<byte[]>) queryResult;
        System.out.println("we have " + permissions.size() + " permissions");
        for (byte[] permission: permissions) {
            System.out.println(Strings.hexStringToAscii(Numeric.toHexString(permission).substring(2)));
        }
    }

    public void modifyRoleName(byte[] name, byte[] newName, byte[] group, byte[] resource) throws Exception {
        AbiDefinition modifyRoleNameAbi = permissionSystem.getFunctionAbi("modifyRoleName", 4);
        EthSendTransaction ethSendTransaction = (EthSendTransaction) superAdmin.callContract(permissionSystemContractAddress,
                modifyRoleNameAbi, randomNonce(), quota, name, newName, group, resource);
        TransactionReceipt receipt = waitToGetReceipt(ethSendTransaction.getSendTransactionResult().getHash());
        if (receipt.getErrorMessage() != null) {
            System.out.println("modify role name failed because of " + receipt.getErrorMessage());
            System.exit(1);
        }
    }

    public void queryAllRoles() throws Exception {
        AbiDefinition queryAllRolesAbi = permissionSystem.getFunctionAbi("queryAllRoles", 0);
        Object queryResult = superAdmin.callContract(permissionSystemContractAddress, queryAllRolesAbi, null, null);
        List<byte[]> roles = (List<byte[]>) queryResult;
        System.out.println("we have " + roles.size() + " roles");
        for (byte[] role: roles) {
            System.out.println(Strings.hexStringToAscii(Numeric.toHexString(role).substring(2)));
        }
    }

    public void deleteRole(byte[] role, byte[] group, byte[] resource) throws Exception {
        AbiDefinition deleteRoleAbi = permissionSystem.getFunctionAbi("deleteRole", 3);
        EthSendTransaction ethSendTransaction = (EthSendTransaction) superAdmin.callContract(permissionSystemContractAddress,
                deleteRoleAbi, randomNonce(), quota, role, group, resource);
        TransactionReceipt receipt = waitToGetReceipt(ethSendTransaction.getSendTransactionResult().getHash());
        if (receipt.getErrorMessage() != null) {
            System.out.println("delete role failed because of " + receipt.getErrorMessage());
            System.exit(1);
        }
    }

    public void setAuthorization(byte[] group, byte[] role, byte[] resource) throws Exception {
        AbiDefinition setAuthorizationAbi = permissionSystem.getFunctionAbi("setAuthorization", 3);
        EthSendTransaction ethSendTransaction = (EthSendTransaction) superAdmin.callContract(permissionSystemContractAddress,
                setAuthorizationAbi, randomNonce(), quota, group, role, resource);
        TransactionReceipt receipt = waitToGetReceipt(ethSendTransaction.getSendTransactionResult().getHash());
        if (receipt.getErrorMessage() != null) {
            System.out.println("set authorization failed because of " + receipt.getErrorMessage());
            System.exit(1);
        }
    }

    public void queryRoles(byte[] group) throws Exception {
        AbiDefinition queryRolesAbi = permissionSystem.getFunctionAbi("queryRoles", 1);
        Object queryResult = superAdmin.callContract(permissionSystemContractAddress, queryRolesAbi, null, null, group);
        List<byte[]> roles = (List<byte[]>) queryResult;
        System.out.println(Strings.hexStringToAscii(Numeric.toHexString(group).substring(2)) + " have " + roles.size() + " roles");
        for (byte[] role: roles) {
            System.out.println(Strings.hexStringToAscii(Numeric.toHexString(role).substring(2)));
        }
    }

    public void cancelAuthorization(byte[] group, byte[] role, byte[] resource) throws Exception {
        AbiDefinition cancelAuthorizationAbi = permissionSystem.getFunctionAbi("cancelAuthorization", 3);
        EthSendTransaction ethSendTransaction = (EthSendTransaction) superAdmin.callContract(permissionSystemContractAddress,
                cancelAuthorizationAbi, randomNonce(), quota, group, role, resource);
        TransactionReceipt receipt = waitToGetReceipt(ethSendTransaction.getSendTransactionResult().getHash());
        if (receipt.getErrorMessage() != null) {
            System.out.println("cancel authorization failed because of " + receipt.getErrorMessage());
            System.exit(1);
        }
    }

    private static class Config {
        public String permissionSystemPath;
        public String permissionManagerPath;
        public String senderPrivateKey;
        public String superAdminPrivateKey;
        public String superAdminAddress;
        public String permissionSystemContractAddress;
        public String permissionManagerContractAddress;

        public static Config load(File file) throws IOException {
            ObjectMapper m = new ObjectMapper();
            return m.readValue(file, Config.class);
        }
    }

    public static void main(String[] args) throws Exception {
        Config config = Config.load(new File(args[0]));

        PermissionSystemTest permissionSystemTest = new PermissionSystemTest(config);
        permissionSystemTest.grantPermission(config.superAdminAddress, BigInteger.valueOf(1));
        permissionSystemTest.revokePermission(config.superAdminAddress, BigInteger.valueOf(1));
        permissionSystemTest.querySuperAdmin();
        permissionSystemTest.grantPermission(config.superAdminAddress, BigInteger.valueOf(1));
        String[] testAddress = {"0x1a702a25c6bca72b67987968f0bfb3a3213c5600",
                                "0x1a702a25c6bca72b67987968f0bfb3a3213c5601",
                                "0x1a702a25c6bca72b67987968f0bfb3a3213c5602"};
        byte[] testGroup = Strings.asciiToHex("test_group", 32);
        byte[] empty = Strings.asciiToHex("", 32);
        permissionSystemTest.newGroup(Strings.asciiToHex("zz", 32), testGroup, testAddress,
                true, BigInteger.valueOf(0), empty, "This is a test group");
        permissionSystemTest.queryGroup(testAddress[0]);
        permissionSystemTest.queryUsers(testGroup);
        permissionSystemTest.querySubSwitch(testGroup);
        permissionSystemTest.queryProfile(testGroup);
        permissionSystemTest.modifySubSwitch(testGroup, testGroup, empty, false);
        permissionSystemTest.querySubSwitch(testGroup);
        byte[] testNewGroup = Strings.asciiToHex("test_group_new", 32);
        permissionSystemTest.modifyGroupName(testGroup, testNewGroup, empty, Strings.asciiToHex("", 32));
        permissionSystemTest.queryGroup(testAddress[0]);
        permissionSystemTest.queryAllGroups();
        permissionSystemTest.deleteGroup(Strings.asciiToHex("zz", 32), testNewGroup, empty);
        permissionSystemTest.queryAllGroups();
        byte[] role = Strings.asciiToHex("test_role", 32);
        List<byte[]> newPermissions = new ArrayList<>();
        newPermissions.add(Strings.asciiToHex("DeleteRole", 32));
        newPermissions.add(Strings.asciiToHex("UpdateRole", 32));
        permissionSystemTest.newRole(Strings.asciiToHex("test", 32), role, empty, newPermissions, BigInteger.valueOf(0));
        permissionSystemTest.queryPermissions(role);
        permissionSystemTest.queryAllRoles();
        byte[] newRole = Strings.asciiToHex("test_role_new", 32);
        permissionSystemTest.modifyRoleName(role, newRole, empty, empty);
        permissionSystemTest.queryAllRoles();
        permissionSystemTest.queryPermissions(newRole);
        permissionSystemTest.deleteRole(newRole, Strings.asciiToHex("test", 32), empty);
        permissionSystemTest.queryAllRoles();
        permissionSystemTest.setAuthorization(testGroup, role, empty);
        permissionSystemTest.queryRoles(testGroup);
        permissionSystemTest.cancelAuthorization(testGroup, role, empty);
        permissionSystemTest.queryRoles(testGroup);

        System.exit(0);
    }
}
