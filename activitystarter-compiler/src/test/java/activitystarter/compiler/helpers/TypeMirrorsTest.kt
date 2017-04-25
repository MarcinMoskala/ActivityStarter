package activitystarter.compiler.helpers

import com.google.common.truth.Truth.assertThat
import com.google.testing.compile.CompilationRule
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.TypeName
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.nio.charset.Charset
import javax.lang.model.type.TypeKind

@RunWith(JUnit4::class)
class TypeMirrorsTest {

    @Rule @JvmField val c = CompilationRule()

    @Test
    fun `Basic mapped TypeName is correct`() {
        val booleanTypeMirror = c.boolTypeMirror
        assertTrue(TypeName.BOOLEAN == TypeName.get(booleanTypeMirror))
        assertEquals(TypeName.INT, TypeName.get(c.intTypeMirror))
        val basicTypesMap = mapOf(
                TypeName.get(String::class.java) to c.stringTypeMirror,
                TypeName.BOOLEAN to c.boolTypeMirror,
                TypeName.BYTE to c.byteTypeMirror,
                TypeName.SHORT to c.shortTypeMirror,
                TypeName.INT to c.intTypeMirror,
                TypeName.LONG to c.longTypeMirror,
                TypeName.CHAR to c.charTypeMirror,
                TypeName.FLOAT to c.floatTypeMirror,
                TypeName.DOUBLE to c.doubleTypeMirror
        )
        for ((typeName, typeMirror) in basicTypesMap) {
            assertEquals(typeName, TypeName.get(typeMirror))
        }
    }

    @Test
    fun `Basic types mapped Kind is correct`() {
        val kindToTypeMap = mapOf(
                TypeKind.BOOLEAN to c.boolTypeMirror,
                TypeKind.BYTE to c.byteTypeMirror,
                TypeKind.SHORT to c.shortTypeMirror,
                TypeKind.INT to c.intTypeMirror,
                TypeKind.LONG to c.longTypeMirror,
                TypeKind.CHAR to c.charTypeMirror,
                TypeKind.FLOAT to c.floatTypeMirror,
                TypeKind.DOUBLE to c.doubleTypeMirror
        )
        for ((typeKind, typeMirror) in kindToTypeMap) {
            assertEquals(typeKind, typeMirror.kind)
        }
    }

    @Test
    fun `Array kind is correct`() {
        assertEquals(TypeKind.ARRAY, c.intArrayTypeMirror.kind)
    }

    @Test fun getBasicTypeMirror() {
        assertThat(TypeName.get(c.getMirror<Any>()))
                .isEqualTo(ClassName.get(Any::class.java))
        assertThat(TypeName.get(c.getMirror<Charset>()))
                .isEqualTo(ClassName.get(Charset::class.java))
    }

    @Test fun getPrimitiveTypeMirror() {
        assertThat(TypeName.get(c.boolTypeMirror))
                .isEqualTo(TypeName.BOOLEAN)
        assertThat(TypeName.get(c.byteTypeMirror))
                .isEqualTo(TypeName.BYTE)
        assertThat(TypeName.get(c.shortTypeMirror))
                .isEqualTo(TypeName.SHORT)
        assertThat(TypeName.get(c.intTypeMirror))
                .isEqualTo(TypeName.INT)
        assertThat(TypeName.get(c.longTypeMirror))
                .isEqualTo(TypeName.LONG)
        assertThat(TypeName.get(c.charTypeMirror))
                .isEqualTo(TypeName.CHAR)
        assertThat(TypeName.get(c.floatTypeMirror))
                .isEqualTo(TypeName.FLOAT)
        assertThat(TypeName.get(c.doubleTypeMirror))
                .isEqualTo(TypeName.DOUBLE)
    }

    @Test
    fun arrayListTypeMirrorTests() {
        assertEquals("java.util.ArrayList<java.lang.Integer>", c.integerArrayListTypeMirror.toString())
    }
}