package dev.robinohs.totpkt.recovery

import dev.robinohs.totpkt.random.RandomGenerator
import dev.robinohs.totpkt.recovery.RecoveryCodeConfig.DEFAULT_LENGTH_OF_BLOCK
import dev.robinohs.totpkt.recovery.RecoveryCodeConfig.DEFAULT_NUMBER_OF_BLOCKS
import org.junit.jupiter.api.*
import org.junit.jupiter.api.function.Executable

/**
 * @author : Robin Ohs
 * @created : 23.06.2022
 * @since : 1.0.0
 */
internal class RecoveryCodeGeneratorTest {

    private lateinit var cut: RecoveryCodeGenerator

    @BeforeEach
    fun init() {
        cut = RecoveryCodeGenerator()
    }

    @Test
    fun constructorTest_validatesArguments() {
        Assertions.assertAll(
            Executable {
                Assertions.assertThrows(IllegalArgumentException::class.java) {
                    RecoveryCodeGenerator(
                        numberOfBlocks = -5
                    )
                }
            },
            Executable {
                Assertions.assertThrows(IllegalArgumentException::class.java) {
                    RecoveryCodeGenerator(
                        numberOfBlocks = 0
                    )
                }
            },
            Executable {
                Assertions.assertThrows(IllegalArgumentException::class.java) {
                    RecoveryCodeGenerator(
                        blockLength = -5
                    )
                }
            },
            Executable {
                Assertions.assertThrows(IllegalArgumentException::class.java) {
                    RecoveryCodeGenerator(
                        blockLength = 0
                    )
                }
            },
        )
    }

    /**
     * Setter has logic, so it needs to be tested.
     */
    @TestFactory
    fun numberOfBlocksSetterTest_zeroOrNegativeNumberIllegal() = listOf(
        -55, -1, 0
    ).map {
        DynamicTest.dynamicTest("setting numberOfBlocks to $it results in an IllegalArgumentException") {
            Assertions.assertThrows(IllegalArgumentException::class.java) {
                cut.numberOfBlocks = it
            }
        }
    }

    /**
     * Setter has logic, so it needs to be tested.
     */
    @TestFactory
    fun numberOfBlocksSetterTest_positiveNumberIsSet() = listOf(
        55, 4, 3
    ).map { expected ->
        DynamicTest.dynamicTest("setting numberOfBlocks to $expected is allowed") {
            cut.numberOfBlocks = expected

            val actual = cut.numberOfBlocks

            Assertions.assertEquals(expected, actual) {
                "Setter did not set number to $expected, instead was $actual."
            }
        }
    }

    /**
     * Setter has logic, so it needs to be tested.
     */
    @TestFactory
    fun blockLengthSetterTest_zeroOrNegativeNumberIllegal() = listOf(
        -55, -1, 0
    ).map {
        DynamicTest.dynamicTest("setting blockLength to $it results in an IllegalArgumentException") {
            Assertions.assertThrows(IllegalArgumentException::class.java) {
                cut.blockLength = it
            }
        }
    }

    /**
     * Setter has logic, so it needs to be tested.
     */
    @TestFactory
    fun blockLengthSetterTest_positiveNumberIsSet() = listOf(
        55, 4, 3
    ).map { expected ->
        DynamicTest.dynamicTest("setting blockLength to $expected is allowed") {
            cut.blockLength = expected

            val actual = cut.blockLength

            Assertions.assertEquals(expected, actual) {
                "Setter did not set number to $expected, instead was $actual."
            }
        }
    }

    @Test
    fun generateRecoveryCodeTest_formatUnchangedConfig() {
        val expectedFormatRegex =
            "[A-z\\d]{$DEFAULT_LENGTH_OF_BLOCK}((-[A-z\\d]{$DEFAULT_LENGTH_OF_BLOCK})+)?".toRegex()

        val actual = cut.generateRecoveryCode()

        Assertions.assertTrue(expectedFormatRegex.matchEntire(actual) != null) {
            "Format was not matched by regex but should."
        }
    }

    @TestFactory
    fun generateRecoveryCodeTest_length() = mapOf(
        DEFAULT_NUMBER_OF_BLOCKS to DEFAULT_LENGTH_OF_BLOCK,
        10 to 8,
        12 to 5,
        4 to 9,
        1 to 1,
    ).map { (numberOfBlocks, blockLength) ->
        DynamicTest.dynamicTest("code generated from $numberOfBlocks blocks with a length of $blockLength has wrong length") {
            cut.numberOfBlocks = numberOfBlocks
            cut.blockLength = blockLength
            val expected = numberOfBlocks * blockLength + (numberOfBlocks - 1)

            val actual = cut.generateRecoveryCode().length

            Assertions.assertEquals(expected, actual) {
                "The recovery code was not as long as expected."
            }
        }
    }

