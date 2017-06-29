package com.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LocalImageLoader {
	Executor threadPool;
	
	Context context;
	int cacheSize=(int) (Runtime.getRuntime().freeMemory()/8);
	LruCache<String, Bitmap> cache;
	
	Handler handler=new Handler();
	
	public LocalImageLoader(){
		threadPool=Executors.newFixedThreadPool(10);
		
		cache=new LruCache<String, Bitmap>(cacheSize){
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getRowBytes();
			}
		};
	}
	
	public void displayImage(ImageView imageView,String filePath){
		if(filePath==null)
			{
			imageView.setImageResource(R.drawable.img_empty);
			return;
			}
		threadPool.execute(new LoadTask(imageView, filePath));
	}
	
	public class LoadTask implements Runnable{

		ImageView imageView;
		String filePath;
		Bitmap bitmap;
		public LoadTask(ImageView imageView,String filePath){
			this.imageView=imageView;
			this.filePath=filePath;
		}
		
		@Override
		public void run() {
			bitmap=cache.get(filePath);
			if(bitmap!=null){
				showBitmap(imageView, bitmap);
				//imageView.setImageBitmap(bitmap);
				return;
			}
			
			bitmap=decodeSampledBitmapFromResource(filePath,1024,900);
			try {
				cache.put(filePath, bitmap);
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			
			showBitmap(imageView, bitmap);
			
		}
		
	}
	
	
	public void showBitmap( final ImageView imageView,final Bitmap bitmap){
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				imageView.setImageBitmap(bitmap);
			}
		});
	}
	
	
	/**
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	private int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight)
	{
		int width = options.outWidth;
		int height = options.outHeight;
		int inSampleSize = 1;

		if (width > reqWidth && height > reqHeight)
		{
			int widthRatio = Math.round((float) width / (float) reqWidth);
			int heightRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = Math.max(widthRatio, heightRatio);
		}
		return inSampleSize;
	}

	/**
	 * 
	 * @param pathName
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	private Bitmap decodeSampledBitmapFromResource(String pathName,
			int reqWidth, int reqHeight)
	{
		Log.e("path", pathName+" width"+reqWidth+"reqHeight");

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(pathName, options);
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(pathName, options);

		return bitmap;
	}

}
