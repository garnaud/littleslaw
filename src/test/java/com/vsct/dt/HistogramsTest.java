package com.vsct.dt;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
                assertEquals(sample + 1, histograms.latencies[bucket][sample]);
            }
            assertEquals(100, histograms.counters[bucket]);
        }
    }

    @Test
    public void should_fill_latencies_into_sample_with_fair_probability() {
        // given
        Histograms histograms = new Histograms(100, 100);  // 100 samples maximum

        // test
        for (int bucket = 0; bucket < 100; bucket++) {
            for (int latency = 1; latency <= 200; latency++) {
                // fill 200 latencies over 100 sample
                histograms.latency(latency);
            }
            histograms.nextBucket();
        }

        // check
        int sampledLatency200 = 0;
        for (int bucket = 0; bucket < 100; bucket++) {
            for (int sample = 0; sample < 100; sample++) {
                if (histograms.latencies[bucket][sample] == 200) sampledLatency200++;
            }
        }
        assertEquals("the last setted latency should be present in 50% of cases with +/- 10% of tolerance", 50, sampledLatency200, 10);
    }

    @Test
    public void should_give_last_percentiles_for_latency() {
        // given
        int bucket = 2, sample = 100;
        Histograms histograms = new Histograms(bucket, sample);

        // test
        for (int currentBucket = 0; currentBucket < bucket; currentBucket++) {
            for (int latency = sample; latency > 0; latency--) {
                // fill 100 latencies in reverse, range from 1ms to 100ms
                histograms.latency(latency);
            }
            histograms.nextBucket();
        }

        // check
        assertEquals(1, histograms.percentiles(0));
        assertEquals(90, histograms.percentiles(90));
        assertEquals(100, histograms.percentiles(100));
    }
}