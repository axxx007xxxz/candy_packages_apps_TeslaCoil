/*
 * Copyright (C) 2014 The Dirty Unicorns Project
 *					2017 CandyRoms
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.candyroms.candysettings.fragments;

import android.content.Context;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.UserHandle;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.provider.Settings;

import com.android.settings.R;
import com.android.internal.logging.MetricsProto.MetricsEvent;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;
import com.candyroms.candysettings.preference.CustomSeekBarPreference;

public class PowerMenu extends SettingsPreferenceFragment implements OnPreferenceChangeListener {
    private static final String KEY_ADVANCED_REBOOT = "advanced_reboot";
    private static final String POWER_REBOOT_DIALOG_DIM = "power_reboot_dialog_dim";
    private static final String POWER_MENU_ANIMATIONS = "power_menu_animations";

    private ListPreference mAdvancedReboot;
    private ListPreference mPowerMenuAnimations;
    private CustomSeekBarPreference mPowerRebootDialogDim;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.powermenu);

        final ContentResolver resolver = getActivity().getContentResolver();
        final PreferenceScreen prefScreen = getPreferenceScreen();

        mAdvancedReboot = (ListPreference) findPreference(KEY_ADVANCED_REBOOT);
        mAdvancedReboot.setValue(String.valueOf(Settings.Secure.getInt(
                getContentResolver(), Settings.Secure.ADVANCED_REBOOT, 1)));
        mAdvancedReboot.setSummary(mAdvancedReboot.getEntry());
        mAdvancedReboot.setOnPreferenceChangeListener(this);

        mPowerRebootDialogDim = (CustomSeekBarPreference) prefScreen.findPreference(POWER_REBOOT_DIALOG_DIM);
        int powerRebootDialogDim = Settings.System.getInt(resolver,
                Settings.System.POWER_REBOOT_DIALOG_DIM, 50);
        mPowerRebootDialogDim.setValue(powerRebootDialogDim / 1);
        mPowerRebootDialogDim.setOnPreferenceChangeListener(this);

        mPowerMenuAnimations = (ListPreference) findPreference(POWER_MENU_ANIMATIONS);
        mPowerMenuAnimations.setValue(String.valueOf(Settings.System.getInt(
                getContentResolver(), Settings.System.POWER_MENU_ANIMATIONS, 0)));
        mPowerMenuAnimations.setSummary(mPowerMenuAnimations.getEntry());
        mPowerMenuAnimations.setOnPreferenceChangeListener(this);
    }

    @Override
    protected int getMetricsCategory() {
        return MetricsEvent.CANDYSETTINGS;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mAdvancedReboot) {
            Settings.Secure.putInt(getContentResolver(), Settings.Secure.ADVANCED_REBOOT,
                    Integer.valueOf((String) newValue));
            mAdvancedReboot.setValue(String.valueOf(newValue));
            mAdvancedReboot.setSummary(mAdvancedReboot.getEntry());
        } else if (preference == mPowerRebootDialogDim) {
            int alpha = (Integer) newValue;
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.POWER_REBOOT_DIALOG_DIM, alpha * 1);
            return true;
        } else if (preference == mPowerMenuAnimations) {
            Settings.System.putInt(getContentResolver(), Settings.System.POWER_MENU_ANIMATIONS,
                    Integer.valueOf((String) newValue));
            mPowerMenuAnimations.setValue(String.valueOf(newValue));
            mPowerMenuAnimations.setSummary(mPowerMenuAnimations.getEntry());
            return true;
        }
        return false;
    }
}
