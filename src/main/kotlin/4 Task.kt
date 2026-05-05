// ========== 1. Базовый класс для адреса ==========
sealed class Address(open val description: String) {
    abstract val hostName: String
    abstract fun format(): String
}

// ========== 2. Типы адресов ==========

// Интернет-адрес
data class InternetAddress(
    val protocol: String,
    val host: String,
    val path: String,
    val fileName: String,
    override val description: String
) : Address(description) {
    override val hostName: String get() = host
    override fun format(): String = "$protocol://$host/$path/$fileName"
}

// Адрес в локальной сети
data class LocalNetworkAddress(
    val computerName: String,
    val path: String,
    val fileName: String,
    override val description: String
) : Address(description) {
    override val hostName: String get() = computerName
    override fun format(): String = "\\\\$computerName\\$path\\$fileName"
}

// Локальный ресурс на диске
data class LocalResourceAddress(
    val driveLetter: Char,
    val path: String,
    val fileName: String,
    override val description: String
) : Address(description) {
    override val hostName: String get() = "LocalDrive_$driveLetter"
    override fun format(): String = "$driveLetter:\\$path\\$fileName"
}

// Email-адрес
data class EmailAddress(
    val userName: String,
    val host: String,
    override val description: String
) : Address(description) {
    override val hostName: String get() = host
    override fun format(): String = "$userName@$host"
}

// ========== 3. Адресная книга ==========
class AddressBook {
    private val addresses = mutableListOf<Address>()

    fun add(address: Address) {
        addresses.add(address)
    }

    fun addAll(vararg addresses: Address) {
        this.addresses.addAll(addresses)
    }

    fun sortByHostName() {
        addresses.sortBy { it.hostName.lowercase() }  // . приводим всё к нижнему регистру при поиске
        println("Сортировка выполнена по имени узла (hostName) без учёта регистра")
    }

    fun searchByKeyword(keyword: String): List<Address> {
        return addresses.filter { it.description.contains(keyword, ignoreCase = true) }
    }

    fun printAll() {
        if (addresses.isEmpty()) {
            println("Список адресов пуст.")
            return
        }
        println("=".repeat(60))
        println("Список адресов (всего: ${addresses.size})")
        println("=".repeat(60))
        addresses.forEachIndexed { index, address ->
            println("${index + 1}. ${address.format()}")
            println("   Описание: ${address.description}")
            println("   Хост/узел: ${address.hostName}")
            println("-".repeat(50))
        }
    }
}

// ========== 4. Главная функция ==========
fun main() {
    val book = AddressBook()

    book.addAll(
        // Интернет-адреса (разные домены)
        InternetAddress("https", "wikipedia.org", "wiki", "Kotlin", "Энциклопедия программирования"),
        InternetAddress("https", "google.com", "search", "q=kotlin", "Поисковая система"),
        InternetAddress("https", "github.com", "kotlin", "kotlin", "Репозиторий языка Kotlin"),
        InternetAddress("https", "stackoverflow.com", "questions", "kotlin", "Форум для разработчиков"),
        InternetAddress("https", "yandex.ru", "news", "index", "Новостной портал"),
        InternetAddress("https", "youtube.com", "watch", "v=123", "Видеохостинг"),
        InternetAddress("ftp", "ftp.microsoft.com", "pub", "readme.txt", "Файловый архив Microsoft"),
        InternetAddress("https", "kotlinlang.org", "docs", "home", "Официальная документация Kotlin"),

        // Адреса в локальной сети (разные имена компьютеров)
        LocalNetworkAddress("PC-ANTON", "Documents", "resume.pdf", "Резюме Антона"),
        LocalNetworkAddress("SERVER-DB", "database", "backup.sql", "База данных сервера"),
        LocalNetworkAddress("PC-MARIA", "Photos", "vacation.jpg", "Фото Марии с отдыха"),
        LocalNetworkAddress("LAPTOP-DIMA", "Projects", "app.exe", "Проект Димы"),
        LocalNetworkAddress("PRINTER-SHARP", "print", "jobs", "Сетевой принтер"),
        LocalNetworkAddress("NAS-SAMSUNG", "media", "movie.mp4", "Сетевое хранилище с фильмами"),

        // Локальные ресурсы (разные диски)
        LocalResourceAddress('C', "Windows\\System32", "drivers.txt", "Системные драйверы"),
        LocalResourceAddress('D', "Games\\Steam", "hl2.exe", "Установленная игра Half-Life"),
        LocalResourceAddress('E', "Backup\\2024", "photo.zip", "Архив с фотографиями"),
        LocalResourceAddress('C', "Users\\Admin", "config.ini", "Файл конфигурации"),

        // Email-адреса (разные домены)
        EmailAddress("alexey.ivanov", "gmail.com", "Личная почта Алексея"),
        EmailAddress("support", "microsoft.com", "Техподдержка Microsoft"),
        EmailAddress("admin", "localhost", "Локальный администратор"),
        EmailAddress("info", "mycompany.ru", "Корпоративная почта компании"),
        EmailAddress("contact", "yandex.ru", "Контактная почта Яндекса"),
        EmailAddress("hr.department", "company.com", "Отдел кадров")
    )

     // Показываем исходный (неотсортированный) список
    println("\n ИСХОДНЫЙ СПИСОК (в порядке добавления):")
    book.printAll()

    // Сортируем
    println("\n СОРТИРУЕМ ПО ИМЕНИ УЗЛА...")
    book.sortByHostName()

    // Показываем отсортированный список
    println("\n ОТСОРТИРОВАННЫЙ СПИСОК (по hostName):")
    book.printAll()

    // Интерактивный поиск
    println("\n" + "".repeat(30))
    println("ПОИСК ПО ОПИСАНИЮ")
    println("".repeat(30))

    while (true) {
        print("\n Введите ключевое слово для поиска (или 'выйти' для завершения): ")
        val keyword = readln().trim()

        if (keyword.equals("выйти", ignoreCase = true)) {
            println("\n Программа завершена. До свидания!")
            break
        }

        if (keyword.isEmpty()) {
            println(" Ошибка: Вы ничего не ввели. Попробуйте снова.")
            continue
        }

        val searchResult = book.searchByKeyword(keyword)

        if (searchResult.isEmpty()) {
            println(" Слово \"$keyword\" не найдено ни в одном описании.")
            println(" Попробуйте другое слово (например: почта, сервер, архив, игра, фото)")
            println(" или введите 'выйти' для завершения.")
        } else {
            println(" Найдено ${searchResult.size} адрес(ов) по слову \"$keyword\":")
            println("─".repeat(50))
            searchResult.forEachIndexed { index, addr ->
                println("${index + 1}. ${addr.format()}")
                println("   Описание: ${addr.description}")
                println("   Хост/узел: ${addr.hostName}")
                println()
            }
            println("Поиск завершён. Можете ввести новое слово или 'выйти'.")
        }
    }
}