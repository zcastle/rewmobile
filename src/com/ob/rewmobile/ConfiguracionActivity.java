package com.ob.rewmobile;

import com.ob.rewmobile.R;
import com.ob.rewmobile.model.Equipo;
import com.ob.rewmobile.util.Data;
import com.ob.rewmobile.util.Globals;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

public class ConfiguracionActivity extends PreferenceActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setTheme(android.R.style.Theme_Holo);
		setupActionBar();

		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new ConfiguracionFragment())
				.commit();
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// Show the Up button in the action bar.
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			try {
				NavUtils.navigateUpFromSameTask(this);
			} catch(Exception e) {
				startActivity(new Intent(getBaseContext(), AccesoActivity.class));
				overridePendingTransition(R.animator.slide_in, R.animator.slide_out);
			}
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {

		@Override
		public boolean onPreferenceChange(Preference preference, Object value) {
			preference.setSummary(value.toString());
			return true;
		}
	};
	
	private static void bindPreferenceSummaryToValue(Preference preference) {
		preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
		sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(),""));
	}
	
	public static class ConfiguracionFragment extends PreferenceFragment {

		private String[] keys = {"keyHost","keyPort","keyPath","keyNombre","keyDia","keySerieBoleta","keyNumeroBoleta","keySerieFactura","keyNumeroFactura","keyImpresoraP","keyImpresoraB","keyImpresoraF"};
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			// Load the preferences from an XML resource
			addPreferencesFromResource(R.xml.configuracion);
			
			for (int i = 0; i < keys.length; i++) {
				bindPreferenceSummaryToValue(findPreference(keys[i]));
			}
			
			EditTextPreference txtNombre = (EditTextPreference) findPreference("keyNombre");
			EditTextPreference txtDia = (EditTextPreference) findPreference("keyDia");
			EditTextPreference txtSerieBoleta = (EditTextPreference) findPreference("keySerieBoleta");
			EditTextPreference txtNumeroBoleta = (EditTextPreference) findPreference("keyNumeroBoleta");
			EditTextPreference txtSerieFactura = (EditTextPreference) findPreference("keySerieFactura");
			EditTextPreference txtNumeroFactura = (EditTextPreference) findPreference("keyNumeroFactura");
			EditTextPreference txtImpresoraP = (EditTextPreference) findPreference("keyImpresoraP");
			EditTextPreference txtImpresoraB = (EditTextPreference) findPreference("keyImpresoraB");
			EditTextPreference txtImpresoraF = (EditTextPreference) findPreference("keyImpresoraF");
			
			Equipo equipo = Data.equipoController.getEquipo();
			
			if(equipo.getId()>0) {
				txtNombre.setSummary(equipo.getCentroCosto().getEmpresa().getNombre());
				txtDia.setSummary(equipo.getDia()+"");
				txtSerieBoleta.setSummary(equipo.getSerieBoleta());
				txtNumeroBoleta.setSummary(equipo.getNumeroBoleta()+"");
				txtSerieFactura.setSummary(equipo.getSerieFactura());
				txtNumeroFactura.setSummary(equipo.getNumeroFactura()+"");
				txtImpresoraP.setSummary(equipo.getImpresoraP());
				txtImpresoraB.setSummary(equipo.getImpresoraB());
				txtImpresoraF.setSummary(equipo.getImpresoraF());
				Globals.IMPRESORA_P = equipo.getImpresoraP();
				Globals.IMPRESORA_B = equipo.getImpresoraB();
				Globals.IMPRESORA_F = equipo.getImpresoraF();
			}
		}

	}
}
