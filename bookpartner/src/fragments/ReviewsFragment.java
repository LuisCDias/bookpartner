package fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import AsyncTasks.ResponseCommand;
import AsyncTasks.ResponseCommand.ERROR_TYPE;
import ListAdapter.EmptyAdapter;
import ListAdapter.ListAdapter;
import ListAdapter.ReviewsAdapter;
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


public class ReviewsFragment extends SherlockFragmentActivity {
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setTheme(R.style.Theme_Sherlock);
		// Create the list fragment and add it as our sole content.
		if (getSupportFragmentManager().findFragmentById(android.R.id.content) == null) {
			ReviewsFragmentAux list = new ReviewsFragmentAux();
			getSupportFragmentManager().beginTransaction()
			.add(android.R.id.content, list).commit();
		}

	}

	public static class ReviewsFragmentAux extends SherlockListFragment {

		private ArrayList<String> reviewers;
		private ArrayList<String> dates;
		private ArrayList<String> texts;
		

		private void searchIt(String URL) {
			Log.d("url", URL);
			getActivity().getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

			PartnerAPI.requestURL(URL, new ResponseCommand() {
				
				public void onResultReceived(Object... results) {

					Log.d("REVIEW", results[0].toString());
					JSONArray items2 = (JSONArray) results[0];
					
					reviewers = new ArrayList<String>();
					dates = new ArrayList<String>();
					texts = new ArrayList<String>();

					int i = 0;
					
					try {
						
						JSONArray items = (JSONArray) results[0];
						while (!items.isNull(i)) {

							JSONObject item = items.getJSONObject(i);
							
							reviewers.add(item.getString("reviewer"));
							dates.add(item.getString("date"));
							texts.add(item.getString("text"));
							i++;
						}
						

					} catch (JSONException e) {
						e.printStackTrace();
					}
					
					if(items2.length() == 0)
						setListAdapter(new EmptyAdapter(getActivity(), 1));
					else
						setListAdapter(new ReviewsAdapter(getActivity(), reviewers, dates, texts));

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

		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);


			Bundle b= super.getArguments();
			String id = b.getString("id");

			// remove divider
			this.getListView().setDividerHeight(0);

			//AsyncTasks to search something
			searchIt("http://bookpartnerapi.herokuapp.com/book/"+id+"/reviews");

		}

		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			
						
			/*String id_book = ids.get(position);
			String title_book = titles.get(position);
			String author_book = authors.get(position);
			Intent intent = new Intent(this.getSherlockActivity(), BooksPanelActivity.class );

			intent.putExtra("id", id_book);
			intent.putExtra("title", title_book);
			intent.putExtra("author", author_book);
			//include the user id
			//intent.putExtra(PartnerAPI.Strings.USE_MODE_BUNDLE, useMode);
			
			startActivity(intent);
			 */
		}
	}
}