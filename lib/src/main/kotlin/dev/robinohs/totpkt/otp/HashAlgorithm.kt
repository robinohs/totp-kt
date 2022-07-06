package dev.robinohs.totpkt.otp

import javax.crypto.Mac

/**
 * @author : Robin Ohs
 * @created : 06.07.2022
 * @since : 1.0.0
 */
enum class HashAlgorithm(private val algName: String, val keySize: Int) {
    SHA1("HmacSHA1", 20),
    SHA256("HmacSHA256", 32),
    SHA512("HmacSHA512", 64);

    /**
     * Returns a new [Mac] instance of the [HashAlgorithm].
     */
    fun getMacInstance(): Mac = Mac.getInstance(algName)
}