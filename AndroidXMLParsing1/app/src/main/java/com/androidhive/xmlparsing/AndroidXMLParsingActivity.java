package com.androidhive.xmlparsing;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class AndroidXMLParsingActivity extends ListActivity {

	// All static variables
	static final String URL = "http://172.20.240.11:7002";
	// XML node keys
	static final String KEY_ITEM = "match_results"; // parent node
	static final String KEY_ID = "match";
	static final String HOME_TEAM = "home_team";
	static final String VISITOR_TEAM = "visitor_team";
	static final String HOME_SCORE = "home_goals";
	static final String VISITOR_SCORE = "visitor_goals";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		ArrayList<HashMap<String, String>> menuItems = new ArrayList<HashMap<String, String>>();

		CustomXMLParser parser = new CustomXMLParser();
		String xml = parser.getXmlFromUrl(URL); // getting XML
		Document doc = parser.getDomElement(xml); // getting DOM element
		doc.normalizeDocument();
		NodeList nl = doc.getElementsByTagName(KEY_ITEM);
		// looping through all item nodes <item>
		for (int i = 0; i < nl.getLength(); i++) {
			// creating new HashMap
			HashMap<String, String> map = new HashMap<String, String>();
			Element e = (Element) nl.item(i);
			// adding each child node to HashMap key => value
			map.put(KEY_ID, parser.getValue(e, KEY_ID));
			map.put(HOME_TEAM, parser.getValue(e, HOME_TEAM));
			map.put(VISITOR_TEAM, parser.getValue(e, VISITOR_TEAM));
			map.put(HOME_SCORE, parser.getValue(e, VISITOR_SCORE));
			map.put(VISITOR_SCORE, parser.getValue(e, VISITOR_SCORE));

			// adding HashList to ArrayList
			menuItems.add(map);
		}

		// Adding menuItems to ListView
		ListAdapter adapter = new SimpleAdapter(this, menuItems,
				R.layout.list_item,
				new String[] { HOME_TEAM, VISITOR_TEAM, HOME_SCORE, VISITOR_SCORE}, new int[] {
						R.id.home_team, R.id.visitor_team, R.id.home_score, R.id.visitor_score});

		setListAdapter(adapter);

		// selecting single ListView item
		ListView lv = getListView();

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting values from selected ListItem
				String home = ((TextView) view.findViewById(R.id.home_team)).getText().toString();
				String visitor = ((TextView) view.findViewById(R.id.visitor_team)).getText().toString();
				String home_score = ((TextView) view.findViewById(R.id.home_score)).getText().toString();
				String visitor_score = ((TextView) view.findViewById(R.id.visitor_score)).getText().toString();
				
				// Starting new intent
				Intent in = new Intent(getApplicationContext(), SingleMenuItemActivity.class);
				in.putExtra(HOME_TEAM, home);
				in.putExtra(VISITOR_TEAM, visitor);
				in.putExtra(HOME_SCORE, home_score);
				in.putExtra(VISITOR_SCORE, visitor_score);
				startActivity(in);

			}
		});
	}
}