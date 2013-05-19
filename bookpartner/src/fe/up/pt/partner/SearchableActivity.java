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
		mActionBar.setDisplayShowTitleEnabled(false);
		mActionBar.setHomeButtonEnabled(true);
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);



		String search_query = null;
		final Intent queryIntent = getIntent();
		final String queryAction = queryIntent.getAction();

		Bundle b = queryIntent.getBundleExtra(SearchManager.APP_DATA);

		/*useMode = b.getString(PartnerAPI.Strings.USE_MODE_BUNDLE);
		if(useMode.equals(PartnerAPI.Strings.USER_MODE))
			userToken = PreferenceManager.getDefaultSharedPreferences(this).getString(PartnerAPI.Strings.ACCESS_TOKEN, null);
		 */
		
		if (Intent.ACTION_SEARCH.equals(queryAction)) {
			search_query=  doSearchWithIntent(queryIntent);
		}
		

		Log.d("query", search_query);

		//String query_from_Bundle = b.getString("query_from_Bundle");

		Bundle b_query = new Bundle();
		b_query.putString("search_query", search_query); //query keywords


		String actionBarTitle = "Results For '"+ search_query +"'";

		mTabsAdapter = new TabsAdapter(this, mViewPager);
		mTabsAdapter.addTab(mActionBar.newTab().setText(actionBarTitle),
				SearchableActivityFragment.SearchableActivityFragmentAux.class, b_query);

	}

	/*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.activity_main_menu, menu);
            return true;
    }*/

	private String doSearchWithIntent(final Intent queryIntent) {
		final String queryString = queryIntent
				.getStringExtra(SearchManager.QUERY);
		return queryString;
	}


}