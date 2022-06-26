package dev.robinohs.totpkt.recovery

import dev.robinohs.totpkt.random.RandomGenerator
import dev.robinohs.totpkt.recovery.RecoveryCodeConfig.DEFAULT_LENGTH_OF_BLOCK
import dev.robinohs.totpkt.recovery.RecoveryCodeConfig.DEFAULT_NUMBER_OF_BLOCKS
import org.junit.jupiter.api.*

/**
 * @author : Robin Ohs
 * @created : 23.06.2022
 * @since : 0.0.1
 */
internal class RecoveryCodeGeneratorTest {

    private lateinit var cut: RecoveryCodeGenerator

    @BeforeEach
    fun init() {
        cut = RecoveryCodeGenerator()
    }

    @TestFactory
    fun `RecoveryCodeGenerator number of blocks cannot be set to 0 or a negative number`() = listOf(
        -55, -1, 0
    ).map {
        DynamicTest.dynamicTest("setting numberOfBlocks to $it results in an IllegalArgumentException") {
            Assertions.assertThrows(IllegalArgumentException::class.java) {
                cut.numberOfBlocks = it
            }
        }
    }

    @TestFactory
    fun `RecoveryCodeGenerator block length cannot be set to 0 or a negative number`() = listOf(
        -55, -1, 0
    ).map {
        DynamicTest.dynamicTest("setting blockLength to $it results in an IllegalArgumentException") {
            Assertions.assertThrows(IllegalArgumentException::class.java) {
                cut.blockLength = it
            }
        }
    }

    @Test
    fun `generateSingleRecoveryCode has correct format and charset with default values`() {
        val expectedFormatRegex =
            "[A-z\\d]{$DEFAULT_LENGTH_OF_BLOCK}((-[A-z\\d]{$DEFAULT_LENGTH_OF_BLOCK})+)?".toRegex()

        val actual = cut.generateSingleRecoveryCode()

        val doesRegexMatch = expectedFormatRegex.matchEntire(actual) != null
        Assertions.assertTrue(doesRegexMatch) {
            "Format was not matched by regex but should."
        }
    }

    @Test
    fun `generateSingleRecoveryCode has correct length with default values`() {
        val expected = DEFAULT_NUMBER_OF_BLOCKS * DEFAULT_LENGTH_OF_BLOCK + (DEFAULT_NUMBER_OF_BLOCKS - 1)

        val actual = cut.generateSingleRecoveryCode().length

        Assertions.assertEquals(expected, actual) {
            "The recovery code was not as long as expected."
        }
    }

    @Test
    fun `generateSingleRecoveryCode has correct format and charset with changed values`() {
        cut.numberOfBlocks = 10
        cut.blockLength = 8
        val expectedFormatRegex = "[A-z\\d]{8}((-[A-z\\d]{8})+)?".toRegex()

        val actual = cut.generateSingleRecoveryCode()

        val doesRegexMatch = expectedFormatRegex.matchEntire(actual) != null
        Assertions.assertTrue(doesRegexMatch) {
            "Format was not matched by regex but should."
        }
    }

    @Test
    fun `generateSingleRecoveryCode has correct length with changed values`() {
        cut.numberOfBlocks = 10
        cut.blockLength = 8
        val expected = 10 * 8 + (10 - 1)

        val actual = cut.generateSingleRecoveryCode().length

        Assertions.assertEquals(expected, actual) {
            "The recovery code was not as long as expected."
        }
    }

    @Test
    fun `generateSingleRecoveryCode uses correct charset with changed values`() {
        val charPool = listOf('A', 'B', 'C', 'D')
        cut.randomGenerator = RandomGenerator(charPool = charPool)
        val expectedFormatRegex = "[A-D]{$DEFAULT_LENGTH_OF_BLOCK}((-[A-D]{$DEFAULT_LENGTH_OF_BLOCK})+)?".toRegex()

        val actual = cut.generateSingleRecoveryCode()

        val doesRegexMatch = expectedFormatRegex.matchEntire(actual) != null
        Assertions.assertTrue(doesRegexMatch) {
            "Not all characters were taken from the expected set $charPool."
        }
    }

    @TestFactory
    fun `generateRecoveryCodes throws an IllegalArgumentException for negative number of codes`() = listOf(
        -55, -1
    ).map {
        DynamicTest.dynamicTest("generate $it codes results in an IllegalArgumentException") {
            Assertions.assertThrows(IllegalArgumentException::class.java) {
                cut.generateRecoveryCodes(it)
            }
        }
    }

    @Test
    fun `generateRecoveryCodes creates the default number of recovery codes if no argument given`() {
        val expectedNumber = RecoveryCodeConfig.DEFAULT_NUMBER_OF_RECOVERY_CODES

        val actualNumber = cut.generateRecoveryCodes().size

        Assertions.assertEquals(expectedNumber, actualNumber) {
            "The number of generated recovery codes was wrong."
        }
    }

    @Test
    fun `generateRecoveryCodes creates the given number of recovery codes`() {
        val expectedNumber = 15

        val actualNumber = cut.generateRecoveryCodes(15).size

        Assertions.assertEquals(expectedNumber, actualNumber) {
            "The number of generated recovery codes was wrong."
        }
    }

    @TestFactory
    fun `generateRecoveryCodes creates recovery codes with the correct format`(): List<DynamicTest> {
        val expectedFormatRegex =
            "[A-z\\d]{$DEFAULT_LENGTH_OF_BLOCK}((-[A-z\\d]{$DEFAULT_LENGTH_OF_BLOCK})+)?".toRegex()

        return cut.generateRecoveryCodes().map {
            DynamicTest.dynamicTest("format of $it was not as expected") {
                val doesRegexMatch = expectedFormatRegex.matchEntire(it) != null
                Assertions.assertTrue(doesRegexMatch) {
                    "Format was not matched by regex but should."
                }
            }
        }
    }

    @TestFactory
    fun `generateRecoveryCodes creates recovery codes with the correct format with changed values`(): List<DynamicTest> {
        cut.numberOfBlocks = 10
        cut.blockLength = 8
        val expectedFormatRegex = "[A-z\\d]{8}((-[A-z\\d]{8})+)?".toRegex()

        return cut.generateRecoveryCodes().map {
            DynamicTest.dynamicTest("format of $it was not as expected") {
                val doesRegexMatch = expectedFormatRegex.matchEntire(it) != null
                Assertions.assertTrue(doesRegexMatch) {
                    "Format was not matched by regex but should."
                }
            }
        }
    }

    @TestFactory
    fun `generateRecoveryCodes creates recovery codes with characters from the changed char pool`(): List<DynamicTest> {
        val charPool = listOf('A', 'B', 'C', 'D')
        cut.randomGenerator = RandomGenerator(charPool = charPool)
        val expectedFormatRegex = "[A-D]{$DEFAULT_LENGTH_OF_BLOCK}((-[A-D]{$DEFAULT_LENGTH_OF_BLOCK})+)?".toRegex()

        return cut.generateRecoveryCodes().map {
            DynamicTest.dynamicTest("not all characters of $it came from the charpool") {
                val doesRegexMatch = expectedFormatRegex.matchEntire(it) != null
                Assertions.assertTrue(doesRegexMatch) {
                    "Format was not matched by regex but should."
                }
            }
        }
    }
}