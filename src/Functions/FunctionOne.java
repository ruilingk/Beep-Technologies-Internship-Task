package Functions;

import java.util.ArrayList;
import java.util.Arrays;

public class FunctionOne extends Function {
    private final int numberA;
    private final ArrayList<Integer> list = new ArrayList<>();

    public FunctionOne(int numberA) {
        this.numberA = numberA;
    }

    @Override
    public void execute() {
        boolean[] prime = new boolean[numberA + 1];
        Arrays.fill(prime, true);
        fillPrimeArray(prime);

        for (int i = 2; i <= numberA; i++) {
            if (prime[i] && isPalindrome(i)) {
                list.add(i);
            }
        }
        printList();
    }

    private void fillPrimeArray(boolean[] prime) {
        for (int i = 2; i * i <= numberA; i++) {
            if (prime[i]) {
                // check for multiples of i
                for (int j = i * 2; j <= numberA; j += i) {
                    prime[j] = false;
                }
            }
        }
    }

    private boolean isPalindrome(int n) {
        int check = n;
        int sum = 0;
        while (n > 0) {
            int remainder = n % 10;
            sum = (sum * 10) + remainder;
            n = n / 10;
        }
        return check == sum;
    }

    private void printList() {
        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1) {
                System.out.print(list.get(i));
            } else {
                System.out.print(list.get(i) + ", ");
            }
        }
        System.out.println("\n");
    }
}
