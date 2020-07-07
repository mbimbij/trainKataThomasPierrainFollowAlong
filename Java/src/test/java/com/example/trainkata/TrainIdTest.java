package com.example.trainkata;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TrainIdTest {
    @Test
    void testComparisonByValue() {
        TrainId trainId1 = new TrainId("id");
        TrainId trainId2 = new TrainId("id");
        TrainId trainId3 = new TrainId("id-diff");

        assertThat(trainId1).isEqualTo(trainId2);
        assertThat(trainId1).isNotEqualTo(trainId3);
    }
}