package com.marcinmoskala.kotlinapp

import java.io.Serializable

data class StudentSerializable(var id: Int, var name: String, var grade: Char, var passing: Boolean) : Serializable