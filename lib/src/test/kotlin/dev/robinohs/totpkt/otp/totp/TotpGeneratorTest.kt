package dev.robinohs.totpkt.otp.totp

import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.util.*
import kotlin.test.*

/**
 * @author : Robin Ohs
 * @created : 24.06.2022
 * @since : 0.0.1
 */
internal class TotpGeneratorTest {
    private val secret = "IJAU CQSB IJAU EQKB".toByteArray()

    private lateinit var cut: TotpGenerator

    @BeforeTest
    fun init() {
        cut = TotpGenerator()
    }

    @Test
    fun `generateCode produces 6 digit long codes for given timestamps`() {
        val expected = "316152"

        val actual1 = cut.generateCode(secret, 1656114883887)
        val actual2 = cut.generateCode(secret, 1656114891677)

        assertEquals(expected, actual1, "First code was not the expected one.")
        assertEquals(expected, actual2, "Second code was not the expected one.")
    }

    @Test
    fun `generateCode produces 6 digit long code for given dates`() {
        val expected = "503317"

        val actual1 = cut.generateCode(secret, Date.from(Instant.ofEpochMilli(1656114738767)))
        val actual2 = cut.generateCode(secret, Date.from(Instant.ofEpochMilli(1656114742133)))

        assertEquals(expected, actual1, "First code was not the expected one.")
        assertEquals(expected, actual2, "Second code was not the expected one.")
    }

    @Test
    fun `generateCode produces 6 digit long code for given instants`() {
        val expected = "301527"

        val actual1 = cut.generateCode(secret, Instant.ofEpochMilli(1656114819820))
        val actual2 = cut.generateCode(secret, Instant.ofEpochMilli(1656114826576))

        assertEquals(expected, actual1, "First code was not the expected one.")
        assertEquals(expected, actual2, "Second code was not the expected one.")
    }

    @Test
    fun `generateCode produces 6 digit long code by taking timestamps itself`() {
        val expected = "223710"

        cut.clock = Clock.fixed(Instant.ofEpochMilli(1656115662383), ZoneId.systemDefault())
        val actual1 = cut.generateCode(secret)
        cut.clock = Clock.fixed(Instant.ofEpochMilli(1656115668553), ZoneId.systemDefault())
        val actual2 = cut.generateCode(secret)
        cut.clock = Clock.fixed(Instant.ofEpochMilli(1656115777419), ZoneId.systemDefault())
        val actual3 = cut.generateCode(secret)

        assertEquals(actual1, expected, "First code was not the expected one.")
        assertEquals(actual2, expected, "Second code was not the expected one.")
        assertNotEquals(actual3, expected, "Second code was not the expected one.")
    }

    @Test
    fun `isCodeValid checks codes correctly for given timestamps`() {
        val actual1 = cut.isCodeValid(secret, 1656115068732, "196157")
        val actual2 = cut.isCodeValid(secret, 1656115073318, "355908")

        assertTrue(actual1, "First code should be valid but was not.")
        assertFalse(actual2, "Second code should not be valid but was.")
    }

    @Test
    fun `isCodeValid checks codes correctly by taking the timestamp itself`() {
        cut.clock = Clock.fixed(Instant.ofEpochMilli(1656115068732), ZoneId.systemDefault())
        val actual1 = cut.isCodeValid(secret, "196157")
        cut.clock = Clock.fixed(Instant.ofEpochMilli(1656115073318), ZoneId.systemDefault())
        val actual2 = cut.isCodeValid(secret, "355908")

        assertTrue(actual1, "First code should be valid but was not.")
        assertFalse(actual2, "Second code should not be valid but was.")
    }

    @Test
    fun `isCodeValidWithTolerance checks codes correctly if token is not expired`() {
        cut.clock = Clock.fixed(Instant.ofEpochMilli(1656115068732), ZoneId.systemDefault())
        val actual = cut.isCodeValidWithTolerance(secret, "196157")

        assertTrue(actual, "Code should be valid but was not.")
    }

    @Test
    fun `isCodeValidWithTolerance checks codes correctly if token is expired but in tolerance`() {
        cut.clock = Clock.fixed(Instant.ofEpochMilli(1656116160719), ZoneId.systemDefault())
        val actual = cut.isCodeValidWithTolerance(secret, "853702")

        assertTrue(actual, "Code should be valid but was not.")
    }

    @Test
    fun `isCodeValidWithTolerance checks codes correctly if token is expired and not in tolerance`() {
        cut.clock = Clock.fixed(Instant.ofEpochMilli(1656116466490), ZoneId.systemDefault())

        val actual = cut.isCodeValidWithTolerance(secret, "452088")

        assertFalse(actual, "Code should not be valid but was.")
    }

    @Test
    fun `isCodeValidWithTolerance checks codes correctly if token is expired and in extended tolerance`() {
        cut.tolerance = Duration.ofSeconds(10)
        cut.clock = Clock.fixed(Instant.ofEpochMilli(1656116466490), ZoneId.systemDefault())

        val actual = cut.isCodeValidWithTolerance(secret, "452088")

        assertTrue(actual, "Code should not be valid but was.")
    }

    @Test
    fun `isCodeValidWithTolerance checks codes correctly if token is expired with 50s tolerance`() {
        cut.tolerance = Duration.ofSeconds(50)
        cut.clock = Clock.fixed(Instant.ofEpochMilli(1656118534840), ZoneId.systemDefault())

        val actual1 = cut.isCodeValidWithTolerance(secret, "956804")
        val actual2 = cut.isCodeValidWithTolerance(secret, "364536")
        val actual3 = cut.isCodeValidWithTolerance(secret, "326491")
        val actual4 = cut.isCodeValidWithTolerance(secret, "110215")

        assertFalse(actual1, "Code1 should not be valid but was.")
        assertTrue(actual2, "Code2 should be valid but was not.")
        assertTrue(actual3, "Code3 should be valid but was not.")
        assertTrue(actual4, "Code4 should be valid but was not.")
    }

    @Test
    fun `isCodeValidWithTolerance checks codes correctly if token is expired with 95s tolerance`() {
        cut.tolerance = Duration.ofSeconds(95)
        cut.clock = Clock.fixed(Instant.ofEpochMilli(1656118534840), ZoneId.systemDefault())

        val actual1 = cut.isCodeValidWithTolerance(secret, "956804")
        val actual2 = cut.isCodeValidWithTolerance(secret, "364536")
        val actual3 = cut.isCodeValidWithTolerance(secret, "326491")
        val actual4 = cut.isCodeValidWithTolerance(secret, "110215")

        assertTrue(actual1, "Code1 should be valid but was not.")
        assertTrue(actual2, "Code2 should be valid but was not.")
        assertTrue(actual3, "Code3 should be valid but was not.")
        assertTrue(actual4, "Code4 should be valid but was not.")
    }
}