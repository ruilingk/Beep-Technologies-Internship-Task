import Exceptions.*;

public class Parser {
    void checkInput(String input) throws BeepException {
        String trimmedInput = input.trim();
        String[] splitInput = trimmedInput.split(" ");
        int size = splitInput.length;

        String functionNumber = splitInput[0];
        if (functionNumber.equals("1")) {
            if (size != 2) {
                throw new InvalidFunctionOneException();
            }
            String numberA = splitInput[1];
        } else if (functionNumber.equals("2")) {
            if (size != 3) {
                throw new InvalidFunctionTwoException();
            }
            String numberA = splitInput[1];
            String numberB = splitInput[2];
        } else if (functionNumber.equals("3")) {
            if (size != 2) {
                throw new InvalidFunctionThreeException();
            }
            String text = splitInput[1];
        } else if (functionNumber.equals("4")) {
            if (size != 1) {
                throw new InvalidFunctionFourException();
            }
            // perform GET request

        } else if (functionNumber.equals("5")) {
            if (size != 1) {
                throw new InvalidFunctionFiveException();
            }
            // do some data stuff

        } else {
            throw new InvalidFunctionException();
        }
    }
}
