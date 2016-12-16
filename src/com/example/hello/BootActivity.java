package com.example.hello;


import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import api.Server;
import inputcells.PictureInputCellFragment;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BootActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_boot);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.boot, menu);
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
	@Override
	protected void onResume(){
		super.onResume();
		startLoginActivity();
		
		OkHttpClient client = Server.getClient();
		Request request = new Request.Builder()
				.url(Server.serverAddress)
				.method("GET", null)
				.build();
		
	
			client.newCall(request).enqueue(new Callback() {
			    
				@Override
				public void onFailure(Call arg0, final IOException arg1) {
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(getApplicationContext(),arg1.getLocalizedMessage(),Toast.LENGTH_LONG).show();
							startLoginActivity();
						}
	});
					
				}

				@Override
				public void onResponse(Call arg0, final Response response) throws IOException {
						BootActivity.this.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								
								Toast toast = null;
								try {
									toast = Toast.makeText(getApplicationContext(), response.body().string(),Toast.LENGTH_SHORT);
								
								toast.setGravity(Gravity.CENTER, 0	, 0);
								LinearLayout toastView = (LinearLayout) toast.getView();
								ImageView imagecPro = new ImageView(getApplicationContext());
								imagecPro.setImageResource(R.drawable.dgut);
								toastView.addView(imagecPro);
								toast.show();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								startLoginActivity();
							}
						});
				}
			    });
	}
	

	
		 void startLoginActivity() {
			 Intent intent = new Intent(this, LoginActivity.class);
			 startActivity(intent);
			 finish();
		}
		
		 void startRegisterActivity(){
			 Intent intent = new Intent(this, RegisterActivity.class);
			 startActivity(intent);
			 finish();
		 }
		 
		 void startPicture(){
			 Intent intent = new Intent(this, PictureInputCellFragment.class);
			 startActivity(intent);
			 finish();
		 }
		 
		 
	
}
