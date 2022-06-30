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
    fun testinit() {
        cut = TotpGenerator()
    }

    @Test
    fun testGenerateCode_codesMatchAuthenticatorGeneratedCode() {
        val expected = "223710"

        cut.clock = Clock.fixed(Instant.ofEpochMilli(1656115662383), ZoneId.systemDefault())
        val actual1 = cut.generateCode(secret)
        cut.clock = Clock.fixed(Instant.ofEpochMilli(1656115668553), ZoneId.systemDefault())
        val actual2 = cut.generateCode(secret)
        cut.clock = Clock.fixed(Instant.ofEpochMilli(1656115777419), ZoneId.systemDefault())
        val actual3 = cut.generateCode(secret)

        Assertions.assertAll(
            Executable { Assertions.assertEquals(expected, actual1) { CODE_WAS_NOT_THE_EXPECTED_ONE } },
            Executable { Assertions.assertEquals(expected, actual2) { CODE_WAS_NOT_THE_EXPECTED_ONE } },
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
    fun testGenerateCode_differentSecretsHaveDifferentCodes(timestamp: Long) {
        cut.clock = Clock.fixed(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault())
        val actual1 = cut.generateCode(secret)
        val actual2 = cut.generateCode(secret2)

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

        val actual = cut.generateCode(secret).length

        Assertions.assertEquals(expected, actual) { "Code length was not as expected. $expected not equal to $actual." }
    }

    @Test
    fun testIsCodeValid_checksCodesCorrectly() {
        cut.clock = Clock.fixed(Instant.ofEpochMilli(1656115068732), ZoneId.systemDefault())
        val actual1 = cut.isCodeValid(secret, "196157")
        cut.clock = Clock.fixed(Instant.ofEpochMilli(1656115073318), ZoneId.systemDefault())
        val actual2 = cut.isCodeValid(secret, "355908")

        Assertions.assertAll(
            Executable { Assertions.assertTrue(actual1) { CODE_SHOULD_BE_VALID_BUT_WAS_NOT } },
            Executable { Assertions.assertFalse(actual2) { CODE_SHOULD_NOT_BE_VALID_BUT_WAS } },
        )
    }

    @Test
    fun `isCodeValidWithTolerance checks codes correctly if token is expired with 50s tolerance`() {
        cut.tolerance = 2
        cut.clock = Clock.fixed(Instant.ofEpochMilli(1656605624664), ZoneId.systemDefault())

        val actual1 = cut.isCodeValidWithTolerance(secret, "644152")
        val actual2 = cut.isCodeValidWithTolerance(secret, "289971")
        val actual3 = cut.isCodeValidWithTolerance(secret, "044157")
        val actual4 = cut.isCodeValidWithTolerance(secret, "929325")

        Assertions.assertAll(
            Executable { Assertions.assertFalse(actual1) { CODE_SHOULD_NOT_BE_VALID_BUT_WAS } },
            Executable { Assertions.assertTrue(actual2) { CODE_SHOULD_BE_VALID_BUT_WAS_NOT } },
            Executable { Assertions.assertTrue(actual3) { CODE_SHOULD_BE_VALID_BUT_WAS_NOT } },
            Executable { Assertions.assertTrue(actual4) { CODE_SHOULD_BE_VALID_BUT_WAS_NOT } }
        )
    }

    @Test
    fun `isCodeValidWithTolerance checks codes correctly if token is expired with 95s tolerance`() {
        cut.tolerance = 3
        cut.clock = Clock.fixed(Instant.ofEpochMilli(1656605624664), ZoneId.systemDefault())

        val actual1 = cut.isCodeValidWithTolerance(secret, "644152")
        val actual2 = cut.isCodeValidWithTolerance(secret, "289971")
        val actual3 = cut.isCodeValidWithTolerance(secret, "044157")
        val actual4 = cut.isCodeValidWithTolerance(secret, "929325")

        Assertions.assertAll(
            Executable { Assertions.assertTrue(actual1) { CODE_SHOULD_BE_VALID_BUT_WAS_NOT } },
            Executable { Assertions.assertTrue(actual2) { CODE_SHOULD_BE_VALID_BUT_WAS_NOT } },
            Executable { Assertions.assertTrue(actual3) { CODE_SHOULD_BE_VALID_BUT_WAS_NOT } },
            Executable { Assertions.assertTrue(actual4) { CODE_SHOULD_BE_VALID_BUT_WAS_NOT } }
        )
    }
}