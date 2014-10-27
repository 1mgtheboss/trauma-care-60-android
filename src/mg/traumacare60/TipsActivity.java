package mg.traumacare60;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TipsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tips);
		
		ListView listView1 = (ListView) findViewById(R.id.listView1);
		 
		 String[] items = { "Your first priority is ensuring your own safety. The following precautions will help to make the area safe for you and others:", "Assessing casualties:\nAssess casualties by conducting a primary survey. Deal first with those who have life-threatening injuries. Remember, who is the most quiet may be the one in the highest danger. Assume any casualty involved may have a spinal injury. If possible, treat them where they are, supporting their head and neck at all times. Search the area around the incident for casualties who may have been thrown clear or wandered away from the scene.","Park safely. Park well clear of the incident and put on your hazard lights. If you do have any high visibility clothing available to you, put that on.", "Make vehicles safe. Turn off the ignition of damaged vehicles.", "Stabilise vehicles. If a vehicle is upright, apply the handbrake, put it in gear and or place blocks in front of the wheels. If it is on its side, do not attempt to right it but do try to prevent it from rolling any further.", "Watch out for physical dangers. This could include oncoming traffic etc. People in the vicinity should avoid smoking.","Alert the emergency services. Tell the emergency services as much as you can about the scene, including if fuel has been spilt or is leaking.","Additional Tips:\nEvery driver should have the correct skills and knowledge to effectively carry out first aid techniques. Drivers should attend refresher courses and have a valid first aid certificate which is renewed every five years." };
		 
		 ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, items);
		 
		 listView1.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tips, menu);
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
}
