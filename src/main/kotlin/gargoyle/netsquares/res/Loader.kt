package gargoyle.netsquares.res

fun interface Loader<L : Any, R : Any> {
    fun load(context: NSContext, location: L, suffix: String): R
}
