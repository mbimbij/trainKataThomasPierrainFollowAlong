package com.example.trainkata;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TicketOfficeV1 implements TicketOffice {

    private final IProvideBookingReferences bookingReferenceProvider;
    private final IProvideTrainData trainDataProvider;
    private double threshold;

    public TicketOfficeV1(IProvideBookingReferences bookingReferenceProvider, IProvideTrainData trainDataProvider, double threshold) {
        this.bookingReferenceProvider = bookingReferenceProvider;
        this.trainDataProvider = trainDataProvider;
        this.threshold = threshold;
    }

    @Override
    public Reservation makeReservation(ReservationRequest request) {
        Collection<SeatWithBookingReference> trainTopology = trainDataProvider.getSeats(request.trainId);

        if(request.seatCount > trainTopology.size()*threshold){
            throw new MaxReservationThresholdException(String.format("cannot book more than %s percent of the train", threshold * 100));
        }

        List<Seat> reservedSeats = trainTopology.stream()
                .filter(SeatWithBookingReference::isAvailable)
                .limit(request.seatCount)
                .map(Seat::new)
                .collect(Collectors.toList());

        if (reservedSeats.size() >= request.seatCount) {
            BookingReferenceId bookingReferenceId = bookingReferenceProvider.getBookingReferenceId();
            trainDataProvider.reserveSeats(request.trainId, reservedSeats, bookingReferenceId);
            return new Reservation(request.trainId, reservedSeats, bookingReferenceId);
        } else {
            return new Reservation(request.trainId, Collections.emptyList(), new BookingReferenceId(""));
        }
    }
}
