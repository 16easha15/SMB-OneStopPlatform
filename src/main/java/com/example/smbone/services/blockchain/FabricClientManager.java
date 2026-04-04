package com.example.smbone.services.blockchain;

import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.security.CryptoSuite;

public class FabricClientManager {

    private static HFClient hfClient;

    public static HFClient getClient() throws Exception {

        if(hfClient != null){
            return hfClient;
        }

        hfClient = HFClient.createNewInstance();

        CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();

        hfClient.setCryptoSuite(cryptoSuite);

        return hfClient;
    }

}