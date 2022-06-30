package dev.robinohs.totpkt.otp.totp.timesupport

import TestMessageConstants.CODE_SHOULD_BE_VALID_BUT_WAS_NOT
import TestMessageConstants.CODE_SHOULD_NOT_BE_VALID_BUT_WAS
import TestMessageConstants.CODE_WAS_NOT_THE_EXPECTED_ONE
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
    fun testInit() {
        cut = TotpGenerator()
    }

    @Test
    fun testGenerateCode_datesMatchAuthenticatorGeneratedCode() {
        val expected = "503317"

        val actual1 = cut.generateCode(secret, Date.from(Instant.ofEpochMilli(1656114738767)))
        val actual2 = cut.generateCode(secret, Date.from(Instant.ofEpochMilli(1656114742133)))

        Assertions.assertAll(
            Executable { Assertions.assertEquals(expected, actual1) { CODE_WAS_NOT_THE_EXPECTED_ONE } },
            Executable { Assertions.assertEquals(expected, actual2) { CODE_WAS_NOT_THE_EXPECTED_ONE } },
        )
    }

    @ParameterizedTest
    @CsvSource(
        "1656114887817",
        "1658144883447",
        "1666314881887",
        "1696114888827",
    )
    fun testGenerateCode_differentSecretsHaveDifferentCodes(timestamp: Long) {
        val actual1 = cut.generateCode(secret, Date.from(Instant.ofEpochMilli(timestamp)))
        val actual2 = cut.generateCode(secret2, Date.from(Instant.ofEpochMilli(timestamp)))

        Assertions.assertNotEquals(actual1, actual2) { "Codes should be different but were equal." }
    }

    @ParameterizedTest
    @CsvSource(
        "4",
        "6",
        "9",
        "10",
        "12",
    )
    fun testGenerateCode_codeLengthMatchesConfig(expected: Int) {
        cut.codeLength = expected

        val actual = cut.generateCode(secret, Date.from(Instant.ofEpochMilli(1656114738767))).length

        Assertions.assertEquals(expected, actual) { "Code length was not as expected. $expected not equal to $actual." }
    }

    @Test
    fun testIsCodeValid_checksCodesCorrectly() {
        val actual1 = cut.isCodeValid(secret, Date.from(Instant.ofEpochMilli(1656115068732)), "196157")
        val actual2 = cut.isCodeValid(secret, Date.from(Instant.ofEpochMilli(1656115073318)), "355908")

        Assertions.assertAll(
            Executable { Assertions.assertTrue(actual1) { CODE_SHOULD_BE_VALID_BUT_WAS_NOT } },
            Executable { Assertions.assertFalse(actual2) { CODE_SHOULD_NOT_BE_VALID_BUT_WAS } },
        )
    }

    @Test
    fun testIsCodeValidWithTolerance_threeOldTokenWithToleranceOf2() {
        cut.tolerance = 2
        val date = Date.from(Instant.ofEpochMilli(1656605624664))

        val actual1 = cut.isCodeValidWithTolerance(secret, date, "644152")
        val actual2 = cut.isCodeValidWithTolerance(secret, date, "289971")
        val actual3 = cut.isCodeValidWithTolerance(secret, date, "044157")
        val actual4 = cut.isCodeValidWithTolerance(secret, date, "929325")

        Assertions.assertAll(
            Executable { Assertions.assertFalse(actual1) { CODE_SHOULD_NOT_BE_VALID_BUT_WAS } },
            Executable { Assertions.assertTrue(actual2) { CODE_SHOULD_BE_VALID_BUT_WAS_NOT } },
            Executable { Assertions.assertTrue(actual3) { CODE_SHOULD_BE_VALID_BUT_WAS_NOT } },
            Executable { Assertions.assertTrue(actual4) { CODE_SHOULD_BE_VALID_BUT_WAS_NOT } }
        )
    }

    @Test
    fun testIsCodeValidWithTolerance_threeOldTokenWithToleranceOf3() {
        cut.tolerance = 3
        val date = Date.from(Instant.ofEpochMilli(1656605624664))

        val actual1 = cut.isCodeValidWithTolerance(secret, date, "644152")
        val actual2 = cut.isCodeValidWithTolerance(secret, date, "289971")
        val actual3 = cut.isCodeValidWithTolerance(secret, date, "044157")
        val actual4 = cut.isCodeValidWithTolerance(secret, date, "929325")

        Assertions.assertAll(
            Executable { Assertions.assertTrue(actual1) { CODE_SHOULD_BE_VALID_BUT_WAS_NOT } },
            Executable { Assertions.assertTrue(actual2) { CODE_SHOULD_BE_VALID_BUT_WAS_NOT } },
            Executable { Assertions.assertTrue(actual3) { CODE_SHOULD_BE_VALID_BUT_WAS_NOT } },
            Executable { Assertions.assertTrue(actual4) { CODE_SHOULD_BE_VALID_BUT_WAS_NOT } }
        )
    }

    @ParameterizedTest
    @CsvSource(
        "1656605624624, 1656605610000",
        "1656605654664, 1656605640000",
        "1656605656367, 1656605640000",
        "1656605658664, 1656605640000",
        "1656605684614, 1656605670000",
    )
    fun testCalculateTimeslotBeginning(timestamp: Long, expectedTimestamp: Long) {
        val expected = Date.from(Instant.ofEpochMilli(expectedTimestamp))
        val date = Date.from(Instant.ofEpochMilli(timestamp))

        val actual = cut.calculateTimeslotBeginning(date)

        Assertions.assertEquals(
            expected,
            actual
        ) { "Time slot beginning was different than expected. $expected not equal to $actual." }
    }

    @ParameterizedTest
    @CsvSource(
        "1656605610000, 30",
        "1656605640000, 30",
        "1656605670000, 30",
        "1656605685000, 15",
        "1656605695000, 5",
        "1656605669000, 1",
    )
    fun testCalculateRemainingTime(timestamp: Long, durationInSeconds: Long) {
        val date = Date.from(Instant.ofEpochMilli(timestamp))
        val expected = Duration.ofSeconds(durationInSeconds)

        val actual = cut.calculateRemainingTime(date)

        Assertions.assertEquals(
            expected,
            actual
        ) { "Remaining duration was different than expected. $expected not equal to $actual." }
    }
}