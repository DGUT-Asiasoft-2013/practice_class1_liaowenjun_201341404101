
/**
 * 
 */
package com.example.hello;

import java.io.IOException;

import com.example.hello.R;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import api.Server;
import fragments.PasswordRecoverStep1Frag;
import fragments.PasswordRecoverStep1Frag.OnGoNextListener;
import fragments.PasswordRecoverStep2Frag;
import fragments.PasswordRecoverStep2Frag.OnSubmitClickedListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import utils.User;

/**
 * @author LWJcoder
 *
 */
public class PasswordRecoverActivity extends Activity {
	
	/**
	 * // 恢复密码
	 */
	public PasswordRecoverActivity() {
		
	}
	
	PasswordRecoverStep1Frag step1 =new PasswordRecoverStep1Frag();
	PasswordRecoverStep2Frag step2 = new PasswordRecoverStep2Frag();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_psw_recover);
				
		step1.setOnGoNextListener(new OnGoNextListener() {
					
					@Override
					public void onGoNext() {
						goStep2();
					}
		});
		
		step2.setOnSubmitClickedListener(new OnSubmitClickedListener(){
			@Override
			public void onSubmitClicked(){
				goSubmit();
			}
		});
		
		getFragmentManager()
		.beginTransaction()
		.replace(R.id.fgpsw_container, step1)
		.commit();
		
	
	}
	
	public void goStep2() {
		getFragmentManager()
		.beginTransaction()	
		.setCustomAnimations(
				R.animator.slide_in_right,
				R.animator.slide_out_left,
				R.animator.slide_in_left,
				R.animator.slide_out_right)
		.replace(R.id.fgpsw_container, step2)
		.addToBackStack(null)
		.commit();
	}
	
	void goSubmit(){
		OkHttpClient client = Server.getClient();
		MultipartBody body = new MultipartBody.Builder()
				.addFormDataPart("email", step1.getText())
				.addFormDataPart("passwordHash", MD5.getMD5(step2.getText()))
				 				.build();
				 		Request request = Server.requestBuilderWithPath("passwordrecover")
				 				.method("POST", body)
				 				.post(body).build();
				 				
				 		client.newCall(request).enqueue(new Callback() {
				 			
				 			
				 			@Override
				 			public void onFailure(Call arg0, final IOException e) {
				 				runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										Toast.makeText(PasswordRecoverActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
									}
								});
				 			}

							@Override
							public void onResponse(Call arg0, final Response res) throws IOException {
								runOnUiThread(new Runnable() {
									  public void run() {
										  try { 
											  final String resBody = res.body().string();
										  if(resBody != null ){
									        new AlertDialog.Builder(PasswordRecoverActivity.this)
											
											.setTitle("tips")
											.setMessage("password changed succeed")
											.setNegativeButton("OK", new DialogInterface.OnClickListener() {
												
												@Override
												public void onClick(DialogInterface dialog, int which) {
													goLogin();
												}
											})
											.show();
									        
									        
											
										  }
										  } catch (IOException e) {
												e.printStackTrace();
											}
							}
						});
						}
	
							
				 		});
	}
			protected void goLogin() {
								Intent itt = new Intent(PasswordRecoverActivity.this, LoginActivity.class);
								startActivity(itt);
							}
}
