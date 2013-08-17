package com.gob.rewmobile;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class ConfiguracionFragment extends PreferenceFragment {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.configuracion);
    }

}
