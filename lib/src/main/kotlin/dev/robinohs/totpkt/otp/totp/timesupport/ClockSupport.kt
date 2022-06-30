package dev.robinohs.totpkt.otp.totp.timesupport

import dev.robinohs.totpkt.otp.totp.TotpGenerator
import java.time.Duration

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
 * Checks a generated code against a given code with a counter derived from the actual timestamp of the generators clock
 * and given secret.
 *
 * @param secret the secret that will be used as hashing key.
 * @param givenCode the code that should be validated against the generated code.
 * @return a boolean indicating if the generated and given code are equal.
 */
fun TotpGenerator.isCodeValid(secret: ByteArray, givenCode: String): Boolean = generateCode(secret) == givenCode

/**
 * Checks a generated code against a given code with a counter derived from the actual timestamp of the generators clock
 * and given secret.
 * In addition, the method considers a tolerance and also checks the given code against a number of previous tokens
 * equal to the tolerance. Returns true if the given code matches any of these tokens.
 *
 * @param secret the secret that will be used as hashing key.
 * @param givenCode the code that should be validated against the generated code.
 * @return a boolean indicating if the generated and given code are equal.
 */
fun TotpGenerator.isCodeValidWithTolerance(secret: ByteArray, givenCode: String): Boolean =
    isCodeValidWithTolerance(secret, clock.millis(), givenCode)

/**
 * Calculates the start timestamp of the time slot in which the actual timestamp lies.
 *
 * @return the start timestamp.
 */
fun TotpGenerator.calculateTimeslotBeginning(): Long =
    calculateTimeslotBeginning(clock.millis())

/**
 * Calculates the remaining duration of the time slot in which the actual timestamp lies.
 *
 * @return the remaining duration of the time slot.
 */
fun TotpGenerator.calculateRemainingTime(): Duration =
    calculateRemainingTime(clock.millis())