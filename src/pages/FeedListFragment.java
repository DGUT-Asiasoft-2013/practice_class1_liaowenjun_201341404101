package pages;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import com.example.hello.FeedContentActivity;
import com.example.hello.R;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import CustomViews.AvatarView;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import utils.Article;
import utils.Page;
import android.widget.AdapterView.OnItemClickListener;
import api.Server;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class FeedListFragment extends Fragment {
	public View view, loadMore;
	public ListView listview ;
	public List <Article> data;
	public AvatarView avatar;
	int page = 0;
	public TextView txtLoadmore;
	public TextView textDate;
	public TextView textAuthorName,txt_title;
	public FeedListFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(view== null){
		view = inflater.inflate(R.layout.fragment_feedlist, null);
		loadMore = inflater.inflate(R.layout.widget_load_root_more_btn, null);
		txtLoadmore =  (TextView) loadMore.findViewById(R.id.more_text);
		
		
		listview = (ListView) view.findViewById(R.id.list);
		listview.addFooterView(loadMore);
		listview.setAdapter(listAdp);
		
		listview.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						listviewClicked(position);
					}
				});
	
		
		loadMore.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loadmore();
			}
		});
		}
		return view;	
		}
	
	 void loadmore() {
		loadMore.setEnabled(false);
		txtLoadmore.setText("载入中...");
		
		Request request = Server.requestBuilderWithPath("feeds/"+(page+1)).get().build();
		Server.getClient().newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response res) throws IOException {
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						loadMore.setEnabled(true);
						txtLoadmore.setText("加载更多");
					}
				});
				
				try {
					final Page<Article> feeds = new ObjectMapper().readValue(res.body().string(), new TypeReference<Page<Article>>() {});
					if(feeds.getNumber()> page){
						getActivity().runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								if(data == null){
									data =  feeds.getContent();
									
								}else{
									data.addAll(feeds.getContent());
								}
								
								page = feeds.getNumber();
								
								listAdp.notifyDataSetChanged();
							}
						});
					}
				
				
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
			}
			
			@Override
			public void onFailure(Call arg0, IOException e) {
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						loadMore.setEnabled(true);
						txtLoadmore.setText("加载更多");
					}
				});
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		
		reload();
	}
	
	public void reload() {
		Request request = Server.requestBuilderWithPath("feeds")
				.get()
				.build();
		
		Server.getClient().newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				try {
					 final Page<Article> data = new ObjectMapper()
							.readValue(arg1.body().string(), 
									new TypeReference<Page <Article>>(){});
					
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							FeedListFragment.this.page = data.getNumber();
							FeedListFragment.this.data =  data.getContent();
							Toast.makeText(getActivity(), FeedListFragment.this.data.toString(), Toast.LENGTH_LONG).show();
							listAdp.notifyDataSetInvalidated();
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFailure(Call arg0, final IOException arg1) {
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						new AlertDialog.Builder(getActivity())
						.setMessage(arg1.getMessage())
						.setCancelable(true)
						.show();

					}
				});
			}
		});
	}

	BaseAdapter listAdp = new BaseAdapter(){

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			
			if(convertView == null){
				LayoutInflater inflater = LayoutInflater.from(parent.getContext());
				view = inflater.inflate(R.layout.widget_feed__item, null);
				
			}
			else{
				view = convertView;
			}
			
			TextView tv1 = (TextView )view.findViewById(R.id.feed_list_text);
			textDate= (TextView )view.findViewById(R.id.feed_date);
			textAuthorName = (TextView) view.findViewById(R.id.authorName);
			txt_title = (TextView) view.findViewById(R.id.txt_title);
			
			Article atc = data.get(position);
			
			tv1.setText(atc.getText());
			txt_title.setText(atc.getTitle());
		
			
			
			textAuthorName.setText(atc.getAuthorName());
			try {
				avatar.load(atc.getAuthor());
			} catch (Exception e) {
				e.printStackTrace();
				Log.d("aaaavatar",e.toString());
			}
			String dateStr = DateFormat.format("yyy-MM-dd hh:mm", atc.getCreateDate()).toString();
			textDate.setText(dateStr);
			return view;
		}
			

		@Override
		public int getCount() {
			return data == null ? 0:data.size() ;
		}

		@Override
		public Object getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		};
		
		
		 
		
	};
	
	//Intent
	
	public void listviewClicked(int position) {
		Article article = data.get(position);
		Intent itt = new Intent(getActivity(), FeedContentActivity.class);
		itt.putExtra("data", article);
	     startActivity(itt);
		
	}
	
	
}
