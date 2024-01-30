package com.example.BookMyShow.Repositories;

import com.example.BookMyShow.Entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository //If we don't write @Repository it will then also work because it is interface we don't need to create object
// of this. The class which implements the methods written in JpaRepository already has @Repository written on it
public interface MovieRepository extends JpaRepository<Movie, Integer> {
}
