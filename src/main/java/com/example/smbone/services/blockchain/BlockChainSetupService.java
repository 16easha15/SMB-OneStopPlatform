package com.example.smbone.services.blockchain;

import com.example.smbone.models.FabricUser;
import org.hyperledger.fabric.sdk.Enrollment;

import java.io.File;

public class BlockChainSetupService {

    public void setupBlockchain(String businessId) throws Exception {

        if (orgExists()) {
            DockerService.startPeer();
            return;
        }

        CAService caService = new CAService();

        FabricUser smbAdmin = SuperAdminIdentityLoader.load();

        // Step 1 affiliation
        caService.createAffiliation(businessId, smbAdmin);

        // Step 2 register org admin
        String adminSecret = caService.registerOrgAdmin(businessId, smbAdmin);

        // Step 3 enroll admin
        Enrollment adminEnrollment = caService.enrollOrgAdmin(businessId + "_admin", adminSecret);

        // Step 4 store wallet
        WalletService.storeIdentity(businessId + "_admin", adminEnrollment, businessId + "MSP");

        // Step 5 register peer
        String peerSecret = caService.registerPeer(businessId, smbAdmin);

        // Step 6 enroll peer MSP
        Enrollment peerMSP = caService.enrollPeerMSP("peer0_" + businessId, peerSecret);

        // Step 7 enroll peer TLS
        Enrollment peerTLS = caService.enrollPeerTLS("peer0_" + businessId, peerSecret);

        // Step 8 generate MSP folders
        MSPGeneratorService.createStructure();

        MSPGeneratorService.writePeerMSP(peerMSP);

        MSPGeneratorService.writePeerTLS(peerTLS);

        MSPGeneratorService.writeCACert();

        MSPGeneratorService.writeConfigYaml();

        // Step 9 docker compose
        DockerService.generatePeerCompose(businessId, 7051);

        // Step 10 start peer
        DockerService.startPeer();

        System.out.println("Blockchain setup complete");

    }

    private boolean orgExists() {
        File file = new File(System.getProperty("user.home") + "/smb/blockchain/org/peer/msp");
        return file.exists();
    }

}