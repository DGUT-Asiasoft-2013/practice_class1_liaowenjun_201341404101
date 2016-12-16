package fragments;

import com.example.hello.R;
import com.example.hello.R.id;
import com.example.hello.R.layout;

import android.app.Fragment;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class VersionFragment extends Fragment{
	public VersionFragment(){}
	
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle saveIntancestate ){
		View view = inflater.inflate(R.layout.fragment_version, null);
		TextView textVersion = (TextView) view.findViewById(R.id.text1);
		
		PackageManager pkgm = getActivity().getPackageManager();
		
		try{
			PackageInfo appinfo = pkgm.getPackageInfo(getActivity().getPackageName(),0);
			textVersion.setText(appinfo.packageName+ " "+ appinfo.versionName);
			
		}catch(NameNotFoundException e){
			e.printStackTrace();
			textVersion.setText("Error");
		}
		return view;
		
	}
}
