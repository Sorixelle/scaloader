package net.thereturningvoid.scaloader;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static net.thereturningvoid.scaloader.ScaLoader.log;
import static net.thereturningvoid.scaloader.ScaLoader.instance;

public class LibraryRegistry {

    public static Map<String, String> libraries = new HashMap<>();

    public static void registerLibrary(String name, URL url, JavaPlugin plugin) {
        if (libraries.get(name) != null) return;
        log.info("Plugin " + plugin.getName() + " registered library " + name);
        String filename = url.getPath().split("/")[url.getPath().split("/").length - 1];
        Path libraryJar = Paths.get("lib/" + filename);
        if (!Files.exists(libraryJar)) {
            log.info(name + " not found, downloading now...");
            downloadToFile(url, libraryJar);
        }
        log.info("Injecting " + name + " into classpath...");
        try {
            URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
            Method addURL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            addURL.setAccessible(true);
            addURL.invoke(classLoader, libraryJar.toUri().toURL());
            addURL.invoke(classLoader, libraryJar.toUri().toURL());
        } catch (NoSuchMethodException e) {
            log.severe("NoSuchMethodException occurred getting the \"addURL\" method of URLClassLoader. This really shouldn't happen. Ever. Is there something wrong with your Java install?");
            e.printStackTrace();
            Bukkit.getServer().getPluginManager().disablePlugin(instance);
        } catch (MalformedURLException e) {
            log.severe("MalformedURLException occurred parsing URL. This is a bug in the code, report it to TheReturningVoid.");
            e.printStackTrace();
            Bukkit.getServer().getPluginManager().disablePlugin(instance);
        } catch (IllegalAccessException e) {
            log.severe("IllegalAccessException occurred invoking the addURL method. This shouldn't be happening. Check your Java install.");
            e.printStackTrace();
            Bukkit.getServer().getPluginManager().disablePlugin(instance);
        } catch (InvocationTargetException e) {
            log.severe("InvocationTargetException occurred invoking the addURL method. This could be either a bug in the code, or an issue with your Java install.");
            e.printStackTrace();
            Bukkit.getServer().getPluginManager().disablePlugin(instance);
        }
        libraries.put(name, filename);
        log.info("Registered " + name + " successfully!");
    }

    private static void downloadToFile(URL url, Path location) {
        log.info("Downloading " + location.toString() + "...");
        try {
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream(location.toString());
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            log.info("Done!");
        } catch (MalformedURLException e) {
            log.severe("MalformedURLException occurred parsing URL" + url + ". This is a bug in the code, report it to TheReturningVoid.");
            e.printStackTrace();
            Bukkit.getServer().getPluginManager().disablePlugin(instance);
        } catch (IOException e) {
            log.severe("IOException occurred connecting to URL" + url + ".");
            e.printStackTrace();
            Bukkit.getServer().getPluginManager().disablePlugin(instance);
        }
    }
}
