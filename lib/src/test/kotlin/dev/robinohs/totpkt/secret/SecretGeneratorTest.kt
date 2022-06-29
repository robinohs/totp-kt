package dev.robinohs.totpkt.secret

import dev.robinohs.totpkt.random.RandomGenerator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.util.*

/**
 * @author : Robin Ohs
 * @created : 29.06.2022
 * @since : 0.0.1
 */
internal class SecretGeneratorTest {
    private lateinit var cut: SecretGenerator

    @BeforeEach
    fun init() {
        val randomGenerator = RandomGenerator(
            charPool = listOf('A', 'B'),
            random = Random(5)
        )
        cut = SecretGenerator(randomGenerator)
    }

    @ParameterizedTest
    @CsvSource(
        "10, IJAUCQSBIJAUEQKB",
        "20, IJAUCQSBIJAUEQKBIJBECQKCIJAUCQSC",
        "30, IJAUCQSBIJAUEQKBIJBECQKCIJAUCQSCIJAUCQKCIFAUCQKC",
        "40, IJAUCQSBIJAUEQKBIJBECQKCIJAUCQSCIJAUCQKCIFAUCQKCIJBEEQKBIJAUCQSB",
    )
    fun `generateSecret produces random values and encodes them to valid base32 strings`(
        length: Int,
        expected: String
    ) {
        val actual = cut.generateSecret(length).secretAsString

        assertEquals(expected, actual) {
            "The generated secret was not encoded to the correct value."
        }
    }

    @ParameterizedTest
    @CsvSource(
        "10, IJAUCQSBIJAUEQKB",
        "20, IJAUCQSBIJAUEQKBIJBECQKCIJAUCQSC",
        "30, IJAUCQSBIJAUEQKBIJBECQKCIJAUCQSCIJAUCQKCIFAUCQKC",
        "40, IJAUCQSBIJAUEQKBIJBECQKCIJAUCQSCIJAUCQKCIFAUCQKCIJBEEQKBIJAUCQSB",
    )
    fun `generateSecret produces random values and encodes them to valid base32 bytearrays`(
        length: Int,
        expected: String
    ) {
        val actual = cut.generateSecret(length).secretAsByteArray.toString(Charsets.UTF_8)

        assertEquals(expected, actual) {
            "The generated secret was not encoded to the correct value."
        }
    }

    @ParameterizedTest
    @CsvSource(
        "10",
        "20",
        "30",
        "40",
        "50",
    )
    fun `generateSecret generates a bytearray and a string and they are the same`(
        length: Int
    ) {
        val generatedSecret = cut.generateSecret(length)
        val secretAsByteArray = generatedSecret.secretAsByteArray.toString(Charsets.UTF_8)
        val secretAsString = generatedSecret.secretAsString

        assertEquals(secretAsByteArray, secretAsString) {
            "The generated secret as bytearray and as string are equal."
        }
    }
}