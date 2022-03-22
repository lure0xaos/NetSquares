package gargoyle.netsquares.res

import java.awt.Font
import java.awt.Image
import java.io.InputStream
import java.net.URL
import java.nio.charset.Charset
import java.util.Locale
import java.util.ResourceBundle

interface NSContext {
    fun find(name: String, suffix: String): URL? {
        val locale = locale
        val control = ResourceBundle.Control.getControl(ResourceBundle.Control.FORMAT_DEFAULT)
        val candidateLocales = control.getCandidateLocales(name, locale)
        for (specificLocale in candidateLocales) {
            val bundleName = control.toBundleName(name, specificLocale)
            val resourceName = control.toResourceName(bundleName, suffix)
            val location = classLoader.getResource(resourceName)
            if (location != null) {
                return location
            }
        }
        throw IllegalArgumentException(String.format("$name not found"))
    }

    val locale: Locale
    val classLoader: ClassLoader
    val charset: Charset
    fun loadAudio(name: String, suffix: String): AudioClip
    fun loadBundle(name: String): ResourceBundle
    fun loadFont(name: String): Font
    fun loadImage(name: String, suffix: String): Image
    fun loadMessages(name: String): Messages
    fun open(name: String): InputStream {
        return requireNotNull(classLoader.getResourceAsStream(name)) { "$name not found" }
    }
}
