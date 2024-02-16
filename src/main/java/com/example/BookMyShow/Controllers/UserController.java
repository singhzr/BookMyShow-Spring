package com.example.BookMyShow.Controllers;

import com.example.BookMyShow.Entities.User;
import com.example.BookMyShow.Repositories.UserRepository;
import com.example.BookMyShow.RequestDTOs.AddUserRequest;
import com.example.BookMyShow.Response.UserResponse;
import com.example.BookMyShow.Services.UserService;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/addUser")
    private ResponseEntity addUser(@RequestBody AddUserRequest addUserRequest){
        String result = userService.addUser(addUserRequest);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @GetMapping("/getUserByEmailId")
    public ResponseEntity getUserByEmailId(@RequestParam("emailId")String emailId){

        try{

            User user = userRepository.customMethod(emailId);

            UserResponse userResponse = UserResponse.builder()
                    .name(user.getName())
                    .emailId(user.getEmailId())
                    .build();

            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        }
        catch (Exception e){
            return  new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
