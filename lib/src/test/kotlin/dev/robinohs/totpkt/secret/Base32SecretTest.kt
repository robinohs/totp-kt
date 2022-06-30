package dev.robinohs.totpkt.secret

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable

/**
 * @author : Robin Ohs
 * @created : 30.06.2022
 * @since : 1.0.0
 */
internal class Base32SecretTest {

    inner class Other()

    @Test
    fun testEquals_ShouldBeEqual() {
        val first = Base32Secret("test", "test".encodeToByteArray())
        val second = Base32Secret("test", "test".encodeToByteArray())

        Assertions.assertAll(
            Executable {
                Assertions.assertEquals(second, first) {
                    "$first and $second are equal but were classified as not equal."
                }
            },
            Executable {
                Assertions.assertEquals(first, first) {
                    "$first and $second are equal but were classified as not equal."
                }
            }
        )
    }

    @Test
    fun testEquals_ShouldBeDifferent() {
        val first = Base32Secret("test", "test".encodeToByteArray())
        val second = Other()
        val third = null

        Assertions.assertAll(
            Executable {
                Assertions.assertNotEquals(first, second) {
                    "$first and $second are not equal but were classified as equal."
                }
            },
            Executable {
                Assertions.assertNotEquals(first, third) {
                    "$first and $third are not equal but were classified as equal."
                }
            }
        )
    }

    @Test
    fun testEquals_ShouldBeDifferentString() {
        val first = Base32Secret("test", "test".encodeToByteArray())
        val second = Base32Secret("test2", "test".encodeToByteArray())

        Assertions.assertNotEquals(second, first) {
            "$first and $second are not equal but were classified as equal."
        }
    }

    @Test
    fun testEquals_ShouldBeDifferentByteArray() {
        val first = Base32Secret("test", "test".encodeToByteArray())
        val second = Base32Secret("test", "test2".encodeToByteArray())

        Assertions.assertNotEquals(second, first) {
            "$first and $second are not equal but were classified as equal."
        }
    }

    @Test
    fun testHashCode_ShouldBeEqual() {
        val first = Base32Secret("test", "test".encodeToByteArray()).hashCode()
        val second = Base32Secret("test", "test".encodeToByteArray()).hashCode()

        Assertions.assertEquals(second, first) {
            "$first and $second are equal but were classified as not equal."
        }
    }

    @Test
    fun testHashCode_ShouldBeDifferentString() {
        val first = Base32Secret("test", "test".encodeToByteArray()).hashCode()
        val second = Base32Secret("test2", "test".encodeToByteArray()).hashCode()

        Assertions.assertNotEquals(second, first) {
            "$first and $second are not equal but were classified as equal."
        }
    }

    @Test
    fun testHashCode_ShouldBeDifferentByteArray() {
        val first = Base32Secret("test", "test".encodeToByteArray()).hashCode()
        val second = Base32Secret("test", "test2".encodeToByteArray()).hashCode()

        Assertions.assertNotEquals(second, first) {
            "$first and $second are not equal but were classified as equal."
        }
    }
}