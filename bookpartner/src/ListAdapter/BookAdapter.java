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
    
    private static LayoutInflater inflater=null;

    public ImageLoader imageLoader; 
    
    //BookAdapter(getActivity(), titles, ids, authors, ratings, page_counts, covers));

    public BookAdapter(Activity a, ArrayList<String> tlt,ArrayList<String> id, ArrayList<String> aut, 
    		ArrayList<String> rat, ArrayList<String> pag, ArrayList<String> cov, ArrayList<String> des) {
    	
        activity = a;
        titles=tlt;
        ids=id;
        authors=aut;
        ratings = rat;
        page_counts = pag;
        covers = cov;
        descriptions = des;
        
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
		
		RatingBar book_rating_bar = (RatingBar) vi.findViewById(R.id.book_rating_bar);
		TextView book_rating_text = (TextView) vi.findViewById(R.id.book_rating_text);
		
		if(!ratings.get(position).equals(PartnerAPI.Strings.NO_RATING_AVAILABLE)){
			book_rating_bar.setRating(Float.parseFloat(ratings.get(position)));
			book_rating_bar.setVisibility(View.VISIBLE);
			book_rating_text.setVisibility(View.GONE);
		}
		else
		{
			book_rating_text.setText(ratings.get(position));
			book_rating_bar.setVisibility(View.GONE);
			book_rating_text.setVisibility(View.VISIBLE);
		}
		
		book_author.setText(authors.get(position));
		book_page_count.setText(page_counts.get(position)+" pages");
		book_summary.setText(descriptions.get(position));
		
		imageLoader.DisplayImage(covers.get(position), book_cover,"thumbnail");

        return vi;
    }
}
	