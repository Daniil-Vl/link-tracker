package edu.java.bot.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

@WireMockTest
public abstract class AbstractClientServerTest {
    protected static WireMockServer server;

    @BeforeAll
    static void initServer() {
        server = new WireMockServer();
        server.start();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    protected abstract void initClient();

    @BeforeEach
    void resetServer() {
        server.resetAll();
    }
}

