package com.example.BookMyShow.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "Shows")
public class Show {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer showId;

    private LocalDate showDate;

    private LocalTime showTime;

    @JoinColumn
    @ManyToOne
    private Movie movie;


    @JoinColumn
    @ManyToOne
    private Theater theater;


    @OneToMany(mappedBy = "show", cascade = CascadeType.ALL)
    private List<ShowSeat> showSeatList = new ArrayList<>();

    @OneToMany(mappedBy = "show", cascade = CascadeType.ALL)
    private List<Ticket> ticketList = new ArrayList<>();
    public Show(LocalDate showDate, LocalTime showTime) {
        this.showDate = showDate;
        this.showTime = showTime;
    }
}
