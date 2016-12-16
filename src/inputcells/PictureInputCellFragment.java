package inputcells;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.example.hello.R;

import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.DialogInterface; 

public class PictureInputCellFragment extends BaseInputCellFrag{

		TextView label;
		TextView hintText;
		ImageView image;
		private int FILE_SELECT_CODE =0;
		private int REQUEST_CAMERA = 1;
		private int REQUEST_ALBUM = 2;
		public byte[] pngData;
		
		@Override
		public View onCreateView(LayoutInflater flater, ViewGroup container, Bundle sis){
			View view = flater.inflate(R.layout.fragment_input_picture, container);
			
			label = (TextView) view.findViewById(R.id.label);
			hintText = (TextView) view.findViewById(R.id.hint);
			image = (ImageView) view.findViewById(R.id.image);
			
			image.setOnClickListener(new OnClickListener(){  
				  
			    @Override  
			    public void onClick(View v) {  
			    	onImageViewClick();
			    }

				

			      
			}  );  
			
			
			return view;
		}
		
		
		private void onImageViewClick() {
			String[] items = {"拍照", "相册"};
		
			DialogInterface.OnClickListener actionListener = new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch(which){
					case 0:
						takePhoto();
						break;
					case 1:
						pickFromAlbum();
						break;
					default:
						break;
					}
				}
					};
			
			new AlertDialog.Builder(getActivity())
			.setTitle(label.getText())
			//.setMessage(hintText.getText())
			.setItems(items ,actionListener)
			.setNegativeButton("取消", null)
			.show();
			

			
			
		};
		
//选择相册
		private void pickFromAlbum() {
			 Intent intent = new Intent(Intent.ACTION_GET_CONTENT); 
			 intent.setType("image/*");
			 startActivityForResult(intent, REQUEST_ALBUM);
			  
		}
//拍照
		private void takePhoto() {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intent, REQUEST_CAMERA);
		}
		
		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data){
			if(resultCode == Activity.RESULT_CANCELED){
				return;
			}
			
			if(requestCode == REQUEST_CAMERA){
				Bitmap bmp = (Bitmap) data.getExtras().get("data");
				saveBitmap(bmp);
				image.setImageBitmap(bmp);
				//Toast.makeText(getActivity(), data.getDataString(),Toast.LENGTH_SHORT).show();
			}else if(requestCode == REQUEST_ALBUM)
			{ Bitmap bmp;
				  try {
						bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
						saveBitmap(bmp);
						image.setImageBitmap(bmp);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		}

		void saveBitmap(Bitmap bmp){
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bmp.compress(CompressFormat.PNG, 100, baos);
			pngData = baos.toByteArray();
		}
		
		public byte[] getPngData() {
	
			return pngData;
		}
}
