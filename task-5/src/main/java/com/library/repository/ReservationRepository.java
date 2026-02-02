package com.library.repository;

import com.library.model.Book;
import com.library.model.Reservation;
import com.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUser(User user);

    List<Reservation> findByBook(Book book);

    List<Reservation> findByStatus(String status);

    List<Reservation> findByUserAndStatus(User user, String status);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.status = 'PENDING'")
    Long countPendingReservations();
}
