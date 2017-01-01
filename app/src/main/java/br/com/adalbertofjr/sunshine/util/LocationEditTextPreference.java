package br.com.adalbertofjr.sunshine.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.EditTextPreference;
import android.util.AttributeSet;

import br.com.adalbertofjr.sunshine.R;

/**
 * Created by adalbertofjr on 01/01/17.
 */

public class LocationEditTextPreference extends EditTextPreference {
    public static final int DEFAULT_MIN_LOCATION_LENGTH = 2;
    private final int mMinLength;

    public LocationEditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.LocationEditTextPreference,
                0, 0);

        try {
            mMinLength = a.getInteger(R.styleable.LocationEditTextPreference_minLength, DEFAULT_MIN_LOCATION_LENGTH);
        } finally {
            a.recycle();
        }

    }
}
