import Exceptions.*;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        Parser parser = new Parser();
        try {
            parser.checkInput(input);
        } catch (BeepException e) {
            System.out.println(e.getMessage());
        }
    }

}
