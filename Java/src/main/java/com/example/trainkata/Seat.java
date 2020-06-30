package com.example.trainkata;

import java.util.Objects;

public class Seat {
    public final String coach;
    public final int seatNumber;

    public Seat(String coach, int seatNumber) {
        this.coach = coach;
        this.seatNumber = seatNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seat seat = (Seat) o;
        return seatNumber == seat.seatNumber &&
                Objects.equals(coach, seat.coach);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coach, seatNumber);
    }
}
