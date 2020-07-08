package com.example.trainkata;

public class Reservation {
	public final TrainId trainId;
    public final BookingReferenceId bookingId;
    public final Seats seats;

    public Reservation(TrainId trainId, Seats seats, BookingReferenceId bookingId) {
		this.trainId = trainId;
        this.bookingId = bookingId;
        this.seats = seats;
    }

}
