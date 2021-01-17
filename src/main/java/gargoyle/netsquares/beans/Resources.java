package gargoyle.netsquares.beans;

import gargoyle.netsquares.ioc.Registry;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public final class Resources {
    private final ClassLoader classLoader;

    private Resources(final ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public static Resources forCurrentThread() throws ClassNotFoundException {
        return withClassLoader(Thread.currentThread().getContextClassLoader());
    }

    public static Resources forClass(final Class<?> clazz) throws ClassNotFoundException {
        return withClassLoader(clazz.getClassLoader());
    }

    public static Resources withClassLoader(final ClassLoader classLoader) throws ClassNotFoundException {
        return Registry.forClass(Resources.class, "withClassLoader0").provide(classLoader);
    }

    public static Resources withClassLoader0(final ClassLoader classLoader) {
        return new Resources(classLoader);
    }

    public Image loadImage(final String resource) throws IOException {
        final URL location = classLoader.getResource(resource);
        if (location == null) throw new IOException("resource not found: " + resource);
        return new ImageIcon(location).getImage();
    }

    public Font loadFont(final String resource) throws IOException {
        try {
            final InputStream stream = classLoader.getResourceAsStream(resource);
            if (stream == null) throw new IllegalArgumentException("resource not found: " + resource);
            return Font.createFont(Font.TRUETYPE_FONT, stream);
        } catch (final FontFormatException e) {
            throw new IOException("cannot load font: " + resource, e);
        } catch (final IOException e) {
            throw new IOException("cannot load font: " + resource, e);
        }
    }
}
