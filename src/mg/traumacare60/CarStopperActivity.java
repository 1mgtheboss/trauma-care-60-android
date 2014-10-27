package mg.traumacare60;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

public class CarStopperActivity extends Activity {
	private Handler handler=new Handler();
	private static int counter=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_car_stopper);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}
	
	@Override
	protected void onResume () {
		super.onResume();
		
		
		try{
			
			handler.postDelayed(new Runnable() {
				
			    @Override
			    public void run() {
			    	try{
			    		
			    	if(counter==0) 
			    	{
			    		
			    		findViewById(R.id.car_stopper_relative_layout).setBackgroundColor(Color.RED);
			    		counter=1;
			    		
			    	}
			    	else
			    	{
			    		
			    		findViewById(R.id.car_stopper_relative_layout).setBackgroundColor(Color.BLACK);
			    		counter=0;
			    	}
			    		
			    	
			    	handler.postDelayed(this, 2000);
			    	}
			    	catch(Exception e)
			    	{
			    		
			    	}
			    }
			}, 2000);
			}
			catch(Exception e)
			{
				
			}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.car_stopper, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onDestroy(){
	        super.onDestroy();
	        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

	    }
}
