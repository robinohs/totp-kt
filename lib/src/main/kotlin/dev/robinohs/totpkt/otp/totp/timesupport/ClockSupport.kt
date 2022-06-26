package dev.robinohs.totpkt.otp.totp.timesupport

import dev.robinohs.totpkt.otp.totp.TotpGenerator

/**
 * Extension of TotpGenerator to support taking the realtime timestamps from the clock of the generator.
 *
 * @author : Robin Ohs
 * @created : 26.06.2022
 * @since : 1.0.0
 */

/**
 * Calls the [generateCode] method with the actual timestamp as counter.
 *
 * @param secret the secret that will be used as hashing key.
 * @return the generated code.
 */
fun TotpGenerator.generateCode(secret: ByteArray): String = generateCode(secret, clock.millis())

/**
 * Checks a generated code with a counter derived from the actual timestamp and given secret against a given code.
 *
 * @param secret the secret that will be used as hashing key.
 * @param givenCode the code that should be validated against the generated code.
 * @return a boolean indicating if the generated and given code are equal.
 */
fun TotpGenerator.isCodeValid(secret: ByteArray, givenCode: String): Boolean = generateCode(secret) == givenCode

/**
 * Checks a generated code with a counter derived from the actual timestamp and given secret against a given code.
 *
 * @param secret the secret that will be used as hashing key.
 * @param givenCode the code that should be validated against the generated code.
 * @return a boolean indicating if the generated and given code are equal.
 */
fun TotpGenerator.isCodeValidWithTolerance(secret: ByteArray, givenCode: String): Boolean =
    isCodeValidWithTolerance(secret, clock.millis(), givenCode)