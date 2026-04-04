package com.example.smbone.services.blockchain;

import org.hyperledger.fabric.gateway.Identities;
import org.hyperledger.fabric.sdk.Enrollment;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;

public class MSPGeneratorService {

    public static String getBasePath() {
        return System.getProperty("user.home")
                + "/smb/blockchain";
    }

    public static String getPeerPath() {
        return getBasePath() + "/org/peer";
    }

    public static String getAdminPath() {
        return getBasePath() + "/org/admin";
    }

    public static void createStructure() {
        new File(getBasePath()).mkdirs();
        new File(getAdminPath()).mkdirs();
        new File(getPeerPath()).mkdirs();
        new File(getPeerPath() + "/msp").mkdirs();
        new File(getPeerPath() + "/tls").mkdirs();
    }

    private static void writeFile(
            String path,
            String content
    ) throws Exception {
        File file = new File(path);
        file.getParentFile().mkdirs();
        Files.writeString(
                file.toPath(),
                content
        );
    }

    public static void writePeerMSP(
            Enrollment enrollment
    ) throws Exception {
        String mspPath =
                getPeerPath() + "/msp";
        writeFile(
                mspPath +
                        "/signcerts/cert.pem",
                enrollment.getCert()
        );

        String privateKeyPem = Identities.toPemString(
                enrollment.getKey()
        );
        writeFile(

                mspPath +
                        "/keystore/key.pem",
                privateKeyPem
        );
    }

    public static void writePeerTLS(
            Enrollment tlsEnrollment
    ) throws Exception {
        String tlsPath =
                getPeerPath() + "/tls";
        writeFile(
                tlsPath +
                        "/server.crt",
                tlsEnrollment.getCert()
        );
        String keyPem =
                Identities.toPemString(
                        tlsEnrollment.getKey()
                );
        writeFile(
                tlsPath +
                        "/server.key",
                keyPem
        );
    }

    public static void writeCACert()
            throws Exception {
        InputStream is =
                MSPGeneratorService.class
                        .getClassLoader()
                        .getResourceAsStream(
                                "blockchain/network/ca-cert.pem"
                        );
        String cert =
                new String(
                        is.readAllBytes()
                );
        // MSP CA cert
        writeFile(
                getPeerPath() +
                        "/msp/cacerts/ca-cert.pem",
                cert
        );
        // MSP TLS CA cert ⭐ (THIS IS THE NEW PART)
        writeFile(
                getPeerPath() +
                        "/msp/tlscacerts/ca-cert.pem",
                cert
        );
        // TLS CA cert for peer TLS
        writeFile(
                getPeerPath() +
                        "/tls/ca.crt",
                cert
        );
    }

    public static void writeConfigYaml()
            throws Exception {

        String yaml = """
                NodeOUs:
                 Enable: true
                
                 ClientOUIdentifier:
                  Certificate: cacerts/ca-cert.pem
                  OrganizationalUnitIdentifier: client
                
                 PeerOUIdentifier:
                  Certificate: cacerts/ca-cert.pem
                  OrganizationalUnitIdentifier: peer
                
                 AdminOUIdentifier:
                  Certificate: cacerts/ca-cert.pem
                  OrganizationalUnitIdentifier: admin
                
                 OrdererOUIdentifier:
                  Certificate: cacerts/ca-cert.pem
                  OrganizationalUnitIdentifier: orderer
                """;

        writeFile(
                getPeerPath() +
                        "/msp/config.yaml",
                yaml
        );
    }
}