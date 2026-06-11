// ========== ИНТЕРФЕЙСЫ ==========

interface Printable {
    fun printFormatted()
}

interface Inputable {
    fun inputFromConsole(): Address
}

interface AddressBookOperations {
    fun sortByHostName()
    fun searchByKeyword(keyword: String): List<Address>
    fun printAll()
}

// ========== БАЗОВЫЙ КЛАСС ==========

sealed class Address : Printable, Inputable {
    abstract val description: String
    abstract val hostName: String
    abstract fun format(): String

    override fun printFormatted() {
        println(format())
        println("   Описание: $description")
        println("   Хост/узел: $hostName")
        println("-".repeat(50))
    }
}

// ========== ТИПЫ АДРЕСОВ ==========

data class InternetAddress(
    val protocol: String,
    val host: String,
    val path: String,
    val fileName: String,
    override val description: String
) : Address() {
    override val hostName: String get() = host
    override fun format(): String = "$protocol://$host/$path/$fileName"

    override fun inputFromConsole(): Address {
        println("\n--- Ввод интернет-адреса ---")
        print("Протокол: "); val p = readln().trim()
        print("Домен: "); val h = readln().trim()
        print("Путь: "); val pa = readln().trim()
        print("Файл: "); val f = readln().trim()
        print("Описание: "); val d = readln().trim()
        return InternetAddress(p, h, pa, f, d)
    }
}

data class LocalNetworkAddress(
    val computerName: String,
    val path: String,
    val fileName: String,
    override val description: String
) : Address() {
    override val hostName: String get() = computerName
    override fun format(): String = """\\$computerName\$path\$fileName"""

    override fun inputFromConsole(): Address {
        println("\n--- Ввод адреса в локальной сети ---")
        print("Компьютер: "); val c = readln().trim()
        print("Путь: "); val p = readln().trim()
        print("Файл: "); val f = readln().trim()
        print("Описание: "); val d = readln().trim()
        return LocalNetworkAddress(c, p, f, d)
    }
}

data class LocalResourceAddress(
    val driveLetter: Char,
    val path: String,
    val fileName: String,
    override val description: String
) : Address() {
    override val hostName: String get() = "LocalDrive_$driveLetter"
    override fun format(): String = """$driveLetter:\$path\$fileName"""

    override fun inputFromConsole(): Address {
        println("\n--- Ввод локального ресурса ---")
        print("Буква диска: "); val dl = readln().trim().first()
        print("Путь: "); val p = readln().trim()
        print("Файл: "); val f = readln().trim()
        print("Описание: "); val d = readln().trim()
        return LocalResourceAddress(dl, p, f, d)
    }
}

data class EmailAddress(
    val userName: String,
    val host: String,
    override val description: String
) : Address() {
    override val hostName: String get() = host
    override fun format(): String = "$userName@$host"

    override fun inputFromConsole(): Address {
        println("\n--- Ввод email ---")
        print("Пользователь: "); val u = readln().trim()
        print("Домен: "); val h = readln().trim()
        print("Описание: "); val d = readln().trim()
        return EmailAddress(u, h, d)
    }
}

// ========== АДРЕСНАЯ КНИГА ==========

class AddressBook : AddressBookOperations {
    private val addresses = mutableListOf<Address>()

    fun add(vararg addresses: Address) {
        this.addresses.addAll(addresses)
    }

    override fun sortByHostName() {
        addresses.sortBy { it.hostName.lowercase() }
        println("Сортировка выполнена.")
    }

    override fun searchByKeyword(keyword: String) =
        addresses.filter { it.description.contains(keyword, ignoreCase = true) }

    override fun printAll() {
        if (addresses.isEmpty()) { println("Список пуст."); return }
        println("\n" + "=".repeat(60))
        println("Список адресов (всего: ${addresses.size})")
        println("=".repeat(60))
        addresses.forEach { it.printFormatted() }
    }
}

// ========== MAIN ==========

fun main() {
    val book = AddressBook()
    book.add(
        // Интернет-адреса
        InternetAddress("https", "google.com", "search", "q=kotlin", "Поисковая система Google"),
        InternetAddress("https", "github.com", "kotlin", "kotlin", "Репозиторий языка Kotlin"),
        InternetAddress("ftp", "ftp.microsoft.com", "pub", "readme.txt", "Файловый архив Microsoft"),

        // Адреса в локальной сети
        LocalNetworkAddress("SERVER-DB", "database", "backup.sql", "База данных сервера"),
        LocalNetworkAddress("PC-MARIA", "Photos", "vacation.jpg", "Фото с отпуска Марии"),
        LocalNetworkAddress("NAS-STORAGE", "media", "movie.mp4", "Фильм на сетевом хранилище"),

        // Локальные ресурсы
        LocalResourceAddress('C', "Windows\\System32", "drivers.txt", "Системные драйверы"),
        LocalResourceAddress('D', "Games\\Steam", "hl2.exe", "Установленная игра Half-Life 2"),
        LocalResourceAddress('E', "Backup\\2024", "photo.zip", "Архив с фотографиями"),

        // Email-адреса
        EmailAddress("admin", "localhost", "почта администратора"),
        EmailAddress("support", "microsoft.com", "почта техподдержки Microsoft"),
        EmailAddress("ivan.petrov", "gmail.com", "Личная почта Ивана")
    )


    while (true) {
        println("\n1. Показать  2. Добавить  3. Сортировать  4. Поиск  5. Выход")
        when (readln().trim()) {
            "1" -> book.printAll()
            "2" -> {
                println("Тип: 1-Интернет 2-Сеть 3-Диск 4-Email")
                val addr = when (readln().trim()) {
                    "1" -> InternetAddress("", "", "", "", "").inputFromConsole()
                    "2" -> LocalNetworkAddress("", "", "", "").inputFromConsole()
                    "3" -> LocalResourceAddress('C', "", "", "").inputFromConsole()
                    "4" -> EmailAddress("", "", "").inputFromConsole()
                    else -> null
                }
                if (addr != null) { book.add(addr); println("Добавлен!") }
                else println("Неверный тип.")
            }
            "3" -> book.sortByHostName()
            "4" -> {
                print("Ключевое слово: ")
                val r = book.searchByKeyword(readln().trim())
                if (r.isEmpty()) println("Не найдено.") else r.forEach { it.printFormatted() }
            }
            "5" -> { println("Пока!"); return }
            else -> println("Неверно.")
        }
    }
}