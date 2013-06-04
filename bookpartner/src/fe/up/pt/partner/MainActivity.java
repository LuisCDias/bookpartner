package fe.up.pt.partner;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import fe.up.pt.partner.R;
import fragments.MainFragment;
import fragments.MainFragment_Genres;
import fragments.MainFragment_Recent;
import fragments.MainFragment_Top;

public class MainActivity extends SherlockFragmentActivity implements TabListener {

	ViewPager mViewPager;
	ActionBar mActionBar;
	TabsAdapter mTabsAdapter;
	String userToken;
	String useMode ;
	String userID;
	String userRating;
	String userName;
	String title;
	Bundle extras_top;
	Bundle extras_recent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		setTheme(R.style.Theme_Sherlock);
		super.onCreate(savedInstanceState);		

		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.pager);
		mViewPager.setBackgroundColor(Color.BLACK);
		setContentView(mViewPager);

		mActionBar = getSupportActionBar();
		mActionBar.setDisplayShowTitleEnabled(true);
		mActionBar.setHomeButtonEnabled(true);
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		extras_recent = new Bundle();
		extras_top = new Bundle();
		extras_recent.putString("order", "date");
		extras_top.putString("order", "class");

		mTabsAdapter = new TabsAdapter(this, mViewPager);
		mTabsAdapter.addTab(mActionBar.newTab().setText("Top"),
				MainFragment_Top.MainFragment_Top_Aux.class, extras_top);
		mTabsAdapter.addTab(mActionBar.newTab().setText("Recent"),
				MainFragment_Recent.MainFragment_Recent_Aux.class, extras_recent);
		mTabsAdapter.addTab(mActionBar.newTab().setText("Genre"),
				MainFragment_Genres.MainFragment_Genres_Aux.class, null);
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

			searchManager.startSearch(null, false,new ComponentName(this, SearchableActivity.class), b, false);
			return true;
		}
		return false;
	}

	@Override
	protected void onStop() {
		setResult(0);
		super.onStop();
	}
	@Override
	protected void onDestroy() {
		setResult(0);
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==3){
			finish();
		}
	}

	public void getGenreBooks(View v){

		TextView genre = null;

		switch(v.getId()) {
		case R.id.genre_title_1:
			genre = (TextView)v.findViewById(R.id.genre_title_1);
			Log.d("GENRE", genre.getText()+"");

			break;
		case R.id.genre_title_2:
			genre = (TextView)v.findViewById(R.id.genre_title_2);
			Log.d("GENRE", genre.getText()+"");

			break;
		case R.id.genre_title_3:
			genre = (TextView)v.findViewById(R.id.genre_title_3);
			Log.d("GENRE", genre.getText()+"");

			break;

		}
		Intent intent = new Intent(this, GenreBooksActivity.class);
		intent.putExtra("genre", genre.getText()+"");
		startActivity(intent);
	}

	/*public void openFullCover(View v){

		MainFragment_Recent.openFullCover(v, this);
	}*/

	public void onTabSelected(Tab tab, FragmentTransaction ft) {

	}

	public void onTabUnselected(Tab tab, FragmentTransaction ft) {


	}

	public void onTabReselected(Tab tab, FragmentTransaction ft) {


	}
}