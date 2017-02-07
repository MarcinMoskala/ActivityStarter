package activitystarter

import activitystarter.compiler.ActivityStarterProcessor
import com.google.common.truth.Truth.assertAbout
import com.google.testing.compile.JavaFileObjects
import com.google.testing.compile.JavaSourceSubjectFactory
import com.google.testing.compile.JavaSourceSubjectFactory.javaSource
import com.google.testing.compile.JavaSourcesSubject.SingleSourceAdapter
import org.junit.Test
import javax.tools.JavaFileObject


class FragmentGenerationTest: GenerationTest() {

    @Test
    fun simpleGenerationTest() {
        filePrecessingComparator("fragment/EmptyAnnotated")
    }

    @Test
    fun singleArgGenerationTest() {
        filePrecessingComparator("fragment/SimpleGeneration")
    }

    @Test
    fun multipleOptionalArgGenerationTest() {
        filePrecessingComparator("fragment/MultipleOptional")
    }

    @Test
    fun allGenerationTest() {
        dirPrecessingComparator("fragment")
    }
}