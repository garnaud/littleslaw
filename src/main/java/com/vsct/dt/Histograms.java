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
     * number of received response by bucket.
     */
    int[] responses;

    /**
     * number of received request by bucket.
     */
    int[] requests;

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

    /**
     * window duration in milliseconds of a bucket.
     */
    private final int window;

    Histograms(int bucket, int sample, int window) {
        this.bucket = bucket;
        this.sample = sample;
        this.window = window;
        latencies = new int[bucket][sample];
        for (int i = 0; i < bucket; i++) {
            Arrays.fill(latencies[i], 0);
        }

        responses = new int[bucket];
        requests = new int[bucket];
        Arrays.fill(responses, 0);
        Arrays.fill(requests, 0);
    }


    /**
     * Latency in milliseconds.
     *
     * @param i latency in milliseconds
     */
    void latency(int i) {
        responses[currentBucket]++;
        if (responses[currentBucket] > sample) {
            // sample array is filled, so let choose one to replace b
            int indexToRemove = rand.nextInt(responses[currentBucket]);
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
        // re-init
        Arrays.fill(latencies[currentBucket], 0);
        responses[currentBucket] = 0;
        requests[currentBucket] = 0;
        currentSample = 0;
    }


    int percentiles(int i) {
        int previousBucket = (currentBucket + bucket - 1) % bucket;
        Arrays.sort(latencies[previousBucket]);
        return latencies[previousBucket][Math.max(i * sample / 100 - 1, 0)];
    }


    void request() {
        requests[currentBucket]++;
    }

    double rate() {
        int previousBucket = (currentBucket + bucket - 1) % bucket;
        return ((double) requests[previousBucket]) / (double) window;
    }
}
