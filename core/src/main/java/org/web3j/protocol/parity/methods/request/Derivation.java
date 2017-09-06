package org.web3j.protocol.parity.methods.request;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Derivation used in following methods.
 * <ol>
 *     <li>parity_deriveAddressHash</li>
 *     <li>parity_deriveAddressIndex</li>
 * </ol>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Derivation {
    
    private Integer index;
    private String hash;
    private String type;
    
    private Derivation(Integer index, String hash, String type) {
        this.index = index;
        this.hash = hash;
        this.type = type;
    }
    
    public static Derivation createDerivationHash(String hash, String type) {
        
        return new Derivation(null, hash, type);
    }
    
    public static Derivation createDerivationIndex(Integer index, String type) {
        
        return new Derivation(index, null, type);
    }

    public Integer getIndex() {
        return index;
    }

    public String getHash() {
        return hash;
    }

    public String getType() {
        return type;
    }    
}
