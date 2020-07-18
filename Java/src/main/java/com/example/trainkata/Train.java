package com.example.trainkata;

import java.util.List;
import java.util.stream.Collectors;

public class Train {
    private final TrainId trainId;
    private final List<SeatWithBookingReference> seats;
    private final double threshold;

    public Train(TrainId trainId, List<SeatWithBookingReference> seats, double threshold) {
        this.trainId = trainId;
        this.seats = seats;
        this.threshold = threshold;
    }

    public ReservationOption reserve(int seatCount) {

        if(seatCount > seats.size()*threshold){
            throw new MaxReservationThresholdException(String.format("cannot book more than %s percent of the train", threshold * 100));
        }

        List<Seat> availableSeatsForReservationRequest = seats.stream()
                .filter(SeatWithBookingReference::isAvailable)
                .map(Seat::new)
                .limit(seatCount)
                .collect(Collectors.toList());
        return new ReservationOption(seatCount,availableSeatsForReservationRequest);
    }
}
