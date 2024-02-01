package com.example.BookMyShow.Services;

import com.example.BookMyShow.Controllers.TicketController;
import com.example.BookMyShow.Entities.Show;
import com.example.BookMyShow.Entities.ShowSeat;
import com.example.BookMyShow.Entities.Ticket;
import com.example.BookMyShow.Entities.User;
import com.example.BookMyShow.Repositories.ShowRepository;
import com.example.BookMyShow.Repositories.TicketRepository;
import com.example.BookMyShow.Repositories.UserRepository;
import com.example.BookMyShow.RequestDTOs.AddTicketRequest;
import com.example.BookMyShow.Response.ShowTicketResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    public String bookTicket(AddTicketRequest bookTicketRequest) throws Exception {

        Optional<Show> showOptional = showRepository.findById(bookTicketRequest.getShowId());
        if (showOptional.isEmpty()) {
            throw new Exception("Show with show id " + bookTicketRequest.getShowId() + " does not exists");
        }
        Show show = showOptional.get();


        //Check for seat available or not
        List<ShowSeat> showSeatList = show.getShowSeatList();
        int totalBookingAmount = 0;

        for (String seatNoToBeBooked : bookTicketRequest.getSeatList()) {

            for (ShowSeat showSeat : showSeatList) {

                if (showSeat.getSeatNo().equals(seatNoToBeBooked) &&
                        (bookTicketRequest.getSeatType().equals(showSeat.getSeatType()))) {

                    if (showSeat.getIsAvailable() == 1) {
                        showSeat.setIsAvailable(0);
                        totalBookingAmount = totalBookingAmount + showSeat.getPrice();
                    }
                    else {
                        throw new Exception("Seat No " + showSeat.getSeatNo() + " is already booked.");
                    }
                }
            }
        }

        User user = userRepository.findByEmailId(bookTicketRequest.getEmailId());

        //If we reach here that means all the seats were available
        Ticket ticket = Ticket.builder()
                .seatNumbersBooked(bookTicketRequest.getSeatList().toString())
                .totalAmountPaid(totalBookingAmount)
                .show(show) //FK being set
                .user(user) //FK being set
                .seatType(bookTicketRequest.getSeatType())
                .foodAttached(bookTicketRequest.getFoodAttached())
                .build();

        user.getTicketList().add(ticket); //Bidirectional mapping
        show.getTicketList().add(ticket); //Bidirectional mapping

        ticket = ticketRepository.save(ticket);

        return "This is the ticket with the ticketId " + ticket.getTicketId();
    }
    public ShowTicketResponse viewTicket(Integer ticketId){

        Ticket ticket = ticketRepository.findById(ticketId).get();

        Show show = ticket.getShow();
        String movieName = show.getMovie().getMovieName();
        String theaterInfo = show.getTheater().getName()+" "+show.getTheater().getAddress();
        String bookedSeats = ticket.getSeatNumbersBooked();

        ShowTicketResponse showTicketResponse = ShowTicketResponse.builder()
                .movieName(movieName)
                .theaterInfo(theaterInfo)
                .showDate(show.getShowDate())
                .showTime(show.getShowTime())
                .seatNos(bookedSeats)
                .totalAmt(ticket.getTotalAmountPaid())
                .foodAttached(ticket.getFoodAttached())
                .seatType(ticket.getSeatType())
                .build();

        String emailId = ticket.getUser().getEmailId();

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setFrom("springacciojob@gmail.com");
        simpleMailMessage.setTo(emailId);
        simpleMailMessage.setSubject("Movie Ticket Confirmation");
        simpleMailMessage.setText(  "Movie name - "+showTicketResponse.getMovieName()+"\n"+
                                    "Show time - "+showTicketResponse.getShowTime()+"\n"+
                                    "Show date - "+showTicketResponse.getShowDate()+"\n"+
                                    "Seats booked - "+bookedSeats+"\n"+
                                    "Food attached - "+showTicketResponse.getFoodAttached()+"\n"+
                                    "Theater - "+showTicketResponse.getTheaterInfo()+"\n"+
                                    "Amount paid - "+showTicketResponse.getTotalAmt()+"\n");
        mailSender.send(simpleMailMessage);

        return showTicketResponse;
    }
}