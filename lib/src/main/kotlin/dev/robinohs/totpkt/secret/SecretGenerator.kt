package dev.robinohs.totpkt.secret

import dev.robinohs.totpkt.random.RandomGenerator
import org.apache.commons.codec.binary.Base32
import dev.robinohs.totpkt.otp.HashAlgorithm

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
    fun generateSecret(length: Int = 10): Base32Secret {
        val plainSecret = randomGenerator.generateRandomStringFromCharPool(length).toByteArray()
        val secretAsByteArray = Base32().encode(plainSecret)
        val secretAsString = Base32().encodeAsString(plainSecret)
        return Base32Secret(secretAsString = secretAsString, secretAsByteArray = secretAsByteArray)
    }

    /**
     * Generates a secure random secret for a given [HashAlgorithm] and converts it into Base32 encoding.
     *
     * @param algorithm the algorithm which preferred key size is taken as length.
     * @return the secret that can be used in Authenticator apps. E.g., Google Authenticator, Microsoft Authenticator...
     */
    fun generateSecret(algorithm: HashAlgorithm): Base32Secret = generateSecret(algorithm.keySize)
}