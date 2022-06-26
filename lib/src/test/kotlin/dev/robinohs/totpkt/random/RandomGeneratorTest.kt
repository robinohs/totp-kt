package dev.robinohs.totpkt.random

import org.junit.jupiter.api.*
import java.util.*

/**
 * @author : Robin Ohs
 * @created : 24.06.2022
 * @since : 1.0.0
 */
internal class RandomGeneratorTest {

    private lateinit var cut: RandomGenerator

    @BeforeEach
    fun init() {
        cut = RandomGenerator()
    }

    @Test
    fun `constructor validates arguments`() {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            RandomGenerator(
                charPool = listOf()
            )
        }
    }

    @Test
    fun `RandomGenerator char pool cannot be set to an empty list`() {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            cut.charPool = listOf()
        }
    }

    @TestFactory
    fun `generateRandomStringFromCharPool throws an IllegalArgumentException for a negative length argument`() = listOf(
        -1,
        -5,
        -55
    ).map {
        DynamicTest.dynamicTest("input $it should throw an IllegalArgumentException") {
            Assertions.assertThrows(IllegalArgumentException::class.java) {
                cut.generateRandomStringFromCharPool(it)
            }
        }
    }

    @Test
    fun `generateRandomStringFromCharPool generates the empty string for a length of 0`() {
        cut.charPool = listOf('A', 'B')
        val expected = ""

        val actual = cut.generateRandomStringFromCharPool(0)

        Assertions.assertEquals(expected, actual) {
            "The string was not as expected an empty string."
        }
    }

    @Test
    fun `generateRandomStringFromCharPool throws an IllegalArgumentException for an empty char pool`() {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            cut.charPool = listOf()
        }
    }

    @Test
    fun `generateRandomStringFromCharPool uses random generated numbers as expected`() {
        cut.charPool = listOf('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I')
        cut.random = Random(55)
        val expected = "HBHBIICD"

        val actual = cut.generateRandomStringFromCharPool(8)

        Assertions.assertEquals(expected, actual) {
            "The string was not taken from the charpool as expected."
        }
    }

    @Test
    fun `generateRandomStringFromCharPool uses random generated numbers as expected with another seed`() {
        cut.charPool = listOf('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I')
        cut.random = Random(555)
        val expected = "DFHIBGIC"

        val actual = cut.generateRandomStringFromCharPool(8)

        Assertions.assertEquals(expected, actual) {
            "The string was not taken from the charpool as expected."
        }
    }
}