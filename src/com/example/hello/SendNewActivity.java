package com.example.hello;


import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import api.Server;
import inputcells.SimpleTextInputCellFragment;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SendNewActivity extends Activity {

	
	private View send;
	private EditText myText, myTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_send_new);
		myText = (EditText) findViewById(R.id.txt);
		myTitle=(EditText) findViewById(R.id.title);
		send = findViewById(R.id.btn_send);
		send.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sendText();
				overridePendingTransition(R.anim.none, R.anim.slide_out_bottom);
			}
		});
	}

	protected void sendText() {
		String title = myTitle.getText().toString();
		String txt = myText.getText().toString();
		 MultipartBody.Builder builder = new MultipartBody.Builder()
	   		        .addFormDataPart("title",title)
	   		        .addFormDataPart("text",txt);//article published
		 
		 RequestBody requestBody = builder.build();
		 
		 OkHttpClient client = Server.getClient();
		 
		 
	   	 Request request =Server.requestBuilderWithPath("article")
	   			.post(requestBody)
					.build();
		
	   	client.newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, final Response arg1) throws IOException {
				runOnUiThread(new Runnable() {
					final String responseBody = arg1.body().string();
					@Override
					public void run() {
						SendNewActivity.this.onSucceed(responseBody);
					}
				});

			}
			
			@Override
			public void onFailure(Call arg0, final IOException arg1) {
				runOnUiThread(new Runnable(){

					@Override
					public void run() {
						Toast.makeText(SendNewActivity.this,arg1.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
					}
					
				});
			}
		});
	}

	protected void onSucceed(String responseBody) {
		new AlertDialog.Builder(this).setMessage(responseBody)
		 		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		 			
		 			@Override
		 			public void onClick(DialogInterface dialog, int which) {
		  				finish();
		  				overridePendingTransition(R.anim.none, R.anim.slide_out_bottom);
		  			}
		 		
		 		}).show();
	}
	

	


}
