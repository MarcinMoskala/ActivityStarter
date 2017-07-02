package activitystarter.generation

import activitystarter.compiler.ActivityStarterProcessor
import com.google.common.truth.Truth
import com.google.testing.compile.JavaFileObjects
import com.google.testing.compile.JavaSourceSubjectFactory
import com.google.testing.compile.JavaSourcesSubject
import java.io.File
import javax.tools.JavaFileObject

abstract class GenerationTest {

    val examplesFolder = "../generationExamples/"

    fun filePrecessingComparator(fileName: String) {
        val gen = File("$examplesFolder$fileName").readText().split("********")
        // gen[0] is empty
        val beforeProcess = gen[1] to gen[2]
        val afterProcess = gen[3] to gen[4]

        processingComparator(beforeProcess, afterProcess)
    }

    fun dirPrecessingComparator(dirName: String) {
        File("$examplesFolder$dirName/").walkTopDown().forEach {
            if(it.isDirectory) return@forEach
            filePrecessingComparator("$dirName/${it.name}")
        }
    }

    fun filePrecessingCheckError(fileName: String, expectedErrorPhrase: String? = null) {
        val gen = File("$examplesFolder$fileName").readText().split("********")
        // gen[0] is empty
        val beforeProcess = gen[1] to gen[2]
        processingErrorCheck(beforeProcess, expectedErrorPhrase)
    }

    fun filePrecessingCorrect(fileName: String) {
        val gen = File("$examplesFolder$fileName").readText().split("********")
        // gen[0] is empty
        val beforeProcess = gen[1] to gen[2]
        processingCheck(beforeProcess)
    }

    fun processingCheck(beforeProcess: Pair<String, String>) {
        val source = JavaFileObjects.forSourceString(beforeProcess.first, beforeProcess.second)
        Truth.assertAbout<JavaSourcesSubject.SingleSourceAdapter, JavaFileObject, JavaSourceSubjectFactory>(JavaSourceSubjectFactory.javaSource())
                .that(source)
                .withCompilerOptions("-Xlint:-processing")
                .processedWith(ActivityStarterProcessor())
                .compilesWithoutError()
    }

    fun processingErrorCheck(beforeProcess: Pair<String, String>, errorText: String?) {
        val source = JavaFileObjects.forSourceString(beforeProcess.first, beforeProcess.second)
        Truth.assertAbout<JavaSourcesSubject.SingleSourceAdapter, JavaFileObject, JavaSourceSubjectFactory>(JavaSourceSubjectFactory.javaSource())
                .that(source)
                .withCompilerOptions("-Xlint:-processing")
                .processedWith(ActivityStarterProcessor())
                .failsToCompile()
                .withErrorContaining(errorText ?: "")
    }

    private fun processingComparator(beforeProcess: Pair<String, String>, afterProcess: Pair<String, String>) {
        val source = JavaFileObjects.forSourceString(beforeProcess.first, beforeProcess.second)
        val bindingSource = JavaFileObjects.forSourceString(afterProcess.first, afterProcess.second)
        Truth.assertAbout<JavaSourcesSubject.SingleSourceAdapter, JavaFileObject, JavaSourceSubjectFactory>(JavaSourceSubjectFactory.javaSource())
                .that(source)
                .withCompilerOptions("-Xlint:-processing")
                .processedWith(ActivityStarterProcessor())
                .compilesWithoutWarnings()
                .and()
                .generatesSources(bindingSource)
    }
}