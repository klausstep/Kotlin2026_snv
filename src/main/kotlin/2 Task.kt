import java.math.BigInteger

fun main() {
    println("Программа находит все целые числа типа Short (16 бит),")
    println("в двоичном представлении которых содержится заданное количество единиц.")
    println("Допустимые значения количества единиц: 0..3 или 13..16.")
    println()

    val k = readValidK()
    println()

    println("=== Результат (цикл while) ===")
    processWhile(k)

    println("\n=== Результат (цикл for) ===")
    processFor(k)
}

/**
 * Запрашивает ввод, пока пользователь не введёт целое число
 * в допустимых диапазонах (0..3 или 13..16).
 */
fun readValidK(): Int {
    while (true) {
        print("Введите количество единиц (0..3 или 13..16): ")
        val input = readln().trim()
        try {
            val k = input.toInt()
            if (k in 0..3 || k in 13..16) {
                return k
            } else {
                println("Ошибка: число должно быть в диапазоне 0..3 или 13..16.")
            }
        } catch (_: NumberFormatException) {
            println("Ошибка: введите целое число.")
        }
    }
}

/**
 * Обрабатывает k с использованием цикла while для генерации чисел.
 */
fun processWhile(k: Int) {
    println("Short (16 бит):")
    generateAllShortNumbersWhile(k) { println(it) }
    println("Всего чисел: ${countCombinations(16, k)}")
}

/**
 * Обрабатывает k с использованием цикла for для генерации чисел.
 */
fun processFor(k: Int) {
    println("Short (16 бит):")
    generateAllShortNumbersFor(k) { println(it) }
    println("Всего чисел: ${countCombinations(16, k)}")
}

/**
 * Генерирует все числа типа Short с ровно k единицами, используя while.
 * Для каждого числа вызывает action.
 */
fun generateAllShortNumbersWhile(k: Int, action: (Short) -> Unit) {
    val bits = 16
    if (k == 0) {
        action(0)
        return
    }
    if (k == bits) {
        action((-1).toShort())
        return
    }

    // Начальное число: k единиц в младших битах
    var current = (1uL shl k) - 1uL
    // Конечное число: k единиц в старших битах
    val last = (1uL shl k) - 1uL shl (bits - k)

    while (true) {
        action(current.toShort())   // преобразование в Short даёт правильный знаковый результат
        if (current == last) break
        current = nextCombination(current)
    }
}

/**
 * Генерирует все числа типа Short с ровно k единицами, используя for.
 * Количество итераций заранее вычислено через биномиальный коэффициент.
 */
fun generateAllShortNumbersFor(k: Int, action: (Short) -> Unit) {
    val bits = 16
    if (k == 0) {
        action(0)
        return
    }
    if (k == bits) {
        action((-1).toShort())
        return
    }

    var current = (1uL shl k) - 1uL
    val total = countCombinations(bits, k)

    for (i in 1L..total) {
        action(current.toShort())
        if (i < total) current = nextCombination(current)
    }
}

/**
 * Использует метод Госпера (Gosper's hack): возвращает следующее беззнаковое число с тем же количеством единиц.
 */
fun nextCombination(x: ULong): ULong {
    val smallest = x and (x.inv() + 1uL)   // самый младший установленный бит
    val ripple = x + smallest               // добавляем его, вызывая перенос
    val ones = x xor ripple                  // биты, изменившиеся при переносе
    val shifted = (ones shr 2) / smallest    // сдвигаем и делим для восстановления единиц
    return ripple or shifted
}

/**
 * Вычисляет биномиальный коэффициент C(n, k) для n ≤ 16.
 */
fun countCombinations(n: Int, k: Int): Long {
    if (k !in 0..n) return 0L
    if (k == 0 || k == n) return 1L
    var k = k
    if (k > n - k) k = n - k
    var result = BigInteger.ONE
    for (i in 1..k) {
        result = result.multiply(BigInteger.valueOf((n - k + i).toLong()))
            .divide(BigInteger.valueOf(i.toLong()))
    }
    return result.toLong()
}