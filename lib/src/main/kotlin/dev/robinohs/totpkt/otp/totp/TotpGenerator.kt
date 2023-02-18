package dev.robinohs.totpkt.otp.totp

import dev.robinohs.totpkt.otp.HashAlgorithm
import dev.robinohs.totpkt.otp.hotp.HotpGenerator
import java.time.Clock
import java.time.Duration

/**
 * @author : Robin Ohs
 * @created : 24.06.2022
 * @since : 1.0.0
 */
class TotpGenerator(
    override var algorithm: HashAlgorithm = HashAlgorithm.SHA1,
    codeLength: Int = 6,
    timePeriod: Duration = Duration.ofSeconds(30),
    tolerance: Int = 1,
    var clock: Clock = Clock.systemUTC(),
) : HotpGenerator(
    algorithm = algorithm,
    codeLength = codeLength
) {

    init {
        require(codeLength >= 0) { "Code length must be >= 0." }
        require(timePeriod.toMillis() >= 1) { "Time period must be be >= 1." }
        require(tolerance >= 0) { "Tolerance must be be >= 0." }
    }

    override var codeLength = codeLength
        set(value) {
            require(value >= 0) { "Code length must be >= 0." }
            field = value
        }

    var timePeriod = timePeriod
        set(value) {
            require(value.toMillis() >= 1) { "Time period must be be >= 1." }
            field = value
        }

    var tolerance = tolerance
        set(value) {
            require(value >= 0) { "Tolerance must be be >= 0." }
            field = value
        }

    override fun generateCode(secret: ByteArray, counter: Long): String {
        val currentCounter = computeCounterForMillis(counter)
        return super.generateCode(secret, currentCounter)
    }

    override fun isCodeValid(secret: ByteArray, counter: Long, givenCode: String): Boolean {
        val code = generateCode(secret, counter)
        return code == givenCode
    }

    /**
     * Checks a generated code against a given code with a counter derived from the given millis and secret.
     * In addition, the method considers a tolerance and also checks the given code against a number of previous tokens
     * equal to the tolerance. Returns true if the given code matches any of these tokens.
     *
     * @param secret the secret that will be used as hashing key.
     * @param millis the timestamp as millis.
     * @param givenCode the code that should be validated against the generated code.
     * @return a boolean indicating if the generated and given code are equal.
     */
    fun isCodeValidWithTolerance(secret: ByteArray, millis: Long, givenCode: String): Boolean {
        val validToken = getCodesInInterval(secret, millis)
        return givenCode in validToken
    }

    private fun getCodesInInterval(secret: ByteArray, start: Long): Set<String> {
        val validTokens = mutableSetOf<String>()
        validTokens.add(generateCode(secret, start))
        var currentTime = start - timePeriod.toMillis()
        repeat(tolerance) {
            validTokens.add(generateCode(secret, currentTime))
            currentTime -= timePeriod.toMillis()
        }
        return validTokens
    }

    private fun computeCounterForMillis(millis: Long): Long = Math.floorDiv(millis, timePeriod.toMillis())


    /**
     * Calculates the start timestamp of the time slot in which the given timestamp lies.
     *
     * @param millis the timestamp as millis.
     * @return the start timestamp.
     */
    fun calculateTimeslotBeginning(millis: Long): Long {
        val counter = computeCounterForMillis(millis)
        return timePeriod.toMillis() * counter
    }

    /**
     * Calculates the remaining duration of the time slot in which the given timestamp lies.
     *
     * @param millis the timestamp as millis.
     * @return the remaining duration of the time slot.
     */
    fun calculateRemainingTime(millis: Long): Duration {
        val beginning = calculateTimeslotBeginning(millis)
        val end = beginning + timePeriod.toMillis()
        return Duration.ofMillis(end - millis)
    }
}
