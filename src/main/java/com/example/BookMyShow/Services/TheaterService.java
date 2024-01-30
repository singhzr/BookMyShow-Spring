package com.example.BookMyShow.Services;

import com.example.BookMyShow.Entities.Theater;
import com.example.BookMyShow.Repositories.TheaterRepository;
import com.example.BookMyShow.RequestDTOs.AddTheaterRequest;
import com.example.BookMyShow.Transformers.TheaterTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TheaterService {

    @Autowired
    private TheaterRepository theaterRepository;

    public String addTheater(AddTheaterRequest addTheaterRequest){

        Theater theater = TheaterTransformer.convertTheaterRequestToEntity(addTheaterRequest);

        theater = theaterRepository.save(theater);

        return "The theater has been saved with the theaterId "+theater.getTheaterId();

    }
}
