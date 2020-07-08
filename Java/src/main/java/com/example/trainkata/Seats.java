package com.example.trainkata;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Seats {
    protected List<Seat> seats = new ArrayList<>();

    public int size(){
        return seats.size();
    }

    public Seats add(Seat seat) {
        seats.add(seat);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seats seats1 = (Seats) o;
        return Objects.equals(seats, seats1.seats);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seats);
    }

    public static class Empty extends Seats{
        public Empty() {
        }

        @Override
        public Empty add(Seat seat) {
            throw new UnsupportedOperationException("cannot add seat to Seats.Empty");
        }
    }
}
