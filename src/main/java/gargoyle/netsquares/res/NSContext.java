package gargoyle.netsquares.res;

import gargoyle.netsquares.util.Assert;

import java.awt.Font;
import java.awt.Image;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public interface NSContext {
    default URL find(String name, String suffix) {
        Locale locale = getLocale();
        ResourceBundle.Control control = ResourceBundle.Control.getControl(ResourceBundle.Control.FORMAT_DEFAULT);
        List<Locale> candidateLocales = control.getCandidateLocales(name, locale);
        for (Locale specificLocale : candidateLocales) {
            String bundleName = control.toBundleName(name, specificLocale);
            String resourceName = control.toResourceName(bundleName, suffix);
            URL location = getClassLoader().getResource(resourceName);
            if (location != null) {
                return location;
            }
        }
        throw new IllegalArgumentException(String.format("%s not found", name));
    }

    Locale getLocale();

    ClassLoader getClassLoader();

    Charset getCharset();

    AudioClip loadAudio(String name, String suffix);

    ResourceBundle loadBundle(String name);

    Font loadFont(String name);

    Image loadImage(String name, String suffix);

    Messages loadMessages(String name);

    default InputStream open(String name) {
        InputStream resource = getClassLoader().getResourceAsStream(name);
        Assert.assertNotNull(resource, String.format("%s not found", name));
        return resource;
    }
}
