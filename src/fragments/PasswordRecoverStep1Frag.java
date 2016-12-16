package fragments;

import com.example.hello.R;
import android.app.*;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnCreateContextMenuListener;
import inputcells.SimpleTextInputCellFragment;

public class PasswordRecoverStep1Frag extends Fragment{
	SimpleTextInputCellFragment fragEmail;
	View view ;
	public PasswordRecoverStep1Frag() {

	}
	
	public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle saveIntancestate ){
		if( view ==null){
		view = inflater.inflate(R.layout.fragment_password_recover_step1, null);
		
		fragEmail = (SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.input_email);
		view.findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				goNext();
			}
		});
		}
		return view;

	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		fragEmail.setLabelText("邮箱");
		fragEmail.setHintText("邮箱地址");
	}
		
	 void goNext() {
		if (onGoNextListener != null) {
			onGoNextListener.onGoNext();
}
}
	public static interface OnGoNextListener{
		void onGoNext();
	}
	
	OnGoNextListener onGoNextListener;

	public void setOnGoNextListener(OnGoNextListener onGoNextListener) {
		this.onGoNextListener = onGoNextListener;
	}

	public String getText(){
		return fragEmail.getText();
	}

}
