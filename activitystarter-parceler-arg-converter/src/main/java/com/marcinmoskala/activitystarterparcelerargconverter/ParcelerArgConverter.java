package com.marcinmoskala.activitystarterparcelerargconverter;

import android.os.Parcelable;

import org.parceler.Parcels;

import activitystarter.wrapping.ArgConverter;

public class ParcelerArgConverter implements ArgConverter<Object, Parcelable> {

    @Override
    public Parcelable wrap(Object toWrap) {
        return Parcels.wrap(toWrap);
    }

    @Override
    public Object unwrap(Parcelable wrapped) {
        return Parcels.unwrap(wrapped);
    }
}
