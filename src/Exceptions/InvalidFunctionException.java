package Exceptions;

public class InvalidFunctionException extends BeepException {
    public InvalidFunctionException() {
        super("Invalid function number! Function number is only from 1 to 5.");
    }
}
