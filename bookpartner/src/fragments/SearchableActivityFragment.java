package fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import AsyncTasks.ResponseCommand;
import ListAdapter.ListAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import fe.up.pt.partner.BooksPanelActivity;
import fe.up.pt.partner.PartnerAPI;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListFragment;


public class SearchableActivityFragment extends SherlockFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_browse);

		// Create the list fragment and add it as our sole content.
		if (getSupportFragmentManager().findFragmentById(android.R.id.content) == null) {
			SearchableActivityFragmentAux list = new SearchableActivityFragmentAux();
			getSupportFragmentManager().beginTransaction()
			.add(android.R.id.content, list).commit();
		}
	}

	public static class SearchableActivityFragmentAux extends SherlockListFragment {


		//public String useMode;
		//public String userToken;
		private ArrayList<String> titles;
		private ArrayList<String> ids;
		private ArrayList<String> authors;
		private ArrayList<String> ratings;
		private ArrayList<String> covers;
		// private Bundle extras ;



		private void searchForBook(String URL) {

			Log.d("SearchURL", URL);
			getActivity().getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

			PartnerAPI.requestURL(URL, new ResponseCommand() {

				public void onResultReceived(Object... results) {
					
					Log.d("json", results[0].toString());
					//JSONObject book = (JSONObject) results[0];
					JSONArray items = (JSONArray) results[0];
					authors = new ArrayList<String>();
					titles = new ArrayList<String>();
					ids = new ArrayList<String>();
					ratings = new ArrayList<String>();
					covers = new ArrayList<String>();

					int i = 0;

					try {

						//JSONArray items = book.getJSONArray("items");
						while (!items.isNull(i)){


							JSONObject item = items.getJSONObject(i);

							ids.add(item.getString("id"));
							//JSONObject volumeInfo = item.getJSONObject("volumeInfo");
							titles.add(item.getString("title"));

							JSONArray authors_array = new JSONArray();

							if(!item.getString("authors").equals("null")){
								authors_array = item.getJSONArray("authors");
								authors.add(authors_array.get(0).toString()); //TODO eventualmente ter vários autores
							}
							else
								authors.add(PartnerAPI.Strings.NO_AUTHOR_AVAILABLE);
							
							if(!item.getString("averageRating").equals("null"))
								ratings.add(item.getString("averageRating"));
							else
								ratings.add(PartnerAPI.Strings.NO_RATING_AVAILABLE);
							

							if(!item.getString("cover").equals("null"))
								covers.add(item.getString("cover"));
							else
								covers.add("no cover");

							i++;
						}


					} catch (JSONException e) {
						e.printStackTrace();
					}


					setListAdapter(new ListAdapter(getActivity(), titles, ids, authors, ratings, covers));

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
			// remove divider
			this.getListView().setDividerHeight(0);

			//get parent extras
			Bundle b= super.getArguments();

			String terms = b.getString("search_query");
			String api_ready_terms = terms.replace(" ", "+");
			String order = b.getString("order");
			
			if(b.containsKey("order"))
				searchForBook("http://bookpartnerapi.herokuapp.com/books?q="+api_ready_terms+"&ord="+order);
			else
				searchForBook("http://bookpartnerapi.herokuapp.com/books?q="+api_ready_terms);

		}

		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {

			String id_book = ids.get(position);
			String title_book = titles.get(position);
			String author_book = authors.get(position);
			String rating_book = ratings.get(position);
			String cover_book = covers.get(position);
			
			Intent intent = new Intent(this.getSherlockActivity(), BooksPanelActivity.class );

			intent.putExtra("id", id_book);
			intent.putExtra("title", title_book);
			intent.putExtra("author", author_book);
			intent.putExtra("rating", rating_book);
			intent.putExtra("cover", cover_book);
			
			//include the user id
			//intent.putExtra(PartnerAPI.Strings.USE_MODE_BUNDLE, useMode);
			
			startActivity(intent);	


		}


	}
}