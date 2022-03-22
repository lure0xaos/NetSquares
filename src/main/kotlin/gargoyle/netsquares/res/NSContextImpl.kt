package gargoyle.netsquares.res

import gargoyle.netsquares.util.Maps
import java.awt.Font
import java.awt.Image
import java.nio.charset.Charset
import java.util.Locale
import java.util.ResourceBundle
import javax.swing.ImageIcon
import kotlin.reflect.KClass

class NSContextImpl : NSContext {
    override var charset: Charset = Charsets.UTF_8
    override var classLoader: ClassLoader = NSContext::class.java.classLoader
    override var locale: Locale = Locale.getDefault()

    override fun loadAudio(name: String, suffix: String): AudioClip {
        return cache[this, name, AudioClip::class, suffix]
    }

    override fun loadMessages(name: String): Messages {
        return Messages(this, name)
    }

    override fun loadBundle(name: String): ResourceBundle {
        return ResourceBundle.getBundle(name, locale, classLoader)
    }

    override fun loadFont(name: String): Font {
        return cache[this, name, Font::class, "ttf"]
    }

    override fun loadImage(name: String, suffix: String): Image {
        return cache[this, name, Image::class, suffix]
    }

    companion object {
        private val cache = HybridCache(
            Maps.map<KClass<*>, Loader<String, *>>(
                ResourceBundle::class,
                Loader { context: NSContext, location: String, _: String -> context.loadBundle(location) },
                Font::class,
                Loader { context: NSContext, location: String, _: String ->
                    Font.createFont(
                        Font.TRUETYPE_FONT,
                        context.open(location)
                    )
                } as Loader<String, Font>,
                AudioClip::class,
                Loader<String, AudioClip> { context: NSContext, location: String, suffix: String ->
                    AudioClipImpl(context.find(location, suffix))
                },
                Image::class,
                Loader { context: NSContext, location: String, suffix: String ->
                    ImageIcon(
                        context.find(
                            location,
                            suffix
                        )
                    ).image
                } as Loader<String, Image>
            )
        )
    }
}
