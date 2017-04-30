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
    val integerToLongConverter by lazy { getConfigElement("IntToLongConverter") }
    val objectToParcelableConverter by lazy { getConfigElement("ParcelableConverter") }
    val multipleConverter by lazy { getConfigElement("IntToLongConverter", "ParcelableConverter") }

    private fun getConfigElement(vararg converters: String): List<TypeMirror> {
        val convertersList = converters.joinToString(separator = ", ", transform = { "MainActivity.$it.class" })
        val source = JavaFileObjects.forSourceString("com.example.activitystarter.MainActivity", """
package com.example.activitystarter;

import android.support.annotation.Nullable;
import java.lang.annotation.Annotation;
import activitystarter.ActivityStarterConfig;
import activitystarter.Arg;
import activitystarter.wrapping.ArgConverter;
import android.os.Parcelable;
import android.os.Parcel;

@ActivityStarterConfig(converters = { $convertersList })
public class MainActivity {

    static public class ParcelableConverter implements ArgConverter<Object, Parcelable> {

        @Override
        public Parcelable wrap(Object toWrap) {
            return new Parcelable() {
                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {

                }
            };
        }

        @Override
        public Object unwrap(Parcelable wrapped) {
            return wrapped;
        }
    }

    static public class IntToLongConverter implements ArgConverter<Integer, Long> {

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