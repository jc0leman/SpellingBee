import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, [ADD YOUR NAME HERE]
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();

    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate() {

         String changeLetters = letters;
         //find a way to remove null indexes later
       for(int i = 1; i <= changeLetters.length(); i++)
        genRecursion("", changeLetters, i);
        // YOUR CODE HERE — Call your recursive method!
    }
    public void genRecursion(String beginFront,  String changeLetters, int length)
    {
        if(length == 0) {
            words.add(beginFront);
        }
        else
        {
            for(int i = 0; i < changeLetters.length(); i++)
            {
                genRecursion(beginFront + changeLetters.charAt(i), changeLetters.substring(0, i) +
                        changeLetters.substring(i + 1), length - 1);
            }
        }
        return;


    }
    public void rmvFirstIndex(ArrayList<String> combos, String changeLetters)
    {
        combos.add(changeLetters.substring(0, changeLetters.length() - 1));
    }

    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort() {
        mSort(words);

    }

        public void mSort(ArrayList<String> words){

        if (words.size() > 1) {
            int mid = words.size() / 2;
            ArrayList<String> leftHalf = new ArrayList<String>(words.subList(0, mid));
            ArrayList<String> rightHalf = new ArrayList<String>(words.subList(mid, words.size()));

            mSort(leftHalf);
            mSort(rightHalf);

            int i = 0, j = 0, k = 0;
            while (i < leftHalf.size() && j < rightHalf.size()) {
                if (leftHalf.get(i).compareTo(rightHalf.get(j)) < 0) {
                    words.set(k++, leftHalf.get(i++));
                } else {
                    words.set(k++, rightHalf.get(j++));
                }
            }

            while (i < leftHalf.size()) {
                words.set(k++, leftHalf.get(i++));
            }

            while (j < rightHalf.size()) {
                words.set(k++, rightHalf.get(j++));
            }
        }
    }


    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }

    }

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    public void checkWords() {

        ArrayList<String> newWords = new ArrayList<>();
        for (int i = 0; i < words.size(); i++)
        {
         if(biSearch(words.get(i))) {
             newWords.add(words.get(i));
         }

        }
        System.out.println(newWords);
        // YOUR CODE HERE
    }
    public boolean biSearch(String a) {
        int end = DICTIONARY_SIZE;
        int begin = 0;
        while (end >= begin)
        {
            int mid = (end + begin) / 2;
            if (DICTIONARY[mid].equals(a))
            {
                return true;
            }
        else if(DICTIONARY[mid].compareTo(a) > 0)
            {
                end = mid - 1;
            }
        else
            {
                begin = mid + 1;
            }
        }
    return false;
    }


    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        System.out.println();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }


}
