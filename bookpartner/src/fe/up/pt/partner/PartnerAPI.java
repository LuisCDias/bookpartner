package fe.up.pt.partner;

import AsyncTasks.ResponseCommand;
import AsyncTasks.URLRequest;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

public class PartnerAPI {

    public static final String PREFERENCES = "bookpartner";
    private static String username = "";
    private static String location = "";
    private static int search = 10;
    
    
    public interface APIkeys{
    	
    	String GOOGLE_BOOKS_KEY = "AIzaSyDGL3odpvv006ZEbgKTxN0_0R7ArRP_0qg";
    	String GOODREADS_KEY = "";
    }
    
    private interface ApplicationKey {
            String NAME = "app_key";
            String VALUE = "";
    }
    public interface Strings{
    	
    	String htmlBlack = "<font  color=black \\>";
    	String htmlGreen = "<font  color=green \\>";
    	String htmlRed= "<font  color=red \\>";
    	String htmlYellow = "<font  color=#FFA824 \\>";
    	String SERVER_URL = "http://localhost/"; //TODO REPLACE WITH WEBSERVER URL
    	String RATING_TAG = "Rating: ";
    	String GUEST_MODE = "GUEST_MODE";
    	String USER_MODE = "USER_MODE";
    	String USE_MODE_BUNDLE = "useMode";
    	String USER_TOKEN_BUNDLE = "userToken";
    	String USER_ID_BUNDLE = "userID";
    	String USER_NAME_BUNDLE = "userName";
    	String USER_RATING_BUNDLE = "userRating";
    	String NO_TOKEN = "NO_TOKEN";
    	String NO_USER_NAME= "NO_USER_NAME";
    	String NO_USER_RATING = "NO_USER_RATING";
    	String NO_USER_ID= "NO_USER_ID";
    	String ACCESS_TOKEN = "access_token";
    	String REFRESH_TOKEN = "refresh_token";
    	String EXPIRES_IN = "expires_in";
    	String CHECK_CONNECTION = "Please Verify Your Connection";
    	String SERVER_CONNECTION = "Server connection currently down";
    	String PLEASE_LOGIN = "Please login to use this Feature";
    	String LOGIN = "Login";
    	String LOGOUT = "Logout";
    	String NOT_IMPLEMENTED = "This feature is not available using Android app. Please use the Website for this";
    	String FLAG_ADAPTER_BUNDLER = "flagAdapter";
    	String LAUNCH_FLAG_BUNDLE = "launcheFlag";
    	String NO_RATING_AVAILABLE = "No rating available";
    	String NO_AUTHOR_AVAILABLE = "Unknown author";
    	String NO_COVER_AVAILABLE = "Thumbnail";
    	String NO_DESCRIPTION_AVAILABLE = "No description available";
    	
    	String NOT_AVAILABLE = "N/A";
    }
    
    public interface Ints{
    	
    	int ORDER_MINUTES_I = 0;
    	int ORDER_HOURS_I = 0;
    	int ORDER_MINUTES_F = 59;
    	int ORDER_HOURS_F = 23;
    	int NO_EDIT_BUTTON = 2;
    	int NO_USER_OFFERS_BUTTON = 1;
    	int YES_EDIT_BUTTON = 0;
    	int YES_USER_OFFERS_BUTTON = 0;
    	int LAUNCH_FLAG_MAIN_LOAD = 1;
    	
    	
    }

    public interface Search {
    	
            String KEYWORDS = "keywords";
            String LOCATION = "location";
            String DATE = "date";
            String CATEGORY = "category";
            String WITHIN = "within";
            String UNITS = "units";
            String COUNT_ONLY = "count_only";
            String SORT_ORDER = "sort_order";
            String SORT_DIRECTION = "sort_direction";
            String PAGE_SIZE = "page_size";
            String PAGE_NUMBER = "page_number";
            String MATURE = "mature";
            String INCLUDE = "include";
            
    }

    public interface SortOrder {
    	
            String RELEVANCE = "relevance";
            String POPULARITY = "popularity";
            String DATE = "date";
    }

    private static final String URL = "";//TODO REPLACE WITH WEBSERVER URL

    private static String userKey = null;
    private static String nonce = null;

    public static final String getURL() {
            return PartnerAPI.URL;
    }

    public static final String getAppKey() {
            return PartnerAPI.ApplicationKey.VALUE;
    }

    public static final void setUserKey(String userKey) {
    	PartnerAPI.userKey = userKey;
    }

    public static String getUserKey() {
            return userKey;
    }

    public static void setUsername(String s) {
    	PartnerAPI.username = s;
    }

    public static String getUsername() {
        return PartnerAPI.username;
    }

    public static void setLocation(String s) {
		PartnerAPI.location = s;
    }

    public static String getLocation() {
        return PartnerAPI.location;
    }

    public static void setNonce(String nonce) {
    	PartnerAPI.nonce = nonce;
    }

    public static String getNonce() {
        return PartnerAPI.nonce;
    }

    public static void setSearchNumber(int n) {
    	PartnerAPI.search = n;
    }

    public static int getSearchNumber() {
        return PartnerAPI.search;
    }

    /**
     * Requests a generic URL
     * 
     * @param command
     *            an implemented interface of {@link ResponseCommand}
     * @return an AsyncTask which returns an Object
     */
    public static AsyncTask<Object, Void, ResponseCommand.ERROR_TYPE> requestURL(
                    String URL, ResponseCommand command) {
       
          return new URLRequest(command, "Request URL").execute(PartnerAPI.getURL() + URL);

    }

}
