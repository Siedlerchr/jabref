package org.jabref.logic.sharelatex;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler.Whole;
import javax.websocket.Session;

import org.glassfish.tyrus.client.ClientManager;

public class WebSocketClientWrapper {

    private static final String SENT_MESSAGE = "Hello_World";

    public static void createAndConnect(String channel) {

        CountDownLatch messageLatch = new CountDownLatch(1);
        try {

            final ClientEndpointConfig cec = ClientEndpointConfig.Builder.create()
                    .preferredSubprotocols(Arrays.asList("mqttt")).build();
            ClientManager client = ClientManager.createClient();
            client.connectToServer(new Endpoint() {

                @Override
                public void onOpen(Session session, EndpointConfig config) {
                    System.out.println("On Open and is Open " + session.isOpen());

                    session.addMessageHandler(String.class, (Whole<String>) message -> {
                        System.out.println("Received message: " + message);

                    });

                    try {
                        session.getBasicRemote().sendText(
                                "5:1+::{\"name\":\"joinProject\",\"args\":[{\"project_id\":\"5909edaff31ff96200ef58dd\"}]}");
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    System.out.println("Sent");
                }
            }, cec, new URI("ws://192.168.1.248/socket.io/1/websocket/" + channel));
            messageLatch.await(100, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
