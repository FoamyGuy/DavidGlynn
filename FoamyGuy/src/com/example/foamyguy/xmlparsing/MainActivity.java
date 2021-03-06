package com.example.foamyguy.xmlparsing;

import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainActivity extends Activity {

	private itemsAdapter mAdapter;
	private ListView itemsList;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("StackSites", "OnCreate()");
		setContentView(R.layout.activity_main);

		//Get reference to our ListView
		itemsList = (ListView)findViewById(R.id.itemsList);

		//Set the click listener to launch the browser when a row is clicked.
		itemsList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int pos,long id) {
				String url = mAdapter.getItem(pos).getLink();
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);

			}

		});

		/*
		 * If network is available download the xml from the Internet.
		 * If not then try to use the local file from last time.
		 */
		if(isNetworkAvailable()){
			Log.i("StackSites", "starting download Task");
			SitesDownloadTask download = new SitesDownloadTask();
			download.execute();
		}else{
			mAdapter = new itemsAdapter(getApplicationContext(), -1, ImagesXmlPullParser.getItemsFromFile(MainActivity.this));
			itemsList.setAdapter(mAdapter);
		}

	}

	//Helper method to determine if Internet connection is available.
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	} 

	/*
	 * AsyncTask that will download the xml file for us and store it locally.
	 * After the download is done we'll parse the local file.
	 */
	private class SitesDownloadTask extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... arg0) {
			//Download the file
			try {
				Downloader.DownloadFromUrl("http://www.nasa.gov/rss/dyn/image_of_the_day.rss", openFileOutput("nasa_image_of_the_day.xml", Context.MODE_PRIVATE));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result){
			//setup our Adapter and set it to the ListView.
			mAdapter = new itemsAdapter(MainActivity.this, -1, ImagesXmlPullParser.getItemsFromFile(MainActivity.this));
			itemsList.setAdapter(mAdapter);
			Log.i("StackSites", "adapter size = "+ mAdapter.getCount());
		}
	}

}