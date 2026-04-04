package com.example.smbone.DTOs;

import com.example.smbone.enums.BlockchainState;

public class BlockchainStatus
{
    private boolean dockerInstalled;

    private boolean dockerRunning;

    private boolean environmentReady;

    private boolean walletPresent;

    private boolean peerRunning;

    private boolean peerCryptoReady;

    private boolean dockerComposeReady;

    private BlockchainState state;

    public BlockchainStatus(){}

    public boolean isDockerInstalled() {
        return dockerInstalled;
    }

    public void setDockerInstalled(boolean dockerInstalled) {
        this.dockerInstalled = dockerInstalled;
    }

    public boolean isDockerRunning() {
        return dockerRunning;
    }

    public void setDockerRunning(boolean dockerRunning) {
        this.dockerRunning = dockerRunning;
    }

    public boolean isEnvironmentReady() {
        return environmentReady;
    }

    public void setEnvironmentReady(boolean environmentReady) {
        this.environmentReady = environmentReady;
    }

    public boolean isWalletPresent() {
        return walletPresent;
    }

    public void setWalletPresent(boolean walletPresent) {
        this.walletPresent = walletPresent;
    }

    public boolean isPeerRunning() {
        return peerRunning;
    }

    public void setPeerRunning(boolean peerRunning) {
        this.peerRunning = peerRunning;
    }

    public boolean isPeerCryptoReady() {
        return peerCryptoReady;
    }

    public void setPeerCryptoReady(boolean peerCryptoReady) {
        this.peerCryptoReady = peerCryptoReady;
    }

    public boolean isDockerComposeReady() {
        return dockerComposeReady;
    }

    public void setDockerComposeReady(boolean dockerComposeReady) {
        this.dockerComposeReady = dockerComposeReady;
    }

    public BlockchainState getState() {
        return state;
    }

    public void setState(BlockchainState state) {
        this.state = state;
    }
}
