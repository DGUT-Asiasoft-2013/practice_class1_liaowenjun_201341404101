package pages;

import java.io.IOException;
import java.util.List;

import com.example.hello.R;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import CustomViews.AvatarView;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import api.Server;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import utils.Comment;
import utils.Page;

public class Fragment_Search_page extends Fragment {
	View view;
	View btnLoadMore;
	 ListView listView;
	  List<Comment> data;
	  protected Integer page = 0;
	  TextView textLoadMore;
	  EditText searchText;
	  Button search;
	public Fragment_Search_page() {
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(view== null){
			 view = inflater.inflate(R.layout.fragment_search_page,null);
			 btnLoadMore = inflater.inflate(R.layout.widget_load_root_more_btn, null);
				
				listView = (ListView )view.findViewById(R.id.list);
				searchText = (EditText) view.findViewById(R.id.searchText);
				textLoadMore = (TextView) btnLoadMore.findViewById(R.id.more_text);
				
				search = (Button) view.findViewById(R.id.search);
				
				listView.addFooterView(btnLoadMore);
				
				listView.setAdapter(adapter);
		}
		return view;	
		}
	
BaseAdapter adapter = new BaseAdapter(){

		
		@Override
		public int getCount() {
			
			return data == null ? 0 :data.size();
		}

		@Override
		public Object getItem(int pos) {
			return data.get(pos);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}
		@SuppressLint("InflateParams")
		@Override
		public View getView(int pos, View contentView, ViewGroup parent) {
			View view = null;
			
			if(contentView == null){
				LayoutInflater inflater = LayoutInflater.from(parent.getContext());
				
				view = inflater.inflate(R.layout.widget_comment_item, null);
			}else{
				view  = contentView;
			}
			
			TextView textContent = (TextView) view.findViewById(R.id.text);
			TextView textAuthorName = (TextView)view.findViewById(R.id.username);
			TextView textDate = (TextView)view.findViewById(R.id.date);
			AvatarView avatar = (AvatarView)view.findViewById(R.id.avatar);
			
			Comment comment = data.get(pos);

			textContent.setText(comment.getText());
			textAuthorName.setText(comment.getAuthor().getName());
			avatar.load(comment.getAuthor());
			
			String dateStr = DateFormat.format("yyyy-MM-dd hh:mm", comment.getCreateDate()).toString();
			textDate.setText(dateStr);

			return view;
		}
		
	};
	
	@Override
	public void onResume() {
		super.onResume();
		reload();
		
		search.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				search();
			}
		});
		
		textLoadMore.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//show more
				loadmore();
			}
		});
	}
	private void search() {
		 String searchText1 = searchText.getText().toString();
		 MultipartBody.Builder body = new MultipartBody.Builder()
				 .setType(MultipartBody.FORM)
				 .addFormDataPart("text", searchText1)
				 ;
		 RequestBody requestBody = body.build();
		 
		 Request request = Server.requestBuilderWithPath("/search")
				 .method("POST", requestBody)
				 .post(requestBody)
				 .build();
		 
		  
		  Server.getClient().newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				try {
					final Page<Comment> data = new ObjectMapper().readValue(arg1.body().string(), new TypeReference<Page<Comment>>(){});
				
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							Fragment_Search_page.this.page  = data.getNumber();
							Fragment_Search_page.this.data = data.getContent();
							adapter.notifyDataSetInvalidated();
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
					Log.d("Fragment_Search_page", e.getMessage());
				}
			
			
			}
			
			@Override
			public void onFailure(Call arg0, IOException e) {
				Log.d("Fragment_Search_page", e.getMessage());
				
			}
		});
	}
	 void reload() {
		search();
	}
	
	void loadmore(){
			btnLoadMore.setEnabled(false);
			textLoadMore.setText("载入中…");
			
			 String searchText1 = searchText.getText().toString();
			 MultipartBody.Builder body = new MultipartBody.Builder()
					 .setType(MultipartBody.FORM)
					 .addFormDataPart("text", searchText1)
					 ;
			 RequestBody requestBody = body.build();
			 
			 Request request = Server.requestBuilderWithPath("/search/"+(page+1))
					 .method("POST", requestBody)
					 .post(requestBody)
					 .build();
			 
		
			Server.getClient().newCall(request).enqueue(new Callback() {
				@Override
				public void onResponse(Call arg0, Response arg1) throws IOException {
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							btnLoadMore.setEnabled(true);
							textLoadMore.setText("加载更多");
						}
					});
					
					try{
						final Page<Comment> comments = new ObjectMapper().readValue(arg1.body().string(), new TypeReference<Page<Comment>>() {});
						if(comments.getNumber()>page){
							
							getActivity().runOnUiThread(new Runnable() {
								public void run() {
									if(data==null){
										data = comments.getContent();
									}else{
										data.addAll(comments.getContent());
									}
									page = comments.getNumber();
									
									adapter.notifyDataSetChanged();
								}
							});
						}
					}catch(Exception ex){
						ex.printStackTrace();
					}
				}
				
				@Override
				public void onFailure(Call arg0, IOException arg1) {
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							btnLoadMore.setEnabled(true);
							textLoadMore.setText("加载更多");
						}
					});
				}
	});
	 }

}
