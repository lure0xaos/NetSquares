package gargoyle.netsquares.model

import gargoyle.netsquares.util.Rnd

class Board {
    var caretX: Int
        private set
    var caretY: Int
        private set
    private var cells: Array<IntArray>
    var left: Int
        private set
    var size: Int
        private set
    var weight: Int
        private set

    init {
        cells = NO_CELLS
        size = 0
        weight = 0
        left = 0
        caretX = 0
        caretY = 0
    }

    fun canMoveBy(move: Move): Boolean {
        return move == Move.OPEN || canMoveBy(requireNotNull(move.direction))
    }

    private fun canMoveBy(direction: Direction): Boolean {
        return canMoveBy(direction.deltaX, direction.deltaY)
    }

    private fun canMoveBy(deltaX: Int, deltaY: Int): Boolean {
        return isIn(caretX + deltaX, caretY + deltaY)
    }

    fun canMoveBy(direction: Direction, step: Int): Boolean {
        return canMoveBy(direction.deltaX * step, direction.deltaY * step)
    }

    fun canOpenBy(move: Move): Boolean {
        return move == Move.OPEN && canOpen() || canOpenBy(requireNotNull(move.direction))
    }

    private fun canOpenBy(direction: Direction): Boolean {
        return canOpenBy(direction.deltaX, direction.deltaY)
    }

    fun canOpen(): Boolean {
        return !isOpenAtCaret
    }

    private fun canOpenBy(deltaX: Int, deltaY: Int): Boolean {
        return canMoveBy(deltaX, deltaY) && !isOpenBy(deltaX, deltaY)
    }

    private fun isOpenBy(deltaX: Int, deltaY: Int): Boolean {
        val x = caretX + deltaX
        val y = caretY + deltaY
        return isIn(x, y) && isOpenAt(x, y)
    }

    private fun isIn(x: Int, y: Int): Boolean {
        return x >= 0 && y >= 0 && x < size && y < size
    }

    fun isOpenAt(x: Int, y: Int): Boolean {
        return cells[x][y] == 0
    }

    fun canOpenBy(direction: Direction, step: Int): Boolean {
        return canOpenBy(direction.deltaX * step, direction.deltaY * step)
    }

    fun each(operation: (Int, Int)-> Int?) {
        for (x in 0 until size) {
            for (y in 0 until size) {
                val res = operation(x, y)
                if (res != null) {
                    cells[x][y] = res
                }
            }
        }
    }

    val atCaret: Int
        get() = getAt(caretX, caretY)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val board = other as Board
        return caretX == board.caretX && caretY == board.caretY && left == board.left && size == board.size && weight == board.weight &&
                cells.contentEquals(board.cells)
    }

    override fun toString(): String {
        return String.format(
            "Board{size=%d, weight=%d, caretX=%d, caretY=%d, left=%d, cells=%s}",
            size,
            weight,
            caretX,
            caretY,
            left,
            cells.contentDeepToString()
        )
    }

    private fun getBy(deltaX: Int, deltaY: Int): Int {
        return getAt(caretX + deltaX, caretY + deltaY)
    }

    fun getAt(x: Int, y: Int): Int {
        return if (isIn(x, y)) cells[x][y] else 0
    }

    fun getBy(direction: Direction, step: Int): Int {
        return getBy(direction.deltaX * step, direction.deltaY * step)
    }

    override fun hashCode(): Int {
        var result = arrayOf(caretX, caretY, left, size, weight).contentHashCode()
        result = 31 * result + cells.contentHashCode()
        return result
    }

    fun getBy(move: Move): Int {
        return if (move == Move.OPEN) atCaret else getBy(requireNotNull(move.direction))
    }

    private fun getBy(direction: Direction): Int {
        return getBy(direction.deltaX, direction.deltaY)
    }

    fun init(size: Int, weight: Int) {
        cells = Array(size) { IntArray(size) }
        for (x in 0 until size) {
            cells[x] = IntArray(size)
            for (y in 0 until size) {
                while (cells[x][y] == 0) {
                    cells[x][y] = Rnd.rnd(-weight, weight)
                }
            }
        }
        this.size = size
        this.weight = weight
        left = size * size
        caretX = size / 2
        caretY = size / 2
    }

    fun isCaretAt(x: Int, y: Int): Boolean {
        return x == caretX && y == caretY
    }

    fun isOpenBy(direction: Direction, step: Int): Boolean {
        return isOpenBy(direction.deltaX * step, direction.deltaY * step)
    }

    fun isOpenBy(move: Move): Boolean {
        return if (move == Move.OPEN) isOpenAtCaret else isOpenBy(requireNotNull(move.direction))
    }

    private val isOpenAtCaret: Boolean
        get() = cells[caretX][caretY] == 0

    private fun isOpenBy(direction: Direction): Boolean {
        return isOpenBy(direction.deltaX, direction.deltaY)
    }

    fun moveBy(move: Move) {
        if (move == Move.OPEN) {
            openAtCaret()
        } else {
            moveBy(requireNotNull(move.direction))
        }
    }

    fun openAtCaret(): Int {
        val r = cells[caretX][caretY]
        cells[caretX][caretY] = 0
        if (r != 0) {
            left--
        }
        return r
    }

    private fun moveBy(deltaX: Int, deltaY: Int): Boolean {
        val move = canMoveBy(deltaX, deltaY)
        if (move) {
            caretX += deltaX
            caretY += deltaY
        }
        return move
    }

    private fun moveBy(direction: Direction) {
        moveBy(direction.deltaX, direction.deltaY)
    }

    fun openBoard() {
        each { _: Int?, _: Int? -> 0 }
        left = 0
    }

    fun reInit() {
        init(size, weight)
    }

    fun moveBy(direction: Direction, step: Int): Boolean {
        return moveBy(direction.deltaX * step, direction.deltaY * step)
    }

    companion object {
        private val NO_CELLS = Array(0) { IntArray(0) }
    }
}
