package activitystarter.compiler

import org.junit.Test

import org.junit.Assert.*

class DifferentVariantsFunKtTest {
    @Test
    fun createSublists() {
        assertEquals(setOf(listOf(1,2,3), listOf(1,3)), listOf(1,2,3).createSublists { it % 2 == 0 }.toSet())
        assertEquals(setOf(listOf(1,2,3,4), listOf(1,2,3), listOf(1,3, 4), listOf(1,3)), listOf(1,2,3,4).createSublists { it % 2 == 0 }.toSet())
    }

    @Test
    fun createBigSublists() {
        // 10 splitters, so 2^10 = 1024 lists
        assert((1..20).toList().createSublists { it % 2 == 0 }.size == 1024)
        assert((1..40).toList().createSublists { it % 4 == 0 }.size == 1024)
    }
}