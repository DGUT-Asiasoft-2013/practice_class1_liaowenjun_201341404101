package fragments;

import com.example.hello.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainTabbarFragment extends Fragment {
	View view;
	private View btnNew;
	private View tabFirst;
	private View tabMsg;
	private View tabFound;
	private View tabMe;
	 int selectedIndex ;
	View[] tabs;
	public MainTabbarFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		view = inflater.inflate(R.layout.fragment_main_tabbar, null);
		
		btnNew = view.findViewById(R.id.img_new);
		tabFirst = view.findViewById(R.id.img0);
		tabMsg = view.findViewById(R.id.img1);
		tabFound = view.findViewById(R.id.img2);
		tabMe = view.findViewById(R.id.img3);
		
		tabs = new View[] {
				tabFirst,tabMsg,tabFound,tabMe
		};
		
		for(final View tab: tabs){		
			tab.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
						onTabClicked(tab);
				}
			});
		}
		
		btnNew.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onNewClicked();
			}
		});
		
		
		return view;
	}

	
		
	 
	 public static interface OnTabSelectedListener{
		 void onTabSelected(int index);
	 }
	 
	 OnTabSelectedListener onTabSelectedListener;
	
	 public void setOnTabSelectedListener(OnTabSelectedListener onTabSelectedListener){
		 this.onTabSelectedListener = onTabSelectedListener;
	 }

	public void setSelectedItem(int i) {
		if(i>=0 && i< tabs.length){
			onTabClicked(tabs[i]);
		}
	}
	 
	 void onTabClicked(View tab) {
		  selectedIndex = -1;
			
			for(int i=0; i<tabs.length; i++){
				View otherTab = tabs[i];
				if(otherTab == tab){
					otherTab.setSelected(true);
					selectedIndex = i;
				}else{
					otherTab.setSelected(false);
				}
			}
			
			if(onTabSelectedListener!=null && selectedIndex>=0){
				onTabSelectedListener.onTabSelected(selectedIndex);
	}
		
	}
	 
	 OnNewClickListener onNewClickListener;
	 public void setOnNewClickedListener(OnNewClickListener listener){
		 this.onNewClickListener = listener;
	 }

	 public static interface OnNewClickListener{
		 void onNewClicked();
	 }
	 
	 void onNewClicked(){
		 if(onNewClickListener != null){
			 onNewClickListener.onNewClicked();
		 }
	 }

	public int getSelectedIndex() {
		return  selectedIndex;
	}
	

	 
}
