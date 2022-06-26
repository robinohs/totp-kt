package dev.robinohs.totpkt.otp.totp.timesupport

import dev.robinohs.totpkt.otp.totp.TotpGenerator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.time.Duration
import java.time.Instant
import java.util.*

/**
 * @author : Robin Ohs
 * @created : 26.06.2022
 * @since : 1.0.0
 */
internal class TotpGeneratorDateTest {
    private val secret = "IJAU CQSB IJAU EQKB".toByteArray()
    private val secret2 = "BJAU CQSB IJAU EQKB".toByteArray()

    private lateinit var cut: TotpGenerator

    @BeforeEach
    fun init() {
        cut = TotpGenerator()
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
    fun `isCodeValid checks codes correctly with given date`() {
        val actual1 = cut.isCodeValid(secret, Date.from(Instant.ofEpochMilli(1656115068732)), "196157")
        val actual2 = cut.isCodeValid(secret, Date.from(Instant.ofEpochMilli(1656115073318)), "355908")

        Assertions.assertAll(
            Executable { Assertions.assertTrue(actual1) { "First code should be valid but was not." } },
            Executable { Assertions.assertFalse(actual2) { "Second code should not be valid but was." } },
        )
    }

    @Test
    fun `isCodeValidWithTolerance checks codes correctly if token is expired with 50s tolerance`() {
        cut.tolerance = Duration.ofSeconds(50)
        val date = Date.from(Instant.ofEpochMilli(1656118534840))

        val actual1 = cut.isCodeValidWithTolerance(secret, date, "956804")
        val actual2 = cut.isCodeValidWithTolerance(secret, date, "364536")
        val actual3 = cut.isCodeValidWithTolerance(secret, date, "326491")
        val actual4 = cut.isCodeValidWithTolerance(secret, date, "110215")

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
        val date = Date.from(Instant.ofEpochMilli(1656118534840))

        val actual1 = cut.isCodeValidWithTolerance(secret, date, "956804")
        val actual2 = cut.isCodeValidWithTolerance(secret, date, "364536")
        val actual3 = cut.isCodeValidWithTolerance(secret, date, "326491")
        val actual4 = cut.isCodeValidWithTolerance(secret, date, "110215")

        Assertions.assertAll(
            Executable { Assertions.assertTrue(actual1) { "Code1 should be valid but was not." } },
            Executable { Assertions.assertTrue(actual2) { "Code2 should be valid but was not." } },
            Executable { Assertions.assertTrue(actual3) { "Code3 should be valid but was not." } },
            Executable { Assertions.assertTrue(actual4) { "Code4 should be valid but was not." } }
        )
    }
}