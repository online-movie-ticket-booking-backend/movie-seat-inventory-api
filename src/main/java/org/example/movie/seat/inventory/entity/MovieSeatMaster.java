package org.example.movie.seat.inventory.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Accessors(chain = true)
@Table(name = "movie_seat_master")
public class MovieSeatMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ms_id")
    private int msId;

    @Column(name = "movie_schedule_unique_id")
    private String movieScheduleUniqueId;

    @Column(name = "seat_remaining")
    private int seatRemaining;

}
