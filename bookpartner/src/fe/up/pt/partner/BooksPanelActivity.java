package fe.up.pt.partner;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
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

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class BooksPanelActivity extends SherlockFragmentActivity implements TabListener {

	public ViewPager mViewPager;
	ActionBar mActionBar;
	TabsAdapter mTabsAdapter;
	Bundle extras ;
	public String userToken;
	public String useMode ;
	
	/** Twitter Variables*/
	
	private static final String TAG = "BooksPanelActivity";
	/** Name to store the users access token */
	private static final String PREF_ACCESS_TOKEN = "accessToken";
	/** Name to store the users access token secret */
	private static final String PREF_ACCESS_TOKEN_SECRET = "accessTokenSecret";
	/** Consumer Key generated when you registered your app at https://dev.twitter.com/apps/ */
	private static final String CONSUMER_KEY = "Hr8KHgZ6jfcltNtJD5Khg";
	/** Consumer Secret generated when you registered your app at https://dev.twitter.com/apps/  */
	private static final String CONSUMER_SECRET = "w7JuAEjmhQ9B46KSr8CWQ2F3fQHkj0fktslCxnCc"; // XXX Encode in your app
	/** The url that Twitter will redirect to after a user log's in - this will be picked up by your app manifest and redirected into this activity */
	private static final String CALLBACK_URL = "book-partner-to-twitter:///";
	/** Preferences to store a logged in users credentials */
	private SharedPreferences mPrefs;
	/** Twitter4j object */
	private Twitter mTwitter;
	/** The request token signifies the unique ID of the request you are sending to twitter  */
	private RequestToken mReqToken;

	
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
		mActionBar.setDisplayShowTitleEnabled(true);
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

		// Create a new shared preference object to remember if the user has
		// already given us permission
		mPrefs = getSharedPreferences("twitterPrefs", MODE_PRIVATE);
		Log.i(TAG, "Got Preferences");
		
		// Load the twitter4j helper
		mTwitter = new TwitterFactory().getInstance();
		Log.i(TAG, "Got Twitter4j");
		
		// Tell twitter4j that we want to use it with our app
		mTwitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		Log.i(TAG, "Inflated Twitter4j");
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

	public void openFullCover(View v){

		Intent intent = new Intent(this, FullCoverActivity.class );
		
		String cover = extras.getString("cover");
		intent.putExtra("cover", cover);
		
		startActivity(intent);
	}

	public void shareTwitter(View v){

		buttonLogin(v);
		//buttonTweet(v);
		/*Intent intent = new Intent(this, FullCoverActivity.class );
		
		String cover = extras.getString("cover");
		intent.putExtra("cover", cover);
		
		startActivity(intent);*/
	}
	public void onTabSelected(Tab tab, FragmentTransaction ft) {


	}

	public void onTabUnselected(Tab tab, FragmentTransaction ft) {


	}

	public void onTabReselected(Tab tab, FragmentTransaction ft) {


	}
	
	public void buttonLogin(View v) {
		Log.i(TAG, "Login Pressed");
		if (mPrefs.contains(PREF_ACCESS_TOKEN)) {
			Log.i(TAG, "Repeat User");
			loginAuthorisedUser();
		} else {
			Log.i(TAG, "New User");
			loginNewUser();
		}
	}

	/**
	 * Button clickables are declared in XML as this projects min SDK is 1.6</br> </br>
	 * 
	 * @param v the clicked button
	 */
	public void buttonTweet(View v) {
		Log.i(TAG, "Tweet Pressed");
		tweetMessage();
	}

	/**
	 * Create a request that is sent to Twitter asking 'can our app have permission to use Twitter for this user'</br> 
	 * We are given back the {@link mReqToken}
	 * that is a unique indetifier to this request</br> 
	 * The browser then pops up on the twitter website and the user logins in ( we never see this informaton
	 * )</br> Twitter then redirects us to {@link CALLBACK_URL} if the login was a success</br>
	 * 
	 */
	private void loginNewUser() {
		try {
			Log.i(TAG, "Request App Authentication");
			mReqToken = mTwitter.getOAuthRequestToken(CALLBACK_URL);

			Log.i(TAG, "Starting Webview to login to twitter");
			WebView twitterSite = new WebView(this);
			twitterSite.loadUrl(mReqToken.getAuthenticationURL());
			setContentView(twitterSite);

		} catch (TwitterException e) {
			Toast.makeText(this, "Twitter Login error, try again later", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * The user had previously given our app permission to use Twitter</br> 
	 * Therefore we retrieve these credentials and fill out the Twitter4j helper
	 */
	private void loginAuthorisedUser() {
		String token = mPrefs.getString(PREF_ACCESS_TOKEN, null);
		String secret = mPrefs.getString(PREF_ACCESS_TOKEN_SECRET, null);

		// Create the twitter access token from the credentials we got previously
		AccessToken at = new AccessToken(token, secret);

		mTwitter.setOAuthAccessToken(at);
		
		Toast.makeText(this, "Welcome back", Toast.LENGTH_SHORT).show();
		
	}

	/**
	 * Catch when Twitter redirects back to our {@link CALLBACK_URL}</br> 
	 * We use onNewIntent as in our manifest we have singleInstance="true" if we did not the
	 * getOAuthAccessToken() call would fail
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Log.i(TAG, "New Intent Arrived");
		dealWithTwitterResponse(intent);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i(TAG, "Arrived at onResume");
	}
	
	/**
	 * Twitter has sent us back into our app</br> 
	 * Within the intent it set back we have a 'key' we can use to authenticate the user
	 * 
	 * @param intent
	 */
	private void dealWithTwitterResponse(Intent intent) {
		Uri uri = intent.getData();
		if (uri != null && uri.toString().startsWith(CALLBACK_URL)) { // If the user has just logged in
			String oauthVerifier = uri.getQueryParameter("oauth_verifier");

			authoriseNewUser(oauthVerifier);
		}
	}

	/**
	 * Create an access token for this new user</br> 
	 * Fill out the Twitter4j helper</br> 
	 * And save these credentials so we can log the user straight in next time
	 * 
	 * @param oauthVerifier
	 */
	private void authoriseNewUser(String oauthVerifier) {
		try {
			AccessToken at = mTwitter.getOAuthAccessToken(mReqToken, oauthVerifier);
			mTwitter.setOAuthAccessToken(at);

			saveAccessToken(at);

			// Set the content view back after we changed to a webview
			setContentView(R.layout.list_books);
			
		} catch (TwitterException e) {
			Toast.makeText(this, "Twitter auth error x01, try again later", Toast.LENGTH_SHORT).show();
		}
	}


	/**
	 * Send a tweet on your timeline, with a Toast msg for success or failure
	 */
	private void tweetMessage() {
		try {
			mTwitter.updateStatus("Test - É o BookPartner nas redes sociais!");

			Toast.makeText(this, "Tweet Successful!", Toast.LENGTH_SHORT).show();
		} catch (TwitterException e) {
			Toast.makeText(this, "Tweet error, try again later", Toast.LENGTH_SHORT).show();
		}
	}

	private void saveAccessToken(AccessToken at) {
		String token = at.getToken();
		String secret = at.getTokenSecret();
		Editor editor = mPrefs.edit();
		editor.putString(PREF_ACCESS_TOKEN, token);
		editor.putString(PREF_ACCESS_TOKEN_SECRET, secret);
		editor.commit();
	}
}