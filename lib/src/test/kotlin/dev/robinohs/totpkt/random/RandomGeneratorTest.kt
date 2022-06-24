package dev.robinohs.totpkt.random

import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * @author : Robin Ohs
 * @created : 24.06.2022
 * @since : 0.0.1
 */
internal class RandomGeneratorTest {

    private lateinit var cut: RandomGenerator

    @BeforeTest
    fun init() {
        cut = RandomGenerator()
    }

    @Test(expected = IllegalArgumentException::class)
    fun `generateRandomStringFromCharPool throws an IllegalArgumentException for a negative length argument`() {
        cut.charPool = listOf('A', 'B')
        cut.generateRandomStringFromCharPool(-1)
    }

    @Test
    fun `generateRandomStringFromCharPool generates the empty string for a length of 0`() {
        cut.charPool = listOf('A', 'B')
        val expected = ""

        val actual = cut.generateRandomStringFromCharPool(0)

        assertEquals(expected, actual, "The string was not as expected an empty string.")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `generateRandomStringFromCharPool throws an IllegalArgumentException for an empty char pool`() {
        cut.charPool = listOf()

        cut.generateRandomStringFromCharPool(5)
    }

    @Test
    fun `generateRandomStringFromCharPool uses random generated numbers as expected`() {
        cut.charPool = listOf('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I')
        cut.random = Random(55)
        val expected = "HBHBIICD"

        val actual = cut.generateRandomStringFromCharPool(8)

        assertEquals(expected, actual, "The string was not taken from the charpool as expected.")
    }

    @Test
    fun `generateRandomStringFromCharPool uses random generated numbers as expected with another seed`() {
        cut.charPool = listOf('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I')
        cut.random = Random(555)
        val expected = "DFHIBGIC"

        val actual = cut.generateRandomStringFromCharPool(8)

        assertEquals(expected, actual, "The string was not taken from the charpool as expected.")
    }
}