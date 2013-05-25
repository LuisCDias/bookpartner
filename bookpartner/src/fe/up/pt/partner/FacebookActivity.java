/**
 * Copyright 2010-present Facebook.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fe.up.pt.partner;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.*;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.facebook.widget.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class FacebookActivity extends FragmentActivity {

	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");

	private final String PENDING_ACTION_BUNDLE_KEY = "fe.up.pt.partner:PendingAction";

	private EditText text_box;
	private Button postStatusUpdateButton;
	private Button cancelButton;

	private LoginButton loginButton;
	private ProfilePictureView profilePictureView;
	private PendingAction pendingAction = PendingAction.NONE;
	private ViewGroup controlsContainer;
	private GraphUser user;

	public String title;
	public String author;
	public String rating;
	public String texto;
	Bundle b;

	private enum PendingAction {
		NONE,
		POST_STATUS_UPDATE
	}
	private UiLifecycleHelper uiHelper;

	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) { 

		super.onCreate(savedInstanceState);
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);

		Bundle b= getIntent().getExtras();
		title = b.getString("title");
		author = b.getString("author");
		rating = b.getString("rating");

		/*
		 * este bloco é para imprimir a ash para depois se colocar no
		 * site da app no facebook 

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "fe.up.pt.partner", 
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                }
        } catch (NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        } */


		if (savedInstanceState != null) {
			String name = savedInstanceState.getString(PENDING_ACTION_BUNDLE_KEY);
			pendingAction = PendingAction.valueOf(name);
		}

		setContentView(R.layout.main);

		loginButton = (LoginButton) findViewById(R.id.login_button);
		loginButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
			@Override
			public void onUserInfoFetched(GraphUser user) {
				FacebookActivity.this.user = user;
				updateUI();
				// It's possible that we were waiting for this.user to be populated in order to post a
				// status update.
				handlePendingAction();

			}
		});

		profilePictureView = (ProfilePictureView) findViewById(R.id.profilePicture);
		text_box = (EditText) findViewById(R.id.share_text);
		postStatusUpdateButton = (Button) findViewById(R.id.postStatusUpdateButton);
		cancelButton = (Button) findViewById(R.id.cancel_button);

		postStatusUpdateButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				onClickPostStatusUpdate();

			}
		});
		cancelButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				finish();

			}
		});


		controlsContainer = (ViewGroup) findViewById(R.id.main_ui_container);

		final FragmentManager fm = getSupportFragmentManager();


		// Listen for changes in the back stack so we know if a fragment got popped off because the user
		// clicked the back button.
		fm.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
			@Override
			public void onBackStackChanged() {
				if (fm.getBackStackEntryCount() == 0) {
					// We need to re-show our UI.
					controlsContainer.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		uiHelper.onResume();

		updateUI();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);

		outState.putString(PENDING_ACTION_BUNDLE_KEY, pendingAction.name());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		if (pendingAction != PendingAction.NONE &&
				(exception instanceof FacebookOperationCanceledException ||
						exception instanceof FacebookAuthorizationException)) {
			new AlertDialog.Builder(FacebookActivity.this)
			.setTitle("Cancelled")
			.setMessage("Unable to perform selected action because permissions were not granted.")
			.setPositiveButton("OK", null)
			.show();
			pendingAction = PendingAction.NONE;
		} else if (state == SessionState.OPENED_TOKEN_UPDATED) {
			handlePendingAction();
		}
		updateUI();
	}

	private void updateUI() {
		Session session = Session.getActiveSession();
		boolean enableButtons = (session != null && session.isOpened());

		postStatusUpdateButton.setEnabled(enableButtons);
		if(enableButtons){

			postStatusUpdateButton.setVisibility(View.VISIBLE);
			cancelButton.setVisibility(View.VISIBLE);

			String hint = "Great book: "+title + " by "+ author + "! Average rating of "+rating;
			text_box.setHint(hint);
			text_box.setVisibility(View.VISIBLE);
		}
		else{
			postStatusUpdateButton.setVisibility(View.GONE);
			cancelButton.setVisibility(View.GONE);
			text_box.setVisibility(View.GONE);
		}

		if (enableButtons && user != null) {
			profilePictureView.setProfileId(user.getId());
		} else {
			profilePictureView.setProfileId(null);
		}
	}

	@SuppressWarnings("incomplete-switch")
	private void handlePendingAction() {
		PendingAction previouslyPendingAction = pendingAction;
		// These actions may re-set pendingAction if they are still pending, but we assume they
		// will succeed.
		pendingAction = PendingAction.NONE;

		switch (previouslyPendingAction) {
		case POST_STATUS_UPDATE:
			postStatusUpdate();
			break;
		}
	}

	private interface GraphObjectWithId extends GraphObject {
		String getId();
	}

	private void showPublishResult(String message, GraphObject result, FacebookRequestError error) {

		String title = null;
		String alertMessage = null;
		if (error == null) 
			alertMessage = "Sucessfully posted on timeline!";
		else
			alertMessage = "Share error, please try again";

		Toast.makeText(this, alertMessage, Toast.LENGTH_SHORT).show();
	}

	private void onClickPostStatusUpdate() {
		performPublish(PendingAction.POST_STATUS_UPDATE);
	}

	private void postStatusUpdate() {
		if (user != null && hasPublishPermission()) {
			//Alterar a mensagem 
			final String message;

			String texto = text_box.getText().toString();
			String hint = text_box.getHint().toString();        	

			if(texto.matches("")){
				message = hint;
			}
			else{
				String info = "["+ title + " by "+author+" ("+rating+")]";
				message  = texto+"\n"+info;
			}

			Request request = Request
					.newStatusUpdateRequest(Session.getActiveSession(), message, new Request.Callback() {

						@Override
						public void onCompleted(Response response) {
							showPublishResult(message, response.getGraphObject(), response.getError());

						}
					});
			request.executeAsync();
			Toast.makeText(this, "Sending...", Toast.LENGTH_SHORT).show();
			finish();
		} else {
			pendingAction = PendingAction.POST_STATUS_UPDATE;
		}
	}


	private boolean hasPublishPermission() {
		Session session = Session.getActiveSession();
		return session != null && session.getPermissions().contains("publish_actions");
	}

	private void performPublish(PendingAction action) {
		Session session = Session.getActiveSession();
		if (session != null) {
			pendingAction = action;
			if (hasPublishPermission()) {
				// We can do the action right away.
				handlePendingAction();
			} else {
				// We need to get new permissions, then complete the action when we get called back.
				session.requestNewPublishPermissions(new Session.NewPermissionsRequest(this, PERMISSIONS));
			}
		}
	}
}
