package com.gob.rewmobile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import com.gob.rewmobile.objects.Data;
import com.gob.rewmobile.objects.Mesa;
import com.gob.rewmobile.util.BloqueAdapter;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class FragmentMesasActivity extends FragmentActivity implements
		ActionBar.TabListener {

	private static GridView gridView = null;
	private static String mozo_name = null;
	private LoadDataTask loadDataTask = null;

	private SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	private ViewPager mViewPager;

	private ProgressDialog mProgress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_fragment_mesas);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// Show the Up button in the action bar.
		actionBar.setDisplayHomeAsUpEnabled(true);

		Bundle bundle = this.getIntent().getExtras();
		mozo_name = bundle.getString("mozo_name");
		// Item item = (Item) findViewById(R.id.labelMozo);
		// item.setText(mozo_name);
		ActionBar ab = getActionBar();
		ab.setTitle(mozo_name);
		ab.setSubtitle("Mesas");

		mProgress = new ProgressDialog(this);
		mProgress.setMessage("Cargando Mesas...");
		mProgress.show();
		if (loadDataTask != null)
			return;
		loadDataTask = new LoadDataTask(this);
		loadDataTask.execute((Void) null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fragment_mesas, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			overridePendingTransition(R.animator.slide_out, R.animator.slide_in);
			finish();
			return true;
		case R.id.action_mesas_refresh:
			mProgress.show();
			if (loadDataTask != null)
				loadDataTask = null;
			loadDataTask = new LoadDataTask(this);
			loadDataTask.execute((Void) null);
			// new Thread(new Runnable() {
			// public void run() {

			// }
			// }).start();

			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public class LoadDataTask extends AsyncTask<Void, Void, Boolean> {

		private Context context;

		public LoadDataTask(Context context) {
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
				Log.i("Data", "Mesas Refresh");
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
				mSectionsPagerAdapter = new SectionsPagerAdapter(
						getSupportFragmentManager());

				mViewPager = (ViewPager) findViewById(R.id.pager);
				mViewPager.setAdapter(mSectionsPagerAdapter);

				mViewPager
						.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
							@Override
							public void onPageSelected(int position) {
								getActionBar().setSelectedNavigationItem(
										position);
							}
						});
				getActionBar().removeAllTabs();
				for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
					getActionBar()
							.addTab(getActionBar()
									.newTab()
									.setText(
											mSectionsPagerAdapter
													.getPageTitle(i))
									.setTabListener((TabListener) this.context));
				}
				Log.i("onPostExecute", "True");
			} else {
				Log.i("onPostExecute", "False");
			}
			loadDataTask = null;
			mProgress.dismiss();
		}

		@Override
		protected void onCancelled() {
			loadDataTask = null;
			mProgress.dismiss();
		}
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
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
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
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
	public static class DummySectionFragment extends Fragment implements
			OnItemClickListener {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.layout_mesas, container,
					false);
			gridView = (GridView) rootView.findViewById(R.id.gridviewmesas);
			gridView.setOnItemClickListener(this);
			ArrayList<Mesa> mesas = new ArrayList<Mesa>();
			int zona = getArguments().getInt(ARG_SECTION_NUMBER) == 1 ? 1 : 51;
			for (int i = zona; i <= zona + 49; i++) {
				Mesa mesa = Data.LST_MESAS.get(i - 1);
				mesas.add(mesa);
			}
			gridView.setAdapter(new BloqueAdapter(getActivity(), mesas,
					BloqueAdapter.ITEM_MESA));
			return rootView;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View arg1, int position,
				long id) {
			// TODO Auto-generated method stub
			Mesa mesa = (Mesa) parent.getItemAtPosition(position);
			// Toast.makeText(getActivity(), mesa.getName(),
			// Toast.LENGTH_SHORT).show();

			Intent intent = new Intent(getActivity(), PedidoActivity.class);
			// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// Bundle bundle = new Bundle();
			// bundle.putInt("mesa_id", mesa.getId());
			// bundle.putString("mozo_name", mozo_name);
			// bundle.putSerializable("mesa", mesa);
			intent.putExtra("mozo_name", mozo_name);
			intent.putExtra("mesa_name", mesa.getName());
			// intent.putExtras(bundle);

			// intent.putExtra("mozo_name", mozo_name);
			// intent.putExtra("mesa", mesa);

			// intent.putExtra("mozo_id", mozo.getId());
			startActivity(intent);
			getActivity().finish();
			// overridePendingTransition(R.animator.slide_in,
			// R.animator.slide_out);
		}
	}

}