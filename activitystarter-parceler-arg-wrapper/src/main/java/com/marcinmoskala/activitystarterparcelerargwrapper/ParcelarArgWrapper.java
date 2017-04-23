package com.marcinmoskala.activitystarterparcelerargwrapper;

import android.os.Parcelable;

import org.parceler.Parcels;

import java.lang.annotation.Annotation;

import activitystarter.wrapping.ArgWrapper;

public class ParcelarArgWrapper implements ArgWrapper<Object, Parcelable> {

    @Override
    public Class<? extends Annotation> requiredAnnotation() {
        return org.parceler.Parcel.class;
    }

    @Override
    public Parcelable wrap(Object toWrap) {
        return Parcels.wrap(toWrap);
    }

    @Override
    public Object unwrap(Parcelable wrapped) {
        return Parcels.unwrap(wrapped);
    }
}
