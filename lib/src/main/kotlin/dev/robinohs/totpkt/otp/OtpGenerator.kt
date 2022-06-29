package dev.robinohs.totpkt.otp

/**
 * @author : Robin Ohs
 * @created : 24.06.2022
 * @since : 1.0.0
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
}