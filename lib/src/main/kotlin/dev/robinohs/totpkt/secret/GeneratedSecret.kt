package dev.robinohs.totpkt.secret

/**
 * @author : Robin Ohs
 * @created : 29.06.2022
 * @since : 1.0.0
 */
data class GeneratedSecret(val secretAsString: String, val secretAsByteArray: ByteArray) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GeneratedSecret

        if (secretAsString != other.secretAsString) return false
        if (!secretAsByteArray.contentEquals(other.secretAsByteArray)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = secretAsString.hashCode()
        result = 31 * result + secretAsByteArray.contentHashCode()
        return result
    }
}
