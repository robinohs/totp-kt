package dev.robinohs.totpkt.otp.hotp

import dev.robinohs.totpkt.random.RandomGenerator
import org.apache.commons.codec.binary.Base32
import java.util.*
import kotlin.test.*

/**
 * @author : Robin Ohs
 * @created : 24.06.2022
 * @since : 0.0.1
 */
internal class HotpGeneratorTest {
    private val secret = "IJAU CQSB IJAU EQKB".toByteArray()
    private lateinit var cut: HotpGenerator

    @BeforeTest
    fun init() {
        val randomGenerator = RandomGenerator(
            charPool = listOf('A', 'B'),
            random = Random(5)
        )
        cut = HotpGenerator(randomGenerator)
    }

    @Test
    fun `generateSecret produces random values and encodes them to base32`() {
        // these are the generated strings with the given seed for the length encoded to base32 with another tool
        val expectedOutputs = sortedMapOf(
            10 to "IJAUCQSBIJAUEQKB",
            20 to "IJAUCQSBIJAUEQKBIJBECQKCIJAUCQSC",
            30 to "IJAUCQSBIJAUEQKBIJBECQKCIJAUCQSCIJAUCQKCIFAUCQKC",
            40 to "IJAUCQSBIJAUEQKBIJBECQKCIJAUCQSCIJAUCQKCIFAUCQKCIJBEEQKBIJAUCQSB"
        )

        for ((length, expected) in expectedOutputs) {
            val actual = cut.generateSecret(length).toString(Charsets.UTF_8)
            init()

            assertEquals(expected, actual, "The generated secret was not encoded to the correct value.")
        }
    }

    @Test
    fun `generateCode produces 6 digit long codes`() {
        val expected = "123379"

        val actual = cut.generateCode(Base32().decode(secret), 1656090712)

        assertEquals(expected, actual, "Code was not the expected one.")
    }

    @Test
    fun `generateCode produces 6 digit long codes with other counter`() {
        val first = cut.generateCode(Base32().decode(secret), 1656090713)
        val second = cut.generateCode(Base32().decode(secret), 1656090714)

        assertNotEquals(first, second, "Codes should not be the same for the given arguments.")
    }

    @Test
    fun `isCodeValid checks codes correctly`() {
        val actual1 = cut.isCodeValid(secret, 55203835, "196157")
        val actual2 = cut.isCodeValid(secret, 55203835, "355908")

        assertTrue(actual1, "First code should be valid but was not.")
        assertFalse(actual2, "Second code should not be valid but was.")
    }
}