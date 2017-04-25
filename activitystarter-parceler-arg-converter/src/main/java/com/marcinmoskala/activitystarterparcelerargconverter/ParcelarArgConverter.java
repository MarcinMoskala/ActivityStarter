package com.marcinmoskala.activitystarterparcelerargconverter;

import android.os.Parcelable;

import org.parceler.Parcels;

import java.lang.annotation.Annotation;

import activitystarter.wrapping.ArgConverter;

public class ParcelarArgConverter implements ArgConverter<Object, Parcelable> {

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
