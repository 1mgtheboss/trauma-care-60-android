package mg.traumacare60;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MDActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_md);
		
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		String aN = sharedPref.getString("pref_aadhaar_no", "");
		TextView tV = (TextView)findViewById(R.id.textView1);
		if(aN.length()!=0)
			tV.setText("Your Aadhaar No:"+"\n"+aN);
		else
			tV.setText("Your Aadhaar No:"+"\n"+"Not set");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.md, menu);
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
	
	public void openHV(View view) {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.healthvault.com"));
		startActivity(intent);
	}
}
