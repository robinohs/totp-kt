package dev.robinohs.totpkt.otp.hotp

import org.junit.jupiter.api.*
import org.junit.jupiter.api.function.Executable

/**
 * @author : Robin Ohs
 * @created : 24.06.2022
 * @since : 1.0.0
 */
internal class HotpGeneratorTest {
    private val secret = "IJAU CQSB IJAU EQKB".toByteArray()
    private val secret2 = "BJAU CQSB IJAU EQKB".toByteArray()
    private lateinit var cut: HotpGenerator

    @BeforeEach
    fun testInit() {
        cut = HotpGenerator()
    }

    /**
     * Constructor has logic, so it needs to be tested.
     */
    @Test
    fun testConstructor_validatesArguments() {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            HotpGenerator(
                codeLength = -5
            )
        }
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

    @Test
    fun testGenerateCode_validCodes() {
        val expected = "196157"

        val actual = cut.generateCode(secret, 55203835)

        Assertions.assertEquals(expected, actual) {
            "Code was not the expected one."
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
            Executable { Assertions.assertTrue(actual1) { "First code should be valid but was not." } },
            Executable { Assertions.assertFalse(actual2) { "Second code should not be valid but was." } },
        )
    }

}