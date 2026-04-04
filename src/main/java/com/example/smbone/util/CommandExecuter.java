package com.example.smbone.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CommandExecuter {

    public static void execute(String command) throws Exception {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("cmd.exe", "/c", command);
        Process process = builder.start();
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Command failed");
        }
    }

    public static void execute(String command, String workingDir) throws Exception {
        ProcessBuilder builder = new ProcessBuilder();
        builder.directory(new java.io.File(workingDir));
        builder.command("cmd.exe", "/c", command);
        Process process = builder.start();
        process.waitFor();
    }

    public static String executeWithResult(String command) throws Exception {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("cmd.exe", "/c", command);
        Process process = builder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        StringBuilder output = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }
        process.waitFor();
        return output.toString();
    }

    public static void executeWithLogs(String command) throws Exception {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("cmd.exe", "/c", command);
        Process process = builder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        process.waitFor();
    }
}