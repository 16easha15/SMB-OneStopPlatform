package com.example.smbone.util;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

import java.io.StringReader;
import java.security.PrivateKey;

public class CryptoUtil {

    public static PrivateKey getPrivateKey(String keyPem)
            throws Exception {

        PEMParser pemParser =
                new PEMParser(new StringReader(keyPem));

        Object object = pemParser.readObject();

        JcaPEMKeyConverter converter =
                new JcaPEMKeyConverter();

        if (object instanceof PrivateKeyInfo) {

            return converter.getPrivateKey(
                    (PrivateKeyInfo) object
            );

        }

        throw new RuntimeException(
                "Unsupported key format"
        );

    }

}