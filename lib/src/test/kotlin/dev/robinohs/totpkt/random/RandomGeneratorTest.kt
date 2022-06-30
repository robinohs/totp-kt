package dev.robinohs.totpkt.random

import org.junit.jupiter.api.*
import java.util.*

/**
 * @author : Robin Ohs
 * @created : 24.06.2022
 * @since : 1.0.0
 */
internal class RandomGeneratorTest {

    private lateinit var cut: RandomGenerator

    @BeforeEach
    fun init() {
        cut = RandomGenerator()
    }

    @Test
    fun constructorTest_validatesArguments() {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            RandomGenerator(
                charPool = listOf()
            )
        }
    }

    /**
     * Setter has logic, so it needs to be tested.
     */
    @Test
    fun charPoolSetterTest_emptyListIllegal() {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            cut.charPool = listOf()
        }
    }

    /**
     * Setter has logic, so it needs to be tested.
     */
    @TestFactory
    fun charPoolSetterTest_nonEmptyListIllegal() = listOf(
        listOf('A', 'B'), listOf('F', 'H', 'Z'), listOf('&')
    ).map { expected ->
        DynamicTest.dynamicTest("setting charPool to $expected is allowed") {
            cut.charPool = expected

            val actual = cut.charPool

            Assertions.assertEquals(expected, actual) {
                "Setter did not set charpool to $expected, instead was $actual."
            }
        }
    }

    @TestFactory
    fun generateRandomStringFromCharPoolTest_negativeLengthIllegal() = listOf(
        -1,
        -5,
        -55
    ).map {
        DynamicTest.dynamicTest("input $it should throw an IllegalArgumentException") {
            Assertions.assertThrows(IllegalArgumentException::class.java) {
                cut.generateRandomStringFromCharPool(it)
            }
        }
    }

    @Test
    fun generateRandomStringFromCharPoolTest_zeroLength() {
        cut.charPool = listOf('A', 'B')
        val expected = ""

        val actual = cut.generateRandomStringFromCharPool(0)

        Assertions.assertEquals(expected, actual) {
            "The string was not as expected an empty string."
        }
    }

    @Test
    fun generateRandomStringFromCharPoolTest_Seed1() {
        cut.charPool = listOf('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I')
        cut.random = Random(55)
        val expected = "HBHBIICD"

        val actual = cut.generateRandomStringFromCharPool(8)

        Assertions.assertEquals(expected, actual) {
            "The string was not taken from the charpool as expected."
        }
    }

    @Test
    fun generateRandomStringFromCharPoolTest_Seed2(){
        cut.charPool = listOf('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I')
        cut.random = Random(555)
        val expected = "DFHIBGIC"

        val actual = cut.generateRandomStringFromCharPool(8)

        Assertions.assertEquals(expected, actual) {
            "The string was not taken from the charpool as expected."
        }
    }
}