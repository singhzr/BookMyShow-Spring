package com.example.BookMyShow.Services;

import com.example.BookMyShow.Entities.Movie;
import com.example.BookMyShow.Repositories.MovieRepository;
import com.example.BookMyShow.RequestDTOs.AddMovieRequest;
import com.example.BookMyShow.Transformers.MovieTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;

    public String addMovie(AddMovieRequest movieRequest){

        //old method : create an object using the constructor

        //new Method : create Object using the Builder annotation. In this order of argument and number of argument
        // passing can be anything
        Movie movie = MovieTransformer.convertTheaterRequestToEntity(movieRequest);

        movie = movieRepository.save(movie);

        return "The movie has been saved with the movieId "+movie.getMovieId();
    }
}

//1. @Service @Repository these annotations internally contains @Component annotation
//2. For any class having @Component Spring creates it object and keeps it in IOC container