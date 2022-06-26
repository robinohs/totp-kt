package dev.robinohs.totpkt.otp.totp

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.function.Executable
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test

/**
 * @author : Robin Ohs
 * @created : 24.06.2022
 * @since : 0.0.1
 */
internal class TotpGeneratorTest {
    private val secret = "IJAU CQSB IJAU EQKB".toByteArray()
    private val secret2 = "BJAU CQSB IJAU EQKB".toByteArray()

    private lateinit var cut: TotpGenerator

    @BeforeTest
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
    fun `generateCode produces 6 digit long code for given dates`() {
        val expected = "503317"

        val actual1 = cut.generateCode(secret, Date.from(Instant.ofEpochMilli(1656114738767)))
        val actual2 = cut.generateCode(secret, Date.from(Instant.ofEpochMilli(1656114742133)))

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
    fun `generateCode produces different codes with different secrets for same date`(timestamp: Long) {
        val actual1 = cut.generateCode(secret, Date.from(Instant.ofEpochMilli(timestamp)))
        val actual2 = cut.generateCode(secret2, Date.from(Instant.ofEpochMilli(timestamp)))

        Assertions.assertNotEquals(actual1, actual2) { "Codes should be different but were equal." }
    }

    @Test
    fun `generateCode produces 6 digit long code for given instants`() {
        val expected = "301527"

        val actual1 = cut.generateCode(secret, Instant.ofEpochMilli(1656114819820))
        val actual2 = cut.generateCode(secret, Instant.ofEpochMilli(1656114826576))

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
    fun `generateCode produces different codes with different secrets for same instant`(timestamp: Long) {
        val actual1 = cut.generateCode(secret, Instant.ofEpochMilli(timestamp))
        val actual2 = cut.generateCode(secret2, Instant.ofEpochMilli(timestamp))

        Assertions.assertNotEquals(actual1, actual2) { "Codes should be different but were equal." }
    }

    @Test
    fun `generateCode produces 6 digit long code by taking timestamps itself`() {
        val expected = "223710"

        cut.clock = Clock.fixed(Instant.ofEpochMilli(1656115662383), ZoneId.systemDefault())
        val actual1 = cut.generateCode(secret)
        cut.clock = Clock.fixed(Instant.ofEpochMilli(1656115668553), ZoneId.systemDefault())
        val actual2 = cut.generateCode(secret)
        cut.clock = Clock.fixed(Instant.ofEpochMilli(1656115777419), ZoneId.systemDefault())
        val actual3 = cut.generateCode(secret)

        Assertions.assertAll(
            Executable { Assertions.assertEquals(expected, actual1) { "First code was not the expected code." } },
            Executable { Assertions.assertEquals(expected, actual2) { "Second code was not the expected code." } },
            Executable { Assertions.assertNotEquals(expected, actual3) { "Second code should not be equal to the expected one." } },
        )
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
    fun `isCodeValid checks codes correctly without time argument`() {
        cut.clock = Clock.fixed(Instant.ofEpochMilli(1656115068732), ZoneId.systemDefault())
        val actual1 = cut.isCodeValid(secret, "196157")
        cut.clock = Clock.fixed(Instant.ofEpochMilli(1656115073318), ZoneId.systemDefault())
        val actual2 = cut.isCodeValid(secret, "355908")

        Assertions.assertAll(
            Executable { Assertions.assertTrue(actual1) { "First code should be valid but was not." } },
            Executable { Assertions.assertFalse(actual2) { "Second code should not be valid but was." } },
        )
    }

    @Test
    fun `isCodeValid checks codes correctly with different secrets without time argument`() {
        cut.clock = Clock.fixed(Instant.ofEpochMilli(1656115068732), ZoneId.systemDefault())
        val actual1 = cut.isCodeValid(secret, "196157")
        val actual2 = cut.isCodeValid(secret2, "196157")

        Assertions.assertAll(
            Executable { Assertions.assertTrue(actual1) { "First code should be valid but was not." } },
            Executable { Assertions.assertFalse(actual2) { "Second code should not be valid but was." } },
        )
    }

    @Test
    fun `isCodeValidWithTolerance checks codes correctly if token is not expired`() {
        cut.clock = Clock.fixed(Instant.ofEpochMilli(1656115068732), ZoneId.systemDefault())
        val actual = cut.isCodeValidWithTolerance(secret, "196157")

        Assertions.assertTrue(actual) { "Code should be valid but was not." }
    }

    @Test
    fun `isCodeValidWithTolerance checks codes correctly if token is expired but in tolerance`() {
        cut.clock = Clock.fixed(Instant.ofEpochMilli(1656116160719), ZoneId.systemDefault())
        val actual = cut.isCodeValidWithTolerance(secret, "853702")

        Assertions.assertTrue(actual) { "Code should be valid but was not." }
    }

    @Test
    fun `isCodeValidWithTolerance checks codes correctly if token is expired and not in tolerance`() {
        cut.clock = Clock.fixed(Instant.ofEpochMilli(1656116466490), ZoneId.systemDefault())

        val actual = cut.isCodeValidWithTolerance(secret, "452088")

        Assertions.assertFalse(actual) { "Code should not be valid but was." }
    }

    @Test
    fun `isCodeValidWithTolerance checks codes correctly if token is expired and in extended tolerance`() {
        cut.tolerance = Duration.ofSeconds(10)
        cut.clock = Clock.fixed(Instant.ofEpochMilli(1656116466490), ZoneId.systemDefault())

        val actual = cut.isCodeValidWithTolerance(secret, "452088")

        Assertions.assertTrue(actual) { "Code should not be valid but was." }
    }

    @Test
    fun `isCodeValidWithTolerance checks codes correctly if token is expired with 50s tolerance`() {
        cut.tolerance = Duration.ofSeconds(50)
        cut.clock = Clock.fixed(Instant.ofEpochMilli(1656118534840), ZoneId.systemDefault())

        val actual1 = cut.isCodeValidWithTolerance(secret, "956804")
        val actual2 = cut.isCodeValidWithTolerance(secret, "364536")
        val actual3 = cut.isCodeValidWithTolerance(secret, "326491")
        val actual4 = cut.isCodeValidWithTolerance(secret, "110215")

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
        cut.clock = Clock.fixed(Instant.ofEpochMilli(1656118534840), ZoneId.systemDefault())

        val actual1 = cut.isCodeValidWithTolerance(secret, "956804")
        val actual2 = cut.isCodeValidWithTolerance(secret, "364536")
        val actual3 = cut.isCodeValidWithTolerance(secret, "326491")
        val actual4 = cut.isCodeValidWithTolerance(secret, "110215")

        Assertions.assertAll(
            Executable { Assertions.assertTrue(actual1) { "Code1 should be valid but was not." } },
            Executable { Assertions.assertTrue(actual2) { "Code2 should be valid but was not." } },
            Executable { Assertions.assertTrue(actual3) { "Code3 should be valid but was not." } },
            Executable { Assertions.assertTrue(actual4) { "Code4 should be valid but was not." } }
        )
    }
}