package dev.robinohs.totpkt.otp.totp.timesupport

import dev.robinohs.totpkt.otp.totp.TotpGenerator
import java.util.*

/**
 * Calls the [generateCode] method with a timestamp as counter converted from a [Date] instance.
 *
 * @param secret the secret that will be used as hashing key.
 * @param date the date that will be converted to a timestamp.
 */
fun TotpGenerator.generateCode(secret: ByteArray, date: Date): String {
    val currentMillis = date.time
    return generateCode(secret, currentMillis)
}

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
 *
 * @param secret the secret that will be used as hashing key.
 * @param date the date which is converted into a timestamp.
 * @param givenCode the code that should be validated against the generated code.
 * @return a boolean indicating if the generated and given code are equal.
 */
fun TotpGenerator.isCodeValidWithTolerance(secret: ByteArray, date: Date, givenCode: String): Boolean {
    val currentMillis = date.time
    return isCodeValidWithTolerance(secret, currentMillis, givenCode)
}