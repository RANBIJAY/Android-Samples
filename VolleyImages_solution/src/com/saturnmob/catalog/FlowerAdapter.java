package com.saturnmob.catalog;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.saturnmob.catalog.R;
import com.saturnmob.catalog.model.Flower;

public class FlowerAdapter extends ArrayAdapter<Flower> {

	private Context context;
	private List<Flower> flowerList;
	
	private LruCache<Integer, Bitmap> imageCache;
	
	private RequestQueue queue;

	public FlowerAdapter(Context context, int resource, List<Flower> objects) {
		super(context, resource, objects);
		this.context = context;
		this.flowerList = objects;
		
		final int maxMemory = (int)(Runtime.getRuntime().maxMemory() /1024);
		final int cacheSize = maxMemory / 8;
		imageCache = new LruCache<>(cacheSize);
		
		queue = Volley.newRequestQueue(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = 
				(LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.item_flower, parent, false);

		//Display flower name in the TextView widget
		final Flower flower = flowerList.get(position);
		TextView tv = (TextView) view.findViewById(R.id.textView1);
		tv.setText(flower.getName());

		//Display flower photo in ImageView widget
		Bitmap bitmap = imageCache.get(flower.getProductId());
		final ImageView image = (ImageView) view.findViewById(R.id.imageView1);
		if (bitmap != null) {
			image.setImageBitmap(bitmap);
		}
		else {
			String imageUrl = MainActivity.PHOTOS_BASE_URL + flower.getPhoto();
			ImageRequest request = new ImageRequest(imageUrl, 
					new Response.Listener<Bitmap>() {

						@Override
						public void onResponse(Bitmap arg0) {
							image.setImageBitmap(arg0);
							imageCache.put(flower.getProductId(), arg0);
						}
					}, 
					80, 80, 
					Bitmap.Config.ARGB_8888, 
					
					new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError arg0) {
							Log.d("FlowerAdapter", arg0.getMessage());
						}
					}
					);
			queue.add(request);
		}

		return view;
	}

}
