package dev.robinohs.totpkt.otp.hotp

import dev.robinohs.totpkt.random.RandomGenerator
import org.apache.commons.codec.binary.Base32
import org.junit.jupiter.api.*
import org.junit.jupiter.api.function.Executable
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.util.*

/**
 * @author : Robin Ohs
 * @created : 24.06.2022
 * @since : 1.0.0
 */
internal class HotpGeneratorTest {
    private val secret = "IJAU CQSB IJAU EQKB".toByteArray()
    private val secret2 = "BJAU CQSB IJAU EQKB".toByteArray()
    private lateinit var cut: HotpGenerator

    @BeforeEach
    fun init() {
        val randomGenerator = RandomGenerator(
            charPool = listOf('A', 'B'),
            random = Random(5)
        )
        cut = HotpGenerator(randomGenerator)
    }

    @Test
    fun `constructor validates arguments`() {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            HotpGenerator(
                codeLength = -5
            )
        }
    }

    @TestFactory
    fun `HotpGenerator codeLength cannot be set to a negative number`() = listOf(
        -55, -1
    ).map {
        DynamicTest.dynamicTest("setting codeLength to $it results in an IllegalArgumentException") {
            Assertions.assertThrows(IllegalArgumentException::class.java) {
                cut.codeLength = it
            }
        }
    }

    @ParameterizedTest
    @CsvSource(
        "10, IJAUCQSBIJAUEQKB",
        "20, IJAUCQSBIJAUEQKBIJBECQKCIJAUCQSC",
        "30, IJAUCQSBIJAUEQKBIJBECQKCIJAUCQSCIJAUCQKCIFAUCQKC",
        "40, IJAUCQSBIJAUEQKBIJBECQKCIJAUCQSCIJAUCQKCIFAUCQKCIJBEEQKBIJAUCQSB",
    )
    fun `generateSecret produces random values and encodes them to base32`(length: Int, expected: String) {
        val actual = cut.generateSecret(length).toString(Charsets.UTF_8)

        Assertions.assertEquals(expected, actual) {
            "The generated secret was not encoded to the correct value."
        }
    }

    @Test
    fun `generateCode produces 6 digit long codes`() {
        val expected = "123379"

        val actual = cut.generateCode(Base32().decode(secret), 1656090712)

        Assertions.assertEquals(expected, actual) {
            "Code was not the expected one."
        }
    }

    @Test
    fun `generateCode produces 6 digit long codes with other counter`() {
        val first = cut.generateCode(Base32().decode(secret), 1656090713)
        val second = cut.generateCode(Base32().decode(secret), 1656090714)

        Assertions.assertNotEquals(first, second) {
            "Codes should not be the same for the given arguments."
        }
    }

    @Test
    fun `generateCode produces 6 digit long codes with different secrets`() {
        val first = cut.generateCode(Base32().decode(secret), 1656090713)
        val second = cut.generateCode(Base32().decode(secret2), 1656090713)

        Assertions.assertNotEquals(first, second) {
            "Codes should not be the same for two different secrets."
        }
    }

    @Test
    fun `isCodeValid checks codes correctly`() {
        val actual1 = cut.isCodeValid(secret, 55203835, "196157")
        val actual2 = cut.isCodeValid(secret, 55203835, "355908")

        Assertions.assertAll(
            Executable { Assertions.assertTrue(actual1) { "First code should be valid but was not." } },
            Executable { Assertions.assertFalse(actual2) { "Second code should not be valid but was." } },
        )
    }

}