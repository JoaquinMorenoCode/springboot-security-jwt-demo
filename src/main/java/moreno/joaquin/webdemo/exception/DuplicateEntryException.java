package moreno.joaquin.webdemo.exception;


public class DuplicateEntryException extends RuntimeException {

    public DuplicateEntryException(String string){

        super(String.format("%s already exists" , string));
    }
}
