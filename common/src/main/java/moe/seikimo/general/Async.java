package moe.seikimo.general;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicReference;

public interface Async {
    AtomicReference<ExecutorService> POOL = new AtomicReference<>(
            new ScheduledThreadPoolExecutor(1));

    /**
     * Sleeps the current thread.
     *
     * @param duration The duration to sleep.
     */
    static void sleep(long duration) {
        try { Thread.sleep(duration); }
        catch (InterruptedException ignored) { }
    }

    /**
     * Submits a task to the thread pool.
     *
     * @param runnable The runnable to submit.
     */
    static void run(Runnable runnable) {
        POOL.get().submit(runnable);
    }

    /**
     * Submits a task to the thread pool.
     * Runs the task after the specified duration.
     *
     * @param runnable The runnable to submit.
     * @param duration The duration to wait before running the task.
     */
    static void runAfter(Runnable runnable, float duration) {
        Async.run(() -> {
            Async.sleep((long) Math.floor(duration * 1000));
            runnable.run(); // Run the task.
        });
    }
}
