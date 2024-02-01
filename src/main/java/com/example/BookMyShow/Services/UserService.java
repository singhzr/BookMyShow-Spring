package com.example.BookMyShow.Services;

import com.example.BookMyShow.Entities.User;
import com.example.BookMyShow.Repositories.UserRepository;
import com.example.BookMyShow.RequestDTOs.AddUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public String addUser(AddUserRequest addUserRequest){

        User user = User.builder().emailId(addUserRequest.getEmailId())
                .name(addUserRequest.getName())
                .build();

        user = userRepository.save(user);
        return "User with userId "+user.getUserId()+" has been saved to the DB";

    }
}
