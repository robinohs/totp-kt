package dev.robinohs.totpkt.otp

import javax.crypto.Mac

/**
 * @author : Robin Ohs
 * @created : 06.07.2022
 * @since : 1.0.0
 */
enum class HashAlgorithm(private val algName: String) {
    SHA1("HmacSHA1"),
    SHA256("HmacSHA256"),
    SHA512("HmacSHA512");

    /**
     * Returns a new [Mac] instance of the [HashAlgorithm].
     */
    fun getMacInstance(): Mac = Mac.getInstance(algName)
}