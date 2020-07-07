package com.example.trainkata;

import java.util.Collection;
import java.util.Optional;

interface IProvideTrainData {
    Optional<Train> getTrain(TrainId trainId);

    void reserveSeats(TrainId trainId, Collection<Seat> seats, BookingReferenceId bookingReferenceid);
}
