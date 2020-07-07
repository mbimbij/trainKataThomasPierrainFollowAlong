package com.example.trainkata;

import java.util.List;

public class Train {
    private final TrainId trainId;
    private final int totalNbOfSeats;
    private final List<SeatWithBookingReference> seatsWithBookingReferences;

    public Train(TrainId trainId, List<SeatWithBookingReference> seatsWithBookingReferences) {
        this.trainId = trainId;
        this.seatsWithBookingReferences = seatsWithBookingReferences;
        totalNbOfSeats = seatsWithBookingReferences.size();
    }

    public TrainId getTrainId() {
        return trainId;
    }

    public int getTotalNbOfSeats() {
        return totalNbOfSeats;
    }
}
