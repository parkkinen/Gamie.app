package fi.oamk.chatclient;



import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatActivity extends Activity {
	// State your name here
	static final String NICKNAME = "Riku Kaipainen";
	// Set the IP 
	static final String SERVER_IP_ADDRESS = "172.20.240.11";

	// ---socket---
	InetAddress serverAddress;
	Socket socket;

	// ---all the Views---
	static TextView txtMessageReceived;
	EditText txtMessage;

	// ---thread for communicating on the socket---
	CommunicationThread communicationThread;

	// ---used for updating the UI on the main activity---
	static Handler UIupdater = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int numOfBytesReceived = msg.arg1;
			byte[] buffer = (byte[]) msg.obj;

			// ---convert the entire byte array to string---
			String strReceived = new String(buffer);

			// ---extract only the actual string received---
			strReceived = strReceived.substring(0, numOfBytesReceived);

			// ---display the text received on the TextView---
			txtMessageReceived.append(strReceived);
		}
	};

	private class CreateCommThreadTask extends AsyncTask<Void, Integer, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				// ---create a socket---
				serverAddress = InetAddress.getByName(SERVER_IP_ADDRESS);
				socket = new Socket(serverAddress, 7001); //IP, PORT NUMBER
				communicationThread = new CommunicationThread(socket);
				communicationThread.start();
				// ---sign in for the user; sends the nick name---
				sendToServer(NICKNAME);
                //
            } catch (UnknownHostException e) {
				Log.d("ChatClient", e.getLocalizedMessage());
			} catch (IOException e) {
				Log.d("ChatClient", e.getLocalizedMessage());
			}
			return null;
		}
	}

	private class WriteToServerTask extends AsyncTask<byte[], Void, Void> {
		protected Void doInBackground(byte[]... data) {
			communicationThread.write(data[0]);
			return null;
		}
	}

	private class CloseSocketTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			try {
				socket.close();
			} catch (IOException e) {
				Log.d("ChatClient", e.getLocalizedMessage());
			}
			return null;
		}
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i("ChatClient", "ChatActivity.onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);

		txtMessage = (EditText) findViewById(R.id.txtMessage);
		txtMessageReceived = (TextView) findViewById(R.id.txtMessagesReceived);
		findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onClickSend(view);
			}
		});
	}

	public void onClickSend(View view) {
		Log.i("ChatClient", "ChatActivity.onClickSend");
		sendToServer(txtMessage.getText().toString());
	}

	private void sendToServer(String message) {
		byte[] theByteArray = message.getBytes();
		if (theByteArray.length > 0) {
			new WriteToServerTask().execute(theByteArray);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		new CreateCommThreadTask().execute();
	}

	@Override
	public void onPause() {
		super.onPause();
		new CloseSocketTask().execute();
	}

}
