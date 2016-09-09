package com.vsct.dt;

import org.junit.Assert;
import org.junit.Test;

public class HistogramsTest {

    @Test
    public void should_initialize_a_zero_latencies_matrice_and_counters() {
        // given
        int[] zeros10 = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        // test
        Histograms histograms = new Histograms(10, 10);

        // check
        for (int i = 0; i < 10; i++) {
            Assert.assertArrayEquals(zeros10, histograms.latencies[i]);
        }
        Assert.assertArrayEquals(zeros10, histograms.counters);
    }

    @Test
    public void should_fill_all_latencies_sample_and_counters() {
        // given
        Histograms histograms = new Histograms(10, 100);

        // test
        for (int bucket = 0; bucket < 10; bucket++) {
            for (int latency = 1; latency <= 100; latency++) {
                histograms.latency(latency);
            }
            histograms.nextBucket();
        }

        // check
        for (int bucket = 0; bucket < 10; bucket++) {
            for (int sample = 0; sample < 100; sample++) {
                Assert.assertEquals(sample + 1, histograms.latencies[bucket][sample]);
            }
        }


    }
}