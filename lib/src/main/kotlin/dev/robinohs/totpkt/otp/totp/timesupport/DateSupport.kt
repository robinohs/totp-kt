package dev.robinohs.totpkt.otp.totp.timesupport

import dev.robinohs.totpkt.otp.totp.TotpGenerator
import java.time.Duration
import java.time.Instant
import java.util.*

/**
 * Extension of TotpGenerator to support deriving timestamps from dates passed as argument.
 *
 * @author : Robin Ohs
 * @created : 26.06.2022
 * @since : 1.0.0
 */

/**
 * Calls the [generateCode] method with a timestamp as counter converted from a [Date] instance.
 *
 * @param secret the secret that will be used as hashing key.
 * @param date the date that will be converted to a timestamp.
 * @return the generated code.
 */
fun TotpGenerator.generateCode(secret: ByteArray, date: Date): String = generateCode(secret, date.time)

/**
 * Checks a generated code with a counter derived from a given [Date] and given secret against a given code.
 *
 * @param secret the secret that will be used as hashing key.
 * @param date the date which is converted into a timestamp.
 * @param givenCode the code that should be validated against the generated code.
 * @return a boolean indicating if the generated and given code are equal.
 */
fun TotpGenerator.isCodeValid(secret: ByteArray, date: Date, givenCode: String): Boolean =
    generateCode(secret, date) == givenCode

/**
 * Checks a generated code with a counter derived from a given [Date] and given secret against a given code.
 * In addition, the method considers a tolerance and also checks the given code against a number of previous tokens
 * equal to the tolerance. Returns true if the given code matches any of these tokens.
 *
 * @param secret the secret that will be used as hashing key.
 * @param date the date which is converted into a timestamp.
 * @param givenCode the code that should be validated against the generated code.
 * @return a boolean indicating if the generated and given code are equal.
 */
fun TotpGenerator.isCodeValidWithTolerance(secret: ByteArray, date: Date, givenCode: String): Boolean =
    isCodeValidWithTolerance(secret, date.time, givenCode)