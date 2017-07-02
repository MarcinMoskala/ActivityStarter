package com.marcinmoskala.kotlinapp

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator

class StudentParcelable(
        var id: Int = 0,
        var name: String? = null,
        var grade: Char = ' ') : Parcelable {

    constructor(`in`: Parcel): this(`in`.readInt(), `in`.readString(), `in`.readString()[0])

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeString(name)
        dest.writeString(grade.toString())
    }

    companion object {
        @JvmField val CREATOR: Creator<*> = object : Creator<StudentParcelable> {
            override fun createFromParcel(`in`: Parcel): StudentParcelable {
                return StudentParcelable(`in`)
            }

            override fun newArray(size: Int): Array<StudentParcelable> {
                return Array(3) { StudentParcelable() }
            }
        }
    }
}