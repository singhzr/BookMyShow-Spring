package com.example.BookMyShow.Entities;

import com.example.BookMyShow.Enums.SeatType;
import jakarta.persistence.*;
import lombok.*;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "show_seats")
public class ShowSeat {//Represents particular Seat booked for a particular show

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer showSeatId;

    private int price;

    private boolean isAvailable;

    private boolean foodAttached;

    private String seatNo;// These values will come from the theater
    @Enumerated(value = EnumType.STRING)
    private SeatType seatType;// seats based on mapping or seat structure

    @JoinColumn
    @ManyToOne
    private Show show;

}
