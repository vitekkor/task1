package task1

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import java.lang.ArithmeticException

internal class UnsignedBigIntegerTest {

    @Test
    @Tag("Normal")
    fun plus() {
        assertEquals(
            UnsignedBigInteger("412316860416"),
            UnsignedBigInteger("386547056640") + UnsignedBigInteger("25769803776")
        )
        assertEquals(UnsignedBigInteger(4), UnsignedBigInteger(2) + UnsignedBigInteger(2))
        assertEquals(UnsignedBigInteger("9087654330"), UnsignedBigInteger("9087654329") + UnsignedBigInteger(1))
    }

    @Test
    @Tag("Normal")
    fun minus() {
        assertEquals(UnsignedBigInteger("999"), UnsignedBigInteger("1000") - UnsignedBigInteger("1"))
        assertEquals(UnsignedBigInteger("950"), UnsignedBigInteger("1000") - UnsignedBigInteger("50"))
        assertEquals(UnsignedBigInteger("1"), UnsignedBigInteger("1000") - UnsignedBigInteger("999"))
        assertEquals(UnsignedBigInteger(2), UnsignedBigInteger(4) - UnsignedBigInteger(2))
        assertEquals(UnsignedBigInteger("9087654329"), UnsignedBigInteger("9087654330") - UnsignedBigInteger(1))
        assertEquals(UnsignedBigInteger("0"), UnsignedBigInteger("9087654330") - UnsignedBigInteger("9087654330"))
        assertThrows(ArithmeticException::class.java) {
            UnsignedBigInteger(2) - UnsignedBigInteger(4)
        }
    }

    @Test
    @Tag("Hard")
    fun times() {
        assertEquals(
            UnsignedBigInteger("3885"),
            UnsignedBigInteger("555") * UnsignedBigInteger("7")
        )
        assertEquals(
            UnsignedBigInteger("18446744073709551616"),
            UnsignedBigInteger("4294967296") * UnsignedBigInteger("4294967296")
        )
    }

    @Test
    @Tag("Impossible")
    fun div() {
        assertThrows(ArithmeticException::class.java) {
            UnsignedBigInteger("254124542215121545") / UnsignedBigInteger(0)
        }
        assertEquals(
            UnsignedBigInteger("4294967296"),
            UnsignedBigInteger("18446744073709551616") / UnsignedBigInteger("4294967296")
        )
        assertEquals(
            UnsignedBigInteger("4294967296"),
            UnsignedBigInteger("4294967296") / UnsignedBigInteger("1")
        )
        assertEquals(
            UnsignedBigInteger("1"),
            UnsignedBigInteger("4294967296") / UnsignedBigInteger("4294967296")
        )
        assertEquals(
            UnsignedBigInteger("0"),
            UnsignedBigInteger("4294967296") / UnsignedBigInteger("42949672962222")
        )
    }

    @Test
    @Tag("Impossible")
    fun rem() {
        assertThrows(ArithmeticException::class.java) {
            UnsignedBigInteger("254124542215121545") % UnsignedBigInteger(0)
        }
        assertEquals(UnsignedBigInteger(5), UnsignedBigInteger(19) % UnsignedBigInteger(7))
        assertEquals(
            UnsignedBigInteger(0),
            UnsignedBigInteger("18446744073709551616") % UnsignedBigInteger("4294967296")
        )
        assertEquals(
            UnsignedBigInteger("0"),
            UnsignedBigInteger("4294967296") % UnsignedBigInteger("4294967296")
        )
        assertEquals(
            UnsignedBigInteger("0"),
            UnsignedBigInteger("4294967296") % UnsignedBigInteger("1")
        )
        assertEquals(
            UnsignedBigInteger("0"),
            UnsignedBigInteger("4294967296") % UnsignedBigInteger("4294967296")
        )
        assertEquals(
            UnsignedBigInteger("4294967296"),
            UnsignedBigInteger("4294967296") % UnsignedBigInteger("4294967297")
        )
    }

    @Test
    @Tag("Normal")
    fun equals() {
        assertEquals(UnsignedBigInteger(123456789), UnsignedBigInteger("123456789"))
    }

    @Test
    @Tag("Normal")
    fun compareTo() {
        assertTrue(UnsignedBigInteger(123456789) < UnsignedBigInteger("9876543210"))
        assertTrue(UnsignedBigInteger("01266874889") < UnsignedBigInteger("4294967296"))
        assertTrue(UnsignedBigInteger("9856809481") > UnsignedBigInteger("4294967296"))
        assertTrue(UnsignedBigInteger("9876543210") > UnsignedBigInteger(123456789))
        assertTrue(UnsignedBigInteger("4294967296") < UnsignedBigInteger("4294967297"))
    }

    @Test
    @Tag("Normal")
    fun toInt() {
        assertEquals(123456789, UnsignedBigInteger("123456789").toInt())
    }
}