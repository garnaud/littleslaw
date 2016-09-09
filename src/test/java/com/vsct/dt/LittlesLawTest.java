package com.vsct.dt;

import org.junit.Assert;
import org.junit.Test;

public class LittlesLawTest {

    @Test
    public void should_return_initial_capacity_when_no_request_yet() {
        // given
        LittlesLaw.initialCapacity = 10;

        // test
        int capacity = LittlesLaw.capacity();

        // check
        Assert.assertEquals("capacity should same as initial", 10, capacity);
    }

}