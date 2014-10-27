package mg.traumacare60;

import mg.traumacare60.NHActivity.ErrorDialogFragment;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Location;
import android.media.AudioManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;

public class MainActivity extends FragmentActivity implements
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener {
	private ListView listView1;
	private UiLifecycleHelper uiHelper;
	private final static int
    CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	private LocationClient mLocationClient;
	
    private Location mCurrentLocation;
    private TelephonyManager mTelephonyManager;
    private StatePhoneReceiver mPhoneStateListener;
    private boolean callFromApp=false; 
    private boolean callFromOffHook=false; 
    private boolean SI_IS_CCDSBR=false; //Source intent is CCDSBR
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Task task_data[] = new Task[]
				 {
				 new Task(R.drawable.placeholder, "Perform emergency tasks"),
				 new Task(R.drawable.placeholder, "Tips for managing road accidents"), 
				 new Task(R.drawable.placeholder, "Search for hospitals"),
				 new Task(R.drawable.placeholder, "Medical data"),
				 new Task(R.drawable.placeholder, "Car stopper"),
				 new Task(R.drawable.placeholder, "Camera tasks"),
				 new Task(R.drawable.placeholder, "Update facebook status"),
				 new Task(R.drawable.placeholder, "Tweet"),
				 new Task(R.drawable.placeholder, "About")
				 };
				 
				 TaskAdapter adapter = new TaskAdapter(this,
				 R.layout.listview_item_row, task_data);
				 
				 
				 listView1 = (ListView)findViewById(R.id.listView1);
				 
				 View header =
				(View)getLayoutInflater().inflate(R.layout.listview_header_row, null);
				 listView1.addHeaderView(header);
				 
				 listView1.setAdapter(adapter);
				 
				 listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			            @Override
			            public void onItemClick(AdapterView<?> parent, View view, int position,
			                    long id) {
			            	Intent intent;
			                
			            	switch(position)
			            	{
			            	case 1:
			            		performEmergencyTasks();
			            		break;
			            	case 2:
			            		intent = new Intent(getApplicationContext(), TipsActivity.class);
			       			 	startActivity(intent);
			       			 	break;
			            	case 3:
			            		intent = new Intent(getApplicationContext(), NHActivity.class);
			       			 	startActivity(intent);
			       			 	break;
			            	case 4:
			            		intent = new Intent(getApplicationContext(), MDActivity.class);
			       			 	startActivity(intent);
			            		break;
			            	case 5:
			            		intent = new Intent(getApplicationContext(), CarStopperActivity.class);
			       			 	startActivity(intent);
			            		break;
			            	case 6:
			            		intent = new Intent(getApplicationContext(), CTActivity.class);
			       			 	startActivity(intent);
			            		break;
			            	case 7:
			            		
			            		if (FacebookDialog.canPresentShareDialog(getApplicationContext(), 
                                        FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
			            		FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(MainActivity.this)
			            		.setName("Trauma Care 60")
			                    .setLink(null)
			                    .build();
			            		uiHelper.trackPendingDialogCall(shareDialog.present());
			            		}
			            		else
			            		{
			            		
			            			publishFeedDialog();
			            		}
			            		break;
			            	case 8:
			            		String url = "https://twitter.com/intent/tweet?button_hashtag=TraumaCare60&text=I%20have%20had%20an%20accident.";
			            		intent = new Intent(Intent.ACTION_VIEW);
			            		intent.setData(Uri.parse(url));
			            		startActivity(intent);
			            		break;
			            	
			            	}
			                
			                
			                
			            }
			        });
				 
				 PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
				 
				 getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
				 
				 uiHelper = new UiLifecycleHelper(this, null);
				 uiHelper.onCreate(savedInstanceState);
				 
				 Intent intent = new Intent(getApplicationContext(), CarCrashDetectionService.class );
				 startService(intent);
				 mLocationClient = new LocationClient(this, this, this);
				 
				 mPhoneStateListener = new StatePhoneReceiver(this);
				 mTelephonyManager = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE));
				 
				 Intent sourceIntent = getIntent();
				 if(sourceIntent!=null)		
				 {
					 String message = sourceIntent.getStringExtra("FROM_CCDSBR");
				 if(message!=null&&message.equals("true"))
				 {
					 
					 
					 SI_IS_CCDSBR=true;
				 }
				 }
				 
				 
				 }
	
	
	
	public void performEmergencyTasks()
	{
		
		
		String emergencyMessage="I have had an accident. ";
		String location;
		if(mLocationClient!=null)
		mCurrentLocation = mLocationClient.getLastLocation();
		
		if(mCurrentLocation!=null)
		{
			location="Location:"+mCurrentLocation.getLatitude()+","+mCurrentLocation.getLongitude()+" ";
		}
		else
		{
			location="Location: NA ";
		}
		
		IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		Intent batteryStatus = this.registerReceiver(null, ifilter);
		int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
		int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

		float batteryPct = (level / (float)scale)*100.0f;
		String batteryLevel="Battery level: "+batteryPct+"% ";
		
		String message=emergencyMessage+location+batteryLevel;
		Toast.makeText(getApplicationContext(),
				message,
                Toast.LENGTH_SHORT).show();
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		String emergencyContacts = sharedPref.getString("pref_ec", "").replaceAll(",",";");
		Uri mUri = Uri.parse("smsto:" + emergencyContacts);
		Intent intent = new Intent(
                android.content.Intent.ACTION_SENDTO, mUri);
        intent.putExtra("sms_body", message);
        mTelephonyManager.listen(mPhoneStateListener,
        	    PhoneStateListener.LISTEN_CALL_STATE); 
        	    callFromApp=true;
        String mASP=sharedPref.getString("pref_asp","");
        
        intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + mASP));
        startActivity(intent); 
        		
		
		
		 
		
	}
	
	// 
	 public class StatePhoneReceiver extends PhoneStateListener {
	     Context context;
	     public StatePhoneReceiver(Context context) {
	         this.context = context;
	     }
	 
	     @Override
	     public void onCallStateChanged(int state, String number) {
	         super.onCallStateChanged(state, number);
	         
	         switch (state) {
	         
	         case TelephonyManager.CALL_STATE_OFFHOOK: 
	          if (callFromApp) {
	              callFromApp=false;
	              callFromOffHook=true;
	                   
	              try {
	                Thread.sleep(500); // Delay 0.5 seconds to handle better turning on loudspeaker
	              } catch (InterruptedException e) {
	              }
	           
	              
	              AudioManager audioManager = (AudioManager)
	                                          getSystemService(Context.AUDIO_SERVICE);
	              audioManager.setMode(AudioManager.MODE_IN_CALL);
	              audioManager.setSpeakerphoneOn(true);
	           }
	           break;
	         
	        case TelephonyManager.CALL_STATE_IDLE: 
	          if (callFromOffHook) {
	                callFromOffHook=false;
	                AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
	                audioManager.setMode(AudioManager.MODE_NORMAL); 
	                mTelephonyManager.listen(mPhoneStateListener, 
	                      PhoneStateListener.LISTEN_NONE);
	             }
	          break;
	         }
	     }
	 }
	
	@Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
        mLocationClient.connect();
    }
	
	@Override
	protected void onResume() {
	    super.onResume();  
	    uiHelper.onResume();

	    Context context = this;
		 SharedPreferences sP = context.getSharedPreferences(
		         getString(R.string.preference_initial_setup), Context.MODE_PRIVATE);
		 int defaultValue = 0;
		 int iSCS=sP.getInt(getString(R.string.preference_initial_setup_completion_status),defaultValue);
		 if(iSCS!=100)
		 {
			 
				 
			 Intent intent = new Intent(this, InitialSetupActivity.class);
			 startActivity(intent);
		 }
		 
		 
		 
		 
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	}
	
	@Override
	public void onPause() {
	    super.onPause();
	    uiHelper.onPause();
	}

	@Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        mLocationClient.disconnect();
        super.onStop();
    }
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    
	    switch (requestCode) {
        
        case CONNECTION_FAILURE_RESOLUTION_REQUEST :
        /*
         * If the result code is Activity.RESULT_OK, try
         * to connect again
         */
            switch (resultCode) {
                case Activity.RESULT_OK :
                /*
                 * Try the request again
                 */
                
                break;
            }
            break;
            default:
            	uiHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
        	        @Override
        	        public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
        	            Log.e("Activity", String.format("Error: %s", error.toString()));
        	        }

        	        @Override
        	        public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
        	            Log.i("Activity", "Success!");
        	        }
        	    });
            	break;
	    }
        
    }
	
	private boolean servicesConnected() {
        // Check that Google Play services is available
        int resultCode =
                GooglePlayServicesUtil.
                        isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates",
                    "Google Play services is available.");
            // Continue
            return true;
        // Google Play services was not available for some reason.
        // resultCode holds the error code.
        } else {
            // Get the error dialog from Google Play services
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                    resultCode,
                    this,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);

            // If Google Play services can provide an error dialog
            if (errorDialog != null) {
                // Create a new DialogFragment for the error dialog
                ErrorDialogFragment errorFragment =
                        new ErrorDialogFragment();
                // Set the dialog in the DialogFragment
                errorFragment.setDialog(errorDialog);
                // Show the error dialog in the DialogFragment
                errorFragment.show(getFragmentManager(),
                        "Location Updates");
            }
            return false;
        }
    }
 

	    
	
	
	private void publishFeedDialog() {
	    Bundle params = new Bundle();
	    params.putString("name", "Trauma Care 60");
	    
	    
	    
	    params.putString("link", null);
	    
	    try{
	    WebDialog feedDialog = (
	        new WebDialog.FeedDialogBuilder(this,
	            Session.getActiveSession(),
	            params))
	        .setOnCompleteListener(new OnCompleteListener() {

	            @Override
	            public void onComplete(Bundle values,
	                FacebookException error) {
	                if (error == null) {
	                    // When the story is posted, echo the success
	                    // and the post Id.
	                    final String postId = values.getString("post_id");
	                    if (postId != null) {
	                        Toast.makeText(getApplicationContext(),
	                            "Posted story, id: "+postId,
	                            Toast.LENGTH_SHORT).show();
	                    } else {
	                        // User clicked the Cancel button
	                        Toast.makeText(getApplicationContext(), 
	                            "Publish cancelled", 
	                            Toast.LENGTH_SHORT).show();
	                    }
	                } else if (error instanceof FacebookOperationCanceledException) {
	                    // User clicked the "x" button
	                    Toast.makeText(getApplicationContext(), 
	                        "Publish cancelled", 
	                        Toast.LENGTH_SHORT).show();
	                } else {
	                    // Generic, ex: network error
	                    Toast.makeText(getApplicationContext(), 
	                        "Error posting story", 
	                        Toast.LENGTH_SHORT).show();
	                }
	            }

	        })
	        .build();
	    feedDialog.show();
	    }
	    catch(Exception e)
	    {
	    	Toast.makeText(getApplicationContext(), 
                    "Something went wrong...", 
                    Toast.LENGTH_SHORT).show();
	    }
	}
	
	public static class ErrorDialogFragment extends DialogFragment {
        // Global field to contain the error dialog
        private Dialog mDialog;
        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }
        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }
        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }  
	
	 @Override
	    public void onConnected(Bundle dataBundle) {
	        // Display the connection status
	        //Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
		 if(SI_IS_CCDSBR==true)
		 {
			 SI_IS_CCDSBR=false;
			 performEmergencyTasks();
		 }

	    }
	 
	 @Override
	    public void onDisconnected() {
	        // Display the connection status
	        /*Toast.makeText(this, "Disconnected. Please re-connect.",
	                Toast.LENGTH_SHORT).show();*/
	    }
	 
	 @Override
	    public void onConnectionFailed(ConnectionResult connectionResult) {
	        /*
	         * Google Play services can resolve some errors it detects.
	         * If the error has a resolution, try sending an Intent to
	         * start a Google Play services activity that can resolve
	         * error.
	         */
	        if (connectionResult.hasResolution()) {
	            try {
	                // Start an Activity that tries to resolve the error
	                connectionResult.startResolutionForResult(
	                        this,
	                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
	                /*
	                 * Thrown if Google Play services canceled the original
	                 * PendingIntent
	                 */
	            } catch (IntentSender.SendIntentException e) {
	                // Log the error
	                e.printStackTrace();
	            }
	        } else {
	            /*
	             * If no resolution is available, display a dialog to the
	             * user with the error.
	             */
	            //showErrorDialog(connectionResult.getErrorCode());
	        	Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(),this,
	                    CONNECTION_FAILURE_RESOLUTION_REQUEST);
	            // If Google Play services can provide an error dialog
	            if (errorDialog != null) {
	                        // Create a new DialogFragment for the error dialog
	                        ErrorDialogFragment errorFragment =  new ErrorDialogFragment();
	                        // Set the dialog in the DialogFragment
	                        errorFragment.setDialog(errorDialog);
	                        // Show the error dialog in the DialogFragment
	                        errorFragment.show(getFragmentManager(),"Location Updates");
	            }
	        }
	    }
	
	@Override
	protected void onDestroy(){
	        super.onDestroy();
	        uiHelper.onDestroy();
	        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	        

	    }
	
	
}
