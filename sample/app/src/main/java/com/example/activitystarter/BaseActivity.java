package com.example.activitystarter;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.lang.annotation.Annotation;

import activitystarter.ActivityStarterConfig;
import activitystarter.Arg;
import activitystarter.wrapping.ArgConverter;

@ActivityStarterConfig(converters = { BaseActivity.IntToLongConverter.class })
public class BaseActivity extends AppCompatActivity {

    @Arg long l;

    static class IntToLongConverter implements ArgConverter<Integer, Long> {

        @Nullable
        @Override
        public Class<? extends Annotation> requiredAnnotation() {
            return null;
        }

        @Override
        public Long wrap(Integer toWrap) {
            return toWrap.longValue();
        }

        @Override
        public Integer unwrap(Long wrapped) {
            return wrapped.intValue();
        }
    }
}
