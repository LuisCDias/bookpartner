package ListAdapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import fe.up.pt.partner.PartnerAPI;
import fe.up.pt.partner.R;

public class BookAdapter extends BaseAdapter {
    
    private Activity activity;
    private ArrayList<String> titles;
    private ArrayList<String> ids;
    private ArrayList<String> authors;
    private ArrayList<String> ratings;
    private ArrayList<String> covers;
    private ArrayList<String> page_counts;
    private ArrayList<String> descriptions;
    private ArrayList<String> dates;
    
    private static LayoutInflater inflater=null;

    public ImageLoader imageLoader; 
    
    //BookAdapter(getActivity(), titles, ids, authors, ratings, page_counts, covers));

    public BookAdapter(Activity a, ArrayList<String> tlt,ArrayList<String> id, ArrayList<String> aut, 
    		ArrayList<String> rat, ArrayList<String> pag, ArrayList<String> cov, ArrayList<String> des, ArrayList<String> dat) {
    	
        activity = a;
        titles=tlt;
        ids=id;
        authors=aut;
        ratings = rat;
        page_counts = pag;
        covers = cov;
        descriptions = des;
        dates = dat;
        
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return ids.size(); 
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.book_view, null);
     
        Log.d("Position", position+"");
       
        ImageView book_cover =(ImageView)vi.findViewById(R.id.book_cover);
		TextView book_author = (TextView) vi.findViewById(R.id.book_author);
		TextView book_page_count = (TextView) vi.findViewById(R.id.book_page_count);
		//TextView book_rating = (TextView) v.findViewById(R.id.book_rating); //placeholder
		TextView book_summary = (TextView) vi.findViewById(R.id.book_summary);
		TextView publish_date = (TextView) vi.findViewById(R.id.book_publish_date);
		
		RatingBar book_rating_bar = (RatingBar) vi.findViewById(R.id.book_rating_bar);
		TextView book_rating_text = (TextView) vi.findViewById(R.id.book_rating_text);
		LinearLayout google_rating_layout = (LinearLayout) vi.findViewById(R.id.google_rating_layout);
		LinearLayout goodreads_rating_layout = (LinearLayout) vi.findViewById(R.id.goodreads_rating_layout);
		LinearLayout overall_rating_layout = (LinearLayout) vi.findViewById(R.id.overall_rating_layout);
		
		
		/*SO MOSTRAR OS RATINGS AVAILABLE
		 * Neste momento apenas verifica o googlebooks, mas vamos ter de ter o rating
		 * para o goodreads e o nosso rating médio (x*g_books + y*goodreads)/(x+y)
		 * */
		
		if(!ratings.get(position).equals(PartnerAPI.Strings.NO_RATING_AVAILABLE)){
			book_rating_bar.setRating(Float.parseFloat(ratings.get(position)));
			google_rating_layout.setVisibility(View.VISIBLE);
			goodreads_rating_layout.setVisibility(View.VISIBLE);
			overall_rating_layout.setVisibility(View.VISIBLE);
			book_rating_text.setVisibility(View.GONE);
		}
		else
		{
			book_rating_text.setText(ratings.get(position));
			google_rating_layout.setVisibility(View.GONE);
			goodreads_rating_layout.setVisibility(View.GONE);
			overall_rating_layout.setVisibility(View.GONE);
			book_rating_text.setVisibility(View.VISIBLE);
		}
		
		if(!dates.get(position).equals(PartnerAPI.Strings.NOT_AVAILABLE))
			publish_date.setText(dates.get(position));
		else
			publish_date.setVisibility(View.GONE);
		
		book_author.setText(authors.get(position));
		book_page_count.setText(page_counts.get(position)+" pages");
		book_summary.setText(descriptions.get(position));
		
		imageLoader.DisplayImage(covers.get(position), book_cover,"thumbnail");

        return vi;
    }
}
	