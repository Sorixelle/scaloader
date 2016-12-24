package cf.retvoid.scaloader;

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
import java.util.logging.Logger;

public class ScaLoader extends JavaPlugin {

    public static Logger log = Logger.getLogger("Minecraft");

    public void onEnable() {
        Path libDir = Paths.get("lib");
        if (!Files.exists(libDir)) {
            log.info("Library directory does not exist, creating it now...");
            try {
                Files.createDirectory(libDir);
            } catch (IOException e) {
                log.severe("IOException occurred creating the library directory! Details below.");
                e.printStackTrace();
                setEnabled(false);
            }
        }
        Path scalaLibraryJar = Paths.get("lib/scala-library-2.11.8.jar");
        Path scalaReflectJar = Paths.get("lib/scala-reflect-2.11.8.jar");
        if (!Files.exists(scalaLibraryJar) || !Files.exists(scalaReflectJar)) {
            log.info("Scala library not found, downloading now...");
            downloadToFile("http://central.maven.org/maven2/org/scala-lang/scala-library/2.11.8/scala-library-2.11.8.jar", scalaLibraryJar); // scala library
            downloadToFile("http://central.maven.org/maven2/org/scala-lang/scala-reflect/2.11.8/scala-reflect-2.11.8.jar", scalaReflectJar); // scala reflect
        }
        log.info("Injecting Scala libraries into classpath...");
        try {
            URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
            Method addURL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            addURL.setAccessible(true);
            addURL.invoke(classLoader, scalaLibraryJar.toUri().toURL());
            addURL.invoke(classLoader, scalaReflectJar.toUri().toURL());
        } catch (NoSuchMethodException e) {
            log.severe("NoSuchMethodException occurred getting the \"addURL\" method of URLClassLoader. This really shouldn't happen. Ever. Is there something wrong with your Java install?");
            e.printStackTrace();
            setEnabled(false);
        } catch (MalformedURLException e) {
            log.severe("MalformedURLException occurred parsing URL. This is a bug in the code, report it to TheReturningVoid.");
            e.printStackTrace();
            setEnabled(false);
        } catch (IllegalAccessException e) {
            log.severe("IllegalAccessException occurred invoking the addURL method. This shouldn't be happening. Check your Java install.");
            e.printStackTrace();
            setEnabled(false);
        } catch (InvocationTargetException e) {
            log.severe("InvocationTargetException occurred invoking the addURL method. This could be either a bug in the code, or an issue with your Java install.");
            e.printStackTrace();
            setEnabled(false);
        }
        log.info("Started ScaLoader v1.0");
    }

    public void onDisable() { }

    private void downloadToFile(String url, Path location) {
        log.info("Downloading " + location.toString() + "...");
        try {
            URL u = new URL(url);
            ReadableByteChannel rbc = Channels.newChannel(u.openStream());
            FileOutputStream fos = new FileOutputStream(location.toString());
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            log.info("Done!");
        } catch (MalformedURLException e) {
            log.severe("MalformedURLException occurred parsing URL" + url + ". This is a bug in the code, report it to TheReturningVoid.");
            e.printStackTrace();
            setEnabled(false);
        } catch (IOException e) {
            log.severe("IOException occurred connecting to URL" + url + ".");
            e.printStackTrace();
            setEnabled(false);
        }
    }

}
