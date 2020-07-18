package com.example.trainkata;

import java.util.Collection;
import java.util.Optional;

interface IProvideTrainData {
    Collection<SeatWithBookingReference> getSeats(TrainId trainId);

    void reserveSeats(TrainId trainId, Collection<Seat> seats, BookingReferenceId bookingReferenceid);

    Optional<Train> getTrain(TrainId trainId);
}
