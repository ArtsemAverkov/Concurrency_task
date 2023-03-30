package ru.clevertec;

import org.junit.Test;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;

public class ClientServerTest {
    @Test
    public void testClientServerInteraction() {
        int n = 10;
        Client client = new Client(n);
        List<Future<Integer>> futures = client.request();
        client.summarize(futures);
            assertEquals(n, Server.sharedList.size());
    }
}
