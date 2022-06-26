package dev.robinohs.totpkt.otp.hotp

import dev.robinohs.totpkt.otp.OtpGenerator
import dev.robinohs.totpkt.random.RandomGenerator
import org.apache.commons.codec.binary.Base32
import java.nio.ByteBuffer
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.and
import kotlin.math.pow

/**
 * Code computation was built with the help of the pseudocode taken from
 * [https://de.wikipedia.org/wiki/Google_Authenticator](https://de.wikipedia.org/wiki/Google_Authenticator).
 *
 * @author : Robin Ohs
 * @created : 24.06.2022
 * @since : 1.0.0
 */
open class HotpGenerator(
    open var randomGenerator: RandomGenerator = RandomGenerator(),
    codeLength: Int = 6
) : OtpGenerator {

    init {
        require(codeLength >= 0) { "Code length must be >= 0." }
    }

    open var codeLength = codeLength
        set(value) {
            require(value >= 0) { "Code length must be >= 0." }
            field = value
        }

    override fun isCodeValid(secret: ByteArray, counter: Long, givenCode: String): Boolean {
        return generateCode(secret, counter) == givenCode
    }

    override fun generateSecret(length: Int): ByteArray {
        val plainSecret = randomGenerator.generateRandomStringFromCharPool(length).toByteArray()
        return Base32().encode(plainSecret)
    }

    override fun generateCode(secret: ByteArray, counter: Long): String {
        // convert counter to long and insert into bytearray
        val payload: ByteArray = ByteBuffer.allocate(8).putLong(0, counter).array()
        val hash = generateHash(secret, payload)
        val truncatedHash = truncateHash(hash)
        // generate code by computing the hash as integer mod 1000000
        val code = ByteBuffer.wrap(truncatedHash).int % 10.0.pow(codeLength).toInt()
        // pad code to correct length, could be too small
        return code.toString().padStart(codeLength, '0')
    }

    private fun generateHash(secret: ByteArray, payload: ByteArray): ByteArray {
        val key = Base32().decode(secret)
        val mac = Mac.getInstance("HmacSHA1")
        mac.init(SecretKeySpec(key, "RAW"))
        return mac.doFinal(payload)
    }

    private fun truncateHash(hash: ByteArray): ByteArray {
        // last nibble of hash
        val offset = hash.last().and(0x0F).toInt()
        // get 4 bytes of the hash starting at the offset
        val truncatedHash = ByteArray(4)
        for (i in 0..3) {
            truncatedHash[i] = hash[offset + i]
        }
        // remove most significant bit
        truncatedHash[0] = truncatedHash[0].and(0x7F)
        return truncatedHash
    }
}