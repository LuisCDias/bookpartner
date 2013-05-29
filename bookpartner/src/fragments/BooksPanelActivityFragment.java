package fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import AsyncTasks.ResponseCommand;
import AsyncTasks.ResponseCommand.ERROR_TYPE;
import ListAdapter.BookAdapter;
import ListAdapter.ImageLoader;
import ListAdapter.ListAdapter;
import ListAdapter.BooksAdapter;
import android.app.Activity;
import android.content.Context;
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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import fe.up.pt.partner.BooksPanelActivity;
import fe.up.pt.partner.ComposeMessagePopUpActivity;
import fe.up.pt.partner.FacebookActivity;
import fe.up.pt.partner.PartnerAPI;
import fe.up.pt.partner.R;
import fragments.MainFragment_Recent.MainFragment_Recent_Aux;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListFragment;


public class BooksPanelActivityFragment extends SherlockFragmentActivity {

	public static ImageLoader imageLoader; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		// Create the list fragment and add it as our sole content.		
		if (getSupportFragmentManager().findFragmentById(android.R.id.content) == null) {
			BooksPanelActivityFragmentAux list = new BooksPanelActivityFragmentAux();
			getSupportFragmentManager().beginTransaction()
			.add(android.R.id.content, list).commit();
		}
				
	}

	public static class BooksPanelActivityFragmentAux extends SherlockListFragment {

		private static ArrayList<String> titles;
		private ArrayList<String> ids;
		private static ArrayList<String> authors;
		private ArrayList<String> page_counts;
		private  ArrayList<String> google_ratings;
		private  ArrayList<String> goodreads_ratings;
		private static ArrayList<String> bookpartner_ratings;
		private ArrayList<String> covers;
		private ArrayList<String> covers_hd;
		private ArrayList<String> descriptions;
		private ArrayList<String> dates;

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
					google_ratings = new ArrayList<String>();
					goodreads_ratings = new ArrayList<String>();
					bookpartner_ratings = new ArrayList<String>();
					covers = new ArrayList<String>();
					covers_hd = new ArrayList<String>();
					descriptions = new ArrayList<String>();
					dates = new ArrayList<String>();	

					try {

						ids.add(book.getString("id"));
						//JSONObject volumeInfo = book.getJSONObject("volumeInfo");
						titles.add(book.getString("title"));

						JSONArray authors_array = null;

						if(!book.getString("authors").equals("null")){
							authors_array = book.getJSONArray("authors");
							/*mais do que um author? tratar no webservice, para já placeholder com o primeiro encontrado*/
							authors.add(authors_array.get(0).toString());
						}
						else
							authors.add(PartnerAPI.Strings.NO_AUTHOR_AVAILABLE);


						if(!book.getString("pageCount").equals("null"))
							page_counts.add(book.getString("pageCount"));
						else
							page_counts.add("N/A");
						
						if(!book.getString("googleRating").equals("null"))
							google_ratings.add(book.getString("googleRating"));
						else
							google_ratings.add(PartnerAPI.Strings.NO_RATING_AVAILABLE);
						
						if(!book.getString("goodReadsRating").equals("null"))
							goodreads_ratings.add(book.getString("goodReadsRating"));
						else
							goodreads_ratings.add(PartnerAPI.Strings.NO_RATING_AVAILABLE);
						
						if(!book.getString("ourRating").equals("null"))
							bookpartner_ratings.add(book.getString("ourRating"));
						else
							bookpartner_ratings.add(PartnerAPI.Strings.NO_RATING_AVAILABLE);
						
						if(!book.getString("publishedDate").equals("null"))
							dates.add(book.getString("publishedDate"));
						else
							dates.add(PartnerAPI.Strings.NOT_AVAILABLE);
						
						/* Para evitar o facto de poder vir com uma descrição vazia,
						 * ou não ter.*/
						if(!book.getString("description").equals("null")){
							String descripton_clean = cleanDescriptionHtml(book.getString("description"));
							descriptions.add(descripton_clean);
						}
						else
							descriptions.add(PartnerAPI.Strings.NO_DESCRIPTION_AVAILABLE);

						
						if(!book.getString("cover").equals("null")) 
							covers.add(book.getString("cover"));
						else
							covers.add(PartnerAPI.Strings.NO_COVER_AVAILABLE);
						
						if(!book.getString("largeCorver").equals("null"))
							covers_hd.add(book.getString("largeCorver"));
						else
							covers_hd.add(PartnerAPI.Strings.NO_COVER_AVAILABLE);
					

					} catch (JSONException e) {
						e.printStackTrace();
					}


					setListAdapter(new BookAdapter(getActivity(), titles, ids, authors, google_ratings, 
							goodreads_ratings, bookpartner_ratings,page_counts, covers, descriptions, dates));

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

		public String cleanDescriptionHtml(String str){

			str = str.replace("<p>", "");
			str = str.replace("</p> ", "\n");
			str = str.replace("</p>", "\n");
			str = str.replace("<br>", "\n");
			str = str.replace("<i>", "");
			str = str.replace("</i>", "");
			str = str.replace("<b>", "");
			str = str.replace("</b>", "");

			return str;
		}
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);

			Bundle b= super.getArguments();
			String id = b.getString("id");

			// remove divider
			this.getListView().setDividerHeight(0);

			//AsyncTasks to search something
			searchIt("http://bookpartnerapi.herokuapp.com/book/"+id);

		}
		
		/*chamada indirectamente pelo onClick do twitter*/
		public static void shareFacebook(View v, Context ctx){
			
			//Intent intent = new Intent(ctx, FacebookActivity.class );
			Intent intent = new Intent(ctx, FacebookActivity.class );
			/*dados para enviar para o hint do tweet*/
			intent.putExtra("title", titles.get(0));
			intent.putExtra("author", authors.get(0));
			intent.putExtra("rating", bookpartner_ratings.get(0));;
			
			ctx.startActivity(intent);
		}
		
		/*chamada indirectamente pelo onClick do twitter*/
		public static void shareTwitter(View v, Context ctx){
			
			Intent intent = new Intent(ctx, ComposeMessagePopUpActivity.class );
			
			/*dados para enviar para o hint do tweet*/

			intent.putExtra("title", titles.get(0));
			intent.putExtra("author", authors.get(0));
			intent.putExtra("rating", bookpartner_ratings.get(0));;
			
			ctx.startActivity(intent);
		}
	}
}