package com.example.smbone.services.blockchain;

import com.example.smbone.DTOs.BlockchainStatus;
import com.example.smbone.enums.BlockchainState;

import java.io.File;

import static com.example.smbone.services.blockchain.MSPGeneratorService.getBasePath;

public class BlockchainStateService {

    private EnvironmentService environmentService;

    private WalletService walletService;

    private DockerService dockerService;

    private static final String BLOCKCHAIN_ROOT = getBasePath();

    public BlockchainStatus getStatus(String business_id) {
        BlockchainStatus status = new BlockchainStatus();

        boolean dockerInstalled = DockerService.isDockerInstalled();

        boolean dockerRunning = DockerService.isDockerRunning();

        boolean envReady = isEnvironmentReady();

        boolean walletExists = doesWalletExist();

        boolean peerRunning = DockerService.isPeerRunning(business_id);

        boolean dockerCompose = isDockerComposeReady();

        boolean peerCrypto = isPeerCryptoReady(business_id);

        status.setDockerInstalled(dockerInstalled);

        status.setDockerRunning(dockerRunning);

        status.setEnvironmentReady(envReady);

        status.setWalletPresent(walletExists);

        status.setPeerRunning(peerRunning);

        status.setDockerComposeReady(dockerCompose);

        status.setPeerCryptoReady(peerCrypto);

        status.setState(deriveState(dockerInstalled, dockerRunning, envReady, walletExists, peerRunning));

        return status;
    }

    private boolean isEnvironmentReady() {
        File marker = new File(BLOCKCHAIN_ROOT + ".env_ready");

        return marker.exists();
    }

    private boolean doesWalletExist() {
        File wallet = new File(BLOCKCHAIN_ROOT + "wallet");

        return wallet.exists() && wallet.isDirectory() && wallet.list().length > 0;
    }

    public boolean isPeerCryptoReady(String businessId) {
        String root = System.getProperty("user.home") + "/smb/blockchain/org/peer/";

        File msp = new File(root + "msp");

        File tls = new File(root + "tls");

        File config = new File(root + "msp/config.yaml");

        return msp.exists() && tls.exists() && config.exists();
    }

    public boolean isDockerComposeReady() {
        File compose = new File(System.getProperty("user.home") + "/smb/blockchain/docker-compose.yaml");

        return compose.exists();
    }

    private BlockchainState deriveState(boolean dockerInstalled, boolean dockerRunning, boolean envReady, boolean walletExists, boolean peerRunning) {

        if (!dockerInstalled) return BlockchainState.NOT_INITIALIZED;

        if (!dockerRunning) return BlockchainState.ERROR;

        if (!envReady) return BlockchainState.NOT_INITIALIZED;

        if (envReady && !walletExists) return BlockchainState.ENV_PREPARED;

        if (walletExists && !peerRunning) return BlockchainState.ORG_REGISTERED;

        if (peerRunning && walletExists) return BlockchainState.PEER_STARTED;

        if (peerRunning && walletExists && envReady) return BlockchainState.READY;

        return BlockchainState.ERROR;
    }

    public boolean isBlockchainReady(String business_id) {
        return getStatus(business_id).getState() == BlockchainState.READY;
    }

    public void printDebugReport(String business_id) {
        BlockchainStatus s = getStatus(business_id);

        System.out.println("Docker Installed: " + s.isDockerInstalled());

        System.out.println("Docker Running: " + s.isDockerRunning());

        System.out.println("Env Ready: " + s.isEnvironmentReady());

        System.out.println("Wallet: " + s.isWalletPresent());

        System.out.println("Peer Running: " + s.isPeerRunning());

        System.out.println("State: " + s.getState());
    }
}
