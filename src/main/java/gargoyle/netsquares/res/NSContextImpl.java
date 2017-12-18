package gargoyle.netsquares.res;

import gargoyle.netsquares.util.Maps;

import javax.swing.ImageIcon;
import java.awt.Font;
import java.awt.Image;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.ResourceBundle;

class NSContextImpl implements NSContext {
    private static final HybridCache<String, Object> cache = new HybridCache<>(
            Maps.map(
                    ResourceBundle.class, (Loader<String, ResourceBundle>) (context, location, suffix) ->
                            context.loadBundle(location),
                    Font.class, (Loader<String, Font>) (context, location, suffix) ->
                            Font.createFont(Font.TRUETYPE_FONT, context.open(location)),
                    AudioClip.class, (Loader<String, AudioClip>) (context, location, suffix) ->
                            new AudioClipImpl(context.find(location, suffix)),
                    Image.class, (Loader<String, Image>) (context, location, suffix) ->
                            new ImageIcon(context.find(location, suffix)).getImage()
            )
    );
    private Charset charset;
    private ClassLoader classLoader;
    private Locale locale;

    public NSContextImpl() {
        locale = Locale.getDefault();
        charset = StandardCharsets.UTF_8;
        classLoader = NSContext.class.getClassLoader();
    }

    @Override
    public Charset getCharset() {
        return charset;
    }

    @Override
    public ClassLoader getClassLoader() {
        return classLoader;
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public AudioClip loadAudio(String name, String suffix) {
        return cache.get(this, name, AudioClip.class, suffix);
    }

    @Override
    public Messages loadMessages(String name) {
        return new Messages(this, name);
    }

    public ResourceBundle loadBundle(String name) {
        return ResourceBundle.getBundle(name, getLocale(), getClassLoader());
    }

    public Font loadFont(String name) {
        return cache.get(this, name, Font.class, "ttf");
    }

    public Image loadImage(String name, String suffix) {
        return cache.get(this, name, Image.class, suffix);
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }
}
