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

import fe.up.pt.partner.OffersPanelActivity;
import fe.up.pt.partner.PartnerAPI;
import fe.up.pt.partner.R;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListFragment;


public class MainFragment_Results extends SherlockFragmentActivity {
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

		// Create the list fragment and add it as our sole content.
		if (getSupportFragmentManager().findFragmentById(android.R.id.content) == null) {
			MainFragment_Results_Aux list = new MainFragment_Results_Aux();
			getSupportFragmentManager().beginTransaction()
			.add(android.R.id.content, list).commit();
		}

	}

	public static class MainFragment_Results_Aux extends SherlockListFragment {

		private ArrayList<String> titles;
		private ArrayList<String> items;
		private ArrayList<String> ids;
		Bundle b;


		private void searchIt(String URL) {
			Log.d("url", URL);
			getActivity().getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

			PartnerAPI.requestURL(URL, new ResponseCommand() {
				
				public void onResultReceived(Object... results) {

					Log.d("json", results[0].toString());
					JSONObject book = (JSONObject) results[0];

					titles = new ArrayList<String>();
					items = new ArrayList<String>();
					ids = new ArrayList<String>();

					int i = 0;
					//while (!books.isNull(i)) {
					try {
						
						JSONArray items = book.getJSONArray("items");
						while (!items.isNull(i)) {

							JSONObject item = items.getJSONObject(i);
							JSONObject volumeInfo = item.getJSONObject("volumeInfo");
							titles.add(volumeInfo.getString("title"));
							i++;
						}
						

					} catch (JSONException e) {
						e.printStackTrace();
					}
					//i++;
					//}

					
					setListAdapter(new ListAdapter(getActivity(), titles));

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
			searchIt("https://www.googleapis.com/books/v1/volumes?q=magician&key=AIzaSyDGL3odpvv006ZEbgKTxN0_0R7ArRP_0qg");


		}

		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			
						
			/*String id_offer = ids.get(position);
			String title_offer = titles.get(position);
			String owner_offer = owners.get(position);
			Intent intent = new Intent(this.getSherlockActivity(), OffersPanelActivity.class );

			intent.putExtra("id", id_offer);
			intent.putExtra("title", title_offer);
			intent.putExtra("owner", owner_offer);
			//include de user id
			intent.putExtra(PartnerAPI.Strings.USE_MODE_BUNDLE, useMode);
			

			startActivity(intent);*/


		}
	}
}