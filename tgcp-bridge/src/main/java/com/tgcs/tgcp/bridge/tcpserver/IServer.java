package com.tgcs.tgcp.bridge.tcpserver;

import java.io.IOException;

public interface IServer {

    void start() throws IOException;

    void start(String ip, int port) throws IOException;

    void stop();

}
