package com.example.smbone.services.blockchain;

import com.example.smbone.util.CommandExecuter;

import java.io.File;

public class EnvironmentService {

    public static void prepareEnvironment() throws Exception {

        if (isEnvironmentReady()) {
            System.out.println("Environment ready");
            return;
        }

        System.out.println("Checking environment...");

        checkDocker();

        pullFabricImages();

        createNetwork();

        markEnvironmentReady();

    }

    private static void checkDocker() throws Exception {

        if (!DockerService.isDockerInstalled()) {

            throw new RuntimeException("Docker not installed");

        }

        if (!DockerService.isDockerRunning()) {

            throw new RuntimeException("Docker not running");

        }

    }

    private static void pullFabricImages() throws Exception {

        try {

            CommandExecuter.execute("docker image inspect hyperledger/fabric-peer:2.5");

        } catch (Exception e) {

            System.out.println("Pulling Fabric peer image");

            CommandExecuter.execute("docker pull hyperledger/fabric-peer:2.5");

        }

    }

    private static void createNetwork() throws Exception {

        try {

            CommandExecuter.execute("docker network inspect smb_fabric");

        } catch (Exception e) {

            CommandExecuter.execute("docker network create smb_fabric");

        }

    }

    private static void markEnvironmentReady() {
        new File(System.getProperty("user.home") + "/smb/blockchain/.env_ready").mkdirs();
    }

    public static boolean isEnvironmentReady() {
        File file = new File(System.getProperty("user.home") + "/smb/blockchain/.env_ready");
        return file.exists();

    }

}