package org.web3j.console;

import org.junit.Assert;
import org.junit.Test;
import org.web3j.TempFileProvider;

import java.io.File;

public class SolidityProjectCreatorTester extends TempFileProvider {

    SolidityProjectCreator solidityProjectCreator = new SolidityProjectCreator();

    @Test
    public void testGetSolidityFile() throws Exception {
        super.setUp();
        File tempDestination = new File(tempDirPath+File.separator+"Test");
        File tempFile = File.createTempFile("Solidity",".sol", new File(tempDirPath));
        System.out.println(tempFile.getName() + "File Name");
        System.out.println(tempDestination.getAbsolutePath());
        solidityProjectCreator.getSolidityFile(tempFile.getPath(),tempDestination.getPath());
        System.out.println(tempFile+File.separator+"Test"+File.separator+tempFile.getName());
        Assert.assertTrue(new File(tempFile+File.separator+"Test"+File.separator+tempFile.getName()).exists());
    }

}
