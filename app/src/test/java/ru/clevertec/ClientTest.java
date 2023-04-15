package ru.clevertec;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;

public class ClientTest {
    Server server = new Server();

    @Test
    public void testClientServerInteraction() {
        int n = 10;
        Client client = new Client(n);
        List<Future<Integer>> futures = client.request();
        client.summarize(futures);
        assertEquals(n, server.sharedList.size());
    }

    @Test
    public void testRemoveAllData() {
        Client client = new Client(100);
        List<Future<Integer>> futures = client.request();
        client.summarize(futures);
        assertEquals(client.getData().isEmpty(),true);
    }

    @Test
    public void testAccumulatorSum() {
        Client client = new Client(100);
        List<Future<Integer>> futures = client.request();
        client.summarize(futures);
        int expectedSum = (1 + 100) * (100 / 2);
        int actualSum = client.getAccumulator().get();
        assertEquals(expectedSum, actualSum);
    }
}
