package com.marcinmoskala.kotlinapp

import activitystarter.ActivityStarter
import java.util.ArrayList

import activitystarter.Arg
import android.os.Bundle
import com.marcinmoskala.activitystarter.argExtra

class AllTypesActivity : BaseActivity() {

    @get:Arg val str: String by argExtra()
    @get:Arg val i: Int by argExtra()
    @get:Arg val l: Long by argExtra()
    @get:Arg val f: Float by argExtra()
    @get:Arg val bool: Boolean by argExtra()
    @get:Arg val d: Double by argExtra()
    @get:Arg val c: Char by argExtra()
    @get:Arg val b: Byte by argExtra()
    @get:Arg val s: Short by argExtra()
    @get:Arg val cs: CharSequence by argExtra()
    @get:Arg val stra: Array<String> by argExtra()
    @get:Arg val ia: IntArray by argExtra()
    @get:Arg val la: LongArray by argExtra()
    @get:Arg val fa: FloatArray by argExtra()
    @get:Arg val boola: BooleanArray by argExtra()
    @get:Arg val da: DoubleArray by argExtra()
    @get:Arg val ca: CharArray by argExtra()
    @get:Arg val ba: ByteArray by argExtra()
    @get:Arg val sa: ShortArray by argExtra()
    @get:Arg val csa: Array<CharSequence> by argExtra()
    @get:Arg val intList: ArrayList<Int> by argExtra()
    @get:Arg val strList: ArrayList<String> by argExtra()
    @get:Arg val csList: ArrayList<CharSequence> by argExtra()
    @get:Arg val sp: StudentParcelable by argExtra()
    @get:Arg val ss: StudentSerializable by argExtra()
    @get:Arg val spList: ArrayList<StudentParcelable> by argExtra()
}