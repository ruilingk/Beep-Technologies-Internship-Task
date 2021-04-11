package Functions;

import java.util.ArrayList;
import java.util.Arrays;

public class FunctionTwo extends Function {
    private final int numberA;
    private final int numberB;
    private ArrayList<Integer> list = new ArrayList<>();

    public FunctionTwo(int numberA, int numberB) {
        this.numberA = numberA;
        this.numberB = numberB;
    }

//    @Override
//    public void execute() {
//        boolean[] prime = new boolean[numberB + 1];
//        Arrays.fill(prime, true);
//        fillPrimeArray(prime);
//
//        for (int i = numberA; i <= numberB; i++) {
//            if (prime[i] && isPalindrome(i)) {
//                if (!list.contains(i)) {
//                    list.add(i);
//                }
//            } else {
//                if (i >= 10) {
//                    String number = String.valueOf(i);
//                    for (int j = 0; j < number.length(); j++) {
//                        StringBuilder stringBuilder = new StringBuilder(number);
//                        stringBuilder.deleteCharAt(j);
//                        int cutNumber = Integer.parseInt(stringBuilder.toString());
//                        if (prime[cutNumber] && isPalindrome(cutNumber)) {
//                            if (!list.contains(i)) {
//                                list.add(i);
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        printList();
//    }

    // if we don't include palindromic primes without removing 1 digit
    @Override
    public void execute() {
        boolean[] prime = new boolean[numberB + 1];
        Arrays.fill(prime, true);
        fillPrimeArray(prime);

        for (int i = numberA; i <= numberB; i++) {
            if (i >= 10) {
                String number = String.valueOf(i);
                for (int j = 0; j < number.length(); j++) {
                    StringBuilder stringBuilder = new StringBuilder(number);
                    stringBuilder.deleteCharAt(j);
                    int cutNumber = Integer.parseInt(stringBuilder.toString());
                    if (prime[cutNumber] && isPalindrome(cutNumber)) {
                        if (!list.contains(i)) {
                            list.add(i);
                        }
                    }
                }
            }
        }
        printList();
    }

    private void fillPrimeArray(boolean[] prime) {
        prime[0] = false;
        prime[1] = false;
        for (int i = 2; i * i <= numberB; i++) {
            if (prime[i]) {
                // check for multiples of i
                for (int j = i * 2; j <= numberB; j += i) {
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
    }
}
