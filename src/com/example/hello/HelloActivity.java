package com.example.hello;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import fragments.MainTabbarFragment;
import fragments.MainTabbarFragment.OnNewClickListener;
import fragments.MainTabbarFragment.OnTabSelectedListener;
import pages.FeedListFragment;
import pages.Fragment_MyProfile;
import pages.Fragment_Note_list;
import pages.Fragment_Search_page;
import pages.Fragment_SendNew;

public class HelloActivity extends Activity{
	
	FeedListFragment contentFeedList = new FeedListFragment();
	Fragment_Note_list contentNoteList = new Fragment_Note_list();
	Fragment_Search_page contentSearchPage = new Fragment_Search_page();
	Fragment_MyProfile contentMyprofile = new Fragment_MyProfile();
	Fragment_SendNew contentSendNew = new Fragment_SendNew();
	
	MainTabbarFragment tabbar ;
	
	public HelloActivity(){
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_hello);
		
		tabbar = (MainTabbarFragment) getFragmentManager().findFragmentById(R.id.frag_tabbar);
		if(tabbar != null){
		tabbar.setOnTabSelectedListener(new OnTabSelectedListener() {
			@Override
			public void onTabSelected(int index) {
				changeContentFragment(index);
			}
		});
		}
		
	//	OnNewClickListener.se
		tabbar.setOnNewClickedListener(new OnNewClickListener() {
			
			@Override
			public void onNewClicked() {
				bringtoUp();
			}
		});
	}
	
	protected void bringtoUp() {
		//bottom to top
		Intent itt  = new Intent(this, SendNewActivity.class);
		startActivity(itt);
		overridePendingTransition(R.anim.slide_in_bottom, R.anim.none);

	}

	@Override
	protected void onResume() {
		super.onResume();
		
		//if(tabbar.getSelectedIndex() <0)
			tabbar.setSelectedItem(3);
	}
	
	
	 void changeContentFragment(int index) {
		 Fragment  frag = null;
		 
		 switch(index){
		 case 0: frag = contentFeedList; break;
		 case 1:frag = contentNoteList; break;
		 case 2: frag = contentSearchPage; break;
		 case 3: frag = contentMyprofile; break;
		 default: break;
		 }
		 
		 if (frag == null) return; //保护措施，当frag为空时，返回
		 
		 getFragmentManager()
		 .beginTransaction()
		 .replace(R.id.content, frag)
		 .commit();
		 
	}



	
}
