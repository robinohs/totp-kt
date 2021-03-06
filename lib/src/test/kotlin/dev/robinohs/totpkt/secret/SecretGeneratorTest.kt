package dev.robinohs.totpkt.secret

import dev.robinohs.totpkt.otp.HashAlgorithm
import dev.robinohs.totpkt.random.RandomGenerator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.util.*

/**
 * @author : Robin Ohs
 * @created : 29.06.2022
 * @since : 1.0.0
 */
internal class SecretGeneratorTest {
    private lateinit var cut: SecretGenerator

    @BeforeEach
    fun testInit() {
        val randomGenerator = RandomGenerator(
            charPool = listOf('A', 'B'),
            random = Random(5)
        )
        cut = SecretGenerator(randomGenerator)
    }

    @Test
    fun testConstructorAndData() {
        val cut = SecretGenerator()
        val randomGenerator = cut.randomGenerator
        cut.randomGenerator = randomGenerator
    }

    @Test
    fun testGenerateSecret_defaultArgument() {
        val expected = "IJAUCQSBIJAUEQKB"
        val actual = cut.generateSecret().secretAsString

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
    fun testGenerateSecret_secretAsString(
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
    fun testGenerateSecret_secretAsByteArray(
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
    fun testGenerateSecret_byteArrayAndStringAreEqual(length: Int) {
        val base32Secret = cut.generateSecret(length)
        val secretAsByteArray = base32Secret.secretAsByteArray.toString(Charsets.UTF_8)
        val secretAsString = base32Secret.secretAsString

        assertEquals(secretAsByteArray, secretAsString) {
            "The generated secret as bytearray and as string are equal."
        }
    }

    @Test
    fun testGenerateSecret_sha1Algorithm() {
        val expected = "IJAUCQSBIJAUEQKBIJBECQKCIJAUCQSC"

        val (actual, _) = cut.generateSecret(HashAlgorithm.SHA1)

        assertEquals(expected, actual) {
            "The generated secret as bytearray and as string are equal."
        }
    }

    @Test
    fun testGenerateSecret_sha1AlgorithmEqual() {
        val base32Secret = cut.generateSecret(HashAlgorithm.SHA1)
        val secretAsByteArray = base32Secret.secretAsByteArray.toString(Charsets.UTF_8)
        val secretAsString = base32Secret.secretAsString

        assertEquals(secretAsByteArray, secretAsString) {
            "The generated secret as bytearray and as string are equal."
        }
    }

    @Test
    fun testGenerateSecret_sha256Algorithm() {
        val expected = "IJAUCQSBIJAUEQKBIJBECQKCIJAUCQSCIJAUCQKCIFAUCQKCIJBA===="

        val (actual, _) = cut.generateSecret(HashAlgorithm.SHA256)

        assertEquals(expected, actual) {
            "The generated secret as bytearray and as string are equal."
        }
    }

    @Test
    fun testGenerateSecret_sha256AlgorithmEqual() {
        val base32Secret = cut.generateSecret(HashAlgorithm.SHA256)
        val secretAsByteArray = base32Secret.secretAsByteArray.toString(Charsets.UTF_8)
        val secretAsString = base32Secret.secretAsString

        assertEquals(secretAsByteArray, secretAsString) {
            "The generated secret as bytearray and as string are equal."
        }
    }

    @Test
    fun testGenerateSecret_sha512Algorithm() {
        val expected = "IJAUCQSBIJAUEQKBIJBECQKCIJAUCQSCIJAUCQKCIFAUCQKCIJBEEQKBIJAUCQSBIJAUCQSBIFBEEQKBIFBECQKBIFAUEQSCIFBECQI="

        val (actual, _) = cut.generateSecret(HashAlgorithm.SHA512)

        assertEquals(expected, actual) {
            "The generated secret as bytearray and as string are equal."
        }
    }

    @Test
    fun testGenerateSecret_sha512AlgorithmEqual() {
        val base32Secret = cut.generateSecret(HashAlgorithm.SHA512)
        val secretAsByteArray = base32Secret.secretAsByteArray.toString(Charsets.UTF_8)
        val secretAsString = base32Secret.secretAsString

        assertEquals(secretAsByteArray, secretAsString) {
            "The generated secret as bytearray and as string are equal."
        }
    }
}