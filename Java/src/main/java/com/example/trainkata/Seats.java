package com.example.trainkata;

import java.util.List;

public class Seats {
    private final List<Seat> seats;

    private Seats(List<Seat> seats) {
        this.seats = seats;
    }

    public static Seats from(List<Seat> seats){
        return new Seats(seats);
    }

    public List<Seat> asList() {
        return seats;
    }
}
