package mg.traumacare60;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CarCrashDetectionServiceBroadcastReceiver extends BroadcastReceiver{
	   
	   @Override
	   public void onReceive(Context context, Intent intent) {
		   
		   Intent i = new Intent();
	        i.setClassName("mg.traumacare60", "mg.traumacare60.MainActivity");
	        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        i.putExtra("FROM_CCDSBR","true");
	        context.startActivity(i);
	        
		   
	        context.sendBroadcast(new Intent("mg.traumacare60.PERFORM_EMERGENCY_TASKS"));
	   }

}
