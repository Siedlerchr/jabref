package org.jabref.logic.sharelatex;

import org.junit.Test;


public class SharelatexConnectorTest {

    @Test
    public void test() {

        SharelatexConnector connector = new SharelatexConnector();
        connector.connectToServer("http://192.168.1.248", "joe@example.com", "test");

    }

}
