package com.ob.rewmobile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import com.ob.rewmobile.R;
import com.ob.rewmobile.adapter.MesaAdapter;
import com.ob.rewmobile.model.Mesa;
import com.ob.rewmobile.model.Usuario;
import com.ob.rewmobile.util.Data;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class FragmentMesasActivity extends FragmentActivity implements ActionBar.TabListener {

	private static CargarMesasTask cargarMesasTask = null;
	private ViewPager mViewPager;
	private static ProgressDialog pd;
	private static Usuario mozo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fragment_mesas);

		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayHomeAsUpEnabled(true);

		Bundle bundle = getIntent().getExtras();
		mozo = new Usuario(bundle.getInt("mozo_id"), bundle.getString("mozo_name"));

		ActionBar ab = getActionBar();
		ab.setTitle(mozo.getNombre());
		ab.setSubtitle("Mesas");

		pd = new ProgressDialog(this);
		pd.setTitle(R.string.procesando);
		pd.setMessage("Cargando Mesas...");
		pd.show();
		if (cargarMesasTask != null)
			return;
		cargarMesasTask = new CargarMesasTask(this);
		cargarMesasTask.execute((Void) null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.fragment_mesas, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			overridePendingTransition(R.animator.animation_leave, R.animator.animation_enter);
			finish();
			return true;
		case R.id.action_mesas_refresh:
			pd.show();
			if (cargarMesasTask != null)
				cargarMesasTask = null;
			cargarMesasTask = new CargarMesasTask(this);
			cargarMesasTask.execute((Void) null);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public class CargarMesasTask extends AsyncTask<Void, Void, Boolean> {

		private Context context;

		public CargarMesasTask(Context context) {
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				// Looper.prepare();
				Data data = new Data(this.context);
				data.loadMesas();
				// Looper.loop();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			if (success) {
				SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

				mViewPager = (ViewPager) findViewById(R.id.pager);
				mViewPager.setAdapter(sectionsPagerAdapter);

				mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						getActionBar().setSelectedNavigationItem(position);
					}
				});
				getActionBar().removeAllTabs();
				for (int i = 0; i < sectionsPagerAdapter.getCount(); i++) {
					getActionBar().addTab(getActionBar().newTab().setText(sectionsPagerAdapter.getPageTitle(i)).setTabListener((TabListener) this.context));
				}
			}
			cargarMesasTask = null;
			pd.dismiss();
		}

		@Override
		protected void onCancelled() {
			cargarMesasTask = null;
			pd.dismiss();
		}
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
		FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
		FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	@SuppressLint("ValidFragment")
	public static class DummySectionFragment extends Fragment implements OnItemClickListener {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}
		

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.layout_mesas, container, false);
			GridView gridView = (GridView) rootView.findViewById(R.id.gridviewmesas);
			gridView.setOnItemClickListener(this);
			ArrayList<Mesa> mesas = new ArrayList<Mesa>();
			int zona = getArguments().getInt(ARG_SECTION_NUMBER) == 1 ? 1 : 51;
			for (int i = zona; i <= zona + 49; i++) {
				Mesa mesa = Data.LST_MESAS.get(i - 1);
				mesas.add(mesa);
			}
			gridView.setAdapter(new MesaAdapter(getActivity(), mesas));
			return rootView;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View arg1, int position, long id) {
			// TODO Auto-generated method stub
			Mesa mesa = (Mesa) parent.getItemAtPosition(position);

			Intent intent = new Intent(getActivity(), PedidoActivity.class);
			// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			Bundle bundle = new Bundle();
			bundle.putInt("mozo_id", mozo.getId());
			bundle.putString("mozo_name", mozo.getNombre());
			bundle.putString("mesa_name", mesa.getNombre());
			intent.putExtras(bundle);

			startActivity(intent);
			getActivity().finish();
			// overridePendingTransition(R.animator.slide_in, R.animator.slide_out);
		}
	}

}