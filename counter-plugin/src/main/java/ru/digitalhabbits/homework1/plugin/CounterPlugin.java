package ru.digitalhabbits.homework1.plugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CounterPlugin
        implements PluginInterface {

    @Nullable
    @Override
    public String apply(@Nonnull String text) {
        int linesCount = text.split("\n").length;
        int wordsCount = countWords(text);
        int lettersCount = text.toCharArray().length;
        return linesCount + ";" + wordsCount + ";" + lettersCount;
    }

    private int countWords(String text) {
        Matcher matcher = Pattern.compile("(\\b[a-zA-Z][a-zA-Z.0-9-]*\\b)").matcher(text);
        int counter = 0;
        while(matcher.find()) {
            counter++;
        }
        return counter;
    }
}
