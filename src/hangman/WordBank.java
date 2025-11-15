package hangman;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class WordBank {
    private static List<String> pool = new ArrayList<>();
    private static int index = 0;

    static {
        loadWords();
        Collections.shuffle(pool);
    }

    private static void loadWords() {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(WordBank.class.getResourceAsStream("/hangman/words.txt")))) {

            pool = reader.lines()
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .collect(Collectors.toList());

        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            pool = List.of("default", "words", "here"); // fallback
        }
    }

    public static String randomWord() {
        if (pool.isEmpty()) return "empty";

        if (index >= pool.size()) {
            // reshuffle after all words used
            Collections.shuffle(pool);
            index = 0;
        }
        return pool.get(index++);
    }
}