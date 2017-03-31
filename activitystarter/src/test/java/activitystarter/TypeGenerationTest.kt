package activitystarter

import activitystarter.compiler.error.Errors
import org.junit.Test

class TypeGenerationTest : GenerationTest() {

    @Test
    fun longCompilationTest() {
        processingCheck("long")
    }

    @Test
    fun intCompilationTest() {
        processingCheck("int")
    }

    @Test
    fun doubleCompilationTest() {
        processingCheck("double")
    }

    @Test
    fun floatCompilationTest() {
        processingCheck("float")
    }

    @Test
    fun charCompilationTest() {
        processingCheck("char")
    }

    @Test
    fun stringCompilationTest() {
        processingCheck("String")
    }

    @Test
    fun byteCompilationTest() {
        processingCheck("byte")
    }

    @Test
    fun booleanArraySequenceCompilationTest() {
        processingCheck("boolean[]")
    }

    @Test
    fun byteArrayCompilationTest() {
        processingCheck("byte[]")
    }

    @Test
    fun shortArrayCompilationTest() {
        processingCheck("short[]")
    }

    @Test
    fun charArrayCompilationTest() {
        processingCheck("char[]")
    }

    @Test
    fun intArrayCompilationTest() {
        processingCheck("int[]")
    }

    @Test
    fun longArrayCompilationTest() {
        processingCheck("long[]")
    }

    @Test
    fun floatArrayCompilationTest() {
        processingCheck("float[]")
    }

    @Test
    fun doubleArrayCompilationTest() {
        processingCheck("double[]")
    }

    @Test
    fun stringArrayCompilationTest() {
        processingCheck("String[]")
    }

    @Test
    fun charSequenceArrayCompilationTest() {
        processingCheck("CharSequence[]")
    }

    @Test
    fun stringArraySequenceArrayCompilationTest() {
        processingCheck("String[]")
    }

    fun processingCheck(type: String) {
        val code = "com.example.activitystarter.MainActivity" to """
        |package com.example.activitystarter;
        |
        |import android.app.Activity;
        |
        |import activitystarter.Arg;
        |
        |public class MainActivity extends Activity {
        |    @Arg $type name;
        |}""".trimMargin()
        processingCheck(code)
    }
}