package com.example.BookMyShow.Entities;

import jakarta.persistence.*;
import lombok.*;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "tickets")
public class Ticket {//1.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ticketId;

    private Integer seatNumbersBooked;

    private Integer totalAmountPaid;

    @JoinColumn
    @ManyToOne
    private Show show;
}
//1. In this class we are not adding movieName attribute because from
//   ticket entity we can get show entity and from show entity we can get movie entity