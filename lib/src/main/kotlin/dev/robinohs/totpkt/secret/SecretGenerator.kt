package dev.robinohs.totpkt.secret

import dev.robinohs.totpkt.random.RandomGenerator
import org.apache.commons.codec.binary.Base32

/**
 * @author : Robin Ohs
 * @created : 29.06.2022
 * @since : 1.0.0
 */
class SecretGenerator(var randomGenerator: RandomGenerator = RandomGenerator()) {
    /**
     * Generates a secure random secret and converts it into Base32 encoding.
     *
     * @param length the plaintext length of the generated secret. (10 is the default of google authenticator).
     * @return the secret that can be used in Authenticator apps. E.g., Google Authenticator, Microsoft Authenticator...
     */
    fun generateSecret(length: Int = 10): GeneratedSecret {
        val plainSecret = randomGenerator.generateRandomStringFromCharPool(length).toByteArray()
        val secretAsByteArray = Base32().encode(plainSecret)
        val secretAsString = Base32().encodeAsString(plainSecret)
        return GeneratedSecret(secretAsString = secretAsString, secretAsByteArray = secretAsByteArray)
    }
}