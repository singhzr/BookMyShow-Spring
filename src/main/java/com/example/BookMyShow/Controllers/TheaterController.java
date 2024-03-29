package com.example.BookMyShow.Controllers;

import com.example.BookMyShow.RequestDTOs.AddTheaterRequest;
import com.example.BookMyShow.RequestDTOs.AddTheaterSeatsRequest;
import com.example.BookMyShow.Services.TheaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("theater")
public class TheaterController {

    @Autowired
    private TheaterService theaterService;

    @PostMapping("/addTheater")
    public ResponseEntity addTheater(@RequestBody AddTheaterRequest addTheaterRequest){

        String result = theaterService.addTheater(addTheaterRequest);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/addTheaterSeats")
    public ResponseEntity addTheaterSeats(@RequestBody AddTheaterSeatsRequest addTheaterSeatsRequest){
        String result = theaterService.addTheaterSeats(addTheaterSeatsRequest);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
