package fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import AsyncTasks.ResponseCommand;
import AsyncTasks.ResponseCommand.ERROR_TYPE;
import ListAdapter.ListAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import fe.up.pt.partner.BooksPanelActivity;
import fe.up.pt.partner.PartnerAPI;
import fe.up.pt.partner.R;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListFragment;


public class SimilarFragment extends SherlockFragmentActivity {
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setTheme(R.style.Theme_Sherlock);
		// Create the list fragment and add it as our sole content.
		if (getSupportFragmentManager().findFragmentById(android.R.id.content) == null) {
			SimilarFragmentAux list = new SimilarFragmentAux();
			getSupportFragmentManager().beginTransaction()
			.add(android.R.id.content, list).commit();
		}

	}

	public static class SimilarFragmentAux extends SherlockListFragment {

		private ArrayList<String> titles;
		private ArrayList<String> ids;
		private ArrayList<String> authors;
		
		Bundle b;


		private void searchIt(String URL) {
			Log.d("url", URL);
			getActivity().getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

			PartnerAPI.requestURL(URL, new ResponseCommand() {
				
				public void onResultReceived(Object... results) {

					Log.d("json", results[0].toString());
					JSONObject book = (JSONObject) results[0];

					authors = new ArrayList<String>();
					titles = new ArrayList<String>();
					ids = new ArrayList<String>();

					int i = 0;

					try {
						
						JSONArray items = book.getJSONArray("items");
						while (!items.isNull(i)) {

							JSONObject item = items.getJSONObject(i);
							
							ids.add(item.getString("id"));
							JSONObject volumeInfo = item.getJSONObject("volumeInfo");
							titles.add(volumeInfo.getString("title"));
							
							JSONArray authors_array = volumeInfo.getJSONArray("authors");
							/*mais do que um author? tratar no webservice, para já placeholder com o primeiro encontrado*/
							authors.add(authors_array.get(0).toString());
							
							Log.d("author",authors_array.get(0).toString());
							i++;
						}
						

					} catch (JSONException e) {
						e.printStackTrace();
					}

					
					//setListAdapter(new ListAdapter(getActivity(), titles, ids, authors));

				}

				@Override
				public void onError(ERROR_TYPE error) {
					
					if(error.toString().equals(ERROR_TYPE.NETWORK))
						Toast.makeText(getActivity(), PartnerAPI.Strings.SERVER_CONNECTION,
								Toast.LENGTH_LONG).show();
					else if(error.toString().equals(ERROR_TYPE.GENERAL))
						Toast.makeText(getActivity(), PartnerAPI.Strings.CHECK_CONNECTION,
								Toast.LENGTH_LONG).show();
				}
			});
			// }
	}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			
			super.onActivityCreated(savedInstanceState);
			
			b= super.getArguments();
			
			
			// remove divider
			this.getListView().setDividerHeight(0);

			//AsyncTasks to search something
//			searchIt("https://www.googleapis.com/books/v1/volumes?q=magician&key="+PartnerAPI.APIkeys.GOOGLE_BOOKS_KEY);


		}

		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			
						
			String id_book = ids.get(position);
			String title_book = titles.get(position);
			String author_book = authors.get(position);
			Intent intent = new Intent(this.getSherlockActivity(), BooksPanelActivity.class );

			intent.putExtra("id", id_book);
			intent.putExtra("title", title_book);
			intent.putExtra("author", author_book);
			//include the user id
			//intent.putExtra(PartnerAPI.Strings.USE_MODE_BUNDLE, useMode);
			
			startActivity(intent);

		}
	}
}