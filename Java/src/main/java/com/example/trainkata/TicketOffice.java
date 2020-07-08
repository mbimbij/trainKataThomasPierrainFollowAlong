package com.example.trainkata;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TicketOffice {

    private final IProvideBookingReferences bookingReferenceProvider;
    private final IProvideTrainData trainDataProvider;
    private double threshold;

    public TicketOffice(IProvideBookingReferences bookingReferenceProvider, IProvideTrainData trainDataProvider, double threshold) {
        this.bookingReferenceProvider = bookingReferenceProvider;
        this.trainDataProvider = trainDataProvider;
        this.threshold = threshold;
    }

    public Reservation makeReservation(ReservationRequest request) {
        Train train = trainDataProvider.getTrain(request.trainId).orElseThrow(() -> new UnknownTrainException(String.format("train with id=%s is unknown", request.trainId)));
        if (request.seatCount > train.getTotalNbOfSeats() * threshold) {
            throw new MaxReservationThresholdException(String.format("cannot book more than %s percent of the train", threshold * 100));
        }

        ReservationOption reservationOption = train.reserve(request.seatCount);

        if (reservationOption.isFullfilled()) {
            BookingReferenceId bookingReferenceId = bookingReferenceProvider.getBookingReferenceId();
            Seats reservedSeats = reservationOption.getReservedSeats();
            trainDataProvider.reserveSeats(request.trainId, reservedSeats, bookingReferenceId);
            return new Reservation(request.trainId, reservedSeats, bookingReferenceId);
        } else {
            return new Reservation(request.trainId, new Seats.Empty(), new BookingReferenceId(""));
        }
    }
}
