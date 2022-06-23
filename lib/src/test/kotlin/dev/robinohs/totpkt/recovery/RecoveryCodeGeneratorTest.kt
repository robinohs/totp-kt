package dev.robinohs.totpkt.recovery

import dev.robinohs.totpkt.recovery.RecoveryCodeConfig.DEFAULT_LENGTH_OF_BLOCK
import dev.robinohs.totpkt.recovery.RecoveryCodeConfig.DEFAULT_NUMBER_OF_BLOCKS
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * @author : Robin Ohs
 * @created : 23.06.2022
 * @since : 0.0.1
 */
internal class RecoveryCodeGeneratorTest {

    private lateinit var cut: RecoveryCodeGenerator

    @BeforeTest
    fun init() {
        cut = RecoveryCodeGenerator()
    }

    @Test(expected = IllegalArgumentException::class)
    fun `RecoveryCodeGenerator charpool cannot be set to an empty list`() {
        cut.charPool = listOf()
    }

    @Test(expected = IllegalArgumentException::class)
    fun `RecoveryCodeGenerator number of blocks cannot be set to 0`() {
        cut.numberOfBlocks = 0
    }

    @Test(expected = IllegalArgumentException::class)
    fun `RecoveryCodeGenerator number of blocks cannot be set to a negative number`() {
        cut.numberOfBlocks = -5
    }

    @Test(expected = IllegalArgumentException::class)
    fun `RecoveryCodeGenerator block length cannot be set to 0`() {
        cut.blockLength = 0
    }

    @Test(expected = IllegalArgumentException::class)
    fun `RecoveryCodeGenerator block length cannot be set to a negative number`() {
        cut.blockLength = -5
    }

    @Test
    fun `generateSingleRecoveryCode has correct format and charset with default values`() {
        val expectedFormatRegex =
            "[A-z\\d]{$DEFAULT_LENGTH_OF_BLOCK}((-[A-z\\d]{$DEFAULT_LENGTH_OF_BLOCK})+)?".toRegex()

        val actual = cut.generateSingleRecoveryCode()

        val doesRegexMatch = expectedFormatRegex.matchEntire(actual) != null
        assertTrue(doesRegexMatch, "Format was not matched by regex but should.")
    }

    @Test
    fun `generateSingleRecoveryCode has correct length with default values`() {
        val expected = DEFAULT_NUMBER_OF_BLOCKS * DEFAULT_LENGTH_OF_BLOCK + (DEFAULT_NUMBER_OF_BLOCKS - 1)

        val actual = cut.generateSingleRecoveryCode().length

        assertEquals(expected, actual, "The recovery code was not as long as expected.")
    }

    @Test
    fun `generateSingleRecoveryCode has correct format and charset with changed values`() {
        cut.numberOfBlocks = 10
        cut.blockLength = 8
        val expectedFormatRegex = "[A-z\\d]{8}((-[A-z\\d]{8})+)?".toRegex()

        val actual = cut.generateSingleRecoveryCode()

        val doesRegexMatch = expectedFormatRegex.matchEntire(actual) != null
        assertTrue(doesRegexMatch, "Format was not matched by regex but should.")
    }

    @Test
    fun `generateSingleRecoveryCode has correct length with changed values`() {
        cut.numberOfBlocks = 10
        cut.blockLength = 8
        val expected = 10 * 8 + (10 - 1)

        val actual = cut.generateSingleRecoveryCode().length

        assertEquals(expected, actual, "The recovery code was not as long as expected.")
    }

    @Test
    fun `generateSingleRecoveryCode uses correct charset with changed values`() {
        val charPool = listOf('A', 'B', 'C', 'D')
        cut.charPool = charPool
        val expectedFormatRegex = "[A-D]{$DEFAULT_LENGTH_OF_BLOCK}((-[A-D]{$DEFAULT_LENGTH_OF_BLOCK})+)?".toRegex()

        val actual = cut.generateSingleRecoveryCode()

        val doesRegexMatch = expectedFormatRegex.matchEntire(actual) != null
        assertTrue(doesRegexMatch, "Not all characters were taken from the expected set $charPool.")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `generateRecoveryCodes throws an IllegalArgumentException for negative number of codes`() {
        cut.generateRecoveryCodes(-2)
    }

    @Test
    fun `generateRecoveryCodes creates the default number of recovery codes if no argument given`() {
        val expectedNumber = RecoveryCodeConfig.DEFAULT_NUMBER_OF_RECOVERY_CODES

        val actualNumber = cut.generateRecoveryCodes().size

        assertEquals(expectedNumber, actualNumber, "The number of generated recovery codes was wrong.")
    }

    @Test
    fun `generateRecoveryCodes creates the given number of recovery codes`() {
        val expectedNumber = 15

        val actualNumber = cut.generateRecoveryCodes(15).size

        assertEquals(expectedNumber, actualNumber, "The number of generated recovery codes was wrong.")
    }

    @Test
    fun `generateRecoveryCodes creates recovery codes with the correct format`() {
        val expectedFormatRegex =
            "[A-z\\d]{$DEFAULT_LENGTH_OF_BLOCK}((-[A-z\\d]{$DEFAULT_LENGTH_OF_BLOCK})+)?".toRegex()

        val recoveryCodes = cut.generateRecoveryCodes()

        for(recoveryCode in recoveryCodes) {
            val doesRegexMatch = expectedFormatRegex.matchEntire(recoveryCode) != null
            assertTrue(doesRegexMatch, "Format was not matched by regex but should.")
        }
    }

    @Test
    fun `generateRecoveryCodes creates recovery codes with the correct format with changed values`() {
        cut.numberOfBlocks = 10
        cut.blockLength = 8
        val expectedFormatRegex = "[A-z\\d]{8}((-[A-z\\d]{8})+)?".toRegex()

        val recoveryCodes = cut.generateRecoveryCodes()

        for(recoveryCode in recoveryCodes) {
            val doesRegexMatch = expectedFormatRegex.matchEntire(recoveryCode) != null
            assertTrue(doesRegexMatch, "Format was not matched by regex but should.")
        }
    }

    @Test
    fun `generateRecoveryCodes creates recovery codes with characters from the changed char pool`() {
        val charPool = listOf('A', 'B', 'C', 'D')
        cut.charPool = charPool
        val expectedFormatRegex = "[A-D]{$DEFAULT_LENGTH_OF_BLOCK}((-[A-D]{$DEFAULT_LENGTH_OF_BLOCK})+)?".toRegex()

        val recoveryCodes = cut.generateRecoveryCodes()

        for(recoveryCode in recoveryCodes) {
            val doesRegexMatch = expectedFormatRegex.matchEntire(recoveryCode) != null
            assertTrue(doesRegexMatch, "Not all characters were taken from the expected set $charPool.")
        }
    }
}