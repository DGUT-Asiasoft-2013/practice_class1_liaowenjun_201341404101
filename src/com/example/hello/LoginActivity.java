package com.example.hello;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.BeanUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import inputcells.SimpleTextInputCellFragment;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import utils.User;
import api.Server;

public class LoginActivity extends Activity{
	public Button register;
	private SimpleTextInputCellFragment userName;
	private SimpleTextInputCellFragment psw;
	private TextView forgetPsw;
	private View login_btn;

	protected void onCreate(Bundle sis){
		super.onCreate(sis);;
		setContentView(R.layout.activity_login);
		
		userName = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.username);
		psw = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.psw);
		login_btn = findViewById(R.id.login);
		login_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				login();
			}

		
		});
	
	}
	
	private void login() {
		 String my_psw = psw.getText(),
				 account = userName.getText();
		 
		 String password = MD5.getMD5(my_psw);
		 
		 MultipartBody.Builder builder = new MultipartBody.Builder()
	   		        .setType(MultipartBody.FORM)
	   		        .addFormDataPart("account",account)
	   		        .addFormDataPart("passwordHash",password);
		 
		 RequestBody requestBody = builder.build();
		 
		 OkHttpClient client = Server.getClient();
		 
		 
	   	 Request request =Server.requestBuilderWithPath("login")
	   			.method("POST", requestBody)
	   			.post(requestBody)
					.build();
	   	 
		 final ProgressDialog progressD = new ProgressDialog(LoginActivity.this);
	   	 progressD.setCancelable(false);
	   	 progressD.setTitle("提示");
	   	 
	   	 
	   	 
	   	 progressD.setMessage("正在登陆");
	   	 progressD.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	   	 progressD.setCanceledOnTouchOutside(false);
	   	 progressD.show();
	   	 
	   		client.newCall(request).enqueue(new Callback() {
				
				@Override
				public void onResponse(Call arg0, final Response res) throws IOException {
					runOnUiThread(new Runnable() {
						  public void run() {
							  progressD.dismiss();
							  try { 
								  final String resBody = res.body().string();
							  if(resBody != null ){
						
									
								
								
								ObjectMapper mapper = new ObjectMapper();  
						        final User user = mapper.readValue(resBody, User.class); 
						    	goHello();
						    	Toast.makeText(getApplicationContext(), "welcome , "+user.getName(), Toast.LENGTH_SHORT).show(); 
							       
						        
						        
								
							  }
							  } catch (IOException e) {
									e.printStackTrace();
								}
							
							  
							  
							
						  }
						});
				}
				
				@Override
				public void onFailure(Call arg0,final IOException e) {
					runOnUiThread(new Runnable() {
						public void run() {
							progressD.dismiss();

							Toast.makeText(LoginActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
						}
	});
				}
			});
	}
	
	@Override 
	public void onResume(){
		super.onResume();
		
		forgetPsw = (TextView) findViewById(R.id.forgetPsw);
		
		register = (Button) findViewById(R.id.register);
		register.setOnClickListener(new View.OnClickListener(){  
			  
		    @Override  
		    public void onClick(View v) {  
		    	toRegister(v);
		    }

		      
		}  );  
		
		forgetPsw.setOnClickListener(new View.OnClickListener(){  
			  
		    @Override  
		    public void onClick(View v) {  
		    	toForgetPsw();
		    }

		      
		}  );  
		
		findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				login();
			}
		});
		
		userName.setLabelText("USERNAME");
		userName.setLabelText("INPUT YOUR NAME");
		userName.setIsPsw(false);
		
		psw.setLabelText("PASSWORD");
		psw.setHintText("INPUT YOUR PASSWORD");
		psw.setIsPsw(true);
		
		
	}

	protected void goHello() {
		Intent itt = new Intent(LoginActivity.this, HelloActivity.class);
		startActivity(itt);
	}

	
	protected void toForgetPsw() {
		Intent itnt = new Intent(this, PasswordRecoverActivity.class);
		startActivity(itnt);
	}

	public void toRegister(View target) {
		 Intent intent = new Intent(this, RegisterActivity.class);
		 startActivity(intent);
	}  
	
	
	

}


