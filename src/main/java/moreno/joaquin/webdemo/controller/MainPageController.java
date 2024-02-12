package moreno.joaquin.webdemo.controller;

import lombok.RequiredArgsConstructor;
import moreno.joaquin.webdemo.config.JwtService;
import moreno.joaquin.webdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import moreno.joaquin.webdemo.model.User;

import java.util.List;

@RestController
@RequestMapping({"/home"})
@RequiredArgsConstructor
public class MainPageController {

   @Autowired
    UserService userService;



    @ResponseBody
    @GetMapping//(path = {""})
    public ResponseEntity<String>  getHome(){

        return ResponseEntity.ok(userService.getCurrentUser());


        //return userService.getUsers();
    }

}
