package Functions;

import Exceptions.InvalidFunctionThreeException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FunctionThree extends Function {

    private final String filePath;
    private final ArrayList<String> listOfSentences = new ArrayList<>();
    private final HashMap<String, Integer> wordCountMap = new HashMap<>();
    private final HashMap<String, Pair> sentencesMap = new HashMap<>();
    private HashMap<String, Integer> sortedWordCountMap = new HashMap<>();

    public FunctionThree(String filePath) {
        this.filePath = filePath;
    }

    // for containing of first and last sentences
    private static class Pair {
        private final String firstSentence;
        private String lastSentence;

        public Pair(String firstSentence, String lastSentence) {
            this.firstSentence = firstSentence;
            this.lastSentence = lastSentence;
        }

        public void setLastSentence(String sentence) {
            this.lastSentence = sentence;
        }

        public String getFirstSentence() {
            return firstSentence;
        }

        public String getLastSentence() {
            return lastSentence;
        }
    }

    // break text file into sentences
    private void breakIntoSentences(String text) {
        String sentence;
        for (int i = 0; i < text.length(); i++) {
            boolean nextCharIsWord = true;
            int index = text.indexOf(".");
            sentence = text.substring(0, index + 1);

            // meaning after full stop is punctuation mark
            if (!(index + 1 >= text.length()) && !Character.isLetter(text.charAt(index + 1))) {
                sentence += text.charAt(index + 1);
                nextCharIsWord = false;
            }

            if (nextCharIsWord) {
                text = text.substring(index + 1);
            } else {
                text = text.substring(index + 2);
            }
            String trimmedSentence = sentence.trim();
            listOfSentences.add(trimmedSentence);
        }
    }

    // add counter, first and last sentences into associated keywords
    private void addIntoMaps() {
        for (String currentSentence : listOfSentences) {
            String[] splitCurrentSentence = currentSentence.split("[\\p{Punct}\\s]+");

            for (String s : splitCurrentSentence) {
                String currentWord = s.toLowerCase();
                if (wordCountMap.containsKey(currentWord)) {
                    int count = wordCountMap.get(currentWord);
                    count++;
                    wordCountMap.put(currentWord, count);
                } else {
                    wordCountMap.put(currentWord, 1);
                }

                if (sentencesMap.containsKey(currentWord)) {
                    Pair pair = sentencesMap.get(currentWord);
                    pair.setLastSentence(currentSentence);
                } else {
                    sentencesMap.put(currentWord, new Pair(currentSentence, null));
                }
            }
        }
    }

    // sort the hashmap based on values, then keys if values are same
    private static HashMap<String, Integer> sortByValue(HashMap<String, Integer> unsortedMap) {
        List<Map.Entry<String, Integer>> list = new LinkedList<>(unsortedMap.entrySet());

        list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()) == 0
                ? o1.getKey().compareTo(o2.getKey())
                : o2.getValue().compareTo(o1.getValue()));
        return list.stream().collect(Collectors.toMap(Map.Entry::getKey,
                Map.Entry::getValue, (a, b) -> b, LinkedHashMap::new));
    }

    // to print the top ten words with decreasing order of frequency
    private void printTopTenWords() {
        Iterator<Map.Entry<String, Integer>> it = sortedWordCountMap.entrySet().iterator();

        int count = 0;
        String keyword;
        int frequency;
        String firstSentence;
        String lastSentence;

        while (it.hasNext() && count < 10) {
            Map.Entry<String, Integer> pair = it.next();
            keyword = pair.getKey();
            frequency = pair.getValue();
            Pair currentPair = sentencesMap.get(keyword);
            firstSentence = currentPair.getFirstSentence();
            lastSentence = currentPair.getLastSentence();
            count++;
            System.out.println("[{\"keyword\": \"" + keyword + "\", \"frequency\": " + frequency +
                    ", \"first_time\": \"" + firstSentence + "\", \"last_time\": \"" + lastSentence + "\"}].\n");
        }
    }

    @Override
    public void execute() throws InvalidFunctionThreeException, IOException {
        File file = new File(filePath);
        String text = "";
        if (file.exists()) {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                text += line;
                line = bufferedReader.readLine();
            }
        } else {
            throw new InvalidFunctionThreeException("File path is wrong/text file does not exist!");
        }

        breakIntoSentences(text);
        addIntoMaps();
        sortedWordCountMap = sortByValue(wordCountMap);
        printTopTenWords();
    }

}
