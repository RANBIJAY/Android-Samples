package com.saturnmob.catalog;

import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.saturnmob.catalog.R;
import com.saturnmob.catalog.model.Flower;
import com.saturnmob.catalog.parsers.FlowerJSONParser;

public class MainActivity extends ListActivity {

	public static final String PHOTOS_BASE_URL = 
		"http://services.hanselandpetal.com/photos/";

	TextView output;
	ProgressBar pb;
	
	List<Flower> flowerList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		pb = (ProgressBar) findViewById(R.id.progressBar1);
		pb.setVisibility(View.INVISIBLE);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_get_data) {
			if (isOnline()) {
				requestData("http://services.hanselandpetal.com/feeds/flowers.json");
			} else {
				Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG).show();
			}
		}
		return false;
	}

	private void requestData(String uri) {
		
		StringRequest request = new StringRequest(uri, 
				
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						flowerList = FlowerJSONParser.parseFeed(response);
						updateDisplay();
					}
				}, 
				
				new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError ex) {
						Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
					}
				});
	
			RequestQueue queue = Volley.newRequestQueue(this);
			queue.add(request);
	}

	protected void updateDisplay() {
		//Use FlowerAdapter to display data
		FlowerAdapter adapter = new FlowerAdapter(this, R.layout.item_flower, flowerList);
		setListAdapter(adapter);
	}
	
	protected boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		} else {
			return false;
		}
	}
	
}