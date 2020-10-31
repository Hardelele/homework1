package ru.digitalhabbits.homework1.plugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FrequencyDictionaryPlugin
        implements PluginInterface {

    @Nullable
    @Override
    public String apply(@Nonnull String text) {
        text = text.replaceAll("\\\\n", "\n").toLowerCase().trim();
        Map<String, Integer> wordToFrequencyMap = buildWordToFrequencyMap(text);
        StringBuilder result = new StringBuilder();
        wordToFrequencyMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> result
                        .append(entry.getKey())
                        .append(" ")
                        .append(entry.getValue())
                        .append("\n"));
        return result.toString();
    }

    private Map<String, Integer> buildWordToFrequencyMap(String text) {
        Map<String, Integer> wordToFrequencyMap = new HashMap<>();
        Matcher matcher = Pattern.compile("\\b[a-zA-Z][a-zA-Z.0-9]*\\b").matcher(text);
        while (matcher.find()) {
            String word = text.substring(matcher.start(), matcher.end());
            Integer frequency = wordToFrequencyMap.get(word);
            wordToFrequencyMap.put(word, frequency == null ? 1 : ++frequency);
        }
        return wordToFrequencyMap;
    }
}
