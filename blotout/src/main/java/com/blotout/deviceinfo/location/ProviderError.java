package com.blotout.deviceinfo.location;

import androidx.annotation.NonNull;

/**
 * Created by Blotout on 2020-01-08.
 *
 * ernitinjai@gmail.com
 */
public class ProviderError extends Throwable {
    String provider;

    public ProviderError(String provider, String detailMessage) {
        super(detailMessage);
        this.provider = provider;
    }

    public String getProvider() {
        return provider;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString() + " | ProviderError{" +
                "provider='" + provider + '\'' +
                '}';
    }
}
