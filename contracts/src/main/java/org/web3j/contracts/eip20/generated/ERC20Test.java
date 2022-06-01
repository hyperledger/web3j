package org.web3j.contracts.eip20.generated;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ERC20Test {
    private ERC20 erc20;

    @Test
    public void testTypeOfSymbol(){
        assertEquals('String', erc20.symbol());
    }
}