package fragments;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import com.example.hello.R;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import api.Server;
import inputcells.SimpleTextInputCellFragment;

public class PasswordRecoverStep2Frag extends Fragment {
	View view;
	private SimpleTextInputCellFragment fragPassword,fragPasswordRepeat;
	
	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(view==null){
			view = inflater.inflate(R.layout.fragment_password_recover_step2, null);
			fragPassword = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_psw1);
			fragPasswordRepeat = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_psw2);
			
			view.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					onSubmitClicked();
				}
			});
}
		return view;
	}
	OnSubmitClickedListener onSubmitClickedListener;


	public void setOnSubmitClickedListener(OnSubmitClickedListener onSubmitClickedListener) {
		this.onSubmitClickedListener = onSubmitClickedListener;
	}

	public interface OnSubmitClickedListener {
		void  onSubmitClicked();
	}

	public String getText(){
		return fragPassword.getText();
	}
	
	public void  onSubmitClicked(){
			if(fragPassword.getText().equals(fragPasswordRepeat.getText())){
				if(onSubmitClickedListener != null){
					onSubmitClickedListener.onSubmitClicked();
				}
			}else{
				new AlertDialog.Builder(getActivity())
				 		.setMessage("password not same!")
				 			.show();
			}
	}

}
