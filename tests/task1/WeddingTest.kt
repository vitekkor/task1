package task1

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException

class Tests {
    @Test
    @Tag("Normal")
    fun weddingDinner() {
        assertEquals(0, weddingDinner(emptyList()))
        assertEquals(3000, weddingDinner(listOf("Денис")))
        assertEquals(12000, weddingDinner(listOf("Денис+три")))
        assertEquals(30000, weddingDinner(listOf("Денис+три", "андрей+пять")))
        assertEquals(18 * 3000, weddingDinner(listOf("Денис+три", "андрей+пять", "fhhjg", "bjjlf+шесть")))
        assertEquals(100 * 3000, weddingDinner(listOf("аьплп+девяносто девять")))
        assertEquals(115 * 3000, weddingDinner(listOf("аьплп+девяносто девять", "gjgi+четырнадцать")))
        assertEquals(11 * 3000, weddingDinner(listOf("полплп+десять")))
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            weddingDinner(listOf("Денис+три", "андрей+пять", "fhhjg", "bjjlf+сто"))
        }
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            weddingDinner(listOf("Денис+", "андрей+пять", "fhhjg", "bjjlf+сто"))
        }
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            weddingDinner(listOf("Денис+", "андрей+пять", "fhhjg", "bjjlf"))
        }
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            weddingDinner(listOf("Денис+", "андрей+пять", "fhhjg", "bjjlf+300"))
        }
    }
}