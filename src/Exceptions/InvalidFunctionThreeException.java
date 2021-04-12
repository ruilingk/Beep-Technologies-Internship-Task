package Exceptions;

public class InvalidFunctionThreeException extends BeepException {
    public InvalidFunctionThreeException() {
        super("Function three only accepts '3' and a path as an input.\nExample: 3 [RELATIVE_PATH]");
    }

    public InvalidFunctionThreeException(String message) {
        super(message);
    }
}
