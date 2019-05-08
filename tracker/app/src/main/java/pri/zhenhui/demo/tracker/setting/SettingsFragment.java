package pri.zhenhui.demo.tracker.setting;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import pri.zhenhui.demo.tracker.R;

public final class SettingsFragment extends PreferenceFragmentCompat {

    public SettingsFragment() {
        super();
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.app_preference);
    }
}

