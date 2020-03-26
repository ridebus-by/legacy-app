package org.xtimms.ridebus.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class TextUtils {

    @NonNull
    public static String notNull(@Nullable String str) {
        return str == null ? "" : str;
    }

}
