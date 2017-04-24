package activitystarter.compiler.helpers

import activitystarter.compiler.issubtype.ParamProcessor
import com.google.common.truth.Truth
import com.google.testing.compile.JavaFileObjects
import com.google.testing.compile.JavaSourceSubjectFactory
import com.google.testing.compile.JavaSourcesSubject
import javax.lang.model.type.TypeMirror
import javax.tools.JavaFileObject

object ConfigElement {
    val empty by lazy { getConfigElement() }
    val singleConverter by lazy { getConfigElement("IntToLongConverter") }

    private fun getConfigElement(vararg converters: String): List<TypeMirror> {
        val convertersList = converters.joinToString(separator = ", ", transform = { "MainActivity.$it.class" })
        val source = JavaFileObjects.forSourceString("com.example.activitystarter.MainActivity", """
package com.example.activitystarter;

import android.support.annotation.Nullable;

import java.lang.annotation.Annotation;

import activitystarter.ActivityStarterConfig;
import activitystarter.Arg;
import activitystarter.wrapping.ArgWrapper;

@ActivityStarterConfig(converters = { $convertersList })
public class MainActivity {

    static class IntToLongConverter implements ArgWrapper<Integer, Long> {

        @Nullable
        @Override
        public Class<? extends Annotation> requiredAnnotation() {
            return null;
        }

        @Override
        public Long wrap(Integer toWrap) {
            return toWrap.longValue();
        }

        @Override
        public Integer unwrap(Long wrapped) {
            return wrapped.intValue();
        }
    }
}
""")
        var element: List<TypeMirror>? = null
        Truth.assertAbout<JavaSourcesSubject.SingleSourceAdapter, JavaFileObject, JavaSourceSubjectFactory>(JavaSourceSubjectFactory.javaSource())
                .that(source)
                .withCompilerOptions("-Xlint:-processing")
                .processedWith(GetTypeMirrorHelperProcessor({ element = it }))!!
                .compilesWithoutError()
        return element!!
    }

    class GetTypeMirrorHelperProcessor(val typeCallback: (List<TypeMirror>) -> Unit) : ParamProcessor() {

        override fun onConfigFound(element: List<TypeMirror>) {
            typeCallback(element)
        }
    }
}