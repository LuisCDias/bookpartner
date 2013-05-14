package fe.up.pt.partner;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import fe.up.pt.partner.R;
import fragments.BooksPanelActivityFragment;
import fragments.ReviewsFragment;

public class BooksPanelActivity extends SherlockFragmentActivity implements TabListener {

	ViewPager mViewPager;
	ActionBar mActionBar;
	TabsAdapter mTabsAdapter;
	Bundle extras ;
	public String userToken;
	public String useMode ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		String title = null;
		extras= getIntent().getExtras();
		if(extras!=null){
			title = extras.getString("title");
		}
		
		super.onCreate(savedInstanceState);

		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.pager);
		mViewPager.setBackgroundColor(Color.BLACK);
		setContentView(mViewPager);

		mActionBar = getSupportActionBar();
		mActionBar.setDisplayShowTitleEnabled(false);
		mActionBar.setHomeButtonEnabled(true);
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		mTabsAdapter = new TabsAdapter(this, mViewPager);
		mTabsAdapter.addTab(mActionBar.newTab().setText("Reviews"),
				ReviewsFragment.ReviewsFragmentAux.class,null);
		mTabsAdapter.addTab(mActionBar.newTab().setText(title),
				BooksPanelActivityFragment.BooksPanelActivityFragmentAux.class,extras);
		mTabsAdapter.addTab(mActionBar.newTab().setText("Similar"),
				ReviewsFragment.ReviewsFragmentAux.class,null);
		mActionBar.setSelectedNavigationItem(1);

	}	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_main, menu);
		MenuItem LogInItemMenu = menu.findItem(R.id.menu_login);
		
		LogInItemMenu.setTitle(PartnerAPI.Strings.LOGIN);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// TODO handle clicking the app icon/logo
			return false;
		case R.id.menu_search:
			//startSearch(title, false,null,false);
			onSearchRequested();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public boolean onSearchRequested() {
		SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);

		if(searchManager!=null)
		{
			// start the search with the appropriate searchable activity
			// so we get the correct search hint in the search dialog
			Bundle b = new Bundle();
			b.putString(PartnerAPI.Strings.USE_MODE_BUNDLE, useMode);
			//searchManager.startSearch(null, false,new ComponentName(this, SearchableActivity.class), b, false);
			return true;
		}
		return false;
	}

	public void onTabSelected(Tab tab, FragmentTransaction ft) {


	}

	public void onTabUnselected(Tab tab, FragmentTransaction ft) {


	}

	public void onTabReselected(Tab tab, FragmentTransaction ft) {


	}
}