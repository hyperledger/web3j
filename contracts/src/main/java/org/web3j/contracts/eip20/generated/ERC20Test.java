package org.web3j.contracts.eip20.generated;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ERC20Test {
    private ERC20 erc20;

    @Test
    public void testFunctionBalanceOf(){
        assertEquals(1, erc20.balanceOf(0x23971521AB578f4bf58e1C6E35fEb289AcB66bcf));
    }
}