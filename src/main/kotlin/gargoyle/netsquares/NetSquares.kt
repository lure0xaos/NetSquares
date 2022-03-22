package gargoyle.netsquares

import gargoyle.netsquares.res.Messages
import gargoyle.netsquares.res.NSContext
import gargoyle.netsquares.res.NSContextFactory
import gargoyle.netsquares.store.Options
import gargoyle.netsquares.ui.NSFrame
import gargoyle.netsquares.ui.painter.NSPainter

class NetSquares private constructor(private val context: NSContext) {
    private val messages: Messages?
    private val options: Options
    private val painter: NSPainter
    private var frame: NSFrame? = null

    init {
        messages = context.loadMessages(RES_MESSAGES)
        painter = NSPainter(context, RES_BACKGROUND, RES_BACKGROUND_SUFFIX)
        options = Options(NetSquares::class, System.getProperty(ENV_USER_NAME))
    }

    private fun start() {
        frame = NSFrame(context, messages, painter, options)
        frame!!.showMe()
    }

    companion object {
        private const val ENV_USER_NAME = "user.name"
        private const val MSG_PLAYER_HUMAN = "player.human"
        private const val RES_MESSAGES = "gargoyle/netsquares/res/messages"
        private const val RES_BACKGROUND = "gargoyle/netsquares/res/img"
        private const val RES_BACKGROUND_SUFFIX = "jpg"
        @JvmStatic
        fun main(args: Array<String>) {
            NetSquares(NSContextFactory.currentContext()).start()
        }
    }
}