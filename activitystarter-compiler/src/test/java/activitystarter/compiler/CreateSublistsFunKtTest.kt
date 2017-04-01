package activitystarter.compiler

import activitystarter.compiler.utils.createSublists
import org.junit.Assert.assertEquals
import org.junit.Test

class CreateSublistsFunKtTest {

    @Test
    fun testEmptyCreateSublists() {
        assertEquals(listOf(listOf<Int>()), listOf<Int>().createSublists { it % 2 == 0 })
    }

    @Test
    fun testOrderCreateSublists() {
        assertEquals(listOf(listOf(1,2),listOf(1),listOf(2), listOf()), listOf(1,2).createSublists { true })
    }

    @Test
    fun testCreateSublists() {
        assertEquals(setOf(listOf(1, 2, 3), listOf(1, 3)), listOf(1, 2, 3).createSublists { it % 2 == 0 }.toSet())
        assertEquals(setOf(listOf(1, 2, 3, 4), listOf(1, 2, 3), listOf(1, 3, 4), listOf(1, 3)), listOf(1, 2, 3, 4).createSublists { it % 2 == 0 }.toSet())
    }

    @Test
    fun testCreateBigSublists() {
        // 10 splitters, so 2^10 = 1024 lists
        assert((1..20).toList().createSublists { it % 2 == 0 }.size == 1024)
        assert((1..40).toList().createSublists { it % 4 == 0 }.size == 1024)
    }
}