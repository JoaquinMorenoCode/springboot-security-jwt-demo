package moreno.joaquin.webdemo.controller;

import lombok.RequiredArgsConstructor;
import moreno.joaquin.webdemo.model.User;
import moreno.joaquin.webdemo.model.UserDTO;
import moreno.joaquin.webdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/user")
@RequiredArgsConstructor //added
public class UserController {

    @Autowired
    UserService userService;





    @ResponseBody
    @PostMapping(path = {"/register"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO registerUser(@Validated @RequestBody User user){

        return userService.createUser(user);
    }

    @ResponseBody
    @PostMapping(path = {"/login"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String login(@Validated @RequestBody UserDTO user){

        return userService.authUser(user);
    }




    @ResponseBody
    @GetMapping(path ={"/id/{id}"})
    public User getUser(@PathVariable Long id){
        return userService.findUserById(id);
    }



}
