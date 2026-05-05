fun main() {
    val maxSize = 10

    println("Введите до $maxSize целых чисел через пробел:")

    // Считываем строку, убираем лишние пробелы по краям
    val input = readln().trim()

    // Разбиваем строку по пробелам
    val parts = input.split("\\s+".toRegex())

    // Проверка на количество
    if (parts.size > maxSize) {
        println("Ошибка: разрешён ввод до $maxSize значений, вы ввели ${parts.size}.")
        return
    }

    // Проверка, что все элементы — целые числа
    val numbers = try {
        parts.map { it.toInt() }
    } catch (_: NumberFormatException) {
        println("Ошибка: ввод должен содержать только целые числа!")
        return
    }

    // Если ничего не введено
    if (numbers.isEmpty()) {
        println("Вы не ввели ни одного числа.")
        return
    }

    // Вывод исходного массива
    println("\nИсходный массив: $numbers")

    // Удаление повторяющихся элементов
    val uniqueNumbers = numbers.distinct()

    // Вывод итога
    println("Массив без повторений: $uniqueNumbers")
}