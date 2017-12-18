package gargoyle.netsquares.res

fun interface Loader<R : Any> {
    fun load(context: NSContext, location: String, suffix: String): R
}
