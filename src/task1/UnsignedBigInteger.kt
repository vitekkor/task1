package task1

import kotlin.math.abs

/**
 * Класс "беззнаковое большое целое число".
 *
 * Общая сложность задания -- очень сложная.
 * Объект класса содержит целое число без знака произвольного размера
 * и поддерживает основные операции над такими числами, а именно:
 * сложение, вычитание (при вычитании большего числа из меньшего бросается исключение),
 * умножение, деление, остаток от деления,
 * преобразование в строку/из строки, преобразование в целое/из целого,
 * сравнение на равенство и неравенство
 */
class UnsignedBigInteger(number: MutableList<Int>) : Comparable<UnsignedBigInteger> {
    val list = number.dropWhile { it == 0 }.toMutableList()

    init {
        if (list == listOf<Int>()) list.add(0)
    }

    /**
     * Конструктор из строки
     */
    constructor(s: String) : this(s.map { it - '0' }.toMutableList())

    /**
     * Конструктор из целого
     */
    constructor(i: Int) : this(abs(i).toString())

    /**
     * Сложение
     */
    operator fun plus(other: UnsignedBigInteger): UnsignedBigInteger {
        if (other == UnsignedBigInteger(0)) return this
        val maxSize = maxOf(this.list.size, other.list.size)
        val max = if (maxSize == this.list.size) this.list else other.list
        val min = if (maxSize == this.list.size) other.list else this.list
        val result = MutableList(maxSize) { 0 }
        val different = abs(this.list.size - other.list.size)
        var mod = 0
        for (i in maxSize - 1 downTo 0) {
            val index = if (different < max.size) i - different else different - i
            result[i] = (max[i] + (min.getOrNull(index) ?: 0) + mod) % 10
            mod = (max[i] + (min.getOrNull(index) ?: 0) + mod) / 10
        }
        if (mod != 0) result.add(0, mod)
        return UnsignedBigInteger(result)
    }

    /**
     * Вычитание (бросить ArithmeticException, если this < other)
     */
    operator fun minus(other: UnsignedBigInteger): UnsignedBigInteger {
        if (other > this) throw ArithmeticException()
        if (other == this) return UnsignedBigInteger(0)
        if (other == UnsignedBigInteger(0)) return this
        val different = abs(this.list.size - other.list.size)
        val result = list
        var i = list.size - 1
        while (i in list.size - 1 downTo 0) {
            val index = if (different < list.size) i - different else different - i
            if (result[i] >= (other.list.getOrNull(index) ?: 0)) {
                result[i] = result[i] - (other.list.getOrNull(index) ?: 0)
            } else {
                var j = i - 1
                while (result[j] == 0) {
                    j--
                }
                val required = j
                while (j != i + 1) {
                    if (j != i) result[j] -= 1
                    if (j != required) result[j] += 10
                    j++
                }
                result[i] = result[i] - (other.list.getOrNull(index) ?: 0)
            }
            i--
        }
        return UnsignedBigInteger(result)
    }

    /**
     * Умножение
     */
    operator fun times(other: UnsignedBigInteger): UnsignedBigInteger {
        if (other == UnsignedBigInteger(0)) return UnsignedBigInteger(0)
        if (other == UnsignedBigInteger(1)) return this
        var result = UnsignedBigInteger(0)
        val max = maxOf(list.size - 1, other.list.size - 1)
        val min = minOf(list.size - 1, other.list.size - 1)
        val thisMore = max + 1 == list.size
        for ((t, i) in (min downTo 0).withIndex()) {
            val preResult = MutableList(max + 1) { 0 }
            var next = 0
            for (j in max downTo 0) {
                val x1 = other.list.getOrNull(if (!thisMore) j else i) ?: 1
                val x2 = list.getOrNull(if (thisMore) j else i) ?: 1
                val value = x1 * x2 + next
                preResult[j] = value % 10
                next = value / 10
                if (j == 0 && next != 0) preResult.add(0, next)
            }
            repeat(t) { preResult.add(0) }
            result += UnsignedBigInteger(preResult)
        }
        return result
    }

    /**
     * Деление
     */
    operator fun div(other: UnsignedBigInteger): UnsignedBigInteger {
        when {
            other == UnsignedBigInteger(0) -> throw ArithmeticException("by zero")
            other == UnsignedBigInteger(1) -> return this
            other == this -> return UnsignedBigInteger(1)
            other > this -> return UnsignedBigInteger(0)
        }
        return divOrRem(this, other, true)
    }

    /**
     * Взятие остатка
     */
    operator fun rem(other: UnsignedBigInteger): UnsignedBigInteger {
        when {
            other == UnsignedBigInteger(0) -> throw ArithmeticException("by zero")
            other == UnsignedBigInteger(1) || other == this -> return UnsignedBigInteger(0)
            other > this -> return this
        }
        return divOrRem(this, other, false)
    }

    /**
     * Сравнение на равенство (по контракту Any.equals)
     */
    override fun equals(other: Any?): Boolean = other is UnsignedBigInteger && this.list == other.list

    override fun hashCode(): Int = list.hashCode()

    /**
     * Сравнение на больше/меньше (по контракту Comparable.compareTo)
     */
    override fun compareTo(other: UnsignedBigInteger): Int {
        fun compare(list1: List<Int>, list2: List<Int>): Boolean {
            loop@ for (i in list1.indices) {
                when {
                    list1[i] == list2[i] -> continue@loop
                    list1[i] > list2[i] -> return true
                    list1[i] < list2[i] -> return false
                }
            }
            return true
        }
        return when {
            this == other -> 0
            this.list.size > other.list.size || this.list.size == other.list.size && compare(list, other.list) -> 1
            else -> -1
        }
    }


    /**
     * Преобразование в строку
     */
    override fun toString(): String = list.joinToString("")

    /**
     * Преобразование в целое
     * Если число не влезает в диапазон Int, бросить ArithmeticException
     */
    fun toInt(): Int {
        val res = this.toString().toIntOrNull()
        if (res != null) return res else throw ArithmeticException()
    }

}

private fun divOrRem(current: UnsignedBigInteger, other: UnsignedBigInteger, div: Boolean): UnsignedBigInteger {
    val result = mutableListOf<Int>()
    var i = 0
    var previous = UnsignedBigInteger(0)
    var next = UnsignedBigInteger(0)
    while (i in 0 until current.list.size) {
        previous.list.add(0)
        next = UnsignedBigInteger(previous.list) + UnsignedBigInteger(current.list.getOrNull(i) ?: 0)
        while (next < other) {
            i++
            next.list.add(0)
            next = UnsignedBigInteger(next.list) + UnsignedBigInteger(current.list.getOrNull(i) ?: 0)
        }
        var count = 0
        while (next > other || next == other) {
            next -= other
            count++
        }
        previous = next
        result.add(count)
        i++
    }
    return if (div) UnsignedBigInteger(result) else next
}