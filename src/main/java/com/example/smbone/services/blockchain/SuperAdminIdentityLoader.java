package com.example.smbone.services.blockchain;

import com.example.smbone.models.FabricUser;
import com.example.smbone.models.X509Enrollment;
import com.example.smbone.util.CryptoUtil;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;

public class SuperAdminIdentityLoader {

    public static FabricUser load()
            throws Exception {

        String cert = readFile(
                "blockchain/admin/cert.pem"
        );

        String keyPem = readFile(
                "blockchain/admin/key.pem"
        );

        PrivateKey privateKey =
                CryptoUtil.getPrivateKey(keyPem);

        X509Enrollment enrollment =
                new X509Enrollment(
                        privateKey,
                        cert
                );

        return new FabricUser(
                "smbadmin",
                "SMBMSP",
                enrollment
        );

    }

    private static String readFile(String path)
            throws Exception {

        InputStream is =
                SuperAdminIdentityLoader.class
                        .getClassLoader()
                        .getResourceAsStream(path);

        return new String(
                is.readAllBytes(),
                StandardCharsets.UTF_8
        );

    }

}