package activitystarter.compiler.helpers

fun assertContains(what: String, where: String) {
    assert(what in where) { "$what not found in $where" }
}