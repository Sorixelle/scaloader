package net.thereturningvoid.scaloader;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class ScaLoader extends JavaPlugin {

    static Logger log = Logger.getLogger("Minecraft");
    static ScaLoader instance;

    public void onEnable() {
        try {
            instance = this;
            Path libDir = Paths.get("lib");
            if (!Files.exists(libDir)) {
                log.info("Library directory does not exist, creating it now...");
                Files.createDirectory(libDir);
            }

            LibraryRegistry.registerLibrary("Scala Library 2.12.3", new URL("http://central.maven.org/maven2/org/scala-lang/scala-library/2.12.3/scala-library-2.12.3.jar"), this);
            LibraryRegistry.registerLibrary("Scala Reflect 2.12.3", new URL("http://central.maven.org/maven2/org/scala-lang/scala-reflect/2.12.3/scala-reflect-2.12.3.jar"), this);

            Files.list(libDir).forEach(p -> {
                String filename = p.getFileName().toString();
                if (!LibraryRegistry.libraries.containsValue(filename)) {
                    try {
                        Files.delete(p);
                    } catch (IOException e) {
                        log.severe("IOException occurred! Details below.");
                        e.printStackTrace();
                        setEnabled(false);
                    }
                }
            });
        } catch (MalformedURLException e) {
            log.severe("MalformedURLException occurred parsing URL. This is a bug in the code, report it to TheReturningVoid.");
            e.printStackTrace();
            setEnabled(false);
        } catch (IOException e) {
            log.severe("IOException occurred! Details below.");
            e.printStackTrace();
            setEnabled(false);
        }
        log.info("Started ScaLoader v1.0");
    }

    public void onDisable() { instance = null; }

}
