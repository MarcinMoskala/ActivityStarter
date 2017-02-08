package activitystarter

import activitystarter.compiler.ActivityStarterProcessor
import com.google.common.truth.Truth
import com.google.testing.compile.JavaFileObjects
import com.google.testing.compile.JavaSourceSubjectFactory
import com.google.testing.compile.JavaSourcesSubject
import java.io.File
import javax.tools.JavaFileObject

abstract class GenerationTest() {

    fun filePrecessingComparator(fileName: String) {
        val gen = File("../generationExamples/$fileName").readText().split("********")
        // gen[0] is empty
        val beforeProcess = gen[1] to gen[2]
        val afterProcess = gen[3] to gen[4]

        processingComparator(beforeProcess, afterProcess)
    }

    fun dirPrecessingComparator(dirName: String) {
        File("../generationExamples/$dirName/").walkTopDown().forEach {
            if(it.isDirectory) return@forEach
            filePrecessingComparator("$dirName/${it.name}")
        }
    }

    fun processingComparator(beforeProcess: Pair<String, String>, afterProcess: Pair<String, String>) {
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

    fun filePrecessingCheckError(fileName: String, expectedErrorPhrase: String) {
        val gen = File("../generationExamples/$fileName").readText().split("********")
        // gen[0] is empty
        val beforeProcess = gen[1] to gen[2]
        processingErrorCheck(beforeProcess, expectedErrorPhrase)
    }

    fun processingErrorCheck(beforeProcess: Pair<String, String>, errorText: String) {
        val source = JavaFileObjects.forSourceString(beforeProcess.first, beforeProcess.second)
        Truth.assertAbout<JavaSourcesSubject.SingleSourceAdapter, JavaFileObject, JavaSourceSubjectFactory>(JavaSourceSubjectFactory.javaSource())
                .that(source)
                .withCompilerOptions("-Xlint:-processing")
                .processedWith(ActivityStarterProcessor())
                .failsToCompile()
                .withErrorContaining(errorText)
    }
}