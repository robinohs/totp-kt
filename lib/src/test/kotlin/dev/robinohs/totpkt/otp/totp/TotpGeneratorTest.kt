package dev.robinohs.totpkt.otp.totp

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.time.Duration

/**
 * @author : Robin Ohs
 * @created : 24.06.2022
 * @since : 0.0.1
 */
internal class TotpGeneratorTest {
    private val secret = "IJAU CQSB IJAU EQKB".toByteArray()
    private val secret2 = "BJAU CQSB IJAU EQKB".toByteArray()

    private lateinit var cut: TotpGenerator

    @BeforeEach
    fun init() {
        cut = TotpGenerator()
    }

    @Test
    fun `generateCode produces 6 digit long codes for given timestamps`() {
        val expected = "316152"

        val actual1 = cut.generateCode(secret, 1656114883887)
        val actual2 = cut.generateCode(secret, 1656114891677)

        Assertions.assertAll(
            Executable { Assertions.assertEquals(expected, actual1) { "First code was not the expected one." } },
            Executable { Assertions.assertEquals(expected, actual2) { "Second code was not the expected one." } },
        )
    }

    @ParameterizedTest
    @CsvSource(
        "1656114887817",
        "1658144883447",
        "1666314881887",
        "1696114888827",
    )
    fun `generateCode produces different codes with different secrets for same timestamp`(timestamp: Long) {
        val actual1 = cut.generateCode(secret, timestamp)
        val actual2 = cut.generateCode(secret2, timestamp)

        Assertions.assertNotEquals(actual1, actual2) { "Codes should be different but were equal." }
    }

    @Test
    fun `isCodeValid checks codes correctly for given timestamps`() {
        val actual1 = cut.isCodeValid(secret, 1656115068732, "196157")
        val actual2 = cut.isCodeValid(secret, 1656115073318, "355908")

        Assertions.assertAll(
            Executable { Assertions.assertTrue(actual1) { "First code should be valid but was not." } },
            Executable { Assertions.assertFalse(actual2) { "Second code should not be valid but was." } },
        )
    }

    @Test
    fun `isCodeValid checks codes correctly with different secrets for given timestamps`() {
        val actual1 = cut.isCodeValid(secret, 1656115068732, "196157")
        val actual2 = cut.isCodeValid(secret2, 1656115073318, "196157")

        Assertions.assertAll(
            Executable { Assertions.assertTrue(actual1) { "First code should be valid but was not." } },
            Executable { Assertions.assertFalse(actual2) { "Second code should not be valid but was." } },
        )
    }

    @Test
    fun `isCodeValid checks codes correctly with time argument`() {
        val actual1 = cut.isCodeValid(secret, 1656115068732, "196157")
        val actual2 = cut.isCodeValid(secret, 1656115073318, "355908")

        Assertions.assertAll(
            Executable { Assertions.assertTrue(actual1) { "First code should be valid but was not." } },
            Executable { Assertions.assertFalse(actual2) { "Second code should not be valid but was." } },
        )
    }

    @Test
    fun `isCodeValid checks codes correctly with different secrets without time argument`() {
        val actual1 = cut.isCodeValid(secret, 1656115068732, "196157")
        val actual2 = cut.isCodeValid(secret2, 1656115068732, "196157")

        Assertions.assertAll(
            Executable { Assertions.assertTrue(actual1) { "First code should be valid but was not." } },
            Executable { Assertions.assertFalse(actual2) { "Second code should not be valid but was." } },
        )
    }

    @Test
    fun `isCodeValidWithTolerance checks codes correctly if token is not expired`() {
        val actual = cut.isCodeValidWithTolerance(secret, 1656115068732, "196157")

        Assertions.assertTrue(actual) { "Code should be valid but was not." }
    }

    @Test
    fun `isCodeValidWithTolerance checks codes correctly if token is expired but in tolerance`() {
        val actual = cut.isCodeValidWithTolerance(secret, 1656116160719, "853702")

        Assertions.assertTrue(actual) { "Code should be valid but was not." }
    }

    @Test
    fun `isCodeValidWithTolerance checks codes correctly if token is expired and not in tolerance`() {
        val actual = cut.isCodeValidWithTolerance(secret, 1656116466490, "452088")

        Assertions.assertFalse(actual) { "Code should not be valid but was." }
    }

    @Test
    fun `isCodeValidWithTolerance checks codes correctly if token is expired and in extended tolerance`() {
        cut.tolerance = Duration.ofSeconds(10)

        val actual = cut.isCodeValidWithTolerance(secret, 1656116466490, "452088")

        Assertions.assertTrue(actual) { "Code should not be valid but was." }
    }

    @Test
    fun `isCodeValidWithTolerance checks codes correctly if token is expired with 50s tolerance`() {
        cut.tolerance = Duration.ofSeconds(50)

        val actual1 = cut.isCodeValidWithTolerance(secret, 1656118534840, "956804")
        val actual2 = cut.isCodeValidWithTolerance(secret, 1656118534840, "364536")
        val actual3 = cut.isCodeValidWithTolerance(secret, 1656118534840, "326491")
        val actual4 = cut.isCodeValidWithTolerance(secret, 1656118534840, "110215")

        Assertions.assertAll(
            Executable { Assertions.assertFalse(actual1) { "Code1 was valid but should not." } },
            Executable { Assertions.assertTrue(actual2) { "Code2 should be valid but was not." } },
            Executable { Assertions.assertTrue(actual3) { "Code3 should be valid but was not." } },
            Executable { Assertions.assertTrue(actual4) { "Code4 should be valid but was not." } }
        )
    }

    @Test
    fun `isCodeValidWithTolerance checks codes correctly if token is expired with 95s tolerance`() {
        cut.tolerance = Duration.ofSeconds(95)

        val actual1 = cut.isCodeValidWithTolerance(secret, 1656118534840, "956804")
        val actual2 = cut.isCodeValidWithTolerance(secret, 1656118534840, "364536")
        val actual3 = cut.isCodeValidWithTolerance(secret, 1656118534840, "326491")
        val actual4 = cut.isCodeValidWithTolerance(secret, 1656118534840, "110215")

        Assertions.assertAll(
            Executable { Assertions.assertTrue(actual1) { "Code1 should be valid but was not." } },
            Executable { Assertions.assertTrue(actual2) { "Code2 should be valid but was not." } },
            Executable { Assertions.assertTrue(actual3) { "Code3 should be valid but was not." } },
            Executable { Assertions.assertTrue(actual4) { "Code4 should be valid but was not." } }
        )
    }
}