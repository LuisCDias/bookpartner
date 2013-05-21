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

public class GenresListAdapter extends BaseAdapter {
    
    private Activity activity;
    private ArrayList<String> genres;

    
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    
    public GenresListAdapter(Activity a, ArrayList<String> gen) {

    	activity = a;
        genres=gen;

        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return genres.size();
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
            vi = inflater.inflate(R.layout.list_genres_grid, null);
 
    	TextView grid_1_text = (TextView)vi.findViewById(R.id.genre_title_1);
    	TextView grid_2_text = (TextView)vi.findViewById(R.id.genre_title_2);
    	TextView grid_3_text = (TextView)vi.findViewById(R.id.genre_title_3);

    	String grid_1 = genres.get(position).substring(0, genres.get(position).indexOf(','));
    	String resto = genres.get(position).substring(genres.get(position).indexOf(',')+1, genres.get(position).length());
    	
    	String grid_2 = resto.substring(0, resto.indexOf(',')); 	
    	resto = resto.substring(resto.indexOf(',')+1,resto.length());
    	
    	
    	grid_1_text.setText(grid_1);
    	grid_2_text.setText(grid_2);
    	grid_3_text.setText(resto);

        	
        
        return vi;
    }
}