package gargoyle.netsquares.res;

import gargoyle.netsquares.util.Maps;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.ImageIcon;
import java.awt.Font;
import java.awt.Image;
import java.util.Locale;
import java.util.ResourceBundle;

public class Resources {
    private static final HybridCache<String, Object> cache = new HybridCache<>(
            Maps.map(
                    ResourceBundle.class, (Loader<String, ResourceBundle>) (location, locale) -> ResourceBundle.getBundle(location, locale, Loader.classLoader),
                    Font.class, (Loader<String, Font>) (location, locale) -> Font.createFont(Font.TRUETYPE_FONT, Loader.open(location)),
                    AudioClip.class, (Loader<String, AudioClip>) (location, locale) -> Audio.newAudioClip(Loader.find(location)),
                    Image.class, (Loader<String, Image>) (location, locale) -> new ImageIcon(Loader.find(location)).getImage()
            )
    );

    public static @Nullable ResourceBundle loadBundle(@NotNull String name, @NotNull Locale locale) {
        return cache.get(name, ResourceBundle.class, locale);
    }

    public static @Nullable Font loadFont(@NotNull String name, @NotNull Locale locale) {
        return cache.get(name, Font.class, locale);
    }

    public static @Nullable AudioClip loadAudio(@NotNull String name, @NotNull Locale locale) {
        return cache.get(name, AudioClip.class, locale);
    }

    public static @Nullable Image loadImage(@NotNull String name, @NotNull Locale locale) {
        return cache.get(name, Image.class, locale);
    }
}
