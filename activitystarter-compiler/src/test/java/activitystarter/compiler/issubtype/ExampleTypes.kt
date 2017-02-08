package activitystarter.compiler.issubtype

abstract class A()
interface B
abstract class C()

open class D(): A(), B
interface E: B

class F: E, D()
class G: C(), E