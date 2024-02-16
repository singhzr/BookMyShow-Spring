package com.example.BookMyShow.Services;

import com.example.BookMyShow.CustomException.InvalidMovieIdException;
import com.example.BookMyShow.Entities.Movie;
import com.example.BookMyShow.Entities.Show;
import com.example.BookMyShow.Entities.Theater;
import com.example.BookMyShow.Entities.Ticket;
import com.example.BookMyShow.Repositories.MovieRepository;
import com.example.BookMyShow.Repositories.ShowRepository;
import com.example.BookMyShow.Repositories.TicketRepository;
import com.example.BookMyShow.RequestDTOs.AddMovieRequest;
import com.example.BookMyShow.RequestDTOs.GetShowsOfMovieOnGivenDate;
import com.example.BookMyShow.Response.AllShowsOnGivenDate;
import com.example.BookMyShow.Transformers.MovieTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private TicketRepository ticketRepository;

    public String addMovie(AddMovieRequest movieRequest){

        //old method : create an object using the constructor

        //new Method : create Object using the Builder annotation. In this order of argument and number of argument
        // passing can be anything
        Movie movie = MovieTransformer.convertTheaterRequestToEntity(movieRequest);

        movie = movieRepository.save(movie);

        return "The movie has been saved with the movieId "+movie.getMovieId();
    }

    public String deleteMovieByMovieId(Integer movieId) throws Exception {

        Optional<Movie> optionalMovie = movieRepository.findById(movieId);

        if(optionalMovie.isEmpty()){
            throw new InvalidMovieIdException("MovieId you want to delete is does not exist");
        }

        movieRepository.deleteById(movieId);

        return "Movie with movieId "+movieId+" has been deleted";
    }

    public String totalCollectionOfaMovie(String movieName)throws Exception {

        //get movie entity
        Movie movie = movieRepository.findMovieByMovieName(movieName);

        if(movie == null){
            throw new Exception("Movie with movieName "+movieName+ " does not exists");
        }

        //get all the shows in the db
        List<Show> showList = showRepository.findAll();

        //stores shows of a given movie
        List<Show> givenMovieShows = new ArrayList<>();
        for(Show show : showList){

            if(show.getMovie().getMovieId() == movie.getMovieId()){
                givenMovieShows.add(show);
            }
        }

        //gets all the tickets in the movie
        List<Ticket> ticketList = ticketRepository.findAll();

        //stores all the tickets for the given movie
        List<Ticket> givenMovieTickets = new ArrayList<>();

        //checks in the ticketList whether ticket for that show exists
        for(Show show : givenMovieShows){

            for(Ticket ticket : ticketList){

                if(ticket.getShow().getShowId() == show.getShowId()){
                    givenMovieTickets.add(ticket);
                }
            }
        }
        int totalCollection = 0;

        for(Ticket ticket : givenMovieTickets){

            totalCollection += ticket.getTotalAmountPaid();
        }
        return movieName +" collected total of Rs. "+totalCollection;
    }

    public String showsoftheMovieOnaGivenDate(GetShowsOfMovieOnGivenDate getShowsOfMovieOnGivenDate) throws Exception{

        Movie movie = movieRepository.findMovieByMovieName(getShowsOfMovieOnGivenDate.getMovieName());

        if(movie == null){
            throw new Exception( "Movie with movieName "+getShowsOfMovieOnGivenDate.getMovieName()+" does not exists");
        }
        List<Show> showList = showRepository.findAll();

        List<Show> showsOfGivenMovie = new ArrayList<>();

        for(Show show : showList){

            if(show.getMovie().getMovieId() == movie.getMovieId() &&
                    show.getShowDate().isEqual(getShowsOfMovieOnGivenDate.getLocalDate())){

                showsOfGivenMovie.add(show);
            }
        }
        HashMap<String, List<Show>> theaterShowsList = new HashMap<>();

        for(Show show : showsOfGivenMovie){

            String theaterName = show.getTheater().getName();
            List<Show> shows = theaterShowsList.getOrDefault(theaterName, new ArrayList<>());
            shows.add(show);

            theaterShowsList.put(theaterName, shows);
        }
        String present = "";

        for(String theaterName : theaterShowsList.keySet()){

            List<Show> showInTheater = theaterShowsList.get(theaterName);
            present += theaterName+" -> "+movie.getMovieName()+"\n";
            present += "Show Times ";
            for(Show show : showInTheater){
                String showDetails = "|"+show.getShowTime()+ "|";
                present += showDetails+" ";
            }
            present += "\n"+"\n";
        }
        if(present.equals("")){
            return present+"No show on date "+getShowsOfMovieOnGivenDate.getLocalDate()+
                    " is available for "+movie.getMovieName();
        }
        return present;
    }
}

//1. @Service @Repository these annotations internally contains @Component annotation
//2. For any class having @Component Spring creates it object and keeps it in IOC container