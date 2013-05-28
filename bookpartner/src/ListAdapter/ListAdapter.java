package ListAdapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import fe.up.pt.partner.PartnerAPI;
import fe.up.pt.partner.R;

public class ListAdapter extends BaseAdapter {
    
    private Activity activity;
    private ArrayList<String> titles;
    private ArrayList<String> ids;
    private ArrayList<String> authors;
	private ArrayList<String> ratings;
	private ArrayList<String> covers;
    
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    
    public ListAdapter(Activity a, ArrayList<String> tit, ArrayList<String> i, ArrayList<String> au,
    				   ArrayList<String> rat, ArrayList<String> cov) {
        activity = a;
        titles=tit;
        ids = i;
        authors = au;
        ratings = rat;
        covers = cov;
        
        
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return titles.size();
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
            vi = inflater.inflate(R.layout.list_books, null);
     
       // TextView text=(TextView)vi.findViewById(R.id.user_offerText);
        ImageView cover =(ImageView)vi.findViewById(R.id.book_cover);
        TextView title = (TextView)vi.findViewById(R.id.book_title);
        TextView author = (TextView)vi.findViewById(R.id.book_author);
        TextView rating_text = (TextView)vi.findViewById(R.id.rating_star_text);
        
        title.setText(titles.get(position));
        
        if(position<authors.size())
        	author.setText("by "+authors.get(position));
        
        if(position<covers.size())
        	imageLoader.DisplayImage(covers.get(position), cover,"thumbnail");
        
        if(position<ratings.size())
	        if(ratings.get(position).equals(PartnerAPI.Strings.NO_RATING_AVAILABLE))
	        	rating_text.setText("N/A");
	        else
	        	rating_text.setText(ratings.get(position));
        
        return vi;
    }
}