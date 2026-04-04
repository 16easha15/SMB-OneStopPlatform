package com.example.smbone.models;

import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;

import java.util.Set;

public class FabricUser implements User {

    private String name;
    private String mspId;
    private Enrollment enrollment;

    public FabricUser(
            String name,
            String mspId,
            Enrollment enrollment
    ){

        this.name = name;
        this.mspId = mspId;
        this.enrollment = enrollment;

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Set<String> getRoles() {
        return null;
    }

    @Override
    public String getAccount() {
        return null;
    }

    @Override
    public String getAffiliation() {
        return null;
    }

    @Override
    public Enrollment getEnrollment() {
        return enrollment;
    }

    @Override
    public String getMspId() {
        return mspId;
    }

}