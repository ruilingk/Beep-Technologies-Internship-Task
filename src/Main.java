import Exceptions.BeepException;
import Functions.Function;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        Parser parser = new Parser();
        try {
            Function function = parser.checkInput(input);
            function.execute();
        } catch (BeepException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
