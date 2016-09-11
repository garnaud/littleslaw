package com.vsct.dt;

import java.util.Arrays;
import java.util.Random;

class Histograms {

    private Random rand = new Random(System.currentTimeMillis());

    /**
     * sample latencies by bucket.
     */
    int[][] latencies;

    /**
     * counter by bucket.
     */
    int[] counters;

    /**
     * current bucket index.
     */
    private int currentBucket = 0;

    /**
     * current sample index.
     */
    private int currentSample = 0;

    /**
     * number of samples by bucket.
     */
    private int sample;

    /**
     * number of buckets.
     */
    private int bucket;

    Histograms(int bucket, int sample) {
        this.bucket = bucket;
        this.sample = sample;
        latencies = new int[bucket][sample];
        for (int i = 0; i < bucket; i++) {
            Arrays.fill(latencies[i], 0);
        }

        counters = new int[bucket];
        Arrays.fill(counters, 0);
    }


    void latency(int i) {
        counters[currentBucket]++;
        if (counters[currentBucket] > sample) {
            // sample array is filled, so let choose one to replace b
            int indexToRemove = rand.nextInt(counters[currentBucket]);
            if (indexToRemove < sample) {
                latencies[currentBucket][indexToRemove] = i;
            }
        } else {
            latencies[currentBucket][currentSample] = i;
            currentSample++;
        }
    }

    void nextBucket() {
        currentBucket = (currentBucket + 1) % bucket;
        currentSample = 0;
    }


    int percentiles(int i) {
        int previousBucket = (currentBucket + bucket - 1) % bucket;
        Arrays.sort(latencies[previousBucket]);
        return latencies[previousBucket][Math.max(i * sample / 100 - 1, 0)];
    }
}
