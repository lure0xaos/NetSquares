package gargoyle.netsquares.model

import gargoyle.netsquares.util.Rnd

class Board {
    var caretX: Int = 0
        private set
    var caretY: Int = 0
        private set
    private var cells: Array<IntArray> = NO_CELLS
    var left: Int = 0
        private set
    var size: Int = 0
        private set
    var weight: Int = 0
        private set

    fun canMoveBy(move: Move): Boolean =
        move == Move.OPEN || canMoveBy(requireNotNull(move.direction))

    private fun canMoveBy(direction: Direction): Boolean =
        canMoveBy(direction.deltaX, direction.deltaY)

    private fun canMoveBy(deltaX: Int, deltaY: Int): Boolean =
        isIn(caretX + deltaX, caretY + deltaY)

    fun canMoveBy(direction: Direction, step: Int): Boolean =
        canMoveBy(direction.deltaX * step, direction.deltaY * step)

    fun canOpenBy(move: Move): Boolean =
        move == Move.OPEN && canOpen() || canOpenBy(requireNotNull(move.direction))

    private fun canOpenBy(direction: Direction): Boolean =
        canOpenBy(direction.deltaX, direction.deltaY)

    fun canOpen(): Boolean = !isOpenAtCaret

    private fun canOpenBy(deltaX: Int, deltaY: Int): Boolean =
        canMoveBy(deltaX, deltaY) && !isOpenBy(deltaX, deltaY)

    private fun isOpenBy(deltaX: Int, deltaY: Int): Boolean {
        val x = caretX + deltaX
        val y = caretY + deltaY
        return isIn(x, y) && isOpenAt(x, y)
    }

    private fun isIn(x: Int, y: Int): Boolean =
        x >= 0 && y >= 0 && x < size && y < size

    fun isOpenAt(x: Int, y: Int): Boolean =
        cells[x][y] == 0

    fun canOpenBy(direction: Direction, step: Int): Boolean =
        canOpenBy(direction.deltaX * step, direction.deltaY * step)

    fun each(operation: (Int, Int) -> Int?) {
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

    override fun equals(other: Any?): Boolean =
        when {
            this === other -> true
            other == null || javaClass != other.javaClass -> false
            other is Board -> {
                caretX == other.caretX &&
                        caretY == other.caretY &&
                        left == other.left &&
                        size == other.size &&
                        weight == other.weight &&
                        cells.contentEquals(other.cells)
            }

            else -> false
        }

    override fun toString(): String =
        "Board{size=$size, weight=$weight, caretX=$caretX, caretY=$caretY, left=$left, cells=${cells.contentDeepToString()}}"

    private fun getBy(deltaX: Int, deltaY: Int): Int =
        getAt(caretX + deltaX, caretY + deltaY)

    fun getAt(x: Int, y: Int): Int =
        if (isIn(x, y)) cells[x][y] else 0

    fun getBy(direction: Direction, step: Int): Int =
        getBy(direction.deltaX * step, direction.deltaY * step)

    override fun hashCode(): Int =
        31 * arrayOf(caretX, caretY, left, size, weight).contentHashCode() + cells.contentHashCode()

    fun getBy(move: Move): Int =
        if (move == Move.OPEN) atCaret else getBy(requireNotNull(move.direction))

    private fun getBy(direction: Direction): Int =
        getBy(direction.deltaX, direction.deltaY)

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

    fun isCaretAt(x: Int, y: Int): Boolean =
        x == caretX && y == caretY

    fun isOpenBy(direction: Direction, step: Int): Boolean =
        isOpenBy(direction.deltaX * step, direction.deltaY * step)

    fun isOpenBy(move: Move): Boolean =
        if (move == Move.OPEN) isOpenAtCaret else isOpenBy(requireNotNull(move.direction))

    private val isOpenAtCaret: Boolean
        get() = cells[caretX][caretY] == 0

    private fun isOpenBy(direction: Direction): Boolean =
        isOpenBy(direction.deltaX, direction.deltaY)

    fun moveBy(move: Move) {
        when (move) {
            Move.OPEN -> openAtCaret()
            else -> moveBy(requireNotNull(move.direction))
        }
    }

    fun openAtCaret(): Int {
        val r = cells[caretX][caretY]
        cells[caretX][caretY] = 0
        if (r != 0) left--
        return r
    }

    private fun moveBy(deltaX: Int, deltaY: Int): Boolean =
        canMoveBy(deltaX, deltaY).also {
            if (it) {
                caretX += deltaX
                caretY += deltaY
            }
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

    fun moveBy(direction: Direction, step: Int): Boolean =
        moveBy(direction.deltaX * step, direction.deltaY * step)

    companion object {
        private val NO_CELLS = Array(0) { IntArray(0) }
    }
}
