package ru.digitalhabbits.homework1.services;

import ru.digitalhabbits.homework1.plugin.PluginInterface;

import javax.annotation.Nonnull;
import java.util.Objects;

public class PluginEngine {

    @Nonnull
    public  <T extends PluginInterface> String applyPlugin(@Nonnull Class<T> cls, @Nonnull String text) {
        try {
            PluginInterface pluginInterface = cls.getConstructor().newInstance();
            return Objects.requireNonNull(pluginInterface.apply(text));
        } catch (Exception exception) {
            throw new NullPointerException();
        }
    }
}
