package Exceptions;

public class InvalidFunctionTwoException extends BeepException {
    public InvalidFunctionTwoException() {
        super("Function two only accepts 3 integers as an input and NUMBER1 must be lesser than or equals to NUMBER2." +
                "\nExample: 2 [NUMBER1] [NUMBER2]");
    }
}
