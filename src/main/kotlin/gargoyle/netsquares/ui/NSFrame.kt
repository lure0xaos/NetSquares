package gargoyle.netsquares.ui

import gargoyle.netsquares.model.Game
import gargoyle.netsquares.model.GameState
import gargoyle.netsquares.model.Player
import gargoyle.netsquares.model.player.DumbPlayer
import gargoyle.netsquares.model.player.HumanPlayer
import gargoyle.netsquares.prop.base.BaseRWProperty
import gargoyle.netsquares.prop.base.IROProperty
import gargoyle.netsquares.prop.base.IRWProperty
import gargoyle.netsquares.res.Messages
import gargoyle.netsquares.res.NSContext
import gargoyle.netsquares.store.Options
import gargoyle.netsquares.ui.painter.NSPainter
import gargoyle.netsquares.util.SwingDraw
import java.awt.BorderLayout
import java.awt.Container
import java.awt.Dimension
import java.awt.event.ActionEvent
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import javax.swing.AbstractAction
import javax.swing.AbstractButton
import javax.swing.JCheckBoxMenuItem
import javax.swing.JFrame
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem
import javax.swing.JOptionPane

class NSFrame(private val context: NSContext, private val messages: Messages?, painter: NSPainter, options: Options) :
    JFrame() {
    private val game: Game
    private val service: ScheduledExecutorService
    private var board: NSBoard? = null
    private var info: NSInfo? = null

    init {
        game = Game(context, options)
        init(contentPane, painter)
        jMenuBar = initMenuBar(messages, options)
        board!!.game=(game)
        info!!.game=(game)
        service = Executors.newSingleThreadScheduledExecutor()
        service.scheduleAtFixedRate({
            if (game.isState(GameState.GAME) && !game.move()) {
                onGameOver(game, game.computeWinner())
            }
            repaint()
        }, 1000, 300, TimeUnit.MILLISECONDS)
        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent) {
                destroy(service)
            }

            override fun windowActivated(e: WindowEvent) {
                board!!.requestFocusInWindow()
            }
        })
    }

    private fun init(pane: Container, painter: NSPainter) {
        title = messages!![MSG_TITLE]
        iconImage = context.loadImage(RES_ICON, "png")
        pane.layout = BorderLayout()
        board = NSBoard(painter)
        pane.add(board!!, BorderLayout.CENTER)
        info = NSInfo(messages, painter)
        pane.add(info!!, BorderLayout.EAST)
        addComponentListener(object : ComponentAdapter() {
            override fun componentResized(e: ComponentEvent) {
                val size = size
                val min = size.width.coerceAtMost(size.height)
                val boardSize = Dimension(min, min)
                board!!.preferredSize = boardSize
                board!!.minimumSize = boardSize
                board!!.maximumSize = boardSize
                val infoSize = Dimension(size.width - min, min)
                info!!.preferredSize = infoSize
                info!!.minimumSize = infoSize
                info!!.maximumSize = infoSize
                revalidate()
            }
        })
        val bounds = SwingDraw.maximumWindowBounds
        pane.preferredSize = Dimension(bounds.width.toInt() / 2, bounds.height.toInt() / 2)
        pack()
        setLocationRelativeTo(null)
    }

    private fun initMenuBar(messages: Messages?, options: Options): JMenuBar {
        val menuBar = JMenuBar()
        menuBar.add(initGameMenu(messages, options))
        menuBar.add(initOptionsMenu(messages, options))
        return menuBar
    }

    private fun onGameOver(game: Game, winner: Player?) {
        if (game.isState(GameState.GAME)) {
            doEnd()
            game.endGame()
            repaint()
            val message = if (winner == null) messages!![MSG_DRAW] else messages!!.format(MSG__WINNER_S, winner.name)
            JOptionPane.showMessageDialog(this, message)
        }
    }

    private fun destroy(service: ExecutorService) {
        service.shutdown()
        dispose()
    }

    private fun initGameMenu(messages: Messages?, options: Options): JMenu {
        val menu = JMenu(messages!![MSG_MENU_GAME])
        menu.add(JMenuItem(object : AbstractAction(messages[MSG_MENU_GAME_HVC]) {
            override fun actionPerformed(e: ActionEvent) {
                val name = options.player1().get()
                onNewGame(HumanPlayer(name), DumbPlayer(messages[MSG_PLAYER_COMPUTER]))
            }
        }))
        menu.add(JMenuItem(object : AbstractAction(messages[MSG_MENU_GAME_HVH]) {
            override fun actionPerformed(e: ActionEvent) {
                val name1 = options.player1().get()
                val name2 = options.player2().get()
                onNewGame(HumanPlayer(name1), HumanPlayer(name2))
            }
        }))
        menu.add(JMenuItem(object : AbstractAction(messages[MSG_MENU_GAME_CVC]) {
            override fun actionPerformed(e: ActionEvent) {
                onNewGame(DumbPlayer(messages[MSG_PLAYER_1]), DumbPlayer(messages[MSG_PLAYER_2]))
            }
        }))
        return menu
    }

    private fun initOptionsMenu(messages: Messages?, options: Options): JMenu {
        val menu = JMenu(messages!![MSG_MENU_OPTIONS])
        val soundProperty = options.sound()
        val musicProperty = options.music()
        val player1Property: BaseRWProperty<String> = options.player1()
        val player2Property: BaseRWProperty<String> = options.player2()
        val nameSound = messages[MSG_MENU_OPTIONS_SOUND]
        val itemSound = JCheckBoxMenuItem(object : AbstractAction(nameSound) {
            override fun actionPerformed(e: ActionEvent) {
                val soundSelected = (e.source as AbstractButton).isSelected
                soundProperty.set(soundSelected)
                toggleMusic(musicProperty, soundProperty)
            }
        })
        val sound = soundProperty.get()
        itemSound.isSelected = sound
        menu.add(itemSound)
        val nameMusic = messages[MSG_MENU_OPTIONS_MUSIC]
        val itemMusic = JCheckBoxMenuItem(object : AbstractAction(nameMusic) {
            override fun actionPerformed(e: ActionEvent) {
                val musicSelected = (e.source as AbstractButton).isSelected
                musicProperty.set(musicSelected)
                toggleMusic(musicProperty, soundProperty)
            }
        })
        val music = musicProperty.get()
        itemMusic.isSelected = music
        menu.add(itemMusic)
        menu.add(JMenuItem(object : AbstractAction(messages[MSG_MENU_OPTIONS_PLAYER_1]) {
            override fun actionPerformed(e: ActionEvent) {
                val message = messages[MSG_MENU_OPTIONS_PLAYER_1]
                ask(player1Property, message)
            }
        }))
        menu.add(JMenuItem(object : AbstractAction(messages[MSG_MENU_OPTIONS_PLAYER_2]) {
            override fun actionPerformed(e: ActionEvent) {
                val message = messages[MSG_MENU_OPTIONS_PLAYER_2]
                ask(player2Property, message)
            }
        }))
        return menu
    }

    private fun doEnd() {}
    private fun onNewGame(vararg players: Player) {
        game.beginGame(BOARD_SIZE, BOARD_WEIGHT, *players)
        doBegin()
        repaint()
    }

    private fun toggleMusic(musicProperty: IROProperty<Boolean>, soundProperty: IROProperty<Boolean>) {
        val music = musicProperty.get()
        val sound = soundProperty.get()
        if (!sound || !music) {
            game.musicStop()
        }
        if (sound && music) {
            game.musicStart()
        }
    }

    private fun ask(property: IRWProperty<String>, message: String) {
        val initialSelectionValue = property.get()
        val value = JOptionPane.showInputDialog(this, message, initialSelectionValue)
        property.set(value)
    }

    private fun doBegin() {
        board!!.requestFocusInWindow()
    }

    fun showMe() {
        isVisible = true
        requestFocus()
    }

    companion object {
        private const val BOARD_SIZE = 10
        private const val BOARD_WEIGHT = 11
        private const val MSG_DRAW = "draw"
        private const val MSG_MENU_GAME = "menu.game"
        private const val MSG_MENU_GAME_CVC = "menu.game.cvc"
        private const val MSG_MENU_GAME_HVC = "menu.game.hvc"
        private const val MSG_MENU_GAME_HVH = "menu.game.hvh"
        private const val MSG_MENU_OPTIONS = "menu.options"
        private const val MSG_MENU_OPTIONS_MUSIC = "menu.options.music"
        private const val MSG_MENU_OPTIONS_PLAYER_1 = "menu.options.player.1"
        private const val MSG_MENU_OPTIONS_PLAYER_2 = "menu.options.player.2"
        private const val MSG_MENU_OPTIONS_SOUND = "menu.options.sound"
        private const val MSG_PLAYER_1 = "player.1"
        private const val MSG_PLAYER_2 = "player.2"
        private const val MSG_PLAYER_COMPUTER = "player.computer"
        private const val MSG_TITLE = "title"
        private const val MSG__WINNER_S = "winner_s"
        private const val RES_ICON = "gargoyle/netsquares/res/icon"
    }
}
