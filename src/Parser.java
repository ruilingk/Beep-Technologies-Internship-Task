import Exceptions.BeepException;
import Exceptions.InvalidFunctionException;
import Exceptions.InvalidFunctionFiveException;
import Exceptions.InvalidFunctionFourException;
import Exceptions.InvalidFunctionOneException;
import Exceptions.InvalidFunctionThreeException;
import Exceptions.InvalidFunctionTwoException;
import Functions.Function;
import Functions.FunctionFive;
import Functions.FunctionFour;
import Functions.FunctionOne;
import Functions.FunctionThree;
import Functions.FunctionTwo;

public class Parser {
    Function checkInput(String input) throws BeepException {
        String trimmedInput = input.trim();
        String[] splitInput = trimmedInput.split(" ");
        int size = splitInput.length;

        String functionNumber = splitInput[0];
        if (functionNumber.equals("1")) {
            if (size != 2) {
                throw new InvalidFunctionOneException();
            }
            int numberA;
            try {
                numberA = Integer.parseInt(splitInput[1]);
            } catch (NumberFormatException e) {
                throw new InvalidFunctionOneException();
            }
            return new FunctionOne(numberA);
        } else if (functionNumber.equals("2")) {
            if (size != 3) {
                throw new InvalidFunctionTwoException();
            }
            int numberA;
            int numberB;
            try {
                numberA = Integer.parseInt(splitInput[1]);
                numberB = Integer.parseInt(splitInput[2]);
            } catch (NumberFormatException e) {
                throw new InvalidFunctionTwoException();
            }
            return new FunctionTwo(numberA, numberB);
        } else if (functionNumber.equals("3")) {
            if (size != 2) {
                throw new InvalidFunctionThreeException();
            }
            String filePath = splitInput[1];
            return new FunctionThree(filePath);
        } else if (functionNumber.equals("4")) {
            if (size != 1) {
                throw new InvalidFunctionFourException();
            }
            return new FunctionFour();
        } else if (functionNumber.equals("5")) {
            if (size != 1) {
                throw new InvalidFunctionFiveException();
            }
            return new FunctionFive();
        } else {
            throw new InvalidFunctionException();
        }
    }
}
