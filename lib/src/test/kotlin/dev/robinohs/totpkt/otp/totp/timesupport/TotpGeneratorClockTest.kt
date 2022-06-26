package dev.robinohs.totpkt.otp.totp.timesupport

import dev.robinohs.totpkt.otp.totp.TotpGenerator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.time.ZoneId

/**
 * @author : Robin Ohs
 * @created : 26.06.2022
 * @since : 1.0.0
 */
internal class TotpGeneratorClockTest {
    private val secret = "IJAU CQSB IJAU EQKB".toByteArray()
    private val secret2 = "BJAU CQSB IJAU EQKB".toByteArray()

    private lateinit var cut: TotpGenerator

    @BeforeEach
    fun init() {
        cut = TotpGenerator()
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
            Executable {
                Assertions.assertNotEquals(
                    expected,
                    actual3
                ) { "Second code should not be equal to the expected one." }
            },
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
        cut.clock = Clock.fixed(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault())
        val actual1 = cut.generateCode(secret)
        val actual2 = cut.generateCode(secret2)

        Assertions.assertNotEquals(actual1, actual2) { "Codes should be different but were equal." }
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