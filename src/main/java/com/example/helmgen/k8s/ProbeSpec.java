package com.example.helmgen.k8s;

import java.util.Arrays;
import java.util.List;

public class ProbeSpec {
    public enum Type { HTTP_GET, TCP_SOCKET, EXEC }

    private final Type type;
    private final String path;
    private final String portName;
    private final Integer portNumber;
    private final List<String> command;

    private ProbeSpec(Type type, String path, String portName, Integer portNumber, List<String> command) {
        this.type = type;
        this.path = path;
        this.portName = portName;
        this.portNumber = portNumber;
        this.command = command == null ? List.of() : List.copyOf(command);
    }

    public static ProbeSpec httpGet(String path, String portName) {
        return new ProbeSpec(Type.HTTP_GET, path, portName, null, null);
    }

    public static ProbeSpec tcpSocket(int portNumber) {
        return new ProbeSpec(Type.TCP_SOCKET, null, null, portNumber, null);
    }

    public static ProbeSpec command(String... command) {
        return new ProbeSpec(Type.EXEC, null, null, null, Arrays.asList(command));
    }

    public static ProbeSpec shellCommand(String command) {
        return command("/bin/sh", "-c", command);
    }

    public Type type() { return type; }
    public String path() { return path; }
    public String portName() { return portName; }
    public Integer portNumber() { return portNumber; }
    public List<String> command() { return command; }
}
