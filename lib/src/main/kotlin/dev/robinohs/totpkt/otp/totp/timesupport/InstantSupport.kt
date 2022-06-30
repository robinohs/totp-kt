package dev.robinohs.totpkt.otp.totp.timesupport

import dev.robinohs.totpkt.otp.totp.TotpGenerator
import java.time.Duration
import java.time.Instant

/**
 * Extension of TotpGenerator to support deriving timestamps from instants passed as argument.
 *
 * @author : Robin Ohs
 * @created : 26.06.2022
 * @since : 1.0.0
 */

/**
 * Calls the [generateCode] method with a timestamp as counter converted from an [Instant] instance.
 *
 * @param secret the secret that will be used as hashing key.
 * @param instant the instant that will be converted to a timestamp.
 * @return the generated code.
 */
fun TotpGenerator.generateCode(secret: ByteArray, instant: Instant): String =
    generateCode(secret, instant.toEpochMilli())

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
 * In addition, the method considers a tolerance and also checks the given code against a number of previous tokens
 * equal to the tolerance. Returns true if the given code matches any of these tokens.
 *
 * @param secret the secret that will be used as hashing key.
 * @param instant the instant that will be converted to a timestamp.
 * @param givenCode the code that should be validated against the generated code.
 * @return a boolean indicating if the generated and given code are equal.
 */
fun TotpGenerator.isCodeValidWithTolerance(secret: ByteArray, instant: Instant, givenCode: String): Boolean =
    isCodeValidWithTolerance(secret, instant.toEpochMilli(), givenCode)