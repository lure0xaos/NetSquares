package gargoyle.netsquares.res

import java.awt.Font
import java.awt.Image
import java.nio.charset.Charset
import java.util.*
import javax.swing.ImageIcon
import kotlin.reflect.KClass

class NSContextImpl : NSContext {
    override val charset: Charset = Charsets.UTF_8
    override val classLoader: ClassLoader = NSContext::class.java.classLoader
    override val locale: Locale = Locale.getDefault()

    override fun loadAudio(name: String, suffix: String): AudioClip = cache[this, name, AudioClip::class, suffix]

    override fun loadMessages(name: String): Messages = Messages(this, name)

    override fun loadBundle(name: String): ResourceBundle = ResourceBundle.getBundle(name, locale, classLoader)

    override fun loadFont(name: String): Font = cache[this, name, Font::class, "ttf"]

    override fun loadImage(name: String, suffix: String): Image = cache[this, name, Image::class, suffix]

    companion object {
        private val cache = HybridCache(mapOf<KClass<*>, Loader<*>>(
            ResourceBundle::class to Loader { context: NSContext, location: String, _: String ->
                context.loadBundle(location)
            },
            Font::class to Loader { context: NSContext, location: String, _: String ->
                Font.createFont(Font.TRUETYPE_FONT, context.open(location))
            },
            AudioClip::class to Loader<AudioClip> { context: NSContext, location: String, suffix: String ->
                context.find(location, suffix)?.let { AudioClipImpl(it) } ?: error("not found $location")
            },
            Image::class to Loader { context: NSContext, location: String, suffix: String ->
                ImageIcon(context.find(location, suffix)).image
            }
        ))
    }
}
