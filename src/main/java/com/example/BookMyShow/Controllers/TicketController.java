package com.example.BookMyShow.Controllers;

import com.example.BookMyShow.RequestDTOs.AddTicketRequest;
import com.example.BookMyShow.Response.ShowTicketResponse;
import com.example.BookMyShow.Services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping("/bookTicket")
    public ResponseEntity bookTicket(@RequestBody AddTicketRequest addTicketRequest){

        try{
            String result = ticketService.bookTicket(addTicketRequest);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        catch (Exception e){

            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/viewTicket")
    public ShowTicketResponse viewTicket(@RequestParam("ticketId")Integer ticketId){
        ShowTicketResponse showTicketResponse = ticketService.viewTicket(ticketId);
        return showTicketResponse;
    }

}
