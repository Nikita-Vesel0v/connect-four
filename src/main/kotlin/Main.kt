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
        var input: String // for checking input data
        var col: Int
        var row: Int
        var curPlayer: Player
        while (true) {
            curPlayer = players[turnNum % 2]!!
            println("${curPlayer.name}'s turn:")
            input = readln()
            if (input == "end") { println("Game over!"); return }
            try {
                col = input.toInt() - 1
                if (col + 1 !in 1..columns) {
                    println("The column number is out of range (1 - $columns)"); continue
                }
            } catch (e: Exception) {
                println("Incorrect column number"); continue
            }
            if (' ' !in board[col]) { println("Column ${col + 1} is full"); continue }
            row = board[col].indexOfLast { it == ' ' }
            board[col][row] = curPlayer.sign
            printBoard()

            turnNum++
            val data = buildString { board.forEach { c -> append(c.joinToString("") + "n") } }
            println(data)
            val regexPattern = buildString {
                append(".*([${curPlayer.sign}]{4}|")
                append("([${curPlayer.sign}].{$rows}){3}[${curPlayer.sign}]|")
                append("([${curPlayer.sign}].{${rows - 1}}){4}|")
                append("([${curPlayer.sign}].{${rows + 1}}){4}).*")
            }
            when {
                Regex(regexPattern).matches(data)
                -> { println("Player ${curPlayer.name} won \nGame over!");return }
            }
        }
    }
}

fun main() {
    val connectFour = ConnectFour()
    connectFour.makeMove()
}