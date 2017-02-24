package com.marcinmoskala.kotlinapp

import java.io.Serializable

class StudentSerializable(var id: Int, var name: String, var grade: Char, var passing: Boolean) : Serializable