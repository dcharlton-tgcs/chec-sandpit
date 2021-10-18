package com.tgcs.tgcp.bridge.checoperations.handlers;

import com.tgcs.tgcp.bridge.checoperations.BridgeSession;
import com.tgcs.tgcp.bridge.common.XmlDocument;
import com.tgcs.tgcp.bridge.tcpserver.TcpClient;

public interface IHandler {

    void processChecMessage(TcpClient connectedClient, XmlDocument message);

    void setBridgeSession(BridgeSession bridgeSession);

}
