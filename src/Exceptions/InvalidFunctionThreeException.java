package Exceptions;

public class InvalidFunctionThreeException extends BeepException {
    public InvalidFunctionThreeException() {
        super("Function three only accepts '3' and a text as an input.\nExample: 3 [TEXT]");
    }
}
