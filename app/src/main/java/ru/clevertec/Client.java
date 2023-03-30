package ru.clevertec;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Client  {
    private List<Integer> data;
    private final AtomicInteger accumulator;
    private final ExecutorService executor;

    public List<Integer> getData() {
        return data;
    }

    public AtomicInteger getAccumulator() {
        return accumulator;
    }

    public Client(int n) {
        accumulator = new AtomicInteger(0);
        data = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            data.add(i);
        }
        executor = Executors.newCachedThreadPool();
    }

    public List<Future<Integer>> request() {
        int numRequests = data.size();
        ExecutorService executorService = Executors.newFixedThreadPool(numRequests);
        List<Future<Integer>> futures = new ArrayList<>();
        for (int i = 0; i < numRequests; i++) {
            int indexToRemove = new Random().nextInt(data.size());
            Integer remove = data.remove(indexToRemove);
            Callable<Integer> requestTask = () -> Server.processRequest(remove);
            Future<Integer> future = executorService.submit(requestTask);
            futures.add(future);
        }

        return futures;
    }

    public void summarize(List<Future<Integer>> futures) {
        for (Future<Integer> future : futures) {
            try {
                accumulator.addAndGet(future.get());
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        int expectedSum = (1 + futures.size()) * (futures.size() / 2);
        int actualSum = accumulator.get();
        if (actualSum != expectedSum) {
            System.out.println("Ошибка: ожидаемая сумма = " + expectedSum + ", сумма = " + actualSum);
        } else {
            System.out.println("Успех: ожидаемая сумма = " + expectedSum + ", сумма = " + actualSum);
        }
        executor.shutdown();
    }

}
