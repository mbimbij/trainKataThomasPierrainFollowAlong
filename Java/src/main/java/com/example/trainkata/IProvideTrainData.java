package com.example.trainkata;

import java.util.Collection;

interface IProvideTrainData {
    Collection<SeatWithBookingReference> getSeats(TrainId trainId);

    void reserveSeats(TrainId trainId, Collection<Seat> seats, BookingReferenceId bookingReferenceid);
}
