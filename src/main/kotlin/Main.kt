package connectfour

class ConnectFour(boardSize: List<Int>) {
    private var rows: Int = boardSize[0]
    private var columns: Int = boardSize[1]

    fun getBoardSize() = "$rows X $columns board"
}

fun main() {
    println("Connect Four")
    println("First player's name:")
    val firstPlayerName = readln()
    println("Second player's name:")
    val secondPlayerName = readln()

    var bordSize: List<Int>
    while (true) {
        println("Set the board dimensions (Rows x Columns)")
        println("Press Enter for default (6 x 7)")
        val input = readln()
        bordSize =
            try {
                if (input.isEmpty()) listOf(6, 7) // by default
                else input.trim().split(Regex("\\s*[Xx]\\s*")).map(String::toInt)
            } catch (e: Exception) {
                println("Invalid input"); continue
            }

        when {
            bordSize[0] !in 5..9 -> println("Board rows should be from 5 to 9")
            bordSize[1] !in 5..9 -> println("Board columns should be from 5 to 9")
            else -> break
        }
        continue
    }

    val connectFour = ConnectFour(bordSize)
    println("$firstPlayerName VS $secondPlayerName")
    println(connectFour.getBoardSize())
}