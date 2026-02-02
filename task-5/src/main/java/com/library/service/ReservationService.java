package com.library.service;

import com.library.model.Book;
import com.library.model.Reservation;
import com.library.model.User;
import com.library.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public List<Reservation> getReservationsByUser(User user) {
        return reservationRepository.findByUser(user);
    }

    public List<Reservation> getPendingReservations() {
        return reservationRepository.findByStatus("PENDING");
    }

    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    public Reservation createReservation(User user, Book book) {
        Reservation reservation = new Reservation(user, book);
        return reservationRepository.save(reservation);
    }

    public Reservation approveReservation(Long reservationId, String adminNotes) throws Exception {
        Optional<Reservation> reservationOpt = reservationRepository.findById(reservationId);

        if (reservationOpt.isEmpty()) {
            throw new Exception("Reservation not found");
        }

        Reservation reservation = reservationOpt.get();
        reservation.setStatus("APPROVED");
        reservation.setApprovalDate(LocalDateTime.now());
        reservation.setAdminNotes(adminNotes);

        return reservationRepository.save(reservation);
    }

    public Reservation rejectReservation(Long reservationId, String adminNotes) throws Exception {
        Optional<Reservation> reservationOpt = reservationRepository.findById(reservationId);

        if (reservationOpt.isEmpty()) {
            throw new Exception("Reservation not found");
        }

        Reservation reservation = reservationOpt.get();
        reservation.setStatus("REJECTED");
        reservation.setApprovalDate(LocalDateTime.now());
        reservation.setAdminNotes(adminNotes);

        return reservationRepository.save(reservation);
    }

    public void cancelReservation(Long reservationId) throws Exception {
        Optional<Reservation> reservationOpt = reservationRepository.findById(reservationId);

        if (reservationOpt.isEmpty()) {
            throw new Exception("Reservation not found");
        }

        Reservation reservation = reservationOpt.get();
        reservation.setStatus("CANCELLED");
        reservationRepository.save(reservation);
    }

    public long countPendingReservations() {
        Long count = reservationRepository.countPendingReservations();
        return count != null ? count : 0L;
    }
}
