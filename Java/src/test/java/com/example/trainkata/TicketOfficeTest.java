package com.example.trainkata;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TicketOfficeTest {
    @Test
    void shouldReserveSeats_whenUnreservedSeatsAreAvailable() {
        BookingReferenceId expectedBookingId = new BookingReferenceId("expectedBookingId");
        IProvideBookingReferences bookingReferenceProvider = mock(IProvideBookingReferences.class);
        doReturn(expectedBookingId).when(bookingReferenceProvider).getBookingReferenceId();
        IProvideTrainData trainDataProvider = mock(IProvideTrainData.class);
        doReturn(emptyTrainWith1CoachAndNSeatsAvailable("A", 6)).when(trainDataProvider).getSeats(any(TrainId.class));

        TicketOffice ticketOffice = new TicketOffice(bookingReferenceProvider, trainDataProvider, 0.7);

        TrainId trainId = new TrainId("express_2000");
        ReservationRequest reservationRequest = new ReservationRequest(trainId, 3);
        Reservation reservation = ticketOffice.makeReservation(reservationRequest);

        assertThat(reservation.trainId).isEqualTo(trainId);
        assertThat(reservation.bookingId).isEqualTo(expectedBookingId);
        assertThat(reservation.seats)
                .usingFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(
                        new Seat("A", 1),
                        new Seat("A", 2),
                        new Seat("A", 3));
    }

    private List<SeatWithBookingReference> emptyTrainWith1CoachAndNSeatsAvailable(String coach, int nbSeatsAvailable) {
        return IntStream.rangeClosed(1, nbSeatsAvailable)
                .mapToObj(i -> new SeatWithBookingReference(coach, i, BookingReferenceId.NULL))
                .collect(Collectors.toList());
    }

    @Test
    void shouldMarkSeatsAsReserved_whenReserved() {
        // GIVEN
        BookingReferenceId expectedBookingId = new BookingReferenceId("75bcd15");
        IProvideBookingReferences bookingReferenceProvider = mock(IProvideBookingReferences.class);
        doReturn(expectedBookingId).when(bookingReferenceProvider).getBookingReferenceId();

        TrainId trainId = new TrainId("express2000");
        IProvideTrainData trainDataProvider = mock(IProvideTrainData.class);
        doReturn(trainWith1Coach3SeatsAnd1Available()).when(trainDataProvider).getSeats(trainId);

        // WHEN
        TicketOffice ticketOffice = new TicketOffice(bookingReferenceProvider, trainDataProvider, 0.7);
        Reservation reservation = ticketOffice.makeReservation(new ReservationRequest(trainId, 1));

        // THEN le siège réservé est bien le "A2"
        assertThat(reservation.trainId).isEqualTo(trainId);
        assertThat(reservation.bookingId).isEqualTo(expectedBookingId);
        assertThat(reservation.seats).containsExactly(new Seat("A", 2));

        // AND on a appelé la méthode de réservation de l'opérateur historique
        verify(trainDataProvider).reserveSeats(trainId, List.of(new Seat("A", 2)), expectedBookingId);
    }

    private List<SeatWithBookingReference> trainWith1Coach3SeatsAnd1Available() {
        return List.of(
                new SeatWithBookingReference("A", 1, new BookingReferenceId("34Dsq")),
                new SeatWithBookingReference("A", 2, BookingReferenceId.NULL),
                new SeatWithBookingReference("A", 3, new BookingReferenceId("34Dsq"))
        );
    }

    @Test
    void shouldNotReserverMore70PercentOfSeatsForOverallTrain() {
        // GIVEN
        BookingReferenceId expectedBookingId = new BookingReferenceId("75bcd15");
        TrainId trainId = new TrainId("express2000");
        IProvideBookingReferences bookingReferenceProvider = mock(IProvideBookingReferences.class);
        IProvideTrainData trainDataProvider = mock(IProvideTrainData.class);
        TicketOffice ticketOffice = new TicketOffice(bookingReferenceProvider, trainDataProvider, 0.7);
        List<SeatWithBookingReference> trainTopology = emptyTrainWith1CoachAndNSeatsAvailable("A", 10);
        doReturn(expectedBookingId).when(bookingReferenceProvider).getBookingReferenceId();
        doReturn(trainTopology).when(trainDataProvider).getSeats(trainId);

        // WHEN
        assertThatThrownBy(() -> ticketOffice.makeReservation(new ReservationRequest(trainId, 8))).isInstanceOf(MaxReservationThresholdException.class);
    }
}