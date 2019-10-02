package com.androidhive.jsonparsing;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class AndroidJSONParsingActivity extends ListActivity {
	private ProgressBar progressBar;

	public class JsonGetAsync extends AsyncTask<String, Integer, JSONObject> {
		@Override
		protected void onPreExecute() {
			progressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected JSONObject doInBackground(String... url) {

			// Creating JSON Parser instance
			JSONParser jParser = new JSONParser();

			// getting JSON string from URL
			JSONObject json = jParser.getJSONFromUrl(url[0]);
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject jsonObject) {
			progressBar.setVisibility(View.GONE);
			dataReady(jsonObject);
		}
	}

	// url to make request
	private static String url = "http://172.20.240.11:7003";
	
	// JSON Node names
	public static final String TAG_PORTS = "ports";
	public static final String TAG_ID = "id";
	public static final String TAG_NAME = "name";
	public static final String TAG_ADDRESS = "address";
	public static final String TAG_IP = "ip";
	public static final String TAG_PORT = "port";

	// contacts JSONArray
	JSONArray contacts = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		new JsonGetAsync().execute(url);
	}

	public void dataReady(JSONObject object) {

		// Hashmap for ListView
		ArrayList<HashMap<String, String>> portsList = new ArrayList<HashMap<String, String>>();


		try {
			// Getting Array of Contacts
			contacts = object.getJSONArray(TAG_PORTS);

			// looping through All Contacts
			for(int i = 0; i < contacts.length(); i++){
				JSONObject c = contacts.getJSONObject(i);

				// Storing each json item in variable
				String id = c.getString(TAG_ID);
				String name = c.getString(TAG_NAME);
				String address = c.getString(TAG_ADDRESS);
				String ip = c.getString(TAG_IP);
				String port = c.getString(TAG_PORT);

				// creating new HashMap
				HashMap<String, String> map = new HashMap<String, String>();

				// adding each child node to HashMap key => value
				map.put(TAG_ID, id);
				map.put(TAG_NAME, name);
				map.put(TAG_ADDRESS, address);
				map.put(TAG_IP, ip);
				map.put(TAG_PORT, port);

				// adding HashList to ArrayList
				portsList.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}


		/**
		 * Updating parsed JSON data into ListView
		 * */
		ListAdapter adapter = new SimpleAdapter(this, portsList,
				R.layout.list_item,
				new String[] { TAG_NAME, TAG_IP, TAG_ADDRESS }, new int[] {
				R.id.name, R.id.address, R.id.ipAddress});

		setListAdapter(adapter);

		// selecting single ListView item
		ListView lv = getListView();

		// Launching new screen on Selecting Single ListItem
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				// getting values from selected ListItem
				String name = ((TextView) view.findViewById(R.id.name)).getText().toString();
				String address = ((TextView) view.findViewById(R.id.address)).getText().toString();
				String ip = ((TextView) view.findViewById(R.id.ipAddress)).getText().toString();


				// Starting new intent
				Intent in = new Intent(getApplicationContext(), SingleMenuItemActivity.class);
				in.putExtra(TAG_NAME, name);
				in.putExtra(TAG_ADDRESS, address);
				in.putExtra(TAG_IP, ip);
				startActivity(in);

			}
		});
	}
}