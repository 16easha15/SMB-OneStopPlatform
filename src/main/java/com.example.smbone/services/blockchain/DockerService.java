package com.example.smbone.services.blockchain;

import com.example.smbone.util.CommandExecuter;

import java.nio.file.Files;
import java.nio.file.Path;

public class DockerService {

    public static void pullFabricImages() throws Exception {
        CommandExecuter.execute("docker pull hyperledger/fabric-peer:2.5");
        CommandExecuter.execute("docker pull hyperledger/fabric-tools:2.5");
    }

    public static boolean isDockerInstalled() {
        try {
            CommandExecuter.execute("docker --version");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isDockerRunning() {
        try {
            CommandExecuter.execute("docker ps");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void startPeer() throws Exception {
        String base = MSPGeneratorService.getBasePath();
        CommandExecuter.execute("docker compose up -d", MSPGeneratorService.getBasePath());
    }

    public static void stopPeer() throws Exception {
        String base = MSPGeneratorService.getBasePath();
        CommandExecuter.execute("docker compose up -d", MSPGeneratorService.getBasePath());
    }

    public static boolean isPeerRunning(String businessId) {
        try {
            String output = CommandExecuter.executeWithResult("docker ps --filter name=peer0_" + businessId);
            return output.contains("peer0_" + businessId);
        } catch (Exception e) {
            return false;
        }
    }

    public static void waitForPeer(String businessId) throws Exception {
        int retries = 10;
        while (retries > 0) {
            if (isPeerRunning(businessId)) {
                System.out.println("Peer ready");
                return;
            }
            Thread.sleep(3000);
            retries--;
        }
        throw new RuntimeException("Peer failed to start");
    }

    public static void generatePeerCompose(String businessId, int peerPort) throws Exception {

        String base = MSPGeneratorService.getBasePath();
        String yaml = """
                version: '3.7'
                
                services:
                
                  peer0:
                
                    image: hyperledger/fabric-peer:2.5
                
                    container_name: peer0_%s
                
                    environment:
                
                      - CORE_PEER_ID=peer0_%s
                
                      - CORE_PEER_LOCALMSPID=%sMSP
                
                      - CORE_PEER_ADDRESS=peer0:%d
                
                      - CORE_PEER_LISTENADDRESS=0.0.0.0:%d
                
                      - CORE_PEER_MSPCONFIGPATH=/etc/hyperledger/peer/msp
                
                      - CORE_PEER_TLS_ENABLED=true
                
                      - CORE_PEER_TLS_CERT_FILE=/etc/hyperledger/peer/tls/server.crt
                
                      - CORE_PEER_TLS_KEY_FILE=/etc/hyperledger/peer/tls/server.key
                
                      - CORE_PEER_TLS_ROOTCERT_FILE=/etc/hyperledger/peer/tls/ca.crt
                
                      - CORE_VM_ENDPOINT=unix:///host/var/run/docker.sock
                
                      - CORE_CHAINCODE_EXECUTETIMEOUT=300s
                
                    working_dir: /opt/gopath/src/github.com/hyperledger/fabric/peer
                
                    command: peer node start
                
                    volumes:
                
                      - %s/org/peer:/etc/hyperledger/peer
                
                      - /var/run/docker.sock:/host/var/run/docker.sock
                
                    ports:
                
                      - "%d:%d"
                
                """.formatted(businessId, businessId, businessId, peerPort, peerPort, base.replace("\\", "/"), peerPort, peerPort);
        Path file = Path.of(base + "/docker-compose.yaml");
        Files.writeString(file, yaml);
        System.out.println("Docker compose generated");
    }

    public static void createNetwork() throws Exception {
        try {
            CommandExecuter.execute("docker network create fabric_network");
        } catch (Exception e) {
            System.out.println("Network already exists");
        }
    }


}