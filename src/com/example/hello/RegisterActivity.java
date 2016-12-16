package com.example.hello;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import api.Server;
import inputcells.PictureInputCellFragment;
import inputcells.SimpleTextInputCellFragment;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

public class RegisterActivity extends Activity{
	
	private SimpleTextInputCellFragment fragInputAccount;
	private SimpleTextInputCellFragment fragInputCellPsw;
	private SimpleTextInputCellFragment fragInputPswRepeat;
	private SimpleTextInputCellFragment fragEmail;
	private PictureInputCellFragment fragInputAvatar;
	private SimpleTextInputCellFragment fragInputName;


	@Override 
	public void onCreate(Bundle sts){
	
		
	
		super.onCreate(sts);
	
		
		setContentView(R.layout.activity_register);
		
		fragInputName = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.frag0);
		fragInputAccount = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.frag1);
		fragInputCellPsw = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.frag2);
		fragInputPswRepeat = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.frag3);
		fragEmail =  (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.email);
		fragInputAvatar = (PictureInputCellFragment)  getFragmentManager().findFragmentById(R.id.frag4);
		findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				submit();
			}
		});
		}
	

	@Override 
	public void onResume(){
		super.onResume();
		
		
		
		fragInputName.setLabelText("USERNAME");
		fragInputName.setHintText("USERNAME");
		fragInputName.setIsPsw(false);
		
		
		fragInputAccount.setLabelText("account");
		fragInputAccount.setHintText("account");
		fragInputAccount.setIsPsw(false);
		
		fragEmail.setLabelText("EMAIL");
		fragEmail.setHintText("email ");
		fragEmail.setIsPsw(false);
		
		
		fragInputCellPsw.setLabelText("password");{
		fragInputCellPsw.setHintText("input password");
		fragInputCellPsw.setIsPsw(true);}
		
		fragInputPswRepeat.setLabelText("repeat password");{
		fragInputPswRepeat.setHintText("repeat password");
		fragInputPswRepeat.setIsPsw(true);}
		
	
		
	}


	//submit the form
	protected void submit() {

   	 //get all the value
   	 String psw1 = fragInputCellPsw.getText(),
   			psw2 = fragInputPswRepeat.getText(),
   			account = fragInputAccount.getText(),
   			email = fragEmail.getText(),
   			name = fragInputName.getText();
   	 
   	 if(!checkPswIsSame(psw1, psw2)) return;
   	 
   	 //String password = MD5.getMD5(psw1+"gkjio");
   	 //Encryption
   	 String password = MD5.getMD5(psw1);
   	 
   	 MultipartBody.Builder builder = new MultipartBody.Builder()
   		        .setType(MultipartBody.FORM)
   		     .addFormDataPart("name",name)
   		        .addFormDataPart("account",account)
   		        .addFormDataPart("email",email)
   		        .addFormDataPart("passwordHash",password);
 
   	
   	 
		        
       	 if(fragInputAvatar.getPngData() != null){
       		 builder.addFormDataPart("avatar","avatar",
       				 RequestBody.create(MediaType.parse("image/png"), fragInputAvatar.getPngData()));
       	 }
       	 
       	 RequestBody requestBody = builder.build();
   	 OkHttpClient client = new OkHttpClient();
   	 
   	 Request request = Server.requestBuilderWithPath("register")
				.method("POST", requestBody)
				.post(requestBody)
				.build();
   	 
   	 final ProgressDialog progressD = new ProgressDialog(RegisterActivity.this);
   	 progressD.setCancelable(false);
   	 progressD.setTitle("提示");
   	 progressD.setMessage("请稍后");
   	 progressD.setProgressStyle(ProgressDialog.STYLE_SPINNER);
   	 progressD.setCanceledOnTouchOutside(false);
   	 progressD.show();
   	 
   		client.newCall(request).enqueue(new Callback() {
				
				@Override
				public void onResponse(Call arg0, final Response arg1) throws IOException {
					
					runOnUiThread(new Runnable() {
						  public void run() {
							  progressD.dismiss();
							  
								
								try {
									new AlertDialog.Builder(RegisterActivity.this)
											.setNegativeButton("OK", null)
											.setTitle("注册成功")
											.setMessage(arg1.body().string())
											.show();
									startLoginActivity();
								} catch (IOException e) {
									e.printStackTrace();
								}
						  }
						});
				
				}
				
				@Override
				public void onFailure(Call arg0, final IOException e) {
					runOnUiThread(new Runnable() {
						  public void run() {
							  progressD.dismiss();
							  
								
								new AlertDialog.Builder(RegisterActivity.this)
										.setNegativeButton("OK", null)
										.setTitle("注册失败")
										.setMessage(e.toString())
										.show();
							  
						  }
						});
				
					
				
				}

			});
   	 
   		
   	 
   	 
   }


	protected boolean checkPswIsSame(String psw1,String psw2) {
		if(!psw1.equals(psw2)){
		Toast.makeText(getApplicationContext(), "两次密码不一致", Toast.LENGTH_SHORT).show();
		return false;
		}else{
			return true;
		}
	}
	
	//鐧婚檰鎴愬姛
	public void startHelloActivity(){
		Intent itn = new Intent(this, HelloActivity.class);
		startActivity(itn);
	
	}
	
	//鐧婚檰鎴愬姛
	public void startLoginActivity(){
		Intent itn = new Intent(this, LoginActivity.class);
		startActivity(itn);
	
	}
	
}
