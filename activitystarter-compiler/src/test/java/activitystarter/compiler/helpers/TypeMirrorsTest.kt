package activitystarter.compiler.helpers

import com.squareup.javapoet.TypeName
import org.junit.Assert
import org.junit.Test

class TypeMirrorsTest {

    @Test
    fun `Basic mapped TypeName is correct`() {
        val booleanTypeMirror = TypeMirrors.Boolean
        Assert.assertTrue(TypeName.BOOLEAN == TypeName.get(booleanTypeMirror))
        Assert.assertEquals(TypeName.INT, TypeName.get(TypeMirrors.Int))
        val basicTypesMap = mapOf(
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
}