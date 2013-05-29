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

public class ReviewsAdapter extends BaseAdapter {
    
    private Activity activity;
    private ArrayList<String> reviewers;
    private ArrayList<String> dates;
    private ArrayList<String> texts;
    
    private static LayoutInflater inflater=null;
    
    public ReviewsAdapter(Activity a, ArrayList<String> rev, ArrayList<String> da, ArrayList<String> txts) {
        activity = a;
        reviewers=rev;
        dates = da;
        texts = txts;
        
        
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return reviewers.size();
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
            vi = inflater.inflate(R.layout.list_reviews, null);
     
       // TextView text=(TextView)vi.findViewById(R.id.user_offerText);
        TextView author = (TextView)vi.findViewById(R.id.review_author);
        TextView date = (TextView)vi.findViewById(R.id.review_date);
        TextView text = (TextView)vi.findViewById(R.id.review_text);
        
        author.setText(reviewers.get(position));
        date.setText(dates.get(position));
        text.setText(texts.get(position));
        
        return vi;
    }
}