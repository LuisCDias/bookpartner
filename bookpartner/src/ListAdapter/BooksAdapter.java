package ListAdapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import fe.up.pt.partner.R;

public class BooksAdapter extends BaseAdapter {
    
    private Activity activity;
    private ArrayList<String> titles;
    private ArrayList<String> texts;
    private ArrayList<String> images;
    private ArrayList<String> authors;
    
    private static LayoutInflater inflater=null;

    public ImageLoader imageLoader; 
    
    //BooksAdapter(getActivity(), titles, texts, images, authors)
    public BooksAdapter(Activity a, ArrayList<String> tlt, ArrayList<String> txt, ArrayList<String> i, ArrayList<String> au) {
    	
        activity = a;
        titles=tlt;
        texts=txt;
        images=i;
        authors = au;
        
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
            vi = inflater.inflate(R.layout.book_view, null);
     
        //TextView text=(TextView)vi.findViewById(R.id.);
       // TextView author=(TextView)vi.findViewById(R.id.book_author);
        
        //ImageView image=(ImageView)vi.findViewById(R.id.book_cover);
      	
       // author.setText(authors.get(position));
        //title.setText(titles.get(position));
        
      //  imageLoader.DisplayImage(images.get(position), image, "book");

        return vi;
    }
}
	