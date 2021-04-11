package Exceptions;

public class InvalidFunctionOneException extends BeepException {
    public InvalidFunctionOneException() {
        super("Function one only accepts 2 integers as an input.\nExample: 1 [NUMBER]");
    }
}
