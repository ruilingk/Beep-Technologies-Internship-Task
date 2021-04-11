package Exceptions;

public class InvalidFunctionTwoException extends BeepException {
    public InvalidFunctionTwoException() {
        super("Function two only accepts 3 integers as an input.\nExample: 2 [NUMBER1] [NUMBER2]");
    }
}
