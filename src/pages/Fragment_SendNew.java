package pages;

import com.example.hello.R;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Fragment_SendNew extends Fragment {
	View view;
	public Fragment_SendNew() {
		// TODO Auto-generated constructor stub
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(view== null){
			 view = inflater.inflate(R.layout.fragment_new,null);
		}
		return view;
	}
}
