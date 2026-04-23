package itmo.lab.client;

import java.io.IOException;

/**
 * Retry с экспоненциальным backoff.
 *
 * Повторяет операцию при сбое, увеличивая задержку вдвое
 * каждый раз (1с → 2с → 4с → ... до 30с максимум).
 * Максимум 8 попыток.
 */
public class RetryBackoff {

    private static final int INITIAL_DELAY = 1000;
    private static final int MAX_DELAY = 30000;
    private static final int MAX_ATTEMPTS = 8;

    @FunctionalInterface
    public interface Retryable {
        void execute() throws Exception;
    }

    public static void run(Retryable operation) throws IOException {
        int delay = INITIAL_DELAY;
        int attempts = 0;
        Exception lastException = null;

        while (true) {
            try {
                operation.execute();
                return;
            } catch (Exception e) {
                lastException = e;
                attempts++;

                if (attempts >= MAX_ATTEMPTS) {
                    IOException ioe = new IOException("Превышено максимальное количество попыток (" + MAX_ATTEMPTS + ")");
                    ioe.initCause(lastException);
                    throw ioe;
                }

                try {
                    Thread.sleep(delay);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new IOException("Прервано", ie);
                }

                delay = Math.min(delay * 2, MAX_DELAY);
            }
        }
    }
}