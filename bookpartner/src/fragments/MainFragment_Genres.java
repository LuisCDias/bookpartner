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

		private ArrayList<String> genres;

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			
			super.onActivityCreated(savedInstanceState);	
			genres = new ArrayList<String>();
			// remove divider
			this.getListView().setDividerHeight(0);

			
			
			
			
			genres.add("Art,Adventure,Biography");
			genres.add("Business,Children,Christian");
			genres.add("Classics,Comics,Contemporary");
			genres.add("Cookbooks,Crime,Drama");
			genres.add("Ebooks,Fantasy,Fiction");
			genres.add("Horror,History,Humor");
			genres.add("Music,Mystery,Paranormal");
			genres.add("Psychology,Religion,Romance");
			genres.add("Sports,Science,Suspense");
			genres.add("Terror,Thriller,Travel");
			

			/*TODO ADICIONAR MAIS GENEROS!*/
			
			setListAdapter(new GenresListAdapter(getActivity(),genres));

		}	
	}
}