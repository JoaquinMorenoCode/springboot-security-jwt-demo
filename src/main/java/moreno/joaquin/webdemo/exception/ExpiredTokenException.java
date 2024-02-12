package moreno.joaquin.webdemo.exception;

public class ExpiredTokenException extends RuntimeException {

    public ExpiredTokenException(){
        super("TOKEN ESPIRAO");
    }

}
