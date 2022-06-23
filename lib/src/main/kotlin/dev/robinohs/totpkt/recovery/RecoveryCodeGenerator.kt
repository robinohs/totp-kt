package dev.robinohs.totpkt.recovery

import kotlin.random.Random

/**
 * @author : Robin Ohs
 * @created : 23.06.2022
 * @since : 0.0.1
 */
class RecoveryCodeGenerator(
    numberOfBlocks: Int = RecoveryCodeConfig.DEFAULT_NUMBER_OF_BLOCKS,
    blockLength: Int = RecoveryCodeConfig.DEFAULT_LENGTH_OF_BLOCK,
    charPool: List<Char> = RecoveryCodeConfig.DEFAULT_CHARPOOL
) {
    var numberOfBlocks = numberOfBlocks
        set(value) {
            if (value <= 0) throw IllegalArgumentException("Number of blocks must be >= 1.")
            field = value
        }

    var blockLength = blockLength
        set(value) {
            if (value <= 0) throw IllegalArgumentException("Block length must be >= 1.")
            field = value
        }

    var charPool = charPool
        set(value) {
            if (value.isEmpty()) throw IllegalArgumentException("Char pool must not be empty.")
            field = value
        }

    /**
     * Creates a randomly generated string from characters of the charPool and puts it into block form.
     * E.g., AAAA-BBBB-CCCC-DDDD for numberOfBlocks 4 and blockLength 4
     *
     * @return the randomly generated string of character blocks.
     */
    fun generateSingleRecoveryCode(): String {
        return (1..numberOfBlocks)
            .joinToString("-") {
                generateRandomStringOfCharacters(blockLength)
            }
    }

    /**
     * Generates a list of recovery codes with a given size or default 5.
     *
     * @param number optional param to specify the number of generated recovery codes (Default=5).
     * @return the list of randomly generated recovery codes.
     */
    fun generateRecoveryCodes(number: Int = RecoveryCodeConfig.DEFAULT_NUMBER_OF_RECOVERY_CODES): List<String> {
        if (number < 0) throw IllegalArgumentException("Number must be >= 0, but was $number.")
        return (1..number)
            .map { generateSingleRecoveryCode() }
    }

    /**
     * Creates a randomly generated string with characters from the character pool.
     *
     * @param length the length of the generated string.
     * @return the randomly generated string.
     */
    private fun generateRandomStringOfCharacters(length: Int): String {
        if (length < 1) throw IllegalArgumentException("Length must be >= 1, but was $length.")
        return (1..length)
            .map { Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }
}