package com.example.smbone.services.blockchain;

import com.example.smbone.models.FabricUser;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.EnrollmentRequest;
import org.hyperledger.fabric_ca.sdk.HFCAAffiliation;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

public class CAService {

    private HFCAClient caClient;

    public CAService() throws Exception {

        Properties props = new Properties();

        InputStream is =
                getClass().getClassLoader()
                        .getResourceAsStream(
                                "blockchain/network/ca-cert.pem"
                        );

        File tempFile =
                File.createTempFile("ca-cert", ".pem");

        Files.copy(
                is,
                tempFile.toPath(),
                StandardCopyOption.REPLACE_EXISTING
        );

        props.put(
                "pemFile",
                tempFile.getAbsolutePath()
        );

        props.put("allowAllHostNames", "true");

        caClient =
                HFCAClient.createNewInstance(
                        "https://localhost:7054",
                        props
                );

        caClient.setCryptoSuite(
                CryptoSuite.Factory.getCryptoSuite()
        );

    }

    public void createAffiliation(
            String businessId,
            FabricUser smbAdmin
    ) throws Exception {

        try {

            HFCAAffiliation affiliation =
                    caClient.newHFCAAffiliation(
                            businessId
                    );

            affiliation.create(smbAdmin);

            System.out.println(
                    "Affiliation created: " + businessId
            );

        } catch (Exception e) {
            if (e.getMessage().contains("already exists")) {
                System.out.println(
                        "Affiliation exists"
                );
            } else {
                throw e;
            }

        }

    }

    public String registerOrgAdmin(
            String businessId,
            FabricUser smbAdmin
    ) throws Exception {
        String adminId =
                businessId + "_admin";
        try{
            RegistrationRequest rr =
                    new RegistrationRequest(adminId);
            rr.setAffiliation(businessId);
            rr.setType("admin");
            String secret =
                    caClient.register(rr, smbAdmin);
            System.out.println(
                    "Org admin registered: " +
                            adminId
            );
            return secret;
        }catch(Exception e){
            System.out.println(
                    "Admin already registered"
            );
            return null;
        }
    }

    public Enrollment enrollOrgAdmin(
            String adminId,
            String secret
    ) throws Exception {

        Enrollment enrollment =
                caClient.enroll(
                        adminId,
                        secret
                );

        System.out.println(
                "Org admin enrolled: " +
                        adminId
        );

        return enrollment;

    }

    public String registerPeer(
            String businessId,
            FabricUser smbAdmin
    ) throws Exception {
        String peerId =
                "peer0_" + businessId;
        RegistrationRequest rr =
                new RegistrationRequest(peerId);
        rr.setAffiliation(businessId);
        rr.setType("peer");
        String secret =
                caClient.register(
                        rr,
                        smbAdmin
                );
        System.out.println(
                "Peer registered: " +
                        peerId
        );
        return secret;
    }

    public Enrollment enrollPeerMSP(
            String peerId,
            String secret
    ) throws Exception {
        Enrollment enrollment =
                caClient.enroll(
                        peerId,
                        secret
                );
        System.out.println(
                "Peer MSP enrolled"
        );
        return enrollment;
    }

    public Enrollment enrollPeerTLS(
            String peerId,
            String secret
    ) throws Exception {
        EnrollmentRequest req =
                new EnrollmentRequest();
        req.setProfile("tls");
        req.addHost("localhost");
        Enrollment tls =
                caClient.enroll(
                        peerId,
                        secret,
                        req
                );
        System.out.println(
                "Peer TLS enrolled"
        );
        return tls;
    }

}