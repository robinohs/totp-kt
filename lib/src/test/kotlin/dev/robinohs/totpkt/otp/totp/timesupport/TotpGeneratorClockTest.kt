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

private const val CODE_WAS_NOT_THE_EXPECTED_ONE = "Code was not the expected one."
private const val CODE_SHOULD_NOT_BE_VALID_BUT_WAS = "Code should not be valid but was."
private const val CODE_SHOULD_BE_VALID_BUT_WAS_NOT = "Code should be valid but was not."

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
        cut.tolerance = Duration.ofSeconds(50)
        cut.clock = Clock.fixed(Instant.ofEpochMilli(1656118534840), ZoneId.systemDefault())

        val actual1 = cut.isCodeValidWithTolerance(secret, "956804")
        val actual2 = cut.isCodeValidWithTolerance(secret, "364536")
        val actual3 = cut.isCodeValidWithTolerance(secret, "326491")
        val actual4 = cut.isCodeValidWithTolerance(secret, "110215")

        Assertions.assertAll(
            Executable { Assertions.assertFalse(actual1) { CODE_SHOULD_NOT_BE_VALID_BUT_WAS } },
            Executable { Assertions.assertTrue(actual2) { CODE_SHOULD_BE_VALID_BUT_WAS_NOT } },
            Executable { Assertions.assertTrue(actual3) { CODE_SHOULD_BE_VALID_BUT_WAS_NOT } },
            Executable { Assertions.assertTrue(actual4) { CODE_SHOULD_BE_VALID_BUT_WAS_NOT } }
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
            Executable { Assertions.assertTrue(actual1) { CODE_SHOULD_BE_VALID_BUT_WAS_NOT } },
            Executable { Assertions.assertTrue(actual2) { CODE_SHOULD_BE_VALID_BUT_WAS_NOT } },
            Executable { Assertions.assertTrue(actual3) { CODE_SHOULD_BE_VALID_BUT_WAS_NOT } },
            Executable { Assertions.assertTrue(actual4) { CODE_SHOULD_BE_VALID_BUT_WAS_NOT } }
        )
    }
}