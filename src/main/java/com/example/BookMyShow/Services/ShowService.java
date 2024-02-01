package com.example.BookMyShow.Services;

import com.example.BookMyShow.CustomException.InvalidMovieException;
import com.example.BookMyShow.CustomException.InvalidMovieIdException;
import com.example.BookMyShow.CustomException.InvalidShowException;
import com.example.BookMyShow.CustomException.InvalidTheaterException;
import com.example.BookMyShow.Entities.*;
import com.example.BookMyShow.Enums.SeatType;
import com.example.BookMyShow.Repositories.MovieRepository;
import com.example.BookMyShow.Repositories.ShowRepository;
import com.example.BookMyShow.Repositories.ShowSeatRepository;
import com.example.BookMyShow.Repositories.TheaterRepository;
import com.example.BookMyShow.RequestDTOs.AddShowRequest;
import com.example.BookMyShow.RequestDTOs.AddShowSeatsRequest;
import com.example.BookMyShow.RequestDTOs.ShowAvailableSeatRequest;
import com.example.BookMyShow.Transformers.ShowTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service


public class ShowService {

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private TheaterRepository theaterRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ShowSeatRepository showSeatRepository;

    public String addShow(AddShowRequest showRequest) throws Exception {

        Movie movie = movieRepository.findMovieByMovieName(showRequest.getMovieName());

        if (movie == null) {
            throw new InvalidMovieException("Movie Name entered does not exist in the DB");
        }

        Optional<Theater> theaterOptional = theaterRepository.findById(showRequest.getTheaterId());
        if (theaterOptional.isEmpty()) {
            throw new InvalidTheaterException("Theater Id entered does not exist");
        }
        Theater theater = theaterOptional.get();

        Show showEntity = ShowTransformer.convertShowRequestToEntity(showRequest);

        showEntity.setMovie(movie);
        showEntity.setTheater(theater);

        //Bidirectional in the parent class
        movie.getShowList().add(showEntity);
        theater.getShowList().add(showEntity);

        showEntity = showRepository.save(showEntity);//saving child will cascade to both parents

        return "The show has been created with the showId " + showEntity.getShowId();
    }


    public String addShowSeats(AddShowSeatsRequest addShowSeatsRequest)throws Exception {
        Optional<Show> optionalShow = showRepository.findById(addShowSeatsRequest.getShowId());

        if(optionalShow.isEmpty()){
            throw new InvalidShowException("Show does not exist");
        }
        Show show = optionalShow.get();
        Theater theater = show.getTheater();
        List<TheaterSeat> theaterSeatList = theater.getTheaterSeatList();

        List<ShowSeat> showSeatList = new ArrayList<>();

        for(TheaterSeat theaterSeat:theaterSeatList){

            String seatNo = theaterSeat.getSeatNo();
            SeatType seatType = theaterSeat.getSeatType();

            ShowSeat showSeat = ShowSeat.builder()
                    .foodAttached(0)
                    .isAvailable(1)
                    .show(show)
                    .seatNo(seatNo)
                    .seatType(seatType)
                    .build();
            if(seatType.equals(SeatType.CLASSIC)){
                showSeat.setPrice(addShowSeatsRequest.getPriceForClassicSeats());
            }else
                showSeat.setPrice(addShowSeatsRequest.getPriceForPremiumSeats());

            showSeatList.add(showSeat);
        }
        show.setShowSeatList(showSeatList);

        showRepository.save(show);

        return "Show seats have been added to the system";
    }

    public String showAvailableSeats(ShowAvailableSeatRequest availableSeatRequest)throws Exception {
        List<String> seats = new ArrayList<>();

        Optional<Show> optionalShow = showRepository.findById(availableSeatRequest.getShowId());

        if(optionalShow.isEmpty()){
            throw new Exception("Show with showId "+availableSeatRequest.getShowId()+" does not exists");
        }
        Show show = optionalShow.get();
        List<ShowSeat> showSeatList = show.getShowSeatList();

        for(ShowSeat showSeat : showSeatList) {

            if (showSeat.getIsAvailable() == 1 && showSeat.getSeatType().equals(availableSeatRequest.getSeatType())) {
                String seatNo = showSeat.getSeatNo();
                seats.add(seatNo);
            }
        }
        return seats.toString();
    }
    public String deleteShowByShowId(Integer showId) throws Exception {

        Optional<Show> showOptional = showRepository.findById(showId);

        if(showOptional.isEmpty()){
            throw new InvalidMovieIdException("ShowId you want to delete is does not exist");
        }

        showRepository.deleteById(showId);

        return "Show with showId "+showId+" has been deleted";
    }
}