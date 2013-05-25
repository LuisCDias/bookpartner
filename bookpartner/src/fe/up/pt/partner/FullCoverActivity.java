package fe.up.pt.partner;

import ListAdapter.ImageLoader;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

public class FullCoverActivity extends Activity {

	public static ImageLoader imageLoader; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_full_cover);
		
		Bundle extras = getIntent().getExtras();
		
		String cover = extras.getString("cover");
		String title = extras.getString("title");
		
		this.setTitle(title);
		
		ImageView book_cover =(ImageView)findViewById(R.id.full_book_cover);
		imageLoader=new ImageLoader(this.getApplicationContext());
		imageLoader.DisplayImage(cover, book_cover,"thumbnail");
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.full_cover, menu);
		return true;
	}

	public void finishFullCover(View v){
		this.finish();
	}
}
