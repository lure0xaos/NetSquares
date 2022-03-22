package gargoyle.netsquares.res


object NSContextFactory {
    val lazy = lazy { NSContextImpl() }
    val context: NSContext
        get() = lazy.value

    fun currentContext(): NSContext {
        return context
    }
}
