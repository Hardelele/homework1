package ru.digitalhabbits.homework1.services;

import org.slf4j.Logger;
import ru.digitalhabbits.homework1.plugin.PluginInterface;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarFile;

import static com.google.common.collect.Lists.newArrayList;
import static org.slf4j.LoggerFactory.getLogger;

public class PluginLoader {
    private static final Logger logger = getLogger(PluginLoader.class);

    private static final String PLUGIN_EXT = "jar";
    private static final String PACKAGE_TO_SCAN = "ru.digitalhabbits.homework1.plugin";

    @Nonnull
    public List<Class<? extends PluginInterface>> loadPlugins(@Nonnull String pluginDirName) {
        final List<Class<? extends PluginInterface>> pluginsList = new ArrayList<>();
        File pluginDir = new File(pluginDirName);
        File[] jarFiles = getFilesAsArray(pluginDir);
        nonEmptyCheck(jarFiles);
        fillPluginsListWithPluginsLoadedThrowUriClassLoader(jarFiles, pluginsList);
        return pluginsList;
    }

    private void nonEmptyCheck(@Nonnull File[] files) {
        if (files.length == 0) {
            throw new IllegalStateException();
        }
    }

    private void fillPluginsListWithPluginsLoadedThrowUriClassLoader(
            @Nonnull File[] jarFiles,
            @Nonnull List<Class<? extends PluginInterface>> pluginsList) {

        List<URL> urls = loadURLs(jarFiles);
        List<String> classes = loadAllClassesAsList(jarFiles);

        URLClassLoader urlClassLoader = new URLClassLoader(urls.toArray(new URL[0]));
        for (String className :classes) {
            String formattedClassName = className
                    .replaceAll("/", ".")
                    .replace(".class", "");
            try {
                Class loadClass = urlClassLoader.loadClass(formattedClassName);
                Class<?>[] interfaces = loadClass.getInterfaces();
                for (Class<?> i : interfaces) {
                    if (i.equals(PluginInterface.class)) {
                        pluginsList.add(loadClass);
                        break;
                    }
                }
            } catch (ClassNotFoundException exception) {
                exception.printStackTrace();
            }
        }
    }

    private List<URL> loadURLs(File[] jarFiles) {
        List<URL> urls = new ArrayList<>(jarFiles.length);
        for (File file : jarFiles) {
            URL url = tryToGetUrlOrThrowNPE(file);
            urls.add(url);
        }
        return urls;
    }

    private List<String> loadAllClassesAsList(File[] jarFiles) {
        ArrayList<String> classes = newArrayList();
        for (File file : jarFiles) {
            classes.addAll(getClassAndAddArrayList(file));
        }
        return classes;
    }

    private URL tryToGetUrlOrThrowNPE(File file) {
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException exception) {
            throw new NullPointerException();
        }
    }

    @Nonnull
    private File[] getFilesAsArray(@Nonnull File pluginDir) {
        return Objects.requireNonNull(
                pluginDir.listFiles(
                        (dir, filename) -> filename.endsWith("." + PLUGIN_EXT)
                )
        );
    }

    private ArrayList<String> getClassAndAddArrayList (File file)  {
        ArrayList<String> list = newArrayList();
        JarFile jarFile = null;
        try {
            jarFile = new JarFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert jarFile != null;
        jarFile.stream().forEach(jarEntry -> {
            if (jarEntry.getName().endsWith(".class")) {
                list.add(jarEntry.getName());
            }
        });
        return list;
    }
}
