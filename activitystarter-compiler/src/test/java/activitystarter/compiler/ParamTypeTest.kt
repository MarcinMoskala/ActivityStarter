package activitystarter.compiler

import activitystarter.compiler.helpers.TypeMirrors
import com.squareup.javapoet.TypeName
import org.junit.Assert
import org.junit.Test

class ParamTypeTest {

    @Test
    fun `There is class that represents type that include all supported types`() {
        ParamType::class.java
    }

    @Test
    fun `Type mapper is mapping Boolean type to its equivalent`() {
        Assert.assertEquals(ParamType.Boolean, ParamType.fromType(TypeMirrors.Boolean))
    }
}