package com.saturnmob.catalog;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.saturnmob.catalog.R;

public class MainActivity extends Activity {

	TextView output;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
//		Initialize the TextView for vertical scrolling
		output = (TextView) findViewById(R.id.textView);
		output.setMovementMethod(new ScrollingMovementMethod());
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_do_task) {
			MyTask task = new MyTask();
			task.execute("Param 1", "Param 2", "Param 3");
		}
		return false;
	}

	protected void updateDisplay(String message) {
		output.append(message + "\n");
	}
	
	private class MyTask extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			updateDisplay("Starting task");
		}
		
		@Override
		protected String doInBackground(String... params) {
			return "Task complete";
		}
		
		@Override
		protected void onPostExecute(String result) {
			updateDisplay(result);
		}
		
	}

}