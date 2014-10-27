package mg.traumacare60;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.widget.Toast;

public class CarCrashDetectionService extends Service implements SensorEventListener{
	
	   private static boolean mRunning;
	   
	   
	   private boolean mAllowRebind;
	   private SensorManager mSensorManager;
	   private Sensor mA;
	   
	   
	   @Override
	   public void onCreate() {
		   mRunning = false;
	   }
	   
	   @Override
	   public int onStartCommand(Intent intent, int flags, int startId) {
		   if(!mRunning){
		  mRunning=true;
		  mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		  mA = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); 
		  mSensorManager.registerListener(this, mA, SensorManager.SENSOR_DELAY_NORMAL);
		  Toast.makeText(getBaseContext(), "Car Crash Detection Service running.", Toast.LENGTH_SHORT).show();
		   }
		   else
		   {
			     
		   }
	      return START_STICKY;
	   }
	   
	   @Override
	   public IBinder onBind(Intent intent) {
	      return null;
	   }
	   
	   @Override
	   public boolean onUnbind(Intent intent) {
	      return mAllowRebind;
	   }
	   
	   @Override
	   public void onRebind(Intent intent) {

	   }
	   
	   @Override
	   public void onDestroy() {
		   super.onDestroy();
		   mSensorManager.unregisterListener(this);
	   }
	   
	   @Override
		  public final void onAccuracyChanged(Sensor sensor, int accuracy) {
		    // Do something here if sensor accuracy changes.
		  }
		 
		 @Override
		  public final void onSensorChanged(SensorEvent event) {
		    
		    // Many sensors return 3 values, one for each axis.
			 	float x = event.values[0];
			    float y = event.values[1];
			    float z = event.values[2];

			    float gX = x / 9.8f;
			    float gY = y / 9.8f;
			    float gZ = z / 9.8f;
			    float gForce = (float)Math.sqrt(gX * gX + gY * gY + gZ * gZ);
		    if(gForce>20)			    
		    {
		    Intent intent = new Intent("mg.traumacare60.CAR_CRASH_DETECTED");
		    
		    
		    sendBroadcast(intent);
		    }
			    
		    
		  }
	   
	   

}
