package connectfour

class Player(val name: String, val sign: Char)


class ConnectFour {
    private var rows = 6
    private var columns = 7
    private var players = mutableMapOf<Int, Player>()
    private var board = List(rows) { List(columns) { " " }  }

    init {
        println("Connect Four")
        setPlayers()
        setBoardSize()
        println("${players[0]?.name} VS ${players[1]?.name}")
        println("$rows X $columns board")
    }
    private fun setBoardSize() {
        while (true) {
            println("Set the board dimensions (Rows x Columns)")
            println("Press Enter for default (6 x 7)")
            val input = readln()

            try {
                if (input.isEmpty()) { rows = 6; columns = 7; break } // by default
                else input.trim().split(Regex("\\s*[Xx]\\s*")).map(String::toInt)
            } catch (e: Exception) {
                println("Invalid input"); continue
            }
            when {
                rows !in 5..9 -> println("Board rows should be from 5 to 9")
                columns !in 5..9 -> println("Board columns should be from 5 to 9")
                else -> break
            }
        }
    }
    fun printBoard() {
        var col = 1
        repeat(columns) { print(" ${col++}") }; println()
        var row = 0; col = 0
        repeat(rows) { print("║"); repeat(columns) { print("${board[row][col++]}║") }; println(); row++; col = 0 }
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
        var turn: String // means column where will put '0' or '*'
        while (true) {
            printBoard()
            println("${players[turnNum++ % 2]?.name}'s turn:")
            turn = readln()
            if (turn == "end") { println("Game over!"); return }
        }
    }
}



fun main() {
    val connectFour = ConnectFour()
    connectFour.makeMove()
    connectFour.printBoard()
}