package org.example.movie.seat.inventory.repository;

import org.example.movie.seat.inventory.entity.MovieSeatMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieSeatMasterRepository extends JpaRepository<MovieSeatMaster, Integer> {

    public Optional<MovieSeatMaster> findByMovieScheduleUniqueId(String movieScheduleUniqueId);
}
