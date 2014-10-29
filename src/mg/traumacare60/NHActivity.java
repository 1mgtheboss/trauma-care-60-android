package mg.traumacare60;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class NHActivity extends FragmentActivity implements
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener,
LocationListener{
	private LocationClient mLocationClient;
	private GoogleMap mMap;
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	
	// Global variable to hold the current location
    private Location mCurrentLocation;
    
    // Milliseconds per second
    private static final int MILLISECONDS_PER_SECOND = 1000;
    // Update frequency in seconds
    public static final int UPDATE_INTERVAL_IN_SECONDS = 5;
    // Update frequency in milliseconds
    private static final long UPDATE_INTERVAL =
            MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
    // The fastest update frequency, in seconds
    private static final int FASTEST_INTERVAL_IN_SECONDS = 1;
    // A fast frequency ceiling in milliseconds
    private static final long FASTEST_INTERVAL =
            MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;
    
    // Define an object that holds accuracy and frequency parameters
    private LocationRequest mLocationRequest;
    private boolean mUpdatesRequested;
    
    private Marker mMarker;
    
    public final static String EXTRA_MESSAGE = "mg.traumacare60.LISTINGS_BY_MESSAGE";
    private String listingsBy=null; 
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nh);
		
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}
		
		// Create the LocationRequest object
        mLocationRequest = LocationRequest.create();
        // Use high accuracy
        mLocationRequest.setPriority(
                LocationRequest.PRIORITY_HIGH_ACCURACY);
        // Set the update interval to 5 seconds
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        // Set the fastest update interval to 1 second
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
		
		mLocationClient = new LocationClient(this, this, this);
		// Start with updates turned on
        mUpdatesRequested = true;
        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
	}
	
	/*
     * Called when the Activity becomes visible.
     */
    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
        mLocationClient.connect();
        
        
    }
    
    @Override
    protected void onResume() {
        super.onResume();

        
        
    }
    
    /*
     * Called when the Activity is no longer visible.
     */
    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        mLocationClient.disconnect();
        super.onStop();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.nh_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		
		switch (item.getItemId()) {
        case R.id.action_listings_by:
            openLB();
            return true;
        case R.id.action_settings:
            
            return true;
        default:
            return super.onOptionsItemSelected(item);
    }
	}
	
	public void openLB()
	{
		
		Intent intent = new Intent(this, LBActivity.class);
		intent.putExtra(EXTRA_MESSAGE, listingsBy);
		startActivity(intent);
		
		
	}
	
	// Define a DialogFragment that displays the error dialog
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Decide what to do based on the original request code
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
                errorFragment.show(getFragmentManager(),"Location Updates");
            }
            
            return false;
        }
    }
    
    /*
     * Called by Location Services when the request to connect the
     * client finishes successfully. At this point, you can
     * request the current location or start periodic updates
     */
    @Override
    public void onConnected(Bundle dataBundle) {
        // Display the connection status
        //Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
    	
    	
    	
        mCurrentLocation = mLocationClient.getLastLocation();
        if(mCurrentLocation!=null)
        {
        	
            mMarker = mMap.addMarker(new MarkerOptions()
            .position(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()))
            .title("You're here."));
            CameraPosition cameraPosition = new CameraPosition.Builder()
            .target(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude())) 
            .zoom(10.0f)                
            .build();                   
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
        else
        {
        	
        	Toast.makeText(this, "Location unavailable.", Toast.LENGTH_SHORT).show();
        }
        if (mUpdatesRequested) {
            mLocationClient.requestLocationUpdates(mLocationRequest, this);
        }

    }
    
    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onDisconnected() {
        // Display the connection status
        //Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
    	
    }
    
    /*
     * Called by Location Services if the attempt to
     * Location Services fails.
     */
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
    
    // Define the callback method that receives location updates
    @Override
    public void onLocationChanged(Location location) {
    	try{
        // Report to the UI that the location was updated
    	
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        
    	mCurrentLocation = location;
    	mMarker.setPosition(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()));
    	
    	
    	JSONObject mJSON = null;
        String mString = "";
        HttpResponse mResponse;
        HttpClient mClient = new DefaultHttpClient();
        HttpPost mConnection=null;
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String mHFR = sharedPref.getString("pref_hfr", "5");
		
			String url="https://maps.googleapis.com/maps/api/place/nearbysearch/json?"+"location="+Double.toString(mCurrentLocation.getLatitude())+","+Double.toString(mCurrentLocation.getLongitude())+"&radius="+mHFR+"000&"+"types=hospital"+"&key=AIzaSyCrnIdKIYYQnggwDjNbA46CS_BTyVIVCc8";
			Log.i("URL:",url );
			mConnection = new HttpPost(url);
		
        try {
        	mResponse = mClient.execute(mConnection);
        	mString = EntityUtils.toString(mResponse.getEntity(), "UTF-8");
        	
             
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } 
         
         
        try{
        	
            
            mJSON = new JSONObject(mString);
            String mStatus = mJSON.optString("status").toString();
            if(mStatus.equals("OK"))
            {
            JSONArray hAArray = mJSON.optJSONArray("html_attributions"); //html_attributions array
            if(hAArray.length()!=0)
            {
            	listingsBy="";
            	for(int i=0;i<hAArray.length();i++)
            	{
            		listingsBy+=hAArray.getString(i)+"<br>";
            	}
            		
            }
            else
            {
            	listingsBy="";
            }
            JSONArray rArray=mJSON.optJSONArray("results"); //results array
            if(rArray.length()!=0)
            {
            	for(int i=0;i<rArray.length();i++)
            	{
            		JSONObject mResult = rArray.getJSONObject(i);
            		String name=mResult.optString("name").toString();
            		String rating=""+mResult.optDouble("rating");
            		if(rating.equals("NaN")) 
            		{
            			rating="NA";
            			
            		}
            		else
            		{
            			rating+="/5.0";
            		}
            		String lat=mResult.optJSONObject("geometry").optJSONObject("location").optString("lat").toString();
            		String lng=mResult.optJSONObject("geometry").optJSONObject("location").optString("lng").toString();
            		Log.i("Places:","Name:"+name+" "+"Rating:"+rating+" "+"Latitude:"+lat+" Longitude:"+lng );
            		mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(lat),Double.parseDouble(lng)))
                    .title(name)
                    .snippet("Rating:"+rating)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            	}
            
            }
            
            
            Log.i("Status:",mStatus );
            Log.i("HTML Attributions:",hAArray.toString() );
            }
            
            
             
             
        } catch ( JSONException e) {
            e.printStackTrace();                
        }
        
    	}
    	catch(Exception e)
    	{
    		Toast.makeText(this, "Something went wrong...", Toast.LENGTH_SHORT).show();
    	}
    }
    
    @Override
	protected void onDestroy(){
	        super.onDestroy();
	        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

	    }
    
   
}
