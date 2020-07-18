package com.example.trainkata;

public class TicketOfficeV2 implements TicketOffice {

    private final IProvideBookingReferences bookingReferenceProvider;
    private final IProvideTrainData trainDataProvider;


    public TicketOfficeV2(IProvideBookingReferences bookingReferenceProvider, IProvideTrainData trainDataProvider, double threshold) {
        this.bookingReferenceProvider = bookingReferenceProvider;
        this.trainDataProvider = trainDataProvider;
    }

    @Override
    public Reservation makeReservation(ReservationRequest request) {
        Train train = trainDataProvider.getTrain(request.trainId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("train not found : %s", request.trainId)));

        ReservationOption reservationOption = train.reserve(request.seatCount);
        BookingReferenceId bookingReferenceId = bookingReferenceProvider.getBookingReferenceId();
        trainDataProvider.reserveSeats(request.trainId, reservationOption.getSeats(), bookingReferenceId);
        return new Reservation(request.trainId, reservationOption.getSeats(), bookingReferenceId);
    }
}
