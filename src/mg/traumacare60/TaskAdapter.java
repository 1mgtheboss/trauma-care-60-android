package mg.traumacare60;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TaskAdapter extends ArrayAdapter<Task>{
	 Context context;
	 int layoutResourceId; 
	 Task data[] = null;
	 
	 public TaskAdapter(Context context, int layoutResourceId, Task[]
	data) {
	 super(context, layoutResourceId, data);
	 this.layoutResourceId = layoutResourceId;
	 this.context = context;
	 this.data = data;
	 }
	 @Override
	 public View getView(int position, View convertView, ViewGroup parent) {
	 View row = convertView;
	 TaskHolder holder = null;
	 
	 if(row == null)
	 {
	 LayoutInflater inflater =((Activity)context).getLayoutInflater();
	 row = inflater.inflate(layoutResourceId, parent, false);
	 
	 holder = new TaskHolder();
	 holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
	 holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);
	 
	 row.setTag(holder);
	 }
	 else
	 {
	 holder = (TaskHolder)row.getTag();
	 }
	 
	 Task task = data[position];
	 holder.txtTitle.setText(task.title);
	 holder.imgIcon.setImageResource(task.icon);
	 
	 return row;
	 }
	 
	 static class TaskHolder
	 {
	 ImageView imgIcon;
	 TextView txtTitle;
	 }
	}
