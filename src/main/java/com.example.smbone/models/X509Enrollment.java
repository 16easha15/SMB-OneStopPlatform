package com.example.smbone.models;

import org.hyperledger.fabric.sdk.Enrollment;

import java.security.PrivateKey;

public class X509Enrollment implements Enrollment {

    PrivateKey key;
    String cert;

    public X509Enrollment(
            PrivateKey key,
            String cert
    ){

        this.key = key;
        this.cert = cert;

    }

    @Override
    public PrivateKey getKey() {
        return key;
    }

    @Override
    public String getCert() {
        return cert;
    }

}