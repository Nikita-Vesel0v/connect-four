package connectfour

class Player(val name: String, val sign: Char)


class ConnectFour {
    private var rows = 6
    private var columns = 7
    private var players = mutableMapOf<Int, Player>()
    private var board: MutableList<MutableList<Char>>

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
    fun makeMove() {
        var turnNum = 0
        var input: String // means column where will put 'o' or '*'
        var column: Int
        var row: Int
        while (true) {

            println("${players[turnNum % 2]?.name}'s turn:")
            input = readln()
            if (input == "end") { println("Game over!"); return }
            try {
                column = input.toInt()
                if (column !in 1..columns) {
                    println("The column number is out of range (1 - $columns)")
                    continue
                }
            } catch (e: Exception) {
                println("Incorrect column number")
                continue
            }
            column--
            if (' ' !in board[column]) { println("Column ${++column} is full"); continue }
            row = board[column].indexOfLast { it == ' ' }
            board[column][row] = players[turnNum++ % 2]?.sign!!
            printBoard()
        }
    }
}


fun main() {
    val connectFour = ConnectFour()
    connectFour.makeMove()
}