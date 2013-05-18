package fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import AsyncTasks.ResponseCommand;
import AsyncTasks.ResponseCommand.ERROR_TYPE;
import ListAdapter.GenresListAdapter;
import ListAdapter.ListAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import fe.up.pt.partner.BooksPanelActivity;
import fe.up.pt.partner.FullCoverActivity;
import fe.up.pt.partner.PartnerAPI;
import fe.up.pt.partner.R;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListFragment;


public class MainFragment_Genres extends SherlockFragmentActivity {
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

		// Create the list fragment and add it as our sole content.
		if (getSupportFragmentManager().findFragmentById(android.R.id.content) == null) {
			MainFragment_Genres_Aux list = new MainFragment_Genres_Aux();
			getSupportFragmentManager().beginTransaction()
			.add(android.R.id.content, list).commit();
		}

	}

	public static class MainFragment_Genres_Aux extends SherlockListFragment {

		private ArrayList<String> genres = new ArrayList<String>();
		
		
		Bundle b;


		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			b= super.getArguments();
			
			
			// remove divider
			this.getListView().setDividerHeight(0);

			//AsyncTasks to search something
			//searchIt("https://www.googleapis.com/books/v1/volumes?q=a+song+of+ice+and+fire&key="+PartnerAPI.APIkeys.GOOGLE_BOOKS_KEY);
			genres.add("Drama");
			genres.add("Fantasia");
			setListAdapter(new GenresListAdapter(getActivity(),genres));


		}

		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			
			/*String id_book = ids.get(position);
			String title_book = titles.get(position);
			String author_book = authors.get(position);
			String pages_book = page_counts.get(position);
			String rating_book = ratings.get(position);
			String cover_book = covers.get(position);
			String description = descriptions.get(position);
			
			Intent intent = new Intent(this.getSherlockActivity(), BooksPanelActivity.class );

			intent.putExtra("id", id_book);
			intent.putExtra("title", title_book);
			intent.putExtra("author", author_book);
			intent.putExtra("page_count", pages_book);
			intent.putExtra("rating", rating_book);
			intent.putExtra("cover", cover_book);
			intent.putExtra("description", description);
			
			//include the user id
			//intent.putExtra(PartnerAPI.Strings.USE_MODE_BUNDLE, useMode);
			
			startActivity(intent);	
			 */
		}

	}
	/*public static void openFullCover(View v, Context ctx){
		
		switch(v.getId()){
		
		case R.id.book_cover:
			Log.d("BOOK", "COVER");
			
			Intent intent = new Intent(ctx, FullCoverActivity.class);
			v.get
			break;
	
		}
	}*/
}