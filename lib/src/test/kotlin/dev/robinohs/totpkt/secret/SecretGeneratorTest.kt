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
    fun generateSecretTest_secretAsString(
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
    fun generateSecretTest_secretAsByteArray(
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
    fun generateSecretTest_byteArrayAndStringAreEqual(length: Int) {
        val base32Secret = cut.generateSecret(length)
        val secretAsByteArray = base32Secret.secretAsByteArray.toString(Charsets.UTF_8)
        val secretAsString = base32Secret.secretAsString

        assertEquals(secretAsByteArray, secretAsString) {
            "The generated secret as bytearray and as string are equal."
        }
    }
}