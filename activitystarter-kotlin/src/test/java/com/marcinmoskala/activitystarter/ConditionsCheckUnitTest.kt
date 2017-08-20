@file:Suppress("IllegalIdentifier")

package com.marcinmoskala.activitystarter

import activitystarter.Arg
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ConditionsCheckUnitTest {

    @Test
    fun `when property define argument delegate and it does not contain annotation then error is thrown`() {
        assertThrowsError(ErrorMessages.noAnnotation) {
            object {
                val a: A? by BoundToValueDelegateProvider()
            }
        }
    }

    @Test
    fun `when not nullable and not optional value and no default then throwing error`() {
        assertThrowsError(ErrorMessages.optionalValueNeeded) {
            object {
                @get:Arg(optional = true) val a: A by BoundToValueDelegateProvider()
            }
        }
    }

    @Test
    fun `when nullable type, no default value is allowed`() {
        object {
            @get:Arg val a: A? by BoundToValueDelegateProvider()
        }
    }

    @Test
    fun `Other use-cases`() {
        object {
            @get:Arg val a: A? by BoundToValueDelegateProvider(null)
            @get:Arg val c: A? by BoundToValueDelegateProvider(A())
            @get:Arg val d: A by BoundToValueDelegateProvider()
            @get:Arg val e: A by BoundToValueDelegateProvider(A())
            @get:Arg(optional = true) val f: A? by BoundToValueDelegateProvider(A())
            @get:Arg(optional = true) val g: A? by BoundToValueDelegateProvider(null)
            @get:Arg(optional = true) val h: A? by BoundToValueDelegateProvider()
        }
    }

    class A

    private fun assertThrowsError(message: String? = null, f: () -> Unit) {
        assertTrue(try {
            f()
            false
        } catch (t: Throwable) {
            if (message != null) assertEquals(message, t.message)
            true
        })
    }
}