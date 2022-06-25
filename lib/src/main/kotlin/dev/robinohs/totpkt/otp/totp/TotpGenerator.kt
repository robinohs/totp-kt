package dev.robinohs.totpkt.otp.totp

import dev.robinohs.totpkt.otp.hotp.HotpGenerator
import dev.robinohs.totpkt.random.RandomGenerator
import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.util.*

/**
 * @author : Robin Ohs
 * @created : 24.06.2022
 * @since : 0.0.1
 */
class TotpGenerator(
    override var randomGenerator: RandomGenerator = RandomGenerator(),
    var timePeriod: Duration = Duration.ofSeconds(30),
    var tolerance: Duration = Duration.ofSeconds(5),
    var clock: Clock = Clock.systemUTC()
) : HotpGenerator(randomGenerator) {

    override fun isCodeValid(secret: ByteArray, counter: Long, givenCode: String): Boolean {
        val code = generateCode(secret, counter)
        return code == givenCode
    }

    /**
     * Checks a generated code with a counter derived from the actual timestamp and given secret against a given code.
     *
     * @param secret the secret that will be used as hashing key.
     * @param givenCode the code that should be validated against the generated code.
     * @return a boolean indicating if the generated and given code are equal.
     */
    fun isCodeValid(secret: ByteArray, givenCode: String): Boolean = generateCode(secret) == givenCode

    /**
     * Checks a generated code against a given code with a counter derived from the actual timestamp and given secret.
     * Furthermore, the method considers a tolerance and also checks the given code against previous tokens generated
     * within in a specified tolerance duration (Class property). Returns true if given code matches any of these tokens.
     *
     * @param secret the secret that will be used as hashing key.
     * @param givenCode the code that should be validated against the generated code.
     * @return a boolean indicating if the generated and given code are equal.
     */
    fun isCodeValidWithTolerance(secret: ByteArray, givenCode: String): Boolean {
        val timestamp = Instant.now(clock)
        val validToken = mutableSetOf<String>()
        val toleranceLowerBound = timestamp.minus(tolerance)
        var iteratingTimestamp = timestamp
        while (iteratingTimestamp.isAfter(toleranceLowerBound) || iteratingTimestamp == toleranceLowerBound) {
            val currentBeginning = computeTimeslotBeginning(iteratingTimestamp.toEpochMilli())
            validToken.add(generateCode(secret, currentBeginning))
            iteratingTimestamp = iteratingTimestamp.minusMillis(1L)
        }
        return givenCode in validToken
    }

    override fun generateCode(secret: ByteArray, counter: Long): String {
        val currentCounter = computeCounterForMillis(counter)
        return super.generateCode(secret, currentCounter)
    }

    /**
     * Calls the [generateCode] method with the actual timestamp as counter.
     *
     * @param secret the secret that will be used as hashing key.
     */
    fun generateCode(secret: ByteArray): String {
        val currentMillis = clock.millis()
        return generateCode(secret, currentMillis)
    }

    /**
     * Calls the [generateCode] method with a timestamp as counter converted from a [Date] instance.
     *
     * @param secret the secret that will be used as hashing key.
     * @param date the date that will be converted to a timestamp.
     */
    fun generateCode(secret: ByteArray, date: Date): String {
        val currentMillis = date.time
        return generateCode(secret, currentMillis)
    }

    /**
     * Calls the [generateCode] method with a timestamp as counter converted from an [Instant] instance.
     *
     * @param secret the secret that will be used as hashing key.
     * @param instant the instant that will be converted to a timestamp.
     */
    fun generateCode(secret: ByteArray, instant: Instant): String {
        val currentMillis = instant.toEpochMilli()
        return generateCode(secret, currentMillis)
    }

    private fun computeCounterForMillis(millis: Long): Long = Math.floorDiv(millis, timePeriod.toMillis())

    private fun computeTimeslotBeginning(millis: Long): Long {
        val counter = computeCounterForMillis(millis)
        return timePeriod.toMillis() * counter
    }
}