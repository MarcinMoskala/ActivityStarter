package activitystarter

import activitystarter.compiler.Errors
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
    fun longGenerationTest() {
        filePrecessingComparator("differenTypes/Long")
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