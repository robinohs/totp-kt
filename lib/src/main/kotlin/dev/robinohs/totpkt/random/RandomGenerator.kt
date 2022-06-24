package dev.robinohs.totpkt.random

import java.security.SecureRandom
import java.util.*

/**
 * @author : Robin Ohs
 * @created : 24.06.2022
 * @since : 0.0.1
 */
class RandomGenerator(
    charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9'),
    var random: Random = SecureRandom()
) {

    var charPool = charPool
        set(value) {
            if (value.isEmpty()) throw IllegalArgumentException("Char pool cannot be empty.")
            field = value
        }

    /**
     * Creates a randomly generated string with characters from the character pool.
     *
     * @param length the length of the generated string.
     * @return the randomly generated string.
     */
    fun generateRandomStringFromCharPool(length: Int): String {
        if (length < 0) throw IllegalArgumentException("Length must >= 0.")
        if (charPool.isEmpty()) throw IllegalArgumentException("Char pool cannot be empty.")
        return (1..length)
            .map { random.nextInt(charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }
}