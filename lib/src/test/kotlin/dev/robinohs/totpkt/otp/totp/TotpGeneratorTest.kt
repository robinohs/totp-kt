package dev.robinohs.totpkt.otp.totp

import org.junit.jupiter.api.*
import org.junit.jupiter.api.function.Executable
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.time.Duration

private const val FIRST_CODE_WAS_NOT_THE_EXPECTED_ONE = "First code was not the expected one."
private const val SECOND_CODE_WAS_NOT_THE_EXPECTED_ONE = "Second code was not the expected one."
private const val CODE_SHOULD_NOT_BE_VALID_BUT_WAS = "Code should not be valid but was."
private const val CODE_SHOULD_BE_VALID_BUT_WAS_NOT = "Code should be valid but was not."

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

    /**
     * Constructor has logic, so it needs to be tested.
     */
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
                        tolerance = Duration.ofSeconds(-5)
                    )
                }
            }
        )
    }

    /**
     * Setter has logic, so it needs to be tested.
     */
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

    /**
     * Setter has logic, so it needs to be tested.
     */
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

    /**
     * Setter has logic, so it needs to be tested.
     */
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

    /**
     * Setter has logic, so it needs to be tested.
     */
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

    /**
     * Setter has logic, so it needs to be tested.
     */
    @TestFactory
    fun testToleranceSetter_negativeToleranceIllegal() = listOf(
        Duration.ofSeconds(-5), Duration.ofMinutes(-22),
    ).map {
        DynamicTest.dynamicTest("setting tolerance to $it results in an IllegalArgumentException") {
            Assertions.assertThrows(IllegalArgumentException::class.java) {
                cut.tolerance = it
            }
        }
    }

    /**
     * Setter has logic, so it needs to be tested.
     */
    @TestFactory
    fun testToleranceSetter_zeroOrPositiveDurationIsSet() = listOf(
        Duration.ofSeconds(66), Duration.ofMinutes(0), Duration.ofDays(2)
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
            Executable { Assertions.assertEquals(expected, actual1) { FIRST_CODE_WAS_NOT_THE_EXPECTED_ONE } },
            Executable { Assertions.assertEquals(expected, actual2) { SECOND_CODE_WAS_NOT_THE_EXPECTED_ONE } },
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
        val actual = cut.isCodeValidWithTolerance(secret, 1656115068732, "196157")

        Assertions.assertTrue(actual) { CODE_SHOULD_BE_VALID_BUT_WAS_NOT }
    }

    @Test
    fun testIsCodeValidWithTolerance_oldTimeslotButInTolerance() {
        val actual = cut.isCodeValidWithTolerance(secret, 1656116160719, "853702")

        Assertions.assertTrue(actual) { CODE_SHOULD_BE_VALID_BUT_WAS_NOT }
    }

    @Test
    fun testIsCodeValidWithTolerance_oldTimeslotAndNotInTolerance() {
        val actual = cut.isCodeValidWithTolerance(secret, 1656116466490, "452088")

        Assertions.assertFalse(actual) { CODE_SHOULD_NOT_BE_VALID_BUT_WAS }
    }

    @Test
    fun testIsCodeValidWithTolerance_oldTimeSlotButInExtendedTolerance() {
        cut.tolerance = Duration.ofSeconds(10)

        val actual = cut.isCodeValidWithTolerance(secret, 1656116466490, "452088")

        Assertions.assertTrue(actual) { CODE_SHOULD_NOT_BE_VALID_BUT_WAS }
    }

    @Test
    fun testIsCodeValidWithTolerance_oldTimeSlotButInExtendedToleranceOf50Seconds() {
        cut.tolerance = Duration.ofSeconds(50)

        val actual1 = cut.isCodeValidWithTolerance(secret, 1656118534840, "956804")
        val actual2 = cut.isCodeValidWithTolerance(secret, 1656118534840, "364536")
        val actual3 = cut.isCodeValidWithTolerance(secret, 1656118534840, "326491")
        val actual4 = cut.isCodeValidWithTolerance(secret, 1656118534840, "110215")

        Assertions.assertAll(
            Executable { Assertions.assertFalse(actual1) { CODE_SHOULD_NOT_BE_VALID_BUT_WAS } },
            Executable { Assertions.assertTrue(actual2) { CODE_SHOULD_BE_VALID_BUT_WAS_NOT } },
            Executable { Assertions.assertTrue(actual3) { CODE_SHOULD_BE_VALID_BUT_WAS_NOT } },
            Executable { Assertions.assertTrue(actual4) { CODE_SHOULD_BE_VALID_BUT_WAS_NOT } }
        )
    }

    @Test
    fun testIsCodeValidWithTolerance_oldTimeSlotButInExtendedToleranceOf95Seconds() {
        cut.tolerance = Duration.ofSeconds(95)

        val actual1 = cut.isCodeValidWithTolerance(secret, 1656118534840, "956804")
        val actual2 = cut.isCodeValidWithTolerance(secret, 1656118534840, "364536")
        val actual3 = cut.isCodeValidWithTolerance(secret, 1656118534840, "326491")
        val actual4 = cut.isCodeValidWithTolerance(secret, 1656118534840, "110215")

        Assertions.assertAll(
            Executable { Assertions.assertTrue(actual1) { CODE_SHOULD_BE_VALID_BUT_WAS_NOT } },
            Executable { Assertions.assertTrue(actual2) { CODE_SHOULD_BE_VALID_BUT_WAS_NOT } },
            Executable { Assertions.assertTrue(actual3) { CODE_SHOULD_BE_VALID_BUT_WAS_NOT } },
            Executable { Assertions.assertTrue(actual4) { CODE_SHOULD_BE_VALID_BUT_WAS_NOT } }
        )
    }
}