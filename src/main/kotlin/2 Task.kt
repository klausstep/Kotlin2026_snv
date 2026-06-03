fun main() {
    println("Программа находит все целые числа типа Short (16 бит),")
    println("в двоичном представлении которых содержится заданное количество единиц.")
    println("Допустимые значения количества единиц: 0..3 или 13..16.")
    println()

    val k = readValidK()

    if (k == null) {
        println("Выход из программы. До свидания!")
        return
    }

    println()

    println("=== Результат (цикл while) ===")
    processWhile(k)

    println("\n=== Результат (цикл for) ===")
    processFor(k)
}

/** проверка ввода */
fun readValidK(): Int? {
    while (true) {
        print("Введите количество единиц (0..3 или 13..16, Enter для выхода): ")
        val input = readln().trim()

        if (input.isEmpty()) {
            return null
        }

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

/**  Обрабатывает k с использованием цикла while для генерации чисел. */
fun processWhile(k: Int) {
    println("Short (16 бит):")
    generateAllShortNumbersWhile(k)
    println("Всего чисел: ${countCombinations(16, k)}")
}

/** Обрабатывает k с использованием цикла for для генерации чисел. */
fun processFor(k: Int) {
    println("Short (16 бит):")
    generateAllShortNumbersFor(k)
    println("Всего чисел: ${countCombinations(16, k)}")
}

/** Генерирует все числа типа Short с ровно k единицами, используя while. */
fun generateAllShortNumbersWhile(k: Int) {
    val bits = 16
    if (k == 0) {
        println(0)
        return
    }
    if (k == bits) {
        println((-1).toShort())
        return
    }

    // Начальное число: k единиц в младших битах
    var current = (1uL shl k) - 1uL
    // Конечное число: k единиц в старших битах
    val last = (1uL shl k) - 1uL shl (bits - k)

    while (true) {
        println(current.toShort())   // преобразование в Short даёт правильный знаковый результат
        if (current == last) break
        current = nextCombination(current)
    }
}

/** Генерирует все числа типа Short с ровно k единицами, используя for. Количество итераций заранее вычислено через биномиальный коэффициент. */
fun generateAllShortNumbersFor(k: Int) {
    val bits = 16
    if (k == 0) {
        println(0)
        return
    }
    if (k == bits) {
        println((-1).toShort())
        return
    }

    var current = (1uL shl k) - 1uL
    val total = countCombinations(bits, k)

    for (i in 1L..total) {
        println(current.toShort())
        current = nextCombination(current)
    }
}

/** Использует метод Госпера: возвращает следующее беззнаковое число с тем же количеством единиц. */
fun nextCombination(x: ULong): ULong {
    val smallest = x and (x.inv() + 1uL)   // самый младший установленный бит
    val ripple = x + smallest               // добавляем его, вызывая перенос
    val ones = x xor ripple                  // биты, изменившиеся при переносе
    val shifted = (ones shr 2) / smallest    // сдвигаем и делим для восстановления единиц
    return ripple or shifted
}

/** Вычисляет биномиальный коэффициент C(n, k) для n ≤ 16. */
fun countCombinations(n: Int, k: Int): Long {
    if (k !in 0..n) return 0L
    var k = k
    if (k > n - k) { //так уменьшается количество вычислений если k>половины n, меньше факториалов считать
    k = n - k
    }

    var result = 1L
    for (i in 1..k) {
        result = result * (n - k + i) / i
    }
    return result
}