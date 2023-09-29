import java.util.*;
import java.io.*;

public class AnagramGame {
    private List <String> anagrams;
    private Map<StringBuffer, List<String>> gameMap;
    private int points;

    static final String AnagramWordsFile = "anagram_words.txt";

    public AnagramGame() {
        List <String> anagrams = new ArrayList<>();
        try (Scanner fileReader = new Scanner(new File(AnagramWordsFile))) {
            while (fileReader.hasNext())
                anagrams.add(fileReader.next());
            this.points = 0;
            this.anagrams = anagrams;
        } catch (Exception e) {
            System.out.println("Cannot find anagram file.");
            System.exit(0);
        }
    }


    public void playGame() throws InterruptedException{
        Scanner keyb = new Scanner(System.in);
        Random rand = new Random();
        List <String> displayWords = new ArrayList<>();
        List <String> userInputs = new ArrayList<>();
        List <Boolean> isCorrect = new ArrayList<>();
        List <Integer> timeStamps = new ArrayList<>();

        System.out.println("\n========= Welcome to the Anagram Game! =========\n");
        System.out.println("Instructions: A word will pop up on the screen. If you can name one of its anagrams, you gain a point.\nTry to name as many words as you can within a certain amount of time!");

        // can become different method
        // setting up game map
        System.out.print("\nWhat difficulty would you like to play on? (Easy, Normal, or Hard): ");
        String difficulty = keyb.next().toLowerCase();
        this.gameMap = anagramMap(difficulty);
        List <StringBuffer> keys = new ArrayList<>();
        for (StringBuffer key : gameMap.keySet())
            keys.add(key);
        Thread.sleep(3000);
        for (int i = 3; i > 0; i--) {
            System.out.printf("%d\n", i);
            Thread.sleep(1000);
        }

        // can possibly become separate method
        // setting time constraint
        long startTime = System.currentTimeMillis();
        long endTime = startTime + 30000;
        if (difficulty.toLowerCase().equals("normal")) {
            endTime += 15000;
            System.out.println("You have 45 seconds. Good Luck!\n");
        } else if (difficulty.toLowerCase().equals("hard")) {
            endTime += 30000;
            System.out.println("You have 1 minute. Good Luck!\n");
        } else {
            System.out.println("You have 30 seconds. Good Luck!\n");
        }


        // can possibly turn into separate method
        while (System.currentTimeMillis() < endTime) {
            // setup displayed word
            int randKeyNdex = rand.nextInt(keys.size());
            String mainWord = gameMap.get(keys.get(randKeyNdex)).get(0);
            displayWords.add(mainWord);
            System.out.println(mainWord);
            String userAnswer = keyb.next();
            userInputs.add(userAnswer);

            if (alphabetize(mainWord).toString().equals(alphabetize(userAnswer).toString()) && isWithin(userAnswer, anagrams) && ! mainWord.equals(userAnswer)) {
                System.out.println(" +1\n");
                points += 1;
                isCorrect.add(true);
            } else {
                System.out.println(" X\n");
                isCorrect.add(false);
            }
            timeStamps.add((int) (System.currentTimeMillis() - startTime)/1000);
        }
        System.out.printf("\nTime's Up!\nYou earned %d points! Here are your stats:\n", points);
        System.out.println("Given Word  |Your Answer  |Correct?  |Time to Answer (Seconds)\n");
        for (int i = 0; i < displayWords.size(); i++) {
            System.out.printf("%-12s|%-13s|%-10s|%d\n", displayWords.get(i), userInputs.get(i), isCorrect.get(i), timeStamps.get(i));
        }
    }



    public StringBuffer alphabetize(String givenString) {
        StringBuffer result = new StringBuffer();
        List <Integer> charIntVals = new ArrayList<>();
        for (int i = 0; i < givenString.length(); i++) {
            char letter = givenString.toLowerCase().charAt(i);
            charIntVals.add((int) letter);
        }
        Collections.sort(charIntVals);
        for (int charVals : charIntVals)
            result.append((char) charVals);
        return result;
    }


    public Map<StringBuffer, List<String>> anagramMap(String difficulty) {
        Map <StringBuffer, List<String>> result = new HashMap<>();
        List <String> anagramToAlphbetizedString = new ArrayList<>();
        if (difficulty.equals("easy")) {
            assembleMap("easy", 5, result);
        } else if (difficulty.equals("normal")) {
            assembleMap("normal", 8, result);
        } else if (difficulty.equals("hard")) {
            assembleMap("hard", 8, result);
        }
        return result;
    }


    private void assembleMap(String difficulty, int num, Map<StringBuffer, List<String>> mapToEdit) {
        System.out.printf("Beginning %s difficulty...\n", difficulty);
        for (String word : anagrams) {
            if (! difficulty.toLowerCase().equals("hard")) {
                if (word.length() >= num-3 && word.length() <= num) {
                    StringBuffer stringKey = alphabetize(word);
                    mapToEdit.putIfAbsent(stringKey, new ArrayList<String>());
                    mapToEdit.get(stringKey).add(word);
                }
            } else {
                if (word.length() > num) {
                    if (mapToEdit.keySet().contains(alphabetize(word))) {
                        System.out.println("COPY!");
                    }
                    StringBuffer stringKey = alphabetize(word);
                    mapToEdit.putIfAbsent(stringKey, new ArrayList<String>());
                    mapToEdit.get(stringKey).add(word);
                }
            }
        }
    }


    private boolean isWithin(String word, List <String> allWords) {
        for (String selection : allWords) {
            if (word.equals(selection)) {
                return true;
            }
        }
        return false;
    }
}
