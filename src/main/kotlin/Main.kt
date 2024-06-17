package connectfour

class Player(val name: String, val sign: Char)

class ConnectFour {
    private var rows = 6
    private var columns = 7
    private var players = mutableMapOf<Int, Player>()
    private var board = mutableListOf<MutableList<Char>>()
    private var col = 0 // current column in turn

    init {
        println("Connect Four")
        setPlayers()
        setBoardSize()
        println("${players[0]?.name} VS ${players[1]?.name}")
        println("$rows X $columns board")
        board = MutableList(columns) { MutableList(rows) { ' ' } }
        printBoard()
    }
    private fun setBoardSize() {
        var sizes: List<Int>
        while (true) {
            println("Set the board dimensions (Rows x Columns)")
            println("Press Enter for default (6 x 7)")
            val input = readln()
            sizes = try {
                if (input.isEmpty()) listOf(6, 7)  // by default
                else input.trim().split(Regex("\\s*[Xx]\\s*")).map(String::toInt)
            } catch (e: Exception) {
                println("Invalid input"); continue
            }
            when {
                sizes[0] !in 5..9 -> println("Board rows should be from 5 to 9")
                sizes[1] !in 5..9 -> println("Board columns should be from 5 to 9")
                else -> break
            }
        }
        rows = sizes[0]; columns = sizes[1]
    }
    private fun printBoard() {
        var col = 1
        repeat(columns) { print(" ${col++}") }; println()
        var row = 0; col = 0
        repeat(rows) { print("║"); repeat(columns) { print("${board[col++][row]}║") }; println(); row++; col = 0 }
        print("╚"); repeat(columns - 1) { print("═╩") }; println("═╝")
    }
    private fun setPlayers() {
        println("First player's name:")
        players[0] = Player(readln(), 'o')
        println("Second player's name:")
        players[1] = Player(readln(), '*')
    }
    private fun inputCorrect(input: String): Boolean {
        try {
            col = input.toInt() - 1
        } catch (e: Exception) {
            println("Incorrect column number"); return false
        }
        when {
            notInColumnRange()  -> { println("The column number is out of range (1 - $columns)"); return false }
            columnIsFull() -> { println("Column ${col + 1} is full"); return false }
            else -> return true
        }
    }
    private fun notInColumnRange() = col !in 0 until columns
    private fun columnIsFull() = ' ' !in board[col]

    fun makeMove(): String {
        var turnNum = 0
        var input: String // for checking input data
        var row: Int
        var curPlayer: Player
        var boardToString: String // for checking with Regex
        while (true) {
            curPlayer = players[turnNum % 2]!! // set player
            println("${curPlayer.name}'s turn:")
            input = readln()
            if (input == "end") return "end"
            if (!inputCorrect(input)) continue

            row = board[col].indexOfLast { it == ' ' }
            board[col][row] = curPlayer.sign // set sign of current player in empty place
            turnNum++ // set next player
            printBoard()

            boardToString = buildString { board.forEach { c -> append(c.joinToString("") + "n") } } // n == \n
            if (boardToString.count { it == ' '} == 0) return "draw"
            val regexPattern = buildString {
                append(".*([${curPlayer.sign}]{4}|")
                append("([${curPlayer.sign}].{$rows}){3}[${curPlayer.sign}]|")
                append("([${curPlayer.sign}].{${rows - 1}}){3}[${curPlayer.sign}]|")
                append("([${curPlayer.sign}].{${rows + 1}}){3}[${curPlayer.sign}]).*")
            }
            if (Regex(regexPattern).matches(boardToString)) return curPlayer.name
        }
    }
}

fun main() {
    val connectFour = ConnectFour()
    val result = connectFour.makeMove()
    println( when (result) {
        "end" -> ""
        "draw" -> "It is a draw"
        else -> "Player $result won"
    } + "\nGame over!")
}