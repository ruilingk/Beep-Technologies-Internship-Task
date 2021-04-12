package Functions;

import Exceptions.InvalidFunctionThreeException;

import java.io.IOException;

public abstract class Function {
    public abstract void execute() throws IOException, InvalidFunctionThreeException;
}