    @TestFactory
    fun generateRecoveryCodeTest_format() = mapOf(
        DEFAULT_NUMBER_OF_BLOCKS to DEFAULT_LENGTH_OF_BLOCK,
        10 to 8,
        12 to 5,
        4 to 9,
        1 to 1,
    ).map { (numberOfBlocks, blockLength) ->
        DynamicTest.dynamicTest("code generated from $numberOfBlocks blocks with a length of $blockLength has wrong format") {
            val expectedFormatRegex = "[A-z\\d]{$blockLength}((-[A-z\\d]{$blockLength})+)?".toRegex()
            cut.numberOfBlocks = numberOfBlocks
            cut.blockLength = blockLength

            val actual = cut.generateRecoveryCode()

            Assertions.assertTrue(expectedFormatRegex.matchEntire(actual) != null) {
                "Format was not matched by regex but should."
            }
        }
    }

    @Test
    fun generateRecoveryCodeTest_changedCharset() {
        val charPool = listOf('A', 'B', 'C', 'D')
        cut.randomGenerator = RandomGenerator(charPool = charPool)
        val expectedFormatRegex = "[A-D]{$DEFAULT_LENGTH_OF_BLOCK}((-[A-D]{$DEFAULT_LENGTH_OF_BLOCK})+)?".toRegex()

        val actual = cut.generateRecoveryCode()

        Assertions.assertTrue(expectedFormatRegex.matchEntire(actual) != null) {
            "Not all characters were taken from the expected set $charPool."
        }
    }

    @TestFactory
    fun generateRecoveryCodesTest_NegativeNumberOfCodes() = listOf(
        -55, -1
    ).map {
        DynamicTest.dynamicTest("generate $it codes results in an IllegalArgumentException") {
            Assertions.assertThrows(IllegalArgumentException::class.java) {
                cut.generateRecoveryCodes(it)
            }
        }
    }

    @Test
    fun generateRecoveryCodesTest_defaultValue() {
        val expectedNumber = RecoveryCodeConfig.DEFAULT_NUMBER_OF_RECOVERY_CODES

        val actualNumber = cut.generateRecoveryCodes().size

        Assertions.assertEquals(expectedNumber, actualNumber) {
            "The number of generated recovery codes was wrong."
        }
    }

    @TestFactory
    fun generateRecoveryCodesTest_validValues() = listOf(
        0, 5, 10, 12
    ).map { expected ->
        DynamicTest.dynamicTest("did not generate $expected codes") {
            val actual = cut.generateRecoveryCodes(expected).size

            Assertions.assertEquals(expected, actual) {
                "The number of generated recovery codes was wrong."
            }
        }
    }

    @TestFactory
    fun generateRecoveryCodesTest_format(): List<DynamicTest> {
        val expectedFormatRegex =
            "[A-z\\d]{$DEFAULT_LENGTH_OF_BLOCK}((-[A-z\\d]{$DEFAULT_LENGTH_OF_BLOCK})+)?".toRegex()

        return cut.generateRecoveryCodes().map {
            DynamicTest.dynamicTest("format of $it was not as expected") {
                Assertions.assertTrue(expectedFormatRegex.matchEntire(it) != null) {
                    "Format was not matched by regex but should."
                }
            }
        }
    }

    @TestFactory
    fun generateRecoveryCodesTest_formatWithChangedConfig(): List<DynamicTest> {
        cut.numberOfBlocks = 10
        cut.blockLength = 8
        val expectedFormatRegex = "[A-z\\d]{8}((-[A-z\\d]{8})+)?".toRegex()

        return cut.generateRecoveryCodes().map {
            DynamicTest.dynamicTest("format of $it was not as expected") {
                Assertions.assertTrue(expectedFormatRegex.matchEntire(it) != null) {
                    "Format was not matched by regex but should."
                }
            }
        }
    }

    @TestFactory
    fun generateRecoveryCodesTest_formatWithChangedCharset(): List<DynamicTest> {
        val charPool = listOf('A', 'B', 'C', 'D')
        cut.randomGenerator = RandomGenerator(charPool = charPool)
        val expectedFormatRegex = "[A-D]{$DEFAULT_LENGTH_OF_BLOCK}((-[A-D]{$DEFAULT_LENGTH_OF_BLOCK})+)?".toRegex()

        return cut.generateRecoveryCodes().map {
            DynamicTest.dynamicTest("not all characters of $it came from the charpool") {
                Assertions.assertTrue(expectedFormatRegex.matchEntire(it) != null) {
                    "Format was not matched by regex but should."
                }
            }
        }
    }
}