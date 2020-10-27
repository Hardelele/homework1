package ru.digitalhabbits.homework1.services;

import ru.digitalhabbits.homework1.plugin.PluginInterface;

import javax.annotation.Nonnull;

public class PluginEngine {

    @Nonnull
    public  <T extends PluginInterface> String applyPlugin(@Nonnull Class<T> cls, @Nonnull String text) {
        // TODO: NotImplemented
        return "NotImplemented";
    }
}
