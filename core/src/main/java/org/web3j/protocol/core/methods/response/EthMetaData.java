package org.web3j.protocol.core.methods.response;

import org.web3j.abi.datatypes.Address;
import org.web3j.protocol.core.Response;

import java.util.List;

public class EthMetaData extends Response<EthMetaData> {

    private int chainId;
    private String chainName;
    private String operator;    //运营方
    private String website;     //网站
    private String genesisTimestamp;
    private String basicToken;
    private Address[] validators;
    private int blockInterval;

    public int getChainId() {
        return chainId;
    }

    public void setChainId(int chainId) {
        this.chainId = chainId;
    }

    public String getChainName() {
        return chainName;
    }

    public void setChainName(String chainName) {
        this.chainName = chainName;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getGenesisTimestamp() { return genesisTimestamp; }

    public void setGenesisTimestamp(String genesisTimestamp) { this.genesisTimestamp = genesisTimestamp; }

    public String getBasicToken() {
        return basicToken;
    }

    public void setBasicToken(String basicToken) {
        this.basicToken = basicToken;
    }

    public Address[] getValidators() { return validators; }

    public void setValidators(Address[] validators) { this.validators = validators; }

    public int getBlockInterval() {
        return blockInterval;
    }

    public void setBlockInterval(int blockInterval) {
        this.blockInterval = blockInterval;
    }


    public EthMetaData getEthMetaData() {
        return getResult();
    }

}
