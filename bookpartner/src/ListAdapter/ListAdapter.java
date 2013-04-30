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
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    
    public ListAdapter(Activity a, ArrayList<String> tlt) {
        activity = a;
        titles=tlt;
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

        TextView title = (TextView)vi.findViewById(R.id.user_offerTitle);

        title.setText(titles.get(position));

        return vi;
    }
}