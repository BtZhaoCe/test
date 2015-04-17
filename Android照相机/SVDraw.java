package com.supermario.cameratest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SVDraw extends SurfaceView implements SurfaceHolder.Callback{
	protected SurfaceHolder sh;  
    private int mWidth;  
    private int mHeight;  
    
	public SVDraw(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		    sh = getHolder();  
	        sh.addCallback(this);  
	        sh.setFormat(PixelFormat.TRANSPARENT);  
	        setZOrderOnTop(true);  
	         
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		 mWidth = width;  
	        mHeight = height; 
		
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}
	void clearDraw()  
    {  
        Canvas canvas = sh.lockCanvas();  
        canvas.drawColor(Color.BLUE);  
        sh.unlockCanvasAndPost(canvas);  
    }  
    public void drawLine()  
    {  
        Canvas canvas = sh.lockCanvas();  
        canvas.drawColor(Color.TRANSPARENT);  
        Paint p = new Paint();  
        p.setAntiAlias(true);  
        p.setColor(Color.RED);  
        p.setStyle(Style.STROKE);        
        canvas.drawRect(new Rect(150,150,350,350), p);     
        sh.unlockCanvasAndPost(canvas);  
          
    }  

}
