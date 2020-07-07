package com.example.trainkata;

import java.util.ArrayList;
import java.util.List;

public class ReservationOption {
    private final TrainId trainId;
    private final int seatCount;
    private final List<Seat> reservedSeats = new ArrayList<>();

    public ReservationOption(TrainId trainId, int seatCount) {
        this.trainId = trainId;
        this.seatCount = seatCount;
    }

    public void addSeatReservation(Seat seat) {
        reservedSeats.add(seat);
    }

    public boolean isFullfilled(){
        return reservedSeats.size() == seatCount;
    }

    public List<Seat> getReservedSeats() {
        return reservedSeats;
    }
}
