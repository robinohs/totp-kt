package dev.robinohs.totpkt.otp

/**
 * @author : Robin Ohs
 * @created : 24.06.2022
 * @since : 0.0.1
 */
interface OtpGenerator {

    /**
     * Generates an otp token with a given secret and counter.
     *
     * @param secret the secret that will be used as hashing key.
     * @param counter the counter which needs to be synchronized between user and server. (Could be a number or a timestamp)
     * @return the generated code.
     */
    fun generateCode(secret: ByteArray, counter: Long): String

    /**
     * Checks a generated code with a given counter (could be a timestamp) and secret against a given code.
     *
     * @param secret the secret that will be used as hashing key.
     * @param counter the counter which needs to be synchronized between user and server (Could be a number or a timestamp).
     * @param givenCode the code that should be validated against the generated code.
     * @return a boolean indicating if the generated and given code are equal.
     */
    fun isCodeValid(secret: ByteArray, counter: Long, givenCode: String): Boolean

    /**
     * Generates a secure random secret and converts it into Base32 encoding.
     *
     * @param length the plaintext length of the generated secret. (10 is the default of google authenticator).
     * @return the secret that can be used in Authenticator apps. E.g., Google Authenticator, Microsoft Authenticator...
     */
    fun generateSecret(length: Int = 10): ByteArray
}