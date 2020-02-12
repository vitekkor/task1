package task1

fun fromRussian(number: String): Int {
    if (number.isEmpty()) return 0
    val ones = listOf("один", "два", "три", "четыре", "пять", "шесть", "семь", "восемь", "девять")
    val exclusions = listOf(
        "десять", "одиннадцать", "двенадцать", "тринадцать", "четырнадцать", "пятнадцать",
        "шестнадцать", "семнадцать", "восемнадцать", "девятнадцать"
    )
    val tens =
        listOf("", "двадцать", "тридцать", "сорок", "пятьдесят", "шестьдесят", "семьдесят", "восемьдесят", "девяносто")
    val num = number.split(" ")
    var res = 0
    if (num.size == 1) {
        val one = ones.indexOf(number)
        val exclusion = exclusions.indexOf(number)
        val ten = tens.indexOf(number)
        res += when {
            one != -1 -> one + 1
            exclusion != -1 -> exclusion + 10
            else -> (ten + 1) * 10
        }
        require(res > 0)
    } else {
        res = (tens.indexOf(num[0]) + 1) * 10
        require(res >= 20)
        res += ones.indexOf(num[1]) + 1
        require(res > 20)
    }
    return res
}

fun weddingDinner(guests: List<String>): Int {
    var result = 0
    for (guest in guests) {
        require(guest.matches(Regex(""".+(\+.+)?""")))
        require(guest.last() != '+')
        val number = guest.split("+")
        result += 1 + fromRussian(if (number.size == 1) "" else number[1])
    }
    return result * 3000
}