package com.example.hello;


import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import CustomViews.AvatarView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import api.Server;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;
import utils.Article;
import utils.Comment;
import utils.Page;


public class FeedContentActivity extends Activity {

	 Integer page = 0;
	Article atc;
	TextView txtContent, txtTitle, txtAuthorName, txtDate;
	AvatarView avatar;
	ListView list;
	View headerView;
	 View loadMoreView;
	 Button btnLikes;
	 Boolean isLiked = false;
	 List<Comment> comments;
	public FeedContentActivity() {
	}

	@SuppressLint("InflateParams")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.activity_feedlist_content);
		atc = (Article) getIntent().getSerializableExtra("data");
		
		  list = (ListView) findViewById(R.id.list);
		headerView = LayoutInflater.from(this).inflate(R.layout.widget_article_header, null);{
	
		btnLikes = (Button) headerView.findViewById(R.id.like);
	
		 txtContent = (TextView) headerView.findViewById(R.id.content_text);
		 txtTitle = (TextView) headerView.findViewById(R.id.feed_con_title);
		 txtAuthorName = (TextView) headerView.findViewById(R.id.feed_username);
		 txtDate = (TextView) headerView.findViewById(R.id.date);
		 avatar = (AvatarView) headerView.findViewById(R.id.avatar);
		
		 txtContent.setText(atc.getText());
		 txtTitle.setText(atc.getTitle());
		 txtAuthorName.setText(atc.getAuthor().getName());
		 avatar.load(atc.getAuthor());
		
		 String dateStr = DateFormat.format("yyy-MM-dd hh:mm", atc.getCreateDate()).toString();
		 txtDate.setText(dateStr);
		 
		
		 
		 list.addHeaderView(headerView, null, false);
		 headerView.findViewById(R.id.comment).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				makeComment();
			}
		});
		 
		
			
			headerView.findViewById(R.id.like).setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					toggleLikes();
				}
});
		 
		}
		 loadMoreView = LayoutInflater.from(this).inflate(R.layout.widget_load_root_more_btn, null);{
		 list.addFooterView(loadMoreView);
	}
		 TextView moreText = (TextView) loadMoreView.findViewById(R.id.more_text);
		 
		 moreText.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// jia zai ping lun lie biao
				loadmore();
				
			}
		});
		 
		 list.setAdapter(adapter);
	}




	BaseAdapter adapter = new BaseAdapter(){
		@Override
		public View getView(int position, View view, ViewGroup parent) {
			if(view ==null){
				view = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_comment_item, null);
			}
			
			Comment comment =  comments.get(position);
			TextView textContent =(TextView) view.findViewById(R.id.text);
			TextView textAuthorName = (TextView) view.findViewById(R.id.username);
			TextView textDate = (TextView) view.findViewById(R.id.date);
			AvatarView avatar = (AvatarView) view.findViewById(R.id.avatar);
		
			textContent.setText(comment.getText());
			textAuthorName.setText(comment.getAuthor().getName());
			avatar.load(atc.getAuthor());
			
			
			String dateStr = DateFormat.format("yy-MM-dd hh:mm", comment.getCreateDate()).toString();
			textDate.setText(dateStr);
			
			return view;
		}
	
		@Override
		public int getCount() {
			return comments == null ? 0 :comments.size();
		}

		@Override
		public Object getItem(int position) {
			return comments.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		

	
		
	};
	
	
	//添加评论
	 void makeComment() {
		Intent itt = new Intent(this, NewCommentActivity.class);
		itt.putExtra("data",(Serializable) atc);
		startActivity(itt);
		overridePendingTransition(R.anim.slide_in_bottom, R.anim.none);
		
	}

	 
	 
	@Override
	protected void onResume() {
		super.onResume();
		reload();
	}
	
	void checkLiked(){
		 Request request = Server.requestBuilderWithPath("article/"+atc.getId()+"/isliked").get().build();
			Server.getClient().newCall(request).enqueue(new Callback() {
				
				@Override
				public void onResponse(Call arg0, final Response res) throws IOException {
					
					runOnUiThread(new Runnable() {
						
						
						@Override
						public void run() {
							try {
								final  String resStr = res.body().string();
								final Boolean result = new ObjectMapper().readValue(resStr, Boolean.class);
								
								runOnUiThread(new Runnable() {
									
									@Override
									public void run() {

										onCheckLikedResult(result);
									}

							
								});
							
							
							} catch (Exception e) {
								e.printStackTrace();
								runOnUiThread(new Runnable() {
									
									@Override
									public void run() {

										onCheckLikedResult(false);
									}

							
								});
							}
						}
					});
				}
				
				@Override
				public void onFailure(Call arg0, final IOException e) {
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {

							onCheckLikedResult(false);
						}

				
					});
				}
			});
		
	}
	
	 void onCheckLikedResult(Boolean result) {
		 isLiked = result;
		 btnLikes.setTextColor(result? Color.BLUE: Color.BLACK);
	}
	 
	 void reloadLikes(){
		 Request request = Server.requestBuilderWithPath("/article/"+ atc.getId()+"/likes")
				 .get().build();
		 
		 Server.getClient().newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				try {
					String resStr = arg1.body().string();
					final Integer count = new ObjectMapper().readValue(resStr, Integer.class);
					
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							onReloadLikesResult(count);
						}

						
					});
				} catch (Exception e) {
					e.printStackTrace();
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							onReloadLikesResult(0);
						}

						
					});
				}
			}
			
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						onReloadLikesResult(0);
					}

					
				});
				
			}
		});
		 
	 }
	 
	 private void onReloadLikesResult(Integer count) {
		 if(count > 0){
			 btnLikes.setText("赞  ("+ count+")");
		 }else{
			 btnLikes.setText("赞");
		 }
		}
	 
	 void toggleLikes(){
		 MultipartBody body = new MultipartBody.Builder()
				 .addFormDataPart("likes", String.valueOf(!isLiked))
				 .build();
		 
		 Request request = Server.requestBuilderWithPath("article/" + atc.getId()+"/likes")
				 .post(body).build();
		 
		 Server.getClient().newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						reload();
					}
					
				});
			}
			
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						reload();
					}
					
				});
			}
		});
		 
	 }
	 
	
	 
	  void reload() {
		  reloadLikes();
		  checkLiked();
		  
	
		  Request request = Server.requestBuilderWithPath("/article/"+ atc.getId()+"/comments").get().build();
		  
		  Server.getClient().newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				try {
					final Page<Comment> data = new ObjectMapper().readValue(arg1.body().string(),  new TypeReference<Page<Comment>>() {
					});
					
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							FeedContentActivity.this.reloadData(data);
							
						}
					});
				} catch (final Exception e) {
					e.printStackTrace();
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							FeedContentActivity.this.onFail(e);
						}
					});
				}
			}
			
			@Override
			public void onFailure(Call arg0, final IOException e) {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						FeedContentActivity.this.onFail(e);
					}
				});
				
			}
		});
		}



	 void onFail(Exception e) {
			new AlertDialog.Builder(this).setMessage(e.getMessage())
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
					overridePendingTransition(R.anim.none, R.anim.slide_out_bottom);
				}
			}).show();
		
		Log.d("aaaaa", e.getMessage());
	}

	protected void reloadData(Page<Comment> data) {
		page = data.getNumber();
		comments = data.getContent();
		adapter.notifyDataSetInvalidated();
	}
	
	void loadmore(){
	
		
		page++;
		
		Request request = Server.requestBuilderWithPath("article/"+atc.getId()+"/comments/"+page).get().build();
		Log.d("ididid", atc.getId().toString());
		Server.getClient().newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response res) throws IOException {
				// TODO Auto-generated method stub
				try {
					final Page<Comment> data = new ObjectMapper().readValue(res.body().string(), new TypeReference<Page<Comment>>() {
					});
				
				
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						FeedContentActivity.this.appendData(data);
						
					}
				});
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			
			@Override
			public void onFailure(Call arg0, final IOException e) {
				FeedContentActivity.this.onFail(e);
				
			}
		});
	}


	 
	 protected void appendData(Page<Comment> data) {
			if(data.getNumber() > page){
				page = data.getNumber();
				
				if(comments==null){
					comments = data.getContent();		
				}else{
					comments.addAll(data.getContent());
				}
			}
			
			adapter.notifyDataSetChanged();
	}
	 
	
}
