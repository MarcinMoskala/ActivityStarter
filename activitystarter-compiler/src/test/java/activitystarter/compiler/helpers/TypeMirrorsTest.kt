package activitystarter.compiler.helpers

import com.squareup.javapoet.TypeName
import org.junit.Assert
import org.junit.Test
import javax.lang.model.type.TypeKind

class TypeMirrorsTest {

    @Test
    fun `Basic mapped TypeName is correct`() {
        val booleanTypeMirror = TypeMirrors.Boolean
        Assert.assertTrue(TypeName.BOOLEAN == TypeName.get(booleanTypeMirror))
        Assert.assertEquals(TypeName.INT, TypeName.get(TypeMirrors.Int))
        val basicTypesMap = mapOf(
                TypeName.get(String::class.java) to TypeMirrors.String,
                TypeName.BOOLEAN to TypeMirrors.Boolean,
                TypeName.BYTE to TypeMirrors.Byte,
                TypeName.SHORT to TypeMirrors.Short,
                TypeName.INT to TypeMirrors.Int,
                TypeName.LONG to TypeMirrors.Long,
                TypeName.CHAR to TypeMirrors.Char,
                TypeName.FLOAT to TypeMirrors.Float,
                TypeName.DOUBLE to TypeMirrors.Double
        )
        for ((typeName, typeMirror) in basicTypesMap) {
            Assert.assertEquals(typeName, TypeName.get(typeMirror))
        }
    }

    @Test
    fun `Basic types mapped Kind is correct`() {
        val kindToTypeMap = mapOf(
                TypeKind.BOOLEAN to TypeMirrors.Boolean,
                TypeKind.BYTE to TypeMirrors.Byte,
                TypeKind.SHORT to TypeMirrors.Short,
                TypeKind.INT to TypeMirrors.Int,
                TypeKind.LONG to TypeMirrors.Long,
                TypeKind.CHAR to TypeMirrors.Char,
                TypeKind.FLOAT to TypeMirrors.Float,
                TypeKind.DOUBLE to TypeMirrors.Double
        )
        for ((typeKind, typeMirror) in kindToTypeMap) {
            Assert.assertEquals(typeKind, typeMirror.kind)
        }
    }

    @Test
    fun `Array kind is correct`() {
        Assert.assertEquals(TypeKind.ARRAY, TypeMirrors.IntArray.kind)
    }
}