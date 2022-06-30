package dev.robinohs.totpkt.otp.totp

import TestMessageConstants.CODE_SHOULD_BE_VALID_BUT_WAS_NOT
import TestMessageConstants.CODE_SHOULD_NOT_BE_VALID_BUT_WAS
import TestMessageConstants.CODE_WAS_NOT_THE_EXPECTED_ONE
import org.junit.jupiter.api.*
import org.junit.jupiter.api.function.Executable
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.time.Duration

/**
 * @author : Robin Ohs
 * @created : 24.06.2022
 * @since : 1.0.0
 */
internal class TotpGeneratorTest {
    private val secret = "IJAU CQSB IJAU EQKB".toByteArray()
    private val secret2 = "BJAU CQSB IJAU EQKB".toByteArray()

    private lateinit var cut: TotpGenerator

    @BeforeEach
    fun testInit() {
        cut = TotpGenerator()
    }

    @Test
    fun testConstructor_validatesArguments() {
        Assertions.assertAll(
            Executable {
                Assertions.assertThrows(IllegalArgumentException::class.java) {
                    TotpGenerator(
                        codeLength = -5
                    )
                }
            },
            Executable {
                Assertions.assertThrows(IllegalArgumentException::class.java) {
                    TotpGenerator(
                        timePeriod = Duration.ofSeconds(0)
                    )
                }
            },
            Executable {
                Assertions.assertThrows(IllegalArgumentException::class.java) {
                    TotpGenerator(
                        timePeriod = Duration.ofSeconds(-5)
                    )
                }
            },
            Executable {
                Assertions.assertThrows(IllegalArgumentException::class.java) {
                    TotpGenerator(
                        tolerance = -4
                    )
                }
            }
        )
    }

    @Test
    fun testConstructor_allowsValidArguments() {
        Assertions.assertAll(
            Executable {
                val expected = 5

                val actual = TotpGenerator(
                    codeLength = expected
                ).codeLength

                Assertions.assertEquals(expected, actual) { "Expected and actual code length are equal." }
            },
            Executable {
                val expected = Duration.ofSeconds(6)

                val actual = TotpGenerator(
                    timePeriod = expected
                ).timePeriod

                Assertions.assertEquals(expected, actual) { "Expected and actual time period are equal." }
            },
            Executable {
                val expected = 4

                val actual = TotpGenerator(
                    tolerance = expected
                ).tolerance

                Assertions.assertEquals(expected, actual) { "Expected and actual tolerance are equal." }
            },
        )
    }

    @TestFactory
    fun testCodeLengthSetter_negativeNumberIllegal() = listOf(
        -55, -1
    ).map {
        DynamicTest.dynamicTest("setting codeLength to $it results in an IllegalArgumentException") {
            Assertions.assertThrows(IllegalArgumentException::class.java) {
                cut.codeLength = it
            }
        }
    }

    @TestFactory
    fun testCodeLengthSetter_zeroOrPositiveNumberIsSet() = listOf(
        0, 1, 10, 13
    ).map { expected ->
        DynamicTest.dynamicTest("setting codeLength to $expected is allowed") {
            cut.codeLength = expected

            val actual = cut.codeLength

            Assertions.assertEquals(expected, actual) {
                "Setter did not set codeLength to $expected, instead was $actual."
            }
        }
    }

    @TestFactory
    fun testTimePeriodSetter_zeroLengthOrNegativeDurationIllegal() = listOf(
        Duration.ofSeconds(0), Duration.ofSeconds(-5), Duration.ofMinutes(-22),
    ).map {
        DynamicTest.dynamicTest("setting timePeriod to $it results in an IllegalArgumentException") {
            Assertions.assertThrows(IllegalArgumentException::class.java) {
                cut.timePeriod = it
            }
        }
    }

    @TestFactory
    fun testTimePeriodSetter_zeroOrPositiveNumberIsSet() = listOf(
        Duration.ofSeconds(66), Duration.ofMinutes(2), Duration.ofDays(2)
    ).map { expected ->
        DynamicTest.dynamicTest("setting timePeriod to $expected is allowed") {
            cut.timePeriod = expected

            val actual = cut.timePeriod

            Assertions.assertEquals(expected, actual) {
                "Setter did not set timePeriod to $expected, instead was $actual."
            }
        }
    }

    @TestFactory
    fun testToleranceSetter_negativeToleranceIllegal() = listOf(
        -5, -22,
    ).map {
        DynamicTest.dynamicTest("setting tolerance to $it results in an IllegalArgumentException") {
            Assertions.assertThrows(IllegalArgumentException::class.java) {
                cut.tolerance = it
            }
        }
    }

    @TestFactory
    fun testToleranceSetter_zeroOrPositiveDurationIsSet() = listOf(
        3, 0, 2
    ).map { expected ->
        DynamicTest.dynamicTest("setting tolerance to $expected is allowed") {
            cut.tolerance = expected

            val actual = cut.tolerance

            Assertions.assertEquals(expected, actual) {
                "Setter did not set tolerance to $expected, instead was $actual."
            }
        }
    }

