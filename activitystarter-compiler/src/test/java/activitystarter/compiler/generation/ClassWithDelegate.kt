package activitystarter.compiler.generation

import activitystarter.Arg
import kotlin.reflect.KProperty

class ClassWithDelegate {

    @get:Arg val name: String by NamedDelegate()
}

class NamedDelegate() {
    operator fun getValue(classWithDelegate: ClassWithDelegate, property: KProperty<*>): String = "Ala"
}
