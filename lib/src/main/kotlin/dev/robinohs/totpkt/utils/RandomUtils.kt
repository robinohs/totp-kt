package dev.robinohs.totpkt.utils

import java.security.SecureRandom
import java.util.*

/**
 * @author : Robin Ohs
 * @created : 24.06.2022
 * @since : 0.0.1
 */
object RandomUtils {

    /**
     * Creates a randomly generated string with characters from the character pool.
     *
     * @param length the length of the generated string.
     * @param charPool the list of characters used to create the string.
     * @return the randomly generated string.
     */
    fun generateRandomStringFromCharPool(length: Int, charPool: List<Char>, random: Random = SecureRandom()): String {
        if (length < 0) throw IllegalArgumentException("Length must >= 0.")
        if (charPool.isEmpty()) throw IllegalArgumentException("Char pool cannot be empty.")
        return (1..length)
            .map { random.nextInt(charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }
}