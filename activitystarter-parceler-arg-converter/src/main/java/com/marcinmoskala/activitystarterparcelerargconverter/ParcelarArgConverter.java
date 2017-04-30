package com.marcinmoskala.activitystarterparcelerargconverter;

import android.os.Parcelable;

import org.parceler.Parcels;

import activitystarter.wrapping.ArgConverter;

public class ParcelarArgConverter implements ArgConverter<IsParcel, Parcelable> {

    @Override
    public Parcelable wrap(IsParcel toWrap) {
        return Parcels.wrap(toWrap);
    }

    @Override
    public IsParcel unwrap(Parcelable wrapped) {
        return Parcels.unwrap(wrapped);
    }
}
