package fragments;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

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


public class MainFragment_Top extends SherlockFragmentActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// Create the list fragment and add it as our sole content.
		if (getSupportFragmentManager().findFragmentById(android.R.id.content) == null) {
			MainFragment_Top_Aux list = new MainFragment_Top_Aux();
			getSupportFragmentManager().beginTransaction()
			.add(android.R.id.content, list).commit();
		}

	}

	public static class MainFragment_Top_Aux extends SherlockListFragment {

		private ArrayList<String> titles;
		private ArrayList<String> ids;
		private ArrayList<String> authors;
		private ArrayList<String> page_counts;
		private ArrayList<String> ratings;
		private ArrayList<String> covers;
		private ArrayList<String> descriptions;


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
					page_counts = new ArrayList<String>();
					ratings = new ArrayList<String>();
					covers = new ArrayList<String>();
					descriptions = new ArrayList<String>();

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

							if(volumeInfo.has("pageCount"))
								page_counts.add(volumeInfo.getString("pageCount"));
							else
								page_counts.add("N/A");
							if(volumeInfo.has("averageRating"))
								ratings.add(volumeInfo.getString("averageRating"));
							else
								ratings.add(PartnerAPI.Strings.NO_RATING_AVAILABLE);

							/* Para evitar o facto de poder vir com uma descrição vazia,
							 * ou não ter.*/
							if(volumeInfo.has("description"))
								descriptions.add(volumeInfo.getString("description"));
							else
								descriptions.add("N/A");

							JSONObject image_links = null;
							if(volumeInfo.has("imageLinks"))
								image_links = volumeInfo.getJSONObject("imageLinks");

							if(image_links != null){

								if(image_links.has("thumbnail")) 
									covers.add(image_links.getString("thumbnail"));
								else
									covers.add("no cover");
							}
							else
								covers.add("no cover");

							i++;
						}
						/*Todos os arrays estão preenchidos. Agora terão de ser ordenados por rating*/
						
						//TODO algoritmo streets of the nice para ordenar, não me apetece agora fazer e deve-se arranjar
						
						

					} catch (JSONException e) {
						e.printStackTrace();
					}


					setListAdapter(new ListAdapter(getActivity(), titles, ids, authors, ratings, page_counts, covers));

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
			searchIt("https://www.googleapis.com/books/v1/volumes?q=magician&key="+PartnerAPI.APIkeys.GOOGLE_BOOKS_KEY);


		}

		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {


			String id_book = ids.get(position);
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

		}
	}
}