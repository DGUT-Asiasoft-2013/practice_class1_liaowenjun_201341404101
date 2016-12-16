package pages;

import java.io.IOException;

import javax.security.auth.callback.Callback;

import com.example.hello.R;
import com.fasterxml.jackson.databind.ObjectMapper;

import CustomViews.AvatarView;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import api.Server;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import utils.User;

public class Fragment_MyProfile extends Fragment {
	public View view;
	public ProgressBar progressBar;
	 public AvatarView avatar;
	 public TextView my_name;
	public AttributeSet attrs;
	public Fragment_MyProfile() {
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		if(view == null){
			 view = inflater.inflate(R.layout.fragment_my_profile,null);
		
		
		progressBar =(ProgressBar) view.findViewById(R.id.my_progressBar);
		my_name =  (TextView)view.findViewById(R.id.my_name);
		avatar =  (AvatarView) view.findViewById(R.id.avatar);
		

		
		}
		return view;	
		
	}
	
	protected void onFailure(Call arg0, IOException arg1) {
		
	}
	@Override
	public void onResume() {
		super.onResume();
		
		my_name.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);
		
		OkHttpClient client = Server.getClient();
		 		Request request = Server.requestBuilderWithPath("me")
		 				.method("get", null)
		 				.build();
		 		
		 		client.newCall(request).enqueue(new okhttp3.Callback() {
			
			@Override
			public void onResponse(final Call arg0, Response res) throws IOException {
				final User user = new ObjectMapper().readValue(res.body().bytes(), User.class);
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						Fragment_MyProfile.this.onResponse(arg0, user);
					}
				});
			}
			
			@Override
			public void onFailure(final Call arg0, final IOException e) {
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						Fragment_MyProfile.this.onFailuer(arg0, e);
					}
				});
			}
		});
		
	}
	
	protected void onFailuer(Call arg0, IOException ex) {
		progressBar.setVisibility(View.GONE);
		my_name.setVisibility(View.VISIBLE);
		my_name.setTextColor(Color.RED);
		my_name.setText(ex.getMessage());
	}
	protected void onResponse(Call arg0, User user) {
				progressBar.setVisibility(View.GONE);
				avatar.load(user);
				my_name.setVisibility(View.VISIBLE);
				my_name.setTextColor(Color.BLACK);
				my_name.setText("Hello,"+user.getName());
	}
}
