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
    private ArrayList<String> page_counts;
	private ArrayList<String> ratings;
	private ArrayList<String> covers;
    
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    
    public ListAdapter(Activity a, ArrayList<String> tit, ArrayList<String> i, ArrayList<String> au,
    				   ArrayList<String> rat, ArrayList<String> pc, ArrayList<String> cov) {
        activity = a;
        titles=tit;
        ids = i;
        authors = au;
        page_counts = pc;
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

        title.setText(titles.get(position));
        author.setText("by "+authors.get(position));
        
        if(covers.get(position)!=null)
        	imageLoader.DisplayImage(covers.get(position), cover,"thumbnail");
        
        return vi;
    }
}