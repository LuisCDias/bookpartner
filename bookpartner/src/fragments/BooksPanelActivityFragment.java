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

	public static class BooksPanelActivityFragmentAux extends SherlockListFragment {

		private static ArrayList<String> titles;
		private ArrayList<String> ids;
		private static ArrayList<String> authors;
		private ArrayList<String> page_counts;
		private static ArrayList<String> ratings;
		private ArrayList<String> covers;
		private ArrayList<String> covers_hd;
		private ArrayList<String> descriptions;

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
					covers_hd = new ArrayList<String>();
					descriptions = new ArrayList<String>();


					try {

						ids.add(book.getString("id"));
						JSONObject volumeInfo = book.getJSONObject("volumeInfo");
						titles.add(volumeInfo.getString("title"));

						JSONArray authors_array = null;

						if(volumeInfo.has("authors")){
							authors_array = volumeInfo.getJSONArray("authors");
							/*mais do que um author? tratar no webservice, para já placeholder com o primeiro encontrado*/
							authors.add(authors_array.get(0).toString());
						}
						else
							authors.add(PartnerAPI.Strings.NO_AUTHOR_AVAILABLE);


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
						if(volumeInfo.has("description")){
							String descripton_clean = cleanDescriptionHtml(volumeInfo.getString("description"));
							descriptions.add(descripton_clean);
						}
						else
							descriptions.add(PartnerAPI.Strings.NO_DESCRIPTION_AVAILABLE);

						JSONObject image_links = null;
						if(volumeInfo.has("imageLinks"))
							image_links = volumeInfo.getJSONObject("imageLinks");

						if(image_links != null){

							if(image_links.has("thumbnail")) 
								covers.add(image_links.getString("thumbnail"));
							else
								covers.add(PartnerAPI.Strings.NO_COVER_AVAILABLE);
							
							if(image_links.has("medium"))
								covers_hd.add(image_links.getString("medium"));
							else
								covers_hd.add(PartnerAPI.Strings.NO_COVER_AVAILABLE);
						}
						else
							covers.add(PartnerAPI.Strings.NO_COVER_AVAILABLE);


					} catch (JSONException e) {
						e.printStackTrace();
					}


					setListAdapter(new BookAdapter(getActivity(), titles, ids, authors, ratings, page_counts, covers, descriptions));

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
			searchIt("https://www.googleapis.com/books/v1/volumes/"+id+"?key="+PartnerAPI.APIkeys.GOOGLE_BOOKS_KEY);


		}

		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {

		}

		
		public static void shareTwitter(View v, Context ctx){

			//buttonLogin(v);
			//buttonTweet(v);
			//Intent intent = new Intent(this, TwitterActivity.class );
			Intent intent = new Intent(ctx, ComposeMessagePopUpActivity.class );
			
			intent.putExtra("title", titles.get(0));
			intent.putExtra("author", authors.get(0));
			intent.putExtra("rating", ratings.get(0));
			
			Log.d("title", titles.get(0));
			Log.d("author", authors.get(0));
			Log.d("rating", ratings.get(0));
			
			ctx.startActivity(intent);
		}
		
		/*@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

			Bundle b= super.getArguments();

			String id = b.getString("id");
			String author= b.getString("author");
			String page_count= b.getString("page_count");
			String rating= b.getString("rating");
			String cover= b.getString("cover");
			String description = b.getString("description");

			searchIt("https://www.googleapis.com/books/v1/volumes/"+id+"?key="+PartnerAPI.APIkeys.GOOGLE_BOOKS_KEY);

			//View v = inflater.inflate(R.layout.book_view, container, false);

			ImageView book_cover =(ImageView)v.findViewById(R.id.book_cover);
			TextView book_author = (TextView) v.findViewById(R.id.book_author);
			TextView book_page_count = (TextView) v.findViewById(R.id.book_page_count);
			//TextView book_rating = (TextView) v.findViewById(R.id.book_rating); //placeholder
			TextView book_summary = (TextView) v.findViewById(R.id.book_summary);

			RatingBar book_rating_bar = (RatingBar) v.findViewById(R.id.book_rating_bar);
			TextView book_rating_text = (TextView) v.findViewById(R.id.book_rating_text);

			if(!rating.equals(PartnerAPI.Strings.NO_RATING_AVAILABLE)){
				book_rating_bar.setRating(Float.parseFloat(rating));
				book_rating_bar.setVisibility(View.VISIBLE);
				book_rating_text.setVisibility(View.GONE);
			}
			else
			{
				book_rating_text.setText(rating);
				book_rating_bar.setVisibility(View.GONE);
				book_rating_text.setVisibility(View.VISIBLE);
			}

			book_author.setText(author);
			book_page_count.setText(page_count+" pages");
			//book_rating.setText(rating);
			//book_rating_bar.setRating(Float.parseFloat(rating));
			book_summary.setText(description);

			imageLoader=new ImageLoader(this.getActivity().getApplicationContext());
			imageLoader.DisplayImage(cover, book_cover,"thumbnail");

			return v;
		}*/
	}
}