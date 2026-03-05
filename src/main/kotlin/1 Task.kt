fun main() {
    println("=== Задача 1: Поиск среднего числа ===")
    // Тестовые примеры для первой задачи
    testFirstTask(3.0, 5.0, 4.0)    // среднее - 4.0
    testFirstTask(2.0, 2.0, 3.0)    // нет среднего (есть равные)
    testFirstTask(1.0, 10.0, 5.0)   // среднее - 5.0
    testFirstTask(7.0, 3.0, 9.0)    // среднее - 7.0
    testFirstTask(4.0, 4.0, 4.0)    // нет среднего (все равны)

    println("\n=== Задача 2: Взаимное расположение прямоугольников ===")
    // Тестовые примеры для второй задачи
    println("Прямоугольник A: left=1, right=5, bottom=1, top=4")
    println("Прямоугольник B: left=2, right=4, bottom=2, top=3")
    testSecondTask(1.0, 5.0, 1.0, 4.0, 2.0, 4.0, 2.0, 3.0)  // B внутри A

    println("\nПрямоугольник A: left=1, right=5, bottom=1, top=4")
    println("Прямоугольник B: left=0, right=6, bottom=0, top=5")
    testSecondTask(1.0, 5.0, 1.0, 4.0, 0.0, 6.0, 0.0, 5.0)  // A внутри B

    println("\nПрямоугольник A: left=1, right=5, bottom=1, top=4")
    println("Прямоугольник B: left=3, right=7, bottom=2, top=5")
    testSecondTask(1.0, 5.0, 1.0, 4.0, 3.0, 7.0, 2.0, 5.0)  // пересекаются

    println("\nПрямоугольник A: left=1, right=5, bottom=1, top=4")
    println("Прямоугольник B: left=6, right=8, bottom=2, top=3")
    testSecondTask(1.0, 5.0, 1.0, 4.0, 6.0, 8.0, 2.0, 3.0)  // не пересекаются
}

// ==================== ВСПОМОГАТЕЛЬНЫЕ ФУНКЦИИ ДЛЯ ТЕСТИРОВАНИЯ ====================

fun testFirstTask(a: Double, b: Double, c: Double) {
    println("Числа: a=$a, b=$b, c=$c")
    println("Решение через if:  ${findMiddleIf(a, b, c)}")
    println("Решение через when: ${findMiddleWhen(a, b, c)}")
    println()
}

fun testSecondTask(
    left1: Double, right1: Double, bottom1: Double, top1: Double,
    left2: Double, right2: Double, bottom2: Double, top2: Double
) {
    println("Решение через if:  ${rectanglesRelationIf(left1, right1, bottom1, top1, left2, right2, bottom2, top2)}")
    println("Решение через when: ${rectanglesRelationWhen(left1, right1, bottom1, top1, left2, right2, bottom2, top2)}")
    println()
}

// ==================== ЗАДАЧА 1: ПОИСК СРЕДНЕГО ЧИСЛА ====================

// Решение через if
fun findMiddleIf(a: Double, b: Double, c: Double): String {
    // Проверка на наличие равных чисел (тогда нет строго среднего)
    if (a == b || a == c || b == c) {
        return "Такой ситуации нет (есть равные числа)"
    }

    // Поиск среднего числа
    if ((a > b && a < c) || (a < b && a > c)) {
        return "Среднее число: a = $a"
    } else if ((b > a && b < c) || (b < a && b > c)) {
        return "Среднее число: b = $b"
    } else if ((c > a && c < b) || (c < a && c > b)) {
        return "Среднее число: c = $c"
    }

    return "Такой ситуации нет"
}

// Решение через when
fun findMiddleWhen(a: Double, b: Double, c: Double): String {
    // Проверка на наличие равных чисел
    when {
        a == b || a == c || b == c -> return "Такой ситуации нет (есть равные числа)"
    }

    // Поиск среднего числа с использованием when как выражения
    return when {
        (a > b && a < c) || (a < b && a > c) -> "Среднее число: a = $a"
        (b > a && b < c) || (b < a && b > c) -> "Среднее число: b = $b"
        (c > a && c < b) || (c < a && c > b) -> "Среднее число: c = $c"
        else -> "Такой ситуации нет"
    }
}

// ==================== ЗАДАЧА 2: ВЗАИМНОЕ РАСПОЛОЖЕНИЕ ПРЯМОУГОЛЬНИКОВ ====================

// Решение через if
fun rectanglesRelationIf(
    left1: Double, right1: Double, bottom1: Double, top1: Double,
    left2: Double, right2: Double, bottom2: Double, top2: Double
): String {

    // Проверка: находится ли прямоугольник 2 внутри прямоугольника 1
    if (left2 >= left1 && right2 <= right1 && bottom2 >= bottom1 && top2 <= top1) {
        return "Второй прямоугольник находится внутри первого"
    }

    // Проверка: находится ли прямоугольник 1 внутри прямоугольника 2
    if (left1 >= left2 && right1 <= right2 && bottom1 >= bottom2 && top1 <= top2) {
        return "Первый прямоугольник находится внутри второго"
    }

    // Проверка на пересечение (если не внутри, но проекции пересекаются по обеим осям)
    if (right1 > left2 && right2 > left1 && top1 > bottom2 && top2 > bottom1) {
        return "Прямоугольники пересекаются"
    }

    return "Прямоугольники не пересекаются"
}

// Решение через when
fun rectanglesRelationWhen(
    left1: Double, right1: Double, bottom1: Double, top1: Double,
    left2: Double, right2: Double, bottom2: Double, top2: Double
): String {

    return when {
        // Проверка: находится ли прямоугольник 2 внутри прямоугольника 1
        left2 >= left1 && right2 <= right1 && bottom2 >= bottom1 && top2 <= top1 ->
            "Второй прямоугольник находится внутри первого"

        // Проверка: находится ли прямоугольник 1 внутри прямоугольника 2
        left1 >= left2 && right1 <= right2 && bottom1 >= bottom2 && top1 <= top2 ->
            "Первый прямоугольник находится внутри второго"

        // Проверка на пересечение
        right1 > left2 && right2 > left1 && top1 > bottom2 && top2 > bottom1 ->
            "Прямоугольники пересекаются"

        // Во всех остальных случаях не пересекаются
        else -> "Прямоугольники не пересекаются"
    }
}