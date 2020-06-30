package com.example.trainkata;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TicketOfficeTest {
    @Test
    void should_reserve_seats_when_unreserved_seats_are_available() {
        BookingReferenceId expectedBookingId = new BookingReferenceId("expectedBookingId");
        IProvideBookingReferences bookingReferenceProvider = mock(IProvideBookingReferences.class);
        doReturn(expectedBookingId).when(bookingReferenceProvider).getBookingReferenceId();
        IProvideTrainData trainDataProvider = mock(IProvideTrainData.class);
        doReturn(
                List.of(new SeatWithBookingReference("A", 1, new BookingReferenceId("")),
                        new SeatWithBookingReference("A", 2, new BookingReferenceId("")),
                        new SeatWithBookingReference("A", 3, new BookingReferenceId("")),
                        new SeatWithBookingReference("A", 4, new BookingReferenceId("")))
        )
                .when(trainDataProvider).getSeats(any(TrainId.class));
        doAnswer(invocation -> {
            List<SeatWithBookingReference> seatsArgument = invocation.getArgument(1, List.class);
            BookingReferenceId bookingReferenceIdArgument = invocation.getArgument(2, BookingReferenceId.class);
            return seatsArgument.stream()
                    .map(seat -> new SeatWithBookingReference(seat.coach, seat.seatNumber, bookingReferenceIdArgument))
                    .collect(Collectors.toList());
        }).when(trainDataProvider).reserveSeats(any(TrainId.class), anyCollection(), any(BookingReferenceId.class));

        TicketOffice ticketOffice = new TicketOffice(bookingReferenceProvider, trainDataProvider);

        TrainId trainId = new TrainId("express_2000");
        ReservationRequest reservationRequest = new ReservationRequest(trainId, 3);
        Reservation reservation = ticketOffice.makeReservation(reservationRequest);

        assertThat(reservation.trainId).isEqualTo(trainId);
        assertThat(reservation.bookingId).isEqualTo(expectedBookingId);
        assertThat(reservation.seats)
                .usingFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(
                        new SeatWithBookingReference("A", 1, expectedBookingId),
                        new SeatWithBookingReference("A", 2, expectedBookingId),
                        new SeatWithBookingReference("A", 3, expectedBookingId));
    }

    private interface IProvideBookingReferences {
        BookingReferenceId getBookingReferenceId();
    }

    public static class TicketOffice {

        private final IProvideBookingReferences bookingReferenceProvider;
        private final IProvideTrainData trainDataProvider;

        public TicketOffice(IProvideBookingReferences bookingReferenceProvider, IProvideTrainData trainDataProvider) {
            this.bookingReferenceProvider = bookingReferenceProvider;
            this.trainDataProvider = trainDataProvider;
        }

        public Reservation makeReservation(ReservationRequest request) {
            List<SeatWithBookingReference> reservedSeats = trainDataProvider.getSeats(request.trainId).stream()
                    .filter(SeatWithBookingReference::isAvailable)
                    .limit(request.seatCount)
                    .collect(Collectors.toList());
            if (reservedSeats.size() >= request.seatCount) {
                BookingReferenceId bookingReferenceId = bookingReferenceProvider.getBookingReferenceId();
                Collection<SeatWithBookingReference> confirmedSeats = trainDataProvider.reserveSeats(request.trainId, reservedSeats, bookingReferenceId);
                return new Reservation(request.trainId, confirmedSeats, bookingReferenceId);
            } else {
                return new Reservation(request.trainId, Collections.emptyList(), new BookingReferenceId(""));
            }
        }

    }

    private interface IProvideTrainData {
        Collection<SeatWithBookingReference> getSeats(TrainId trainId);

        Collection<SeatWithBookingReference> reserveSeats(TrainId trainId, Collection<SeatWithBookingReference> seats, BookingReferenceId bookingReferenceid);
    }

}