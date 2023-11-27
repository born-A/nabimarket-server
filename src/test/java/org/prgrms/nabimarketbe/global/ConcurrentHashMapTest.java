package org.prgrms.nabimarketbe.global;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ConcurrentHashMapTest {
    private static final int maxThreads = 10;
    private static final ConcurrentHashMap<String, Integer> concurrentHashMap = new ConcurrentHashMap<>();

    @Test
    void concurrencyTest() {
        ExecutorService executorService = Executors.newFixedThreadPool(maxThreads);

        for (int i = 0; i < maxThreads; i++) {
            executorService.execute(() -> {
                for (int j = 0; j < 10; j++) {
                    String key = String.valueOf(j);

                    concurrentHashMap.put(key, j);
                }
            });
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertThat(concurrentHashMap.size()).isEqualTo(10);;
    }


}
