package com.example.trainkata;

import java.util.ArrayList;
import java.util.List;

public class ReservationOption {
    private final TrainId trainId;
    private final int seatCount;
    private final Seats reservedSeats = new Seats();

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

    public Seats getReservedSeats() {
        return reservedSeats;
    }
}
