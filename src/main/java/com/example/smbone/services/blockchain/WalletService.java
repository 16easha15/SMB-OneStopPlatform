package com.example.smbone.services.blockchain;

import org.hyperledger.fabric.gateway.*;
import org.hyperledger.fabric.sdk.Enrollment;

import java.nio.file.Path;
import java.nio.file.Paths;

public class WalletService {

    private static final String WALLET_DIR =
            System.getProperty("user.home") +
                    "/smb/blockchain/wallet";

    public static Wallet getWallet()
            throws Exception {

        Path walletPath =
                Paths.get(WALLET_DIR);

        return Wallets
                .newFileSystemWallet(walletPath);

    }

    public static void storeIdentity(

            String identityName,
            Enrollment enrollment,
            String mspId

    ) throws Exception {

        Wallet wallet = getWallet();

        Identity identity =
                Identities.newX509Identity(
                        mspId,
                        enrollment
                );

        wallet.put(
                identityName,
                identity
        );

        System.out.println(
                "Identity stored: " +
                        identityName
        );

    }

}