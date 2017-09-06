@file:Suppress("IllegalIdentifier")

package com.marcinmoskala.activitystarter

import activitystarter.Arg
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ConditionsCheckUnitTest {

// TODO Depend on part that is in hold bacause of problems in beta versions of Gradle, which are suggested in AndroidStudio 3.0
//    @Test
//    fun `when property define argument delegate and it does not contain annotation then error is thrown`() {
//        assertThrowsError(ErrorMessages.noAnnotation) {
//            object {
//                val a: A? by BoundToArgValueDelegateProvider()
//            }
//        }
//    }
//
//    @Test
//    fun `when not nullable and not optional value and no default then throwing error`() {
//        assertThrowsError(ErrorMessages.optionalValueNeeded) {
//            object {
//                @get:Arg(optional = true) val a: A by BoundToArgValueDelegateProvider()
//            }
//        }
//    }
//
//    @Test
//    fun `when nullable type, no default value is allowed`() {
//        object {
//            @get:Arg val a: A? by BoundToArgValueDelegateProvider()
//        }
//    }

    @Test
    fun `Other use-cases`() {
        object {
            @get:Arg val a: A? by BoundToArgValueDelegateProvider(null)
            @get:Arg val c: A? by BoundToArgValueDelegateProvider(A())
            @get:Arg val d: A by BoundToArgValueDelegateProvider()
            @get:Arg val e: A by BoundToArgValueDelegateProvider(A())
            @get:Arg(optional = true) val f: A? by BoundToArgValueDelegateProvider(A())
            @get:Arg(optional = true) val g: A? by BoundToArgValueDelegateProvider(null)
            @get:Arg(optional = true) val h: A? by BoundToArgValueDelegateProvider()
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