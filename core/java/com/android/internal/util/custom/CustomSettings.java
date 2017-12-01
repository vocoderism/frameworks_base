/*
* Copyright (C) 2017 The Pixel Experience Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.android.internal.util.custom;

import android.os.RemoteException;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class CustomSettings {

    public class Action {
    }

    private static final String TAG = "CustomSettings";

    private Context mContext;
    private ICustomSettingsService mService;

    public CustomSettings(Context context, ICustomSettingsService service) {
        mContext = context;
        mService = service;
    }

    public void addCallback(ICustomSettingsCallback callback) {
        if (mService != null) {
            try {
                mService.addCallback(callback);
            } catch (RemoteException ex) {
                Log.e(TAG, "Failed to dispatch callback");
            }
        }
    }

    public void unregisterCallback(ICustomSettingsCallback callback) {
        if (mService != null) {
            try {
                mService.unregisterCallback(callback);
            } catch (RemoteException ex) {
                Log.e(TAG, "Failed to dispatch callback");
            }
        }
    }

    public void dispatchValueChanged(Intent intent) {
        if (mService != null) {
            try {
                mService.dispatchValueChanged(intent);
            } catch (RemoteException ex) {
                Log.e(TAG, "Failed to dispatch value changed");
            }
        }
    }
}
