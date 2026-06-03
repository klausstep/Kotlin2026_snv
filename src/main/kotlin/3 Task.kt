fun main() {
    val maxSize = 10

    while (true) {
        println("Введите до $maxSize целых чисел через пробел (или Enter для выхода):")

        val numbers = try {
            readln()
                .trim()
                .split("\\s+".toRegex())
                .map { it.toInt() }
        } catch (_: NumberFormatException) {
            println("Ошибка: ввод должен содержать только целые числа!")
            println("Попробуйте снова.\n")
            continue
        }

        // Выход по пустой строке (после trim — пустой список)
        if (numbers.isEmpty()) {
            println("Выход из программы.")
            return
        }

        // Проверка на количество
        if (numbers.size > maxSize) {
            println("Ошибка: разрешён ввод до $maxSize значений, вы ввели ${numbers.size}.")
            println("Попробуйте снова.\n")
            continue
        }

        // Всё хорошо — выводим результат
        println("\nИсходный массив: $numbers")

        val uniqueNumbers = numbers.distinct()
        println("Массив без повторений: $uniqueNumbers")

        break
    }
}