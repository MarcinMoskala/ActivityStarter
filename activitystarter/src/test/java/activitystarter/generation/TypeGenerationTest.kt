package activitystarter.generation

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
    fun integerArrayListSequenceArrayCompilationTest() {
        processingCheck("ArrayList<Integer>", "java.util.ArrayList")
    }

    @Test
    fun charSequenceArrayListSequenceArrayCompilationTest() {
        processingCheck("ArrayList<CharSequence>", "java.util.ArrayList")
    }

    @Test
    fun stringArrayListSequenceArrayCompilationTest() {
        processingCheck("ArrayList<String>", "java.util.ArrayList")
    }

    @Test
    fun subtypeOfParcelableCompilationTest() {
        processingCheck("Account", "android.accounts.Account")
    }

    @Test
    fun subtypeOfSerializableCompilationTest() {
        processingCheck("Color", "java.awt.Color")
    }

    @Test
    fun arrayListOfSubtypeOfParcelableCompilationTest() {
        processingCheck("ArrayList<Account>", "android.accounts.Account", "java.util.ArrayList")
    }

    fun processingCheck(type: String, vararg import: String) {
        val extraImport = import.joinToString(separator = "\n", transform = { "import $it;" })
        val code = "com.example.activitystarter.MainActivity" to """
        |package com.example.activitystarter;
        |
        |import android.app.Activity;
        |$extraImport
        |import activitystarter.Arg;
        |
        |public class MainActivity extends Activity {
        |    @Arg $type name;
        |}""".trimMargin()
        processingCheck(code)
    }
}