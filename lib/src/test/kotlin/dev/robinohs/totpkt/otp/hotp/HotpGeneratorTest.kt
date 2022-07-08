package dev.robinohs.totpkt.otp.hotp

import TestMessageConstants.CODE_SHOULD_BE_VALID_BUT_WAS_NOT
import TestMessageConstants.CODE_SHOULD_NOT_BE_VALID_BUT_WAS
import TestMessageConstants.CODE_WAS_NOT_THE_EXPECTED_ONE
import org.apache.commons.codec.binary.Base32
import org.junit.jupiter.api.*
import org.junit.jupiter.api.function.Executable
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

/**
 * @author : Robin Ohs
 * @created : 24.06.2022
 * @since : 1.0.0
 */
internal class HotpGeneratorTest {
    private val secret = "IJAU CQSB IJAU EQKB".toByteArray()
    private val secret2 = "BJAU CQSB IJAU EQKB".toByteArray()
    private val secretSpecification = Base32().encode("12345678901234567890".toByteArray())
    private lateinit var cut: HotpGenerator

    @BeforeEach
    fun testInit() {
        cut = HotpGenerator()
    }

    @Test
    fun testConstructor_validatesArguments() {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            HotpGenerator(
                codeLength = -5
            )
        }
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

    @Test
    fun testGenerateCode_validCodes() {
        val expected = "196157"

        val actual = cut.generateCode(secret, 55203835)

        Assertions.assertEquals(expected, actual) {
            CODE_WAS_NOT_THE_EXPECTED_ONE
        }
    }

    @Test
    fun testGenerateCode_differentCountersHaveDifferentCode() {
        val first = cut.generateCode(secret, 1656090713)
        val second = cut.generateCode(secret, 1656090714)

        Assertions.assertNotEquals(first, second) {
            "Codes should not be the same for the given arguments."
        }
    }

    @Test
    fun testGenerateCode_differentSecretsHaveDifferentCode() {
        val first = cut.generateCode(secret, 1656090713)
        val second = cut.generateCode(secret2, 1656090713)

        Assertions.assertNotEquals(first, second) {
            "Codes should not be the same for two different secrets."
        }
    }

    @Test
    fun testIsCodeValidTest() {
        val actual1 = cut.isCodeValid(secret, 55203835, "196157")
        val actual2 = cut.isCodeValid(secret, 55203835, "355908")

        Assertions.assertAll(
            Executable { Assertions.assertTrue(actual1) { CODE_SHOULD_BE_VALID_BUT_WAS_NOT } },
            Executable { Assertions.assertFalse(actual2) { CODE_SHOULD_NOT_BE_VALID_BUT_WAS } },
        )
    }

    @ParameterizedTest
    @CsvSource(
        "0, 755224",
        "1, 287082",
        "2, 359152",
        "3, 969429",
        "4, 338314",
        "5, 254676",
        "6, 287922",
        "7, 162583",
        "8, 399871",
        "9, 520489"
    )
    fun testSpecificationDefinedCodes(counter: Long, expected: String) {
        val actual = cut.generateCode(secretSpecification, counter)

        Assertions.assertEquals(expected, actual) {
            "Codes should be the same for the given arguments."
        }
    }

}