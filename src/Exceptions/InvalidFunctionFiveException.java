package Exceptions;

public class InvalidFunctionFiveException extends BeepException {
    public InvalidFunctionFiveException() {
        super("Function five only accepts '5' as an input.\nExample: 5");
    }
}
