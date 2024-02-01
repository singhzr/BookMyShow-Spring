package com.example.BookMyShow.Controllers;

import com.example.BookMyShow.Entities.Movie;
import com.example.BookMyShow.RequestDTOs.AddMovieRequest;
import com.example.BookMyShow.Services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("movie")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @PostMapping("/addMovie")
    public ResponseEntity addMovie(@RequestBody AddMovieRequest addMovieRequest){

        String result = movieService.addMovie(addMovieRequest);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @PutMapping("/deleteMovieByMovieId")
    public ResponseEntity deleteMovieByMovieId(@RequestParam("movieId") Integer movieId){

        try {
            String result = movieService.deleteMovieByMovieId(movieId);
            return new ResponseEntity(result, HttpStatus.OK);

        }
        catch (Exception e) {
            return new ResponseEntity(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
