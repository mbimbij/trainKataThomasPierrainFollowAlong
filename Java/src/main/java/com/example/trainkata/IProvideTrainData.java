package com.example.trainkata;

import java.util.Optional;

interface IProvideTrainData {

    Optional<Train> getTrain(TrainId trainId);

    void reserveSeats(TrainId trainId, Seats seats, BookingReferenceId bookingReferenceid);
}
