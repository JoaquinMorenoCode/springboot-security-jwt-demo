package moreno.joaquin.webdemo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(Long id){
        super(String.format("User with Id %d not found", id));
    }

    public UserNotFoundException(String email){
        super(String.format("User with email %s not found", email));

    }




}
