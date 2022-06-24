package dev.robinohs.totpkt.utils

import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * @author : Robin Ohs
 * @created : 24.06.2022
 * @since : 0.0.1
 */
internal class RandomUtilsTest {

    @Test(expected = IllegalArgumentException::class)
    fun `generateRandomStringFromCharPool throws an IllegalArgumentException for a negative length argument`() {
        RandomUtils.generateRandomStringFromCharPool(-1, listOf('A', 'B'))
    }

    @Test
    fun `generateRandomStringFromCharPool generates the empty string for a length of 0`() {
        val expected = ""

        val actual = RandomUtils.generateRandomStringFromCharPool(0, listOf('A', 'B'))

        assertEquals(expected, actual, "The string was not as expected an empty string.")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `generateRandomStringFromCharPool throws an IllegalArgumentException for an empty char pool`() {
        RandomUtils.generateRandomStringFromCharPool(5, listOf())
    }

    @Test
    fun `generateRandomStringFromCharPool uses random generated numbers as expected`() {
        val expected = "HBHBIICD"

        val actual = RandomUtils.generateRandomStringFromCharPool(
            8,
            listOf('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I'),
            random = Random(55)
        )

        assertEquals(expected, actual, "The string was not taken from the charpool as expected.")
    }

    @Test
    fun `generateRandomStringFromCharPool uses random generated numbers as expected with another seed`() {
        val expected = "DFHIBGIC"

        val actual = RandomUtils.generateRandomStringFromCharPool(
            8,
            listOf('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I'),
            random = Random(555)
        )

        assertEquals(expected, actual, "The string was not taken from the charpool as expected.")
    }
}