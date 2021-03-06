package fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fe.up.pt.partner.PartnerAPI;
import fe.up.pt.partner.R;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

/**
 * Demonstration of using ListFragment to show a list of items
 * from a canned array.
 */
public class MainFragment extends SherlockFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);

	}

	//TODO: Esta actividade vai ser um ListFragment para listar os livros vistos mais recentemente
	public static class MainFragmentAux extends SherlockFragment {
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			Bundle b= super.getArguments();
			//get values from bundles
			
		
			String book = b.getString("book");
			

			View v = inflater.inflate(R.layout.activity_main_menu, container, false);

			TextView userNameTextView = (TextView) v.findViewById(R.id.mysixText);
			userNameTextView.setText(book);

			return v;
		}
	}
}
