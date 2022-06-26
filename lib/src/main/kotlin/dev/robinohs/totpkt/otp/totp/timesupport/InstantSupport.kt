package dev.robinohs.totpkt.otp.totp.timesupport

import dev.robinohs.totpkt.otp.totp.TotpGenerator
import java.time.Instant

/**
 * Calls the [generateCode] method with a timestamp as counter converted from an [Instant] instance.
 *
 * @param secret the secret that will be used as hashing key.
 * @param instant the instant that will be converted to a timestamp.
 */
fun TotpGenerator.generateCode(secret: ByteArray, instant: Instant): String {
    val currentMillis = instant.toEpochMilli()
    return generateCode(secret, currentMillis)
}

/**
 * Checks a generated code with a counter derived from a given [Instant] and given secret against a given code.
 *
 * @param secret the secret that will be used as hashing key.
 * @param instant the instant that will be converted to a timestamp.
 * @param givenCode the code that should be validated against the generated code.
 * @return a boolean indicating if the generated and given code are equal.
 */
fun TotpGenerator.isCodeValid(secret: ByteArray, instant: Instant, givenCode: String): Boolean =
    generateCode(secret, instant) == givenCode

/**
 * Checks a generated code with a counter derived from a given [Instant] and given secret against a given code.
 *
 * @param secret the secret that will be used as hashing key.
 * @param instant the instant that will be converted to a timestamp.
 * @param givenCode the code that should be validated against the generated code.
 * @return a boolean indicating if the generated and given code are equal.
 */
fun TotpGenerator.isCodeValidWithTolerance(secret: ByteArray, instant: Instant, givenCode: String): Boolean {
    val currentMillis = instant.toEpochMilli()
    return isCodeValidWithTolerance(secret, currentMillis, givenCode)
}