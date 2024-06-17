package connectfour

class Player(val name: String, val sign: Char, var score: Int = 0)

class ConnectFour {
    private var rows = 6
    private var columns = 7
    private var players = mutableMapOf<Int, Player>()
    private var board = mutableListOf<MutableList<Char>>()
    private var col = 0 // current column in turn
    private lateinit var curPlayer: Player
    private var turnNum = 0
    private var cntGames = 1

    init {
        println("Connect Four")
        setPlayers()
        setBoardSize()
        setCountGames()
        println("${players[0]?.name} VS ${players[1]?.name}")
        println("$rows X $columns board")
        println(if (cntGames == 1) "Single game" else "Total $cntGames games")
    }

    fun menu() {
        var result: String
        for (gameNum in 1..cntGames) {
            if (cntGames > 1) println("Game #$gameNum")
            createEmptyBoard()
            printBoard()
            result = game()
            when (result) {
                "end" -> break
                "draw" -> println("It is a draw")
                else -> println("Player $result won")
            }
            if (cntGames > 1) {
                println("""
                Score
                ${players[0]?.name}: ${players[0]?.score} ${players[1]?.name}: ${players[1]?.score}
            """.trimIndent())
            }
        }
        println("Game over!")
    }
    private fun setCountGames() {
        var input: String
        while (true) {
            println("Do you want to play single or multiple games?\n" +
                    "For a single game, input 1 or press Enter\n" +
                    "Input a number of games:")
            input = readln()
            cntGames = try {
                when {
                    input.isBlank() -> 1
                    input.toInt() < 1 -> throw Exception()
                    else -> input.toInt()
                }
            } catch (e: Exception) {
                println("Invalid input"); continue
            }
            break
        }

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
    private fun createEmptyBoard() {
        board = MutableList(columns) { MutableList(rows) { ' ' } }
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
    private fun result(): String {
        val boardToString: String = buildString { board.forEach { c -> append(c.joinToString("") + "n") } } // n == \n
        // for checking with Regex

        if (boardToString.count { it == ' '} == 0) {
            players.forEach {it.value.score++} // +1 for each player
            return "draw"
        }
        val regexPattern = buildString {
            append(".*([${curPlayer.sign}]{4}|")
            append("([${curPlayer.sign}].{$rows}){3}[${curPlayer.sign}]|")
            append("([${curPlayer.sign}].{${rows - 1}}){3}[${curPlayer.sign}]|")
            append("([${curPlayer.sign}].{${rows + 1}}){3}[${curPlayer.sign}]).*")
        }
        if (Regex(regexPattern).matches(boardToString)) {
            curPlayer.score += 2
            return "win"
        }
        return "next turn"
    }
    private fun game(): String {
        var input: String // for checking input data
        var row: Int
        while (true) {
            curPlayer = players[turnNum % 2]!! // set player
            println("${curPlayer.name}'s turn:")
            input = readln()
            if (input == "end") return "end"
            if (!inputCorrect(input)) continue

            row = board[col].indexOfLast { it == ' ' }
            board[col][row] = curPlayer.sign // set sign of current player in empty place

            printBoard()
            turnNum++
            when (result()) {
                "draw" -> return "draw"
                "win" -> return curPlayer.name
            }
        }
    }
}

fun main() {
    val connectFour = ConnectFour()
    connectFour.menu()
}