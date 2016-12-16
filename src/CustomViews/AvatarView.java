package CustomViews;

import java.io.IOException;

import com.example.hello.R;
import api.Server;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import utils.User;

@SuppressLint("DrawAllocation")
public class AvatarView extends View {
	public Canvas canvas;
	public Shader shader;
	float srcWidth, srcHeight;
	public AvatarView(Context context) {
		super(context);
	
	}

	public AvatarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		
	}

	public AvatarView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	Handler mainThreadHandler = new Handler();
	 Paint paint;
	 float radius;
	
		
	 public void setBitmap(Bitmap bitmap){
		 	try {
		 		if(bitmap == null){
		 			
		 	
				paint = new Paint();
				paint.setColor(Color.GRAY);
				paint.setStyle(Paint.Style.STROKE);
				paint.setStrokeWidth(1);
				paint.setPathEffect(new DashPathEffect(new float[]{5, 10, 15, 20}, 0));
				paint.setAntiAlias(true);
		 		}else{
		 			
		 			paint = new Paint();
				 paint.setShader(new BitmapShader(bitmap, Shader.TileMode.REPEAT,Shader.TileMode.REPEAT));
				 paint.setAntiAlias(true);
				 radius = Math.min(bitmap.getWidth(), bitmap.getHeight())/2;
				 
				 srcWidth = bitmap.getWidth();
				 srcHeight = bitmap.getHeight();
		 		}
				invalidate();
		 
			} catch (Exception e) {
				e.printStackTrace();
			}
			
	 }
	 

	 public void load(User user){
		 load(Server.serverAddress+ user.getAvatar());
		
	 }
	 public void load(String url){
		 OkHttpClient client = new OkHttpClient();
		 
		 Request request = new Request.Builder()
				 .url(url)
				 .method("GET", null)
				 .build();
		 
		 client.newCall(request).enqueue(new  Callback() {
			
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				try {
					byte[] bytes = arg1.body().bytes();
					final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
					mainThreadHandler.post(new Runnable() {
						
						@Override
						public void run() {
							setBitmap(bitmap);
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				mainThreadHandler.post(new Runnable() {
					
					@Override
					public void run() {
						setBitmap(null);
					}
				});
			}
		});
	 }
	 
	 @Override
		public void draw(Canvas canvas) {
			super.draw(canvas);
			if(paint!=null){
				canvas.save();
				
				float dstW = getWidth();
				float dstH = getHeight();
				
				float scaleX = srcWidth/dstW;
				float scaleY = srcHeight/dstH;
				
				canvas.scale(1/scaleX, 1/scaleY);
				canvas.drawCircle(getWidth()/2, getHeight()/2, Math.min(srcWidth, srcHeight)/2, paint);	
				
				canvas.restore();
	}
	 }
}
