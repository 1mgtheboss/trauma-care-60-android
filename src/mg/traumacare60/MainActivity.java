package mg.traumacare60;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	private ListView listView1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Task task_data[] = new Task[]
				 {
				 new Task(R.drawable.placeholder, "Perform emergency tasks"),
				 new Task(R.drawable.placeholder, "First aid instructions"), 
				 new Task(R.drawable.placeholder, "Search for hospitals"),
				 new Task(R.drawable.placeholder, "Medical data"),
				 new Task(R.drawable.placeholder, "Car stopper"),
				 new Task(R.drawable.placeholder, "Countdown timer"),
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
			                
			            	Context context = getApplicationContext();
			            	CharSequence text = Integer.toString(position);
			            	int duration = Toast.LENGTH_SHORT;

			            	Toast toast = Toast.makeText(context, text, duration);
			            	toast.show();
			                
			                
			                
			            }
			        });
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
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
