package nl._42.springai.hackathon.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BatchUtils {

    private static final int MAX_CONCURRENT_TASKS = 25;

    public static <T> List<List<T>> splitListIntoBatches(List<T> list, int batchSize) {
        List<List<T>> batches = new ArrayList<>();
        int totalSize = list.size();

        for (int i = 0; i < totalSize; i += batchSize) {
            int end = Math.min(i + batchSize, totalSize);
            batches.add(new ArrayList<>(list.subList(i, end)));
        }

        return batches;
    }

    /**
     * This runs provided tasks multithreaded. Useful for building vectors.
     */
    public static void runTasksMultithreaded(List<Callable<Void>> tasks) throws Exception {
        // Multithreaded processing up to a max of MAX_CONCURRENT_TASKS at the same time
        ExecutorService executor = Executors.newFixedThreadPool(MAX_CONCURRENT_TASKS);
        Semaphore semaphore = new Semaphore(MAX_CONCURRENT_TASKS);

        var futures = tasks.stream().map(task -> executor.submit(() -> {
            try {
                semaphore.acquire();
                return task.call();
            } finally {
                semaphore.release();
            }
        })).toList();

        executor.shutdown();
        boolean tasksCompleted = executor.awaitTermination(30, TimeUnit.MINUTES);

        if (!tasksCompleted) {
            throw new IllegalStateException("Terminated before tasks were finished!");
        }

        for (Future<Void> future : futures) {
            try {
                future.get(); // This will throw an exception if the task threw one
            } catch (ExecutionException e) {
                log.error("Task failed with exception: ", e);
            }
        }
    }
}
