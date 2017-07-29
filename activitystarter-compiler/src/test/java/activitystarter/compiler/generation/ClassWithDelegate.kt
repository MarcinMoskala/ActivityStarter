package activitystarter.compiler.generation

import kotlin.reflect.KProperty

class ClassWithDelegate {

    val name: String by NamedDelegate()

}

class NamedDelegate() {
    operator fun getValue(classWithDelegate: ClassWithDelegate, property: KProperty<*>): String = "Ala"
}
