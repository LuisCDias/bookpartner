package fe.up.pt.partner;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import fe.up.pt.partner.R;
import fragments.SearchableActivityFragment;

public class SearchableActivity extends SherlockFragmentActivity {

	ViewPager mViewPager;
	ActionBar mActionBar;
	TabsAdapter mTabsAdapter;
	public String userToken;
	public String useMode ;


	@Override
	public void onCreate(Bundle savedInstanceState) {
   
		super.onCreate(savedInstanceState);

		
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.pager);
		mViewPager.setBackgroundColor(Color.BLACK);
		setContentView(mViewPager);

		mActionBar = getSupportActionBar();
		mActionBar.setDisplayShowTitleEnabled(true);
		mActionBar.setHomeButtonEnabled(true);
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);



		String search_query = null;
		final Intent queryIntent = getIntent();
		final String queryAction = queryIntent.getAction();

		Bundle b = queryIntent.getBundleExtra(SearchManager.APP_DATA);
		
		if (Intent.ACTION_SEARCH.equals(queryAction)) {
			search_query=  doSearchWithIntent(queryIntent);
		}
		
		
		Log.d("query", search_query);

		//String query_from_Bundle = b.getString("query_from_Bundle");
		
		Bundle b_query = new Bundle();
		Bundle b_query_top = new Bundle();
		Bundle b_query_recent = new Bundle();
		
		b_query.putString("search_query", search_query); //query keywords
		b_query_top.putString("search_query", search_query); //query keywords
		b_query_top.putString("order","class");
		b_query_recent.putString("search_query", search_query); //query keywords
		b_query_recent.putString("order","date");
		
		
		search_query = removeLastBlack(search_query);
		String actionBarTitle = "Results for '"+ search_query +"'";
		
		this.setTitle(actionBarTitle);
		mTabsAdapter = new TabsAdapter(this, mViewPager);
		mTabsAdapter.addTab(mActionBar.newTab().setText("Recent"),
				SearchableActivityFragment.SearchableActivityFragmentAux.class, b_query_recent);
		mTabsAdapter.addTab(mActionBar.newTab().setText("All"),
				SearchableActivityFragment.SearchableActivityFragmentAux.class, b_query);
		mTabsAdapter.addTab(mActionBar.newTab().setText("Top"),
				SearchableActivityFragment.SearchableActivityFragmentAux.class, b_query_top);
		mActionBar.setSelectedNavigationItem(1);
		
	}

	private String removeLastBlack(String str) {

		if (str.length() > 0 && str.charAt(str.length()-1)==' ') {
		    str = str.substring(0, str.length()-1);
		  }
		  return str;
	}

	/*@Override
	-    public boolean onCreateOptionsMenu(Menu menu) {
	-            getMenuInflater().inflate(R.menu.activity_main_menu, menu);
	-            return true;
	-    }*/

	private String doSearchWithIntent(final Intent queryIntent) {
		final String queryString = queryIntent
				.getStringExtra(SearchManager.QUERY);
		return queryString;
	}


}