package com.example.trainkata;

import java.util.List;

public class ReservationOption {
    private final int seatCount;
    private final List<Seat> seats;

    public ReservationOption(int seatCount, List<Seat> seats) {
        this.seatCount = seatCount;
        this.seats = seats;
    }

    public List<Seat> getSeats() {
        return seats;
    }
}
