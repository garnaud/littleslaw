package com.vsct.dt;

import java.util.Arrays;

class Histograms {
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
        latencies[currentBucket][currentSample] = i;
        nextSample();
    }

    private void nextSample() {
        currentSample = (currentSample + 1) % sample;
    }

    void nextBucket() {
        currentBucket = (currentBucket + 1) % bucket;
    }

}
