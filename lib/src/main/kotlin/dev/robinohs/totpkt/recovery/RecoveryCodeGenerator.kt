package dev.robinohs.totpkt.recovery

import dev.robinohs.totpkt.random.RandomGenerator

/**
 * @author : Robin Ohs
 * @created : 23.06.2022
 * @since : 0.0.1
 */
class RecoveryCodeGenerator(
    numberOfBlocks: Int = RecoveryCodeConfig.DEFAULT_NUMBER_OF_BLOCKS,
    blockLength: Int = RecoveryCodeConfig.DEFAULT_LENGTH_OF_BLOCK,
    var randomGenerator: RandomGenerator = RandomGenerator()
) {
    init {
        require(numberOfBlocks >= 1) { "Number of blocks must be >= 1." }
        require(blockLength >= 1) { "Block length must be >= 1." }
    }

    var numberOfBlocks = numberOfBlocks
        set(value) {
            require(value >= 1) { "Number of blocks must be >= 1." }
            field = value
        }

    var blockLength = blockLength
        set(value) {
            require(value >= 1) { "Block length must be >= 1." }
            field = value
        }

    /**
     * Creates a randomly generated string from characters of the charPool and puts it into block form.
     * E.g., AAAA-BBBB-CCCC-DDDD for numberOfBlocks 4 and blockLength 4
     *
     * @return the randomly generated string of character blocks.
     */
    fun generateSingleRecoveryCode(): String = (1..numberOfBlocks)
        .joinToString("-") {
            randomGenerator.generateRandomStringFromCharPool(blockLength)
        }

    /**
     * Generates a list of recovery codes with a given size or default number.
     *
     * @param number optional param to specify the number of generated recovery codes (Default=5).
     * @throws IllegalArgumentException if the number of codes is negative.
     * @return the list of randomly generated recovery codes.
     */
    fun generateRecoveryCodes(number: Int = RecoveryCodeConfig.DEFAULT_NUMBER_OF_RECOVERY_CODES): List<String> {
        require(number < 0) { "Number must be >= 0, but was $number." }
        return (1..number).map { generateSingleRecoveryCode() }
    }
}