    @Test
    fun testGenerateCode_codesMatchAuthenticatorGeneratedCode() {
        val expected = "316152"

        val actual1 = cut.generateCode(secret, 1656114883887)
        val actual2 = cut.generateCode(secret, 1656114891677)

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
        val actual1 = cut.generateCode(secret, timestamp)
        val actual2 = cut.generateCode(secret2, timestamp)

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

        val actual = cut.generateCode(secret, 1656115068732).length

        Assertions.assertEquals(expected, actual) { "Code length was not as expected. $expected not equal to $actual." }
    }

    @Test
    fun testIsCodeValid_checksCodesCorrectly() {
        val actual1 = cut.isCodeValid(secret, 1656115068732, "196157")
        val actual2 = cut.isCodeValid(secret, 1656115073318, "355908")

        Assertions.assertAll(
            Executable { Assertions.assertTrue(actual1) { CODE_SHOULD_BE_VALID_BUT_WAS_NOT } },
            Executable { Assertions.assertFalse(actual2) { CODE_SHOULD_NOT_BE_VALID_BUT_WAS } },
        )
    }

    @Test
    fun testIsCodeValid_differentSecretsHaveDifferentCodes() {
        val actual1 = cut.isCodeValid(secret, 1656115068732, "196157")
        val actual2 = cut.isCodeValid(secret2, 1656115068732, "196157")

        Assertions.assertAll(
            Executable { Assertions.assertTrue(actual1) { CODE_SHOULD_BE_VALID_BUT_WAS_NOT } },
            Executable { Assertions.assertFalse(actual2) { CODE_SHOULD_NOT_BE_VALID_BUT_WAS } },
        )
    }

    @Test
    fun testIsCodeValid_differentTimestampsHaveDifferentCodes() {
        val actual1 = cut.isCodeValid(secret, 1656115068732, "196157")
        val actual2 = cut.isCodeValid(secret, 1656115073318, "355908")

        Assertions.assertAll(
            Executable { Assertions.assertTrue(actual1) { CODE_SHOULD_BE_VALID_BUT_WAS_NOT } },
            Executable { Assertions.assertFalse(actual2) { CODE_SHOULD_NOT_BE_VALID_BUT_WAS } },
        )
    }

    @Test
    fun testIsCodeValidWithTolerance_sameTimeslot() {
        val actual = cut.isCodeValidWithTolerance(secret, 1656605624664, "929325")

        Assertions.assertTrue(actual) { CODE_SHOULD_BE_VALID_BUT_WAS_NOT }
    }

    @Test
    fun testIsCodeValidWithTolerance_oldTimeslotButInTolerance() {
        val actual = cut.isCodeValidWithTolerance(secret, 1656605624664, "044157")

        Assertions.assertTrue(actual) { CODE_SHOULD_BE_VALID_BUT_WAS_NOT }
    }

    @Test
    fun testIsCodeValidWithTolerance_oldTimeslotAndNotInTolerance() {
        val actual = cut.isCodeValidWithTolerance(secret, 1656605624664, "289971")

        Assertions.assertFalse(actual) { CODE_SHOULD_NOT_BE_VALID_BUT_WAS }
    }

    @Test
    fun testIsCodeValidWithTolerance_multipleInExtendedTolerance() {
        cut.tolerance = 2

        val actual1 = cut.isCodeValidWithTolerance(secret, 1656605624664, "644152")
        val actual2 = cut.isCodeValidWithTolerance(secret, 1656605624664, "289971")
        val actual3 = cut.isCodeValidWithTolerance(secret, 1656605624664, "044157")
        val actual4 = cut.isCodeValidWithTolerance(secret, 1656605624664, "929325")

        Assertions.assertAll(
            Executable { Assertions.assertFalse(actual1) { CODE_SHOULD_NOT_BE_VALID_BUT_WAS } },
            Executable { Assertions.assertTrue(actual2) { CODE_SHOULD_BE_VALID_BUT_WAS_NOT } },
            Executable { Assertions.assertTrue(actual3) { CODE_SHOULD_BE_VALID_BUT_WAS_NOT } },
            Executable { Assertions.assertTrue(actual4) { CODE_SHOULD_BE_VALID_BUT_WAS_NOT } }
        )
    }

    @Test
    fun testIsCodeValidWithTolerance_allInExtendedTolerance() {
        cut.tolerance = 3

        val actual1 = cut.isCodeValidWithTolerance(secret, 1656605624664, "644152")
        val actual2 = cut.isCodeValidWithTolerance(secret, 1656605624664, "289971")
        val actual3 = cut.isCodeValidWithTolerance(secret, 1656605624664, "044157")
        val actual4 = cut.isCodeValidWithTolerance(secret, 1656605624664, "929325")

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
    fun testCalculateTimeslotBeginning(timestamp: Long, expected: Long) {
        val actual = cut.calculateTimeslotBeginning(timestamp)

        Assertions.assertEquals(expected, actual) { "Time slot beginning was expected one. $expected not equal to $actual." }
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
        val expected = Duration.ofSeconds(durationInSeconds)

        val actual = cut.calculateRemainingTime(timestamp)

        Assertions.assertEquals(expected, actual) { "Time slot beginning was expected one. $expected not equal to $actual." }
    }
}