package fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import AsyncTasks.ResponseCommand;
import AsyncTasks.ResponseCommand.ERROR_TYPE;
import ListAdapter.ImageLoader;
import ListAdapter.ListAdapter;
import ListAdapter.BooksAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import fe.up.pt.partner.BooksPanelActivity;
import fe.up.pt.partner.PartnerAPI;
import fe.up.pt.partner.R;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListFragment;


public class BooksPanelActivityFragment extends SherlockFragmentActivity {
	
	public static ImageLoader imageLoader; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.book_view);
	}

	public static class BooksPanelActivityFragmentAux extends SherlockFragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			
			Bundle b= super.getArguments();
			
			String author= b.getString("author");
			String page_count= b.getString("page_count");
			String rating= b.getString("rating");
			String cover= b.getString("cover");
			String description = b.getString("description");


			View v = inflater.inflate(R.layout.book_view, container, false);

			ImageView book_cover =(ImageView)v.findViewById(R.id.book_cover);
			TextView book_author = (TextView) v.findViewById(R.id.book_author);
			TextView book_page_count = (TextView) v.findViewById(R.id.book_page_count);
			TextView book_rating = (TextView) v.findViewById(R.id.book_release_date); //placeholder
			TextView book_summary = (TextView) v.findViewById(R.id.book_summary);
			
			book_author.setText(author);
			book_page_count.setText(page_count+" pages");
			book_rating.setText(rating);
			book_summary.setText(description);
			
			imageLoader=new ImageLoader(this.getActivity().getApplicationContext());
			imageLoader.DisplayImage(cover, book_cover,"thumbnail");
			
			return v;
		}
	}
}