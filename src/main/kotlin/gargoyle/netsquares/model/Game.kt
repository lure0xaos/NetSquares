package gargoyle.netsquares.model

import gargoyle.netsquares.res.AudioClip
import gargoyle.netsquares.res.NSContext
import gargoyle.netsquares.store.Options

class Game private constructor(
    context: NSContext,
    private val options: Options,
    val board: Board,
    players: List<Player>,
    var state: GameState
) {
    private val mus: AudioClip?
    private val players: MutableList<Player>
    private val tap: AudioClip?
    private var current = 0
    private var playerDirectionsMap: Map<Player?, Directions>? = null

    constructor(context: NSContext, options: Options) : this(context, options, Board(), GameState.MENU)
    private constructor(
        context: NSContext,
        options: Options,
        board: Board,
        state: GameState,
        vararg players: Player
    ) : this(context, options, board, listOf<Player>(*players), state)

    init {
        this.players = mutableListOf()
        tap = context.loadAudio(RES_TAP, "wav")
        mus = context.loadAudio(RES_BG, "au")
        initPlayers(players)
    }

    private fun initPlayers(players: List<Player>) {
        this.players.clear()
        this.players.addAll(players)
        val playerDirectionsMap: MutableMap<Player?, Directions> = mutableMapOf()
        for (i in players.indices) {
            val player = players[i]
            val directions = if (i % 2 == 0) Directions.HORIZONTALS else Directions.VERTICALS
            playerDirectionsMap[player] = directions
        }
        this.playerDirectionsMap = playerDirectionsMap
    }

    private fun beginGame(): Game {
        resetPlayers()
        board.reInit()
        state = GameState.GAME
        return this
    }

    private fun beginGame(vararg players: Player) {
        initPlayers(listOf(*players))
        board.reInit()
        state = GameState.GAME
        if (options.sound.get() && options.music.get()) {
            musicStart()
        }
    }

    fun beginGame(boardSize: Int, boardWeight: Int, vararg players: Player) {
        board.init(boardSize, boardWeight)
        beginGame(*players)
    }

    fun musicStart() {
        mus!!.loop()
    }

    fun computeWinner(): Player? {
        var maxScore = Int.MIN_VALUE
        var winner: Player? = null
        for (player in players) {
            val score = player.score
            if (maxScore < score) {
                maxScore = score
                winner = player
            }
        }
        return winner
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val game = other as Game
        return current == game.current &&
                board == game.board &&
                players == game.players && state == game.state
    }

    fun endGame() {
        if (GameState.GAME.isInState(state)) {
            state = GameState.OPEN
            board.openBoard()
        }
        musicStop()
    }

    fun getPlayers(): List<Player> {
        return players
    }

    override fun hashCode(): Int {
        return arrayOf(board, players, current, state).contentHashCode()
    }

    fun move(): Boolean {
        if (GameState.GAME.isInState(state) && canMove()) {
            makeMove()
            return true
        }
        return false
    }

    override fun toString(): String {
        return String.format(
            "Game{state=%s, current=%d, playerDirectionsMap=%s, players=%s, board=%s}",
            state,
            current,
            playerDirectionsMap,
            players,
            board
        )
    }

    fun isAllowed(player: Player, direction: Direction): Boolean {
        return direction.isAllowed(getAllowed(player))
    }

    private fun canMove(player: Player = currentPlayer!!): Boolean {
        if (board.canOpen()) {
            return true
        }
        val directions = getAllowed(player)
        for (direction in directions.getDirections()) {
            var i = 1
            while (board.canMoveBy(direction, i)) {
                if (board.canOpenBy(direction, i)) {
                    return true
                }
                i++
            }
        }
        return false
    }

    fun isState(state: GameState): Boolean {
        return this.state == state
    }

    fun isState(vararg states: GameState): Boolean {
        return state.isInState(*states)
    }

    val currentPlayer: Player?
        get() = if (players.isEmpty()) null else players[current]

    fun getAllowed(player: Player): Directions {
        return playerDirectionsMap!![player]!!
    }

    private fun makeMove() {
        val currentPlayer = currentPlayer ?: return
        val move = currentPlayer.move(board, getAllowed(currentPlayer)) ?: return
        if (options.sound.get()) {
            tap!!.play()
        }
        if (move == Move.OPEN) {
            currentPlayer.addScore(board.openAtCaret())
            nextPlayer()
            return
        }
        if (getAllowed(currentPlayer).isAllowed(move.direction) && board.canMoveBy(move)) {
            board.moveBy(move)
        }
    }

    fun musicStop() {
        mus!!.stop()
    }

    private fun resetPlayers() {
        for (player in players) {
            player.reset()
        }
    }

    private fun nextPlayer() {
        current = (current + 1) % players.size
    }

    companion object {
        private const val RES_BG = "gargoyle/netsquares/res/Frunze-Evenings"
        private const val RES_TAP = "gargoyle/netsquares/res/tap"
        fun createGame(context: NSContext, options: Options, board: Board, vararg players: Player): Game {
            return Game(context, options, board, GameState.GAME, *players).beginGame()
        }
    }
}
