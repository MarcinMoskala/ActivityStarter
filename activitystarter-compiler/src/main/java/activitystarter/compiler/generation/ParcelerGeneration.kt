package activitystarter.compiler.generation

class ParcelerGeneration() {

    fun wrap(f: () -> String): String {
        return "org.parceler.Parcels.wrap(${f()})"
    }

    fun unwrap(f: () -> String): String {
        return "org.parceler.Parcels.unwrap(${f()})"
    }
}