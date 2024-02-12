package moreno.joaquin.webdemo.exception;

public class UserCredentialsException extends RuntimeException {

    public UserCredentialsException (){
        super("Incorrect username or password. Please try again.");
    }

}
