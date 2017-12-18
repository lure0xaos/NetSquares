package gargoyle.netsquares.res


object NSContextFactory {
    val context: NSContext by lazy { NSContextImpl() }
}
