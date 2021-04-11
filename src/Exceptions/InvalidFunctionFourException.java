package Exceptions;

public class InvalidFunctionFourException extends BeepException {
    public InvalidFunctionFourException() {
        super("Function four only accepts '4' as an input.\nExample: 4");
    }
}
