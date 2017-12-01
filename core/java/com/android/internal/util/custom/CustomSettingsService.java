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

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.DeadObjectException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class CustomSettingsService extends ICustomSettingsService.Stub implements IBinder.DeathRecipient {

    private static final String TAG = CustomSettingsService.class.getSimpleName();

    private final List<ICustomSettingsCallback> mCallbacks = new ArrayList<>();
    private Context mContext;

    public CustomSettingsService(Context context) {
        mContext = context;
    }

    @Override
    public void binderDied() {
        for (int i = mCallbacks.size() - 1; i >= 0; i--) {
            final ICustomSettingsCallback callback = mCallbacks.get(i);
            try {
                if (callback != null) {
                    callback.onServiceDied();
                }
            } catch (DeadObjectException e) {
                Log.w(TAG, "Death object while calling binderDied: ", e);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed to call binderDied: ", e);
            } catch (NullPointerException e) {
                Log.w(TAG, "NullPointer while calling binderDied: ", e);
            }
        }
        mCallbacks.clear();
    }

    @Override
    public void addCallback(ICustomSettingsCallback callback) {
        synchronized (mCallbacks) {
            if (!mCallbacks.contains(callback)) {
                mCallbacks.add(callback);
            }
        }
    }

    @Override
    public void unregisterCallback(ICustomSettingsCallback callback) {
        synchronized (mCallbacks) {
            if (mCallbacks.contains(callback)) {
                mCallbacks.remove(callback);
            }
        }
    }

    @Override
    public void dispatchValueChanged(Intent intent) {
        for (int i = 0; i < mCallbacks.size(); i++) {
            ICustomSettingsCallback callback = mCallbacks.get(i);
            try {
                if (callback != null) {
                    callback.onValueChanged(intent);
                }
            } catch (RemoteException ex) {
                // Callback is dead
            } catch (NullPointerException e) {
                Log.e(TAG, "NullPointer while calling dispatchValueChanged: ", e);
            }
        }
    }
}