package com.supermario.cameratest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import android.R.color;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.PreviewCallback;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.MediaPlayer.OnCompletionListener;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.Gravity;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;

import org.apache.http.impl.client.RedirectLocations;
public class CameraTest extends Activity implements    
Callback, AutoFocusCallback{    
	//surfaceView声明    
   private SurfaceView mySurfaceView;
   private SVDraw mDraw;
    //surfaceHolder声明 
    SurfaceHolder holder;   
    //相机声明    
    Camera myCamera;
   
    //照片保存路径    
    String filePath="/sdcard/Pictures/";
    //是否点击标志位
    boolean isClicked = false; 
    boolean isVideo = true;
    //拍照按钮
    ImageButton capture;
    //照片缩略图
    ImageView editPic;
    Context mContext;
    Intent intent;
    Button bt;
    Button bt1;
    Spinner sp;
    
    //创建jpeg图片回调数据对象    
    /** Called when the activity is first created. */    
    @Override    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      
	//无标题      mContainer = (LinearLayout) findViewById(R.id.container);
       ;
        requestWindowFeature(Window.FEATURE_NO_TITLE);         
        //设置拍摄方向    
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);   
        setContentView(R.layout.main);    
        //获得控件    
        mySurfaceView = (SurfaceView)findViewById(R.id.surfaceView1);    
        //获得句柄    
        holder = mySurfaceView.getHolder();    
        //添加回调    
        holder.addCallback(this);   
        mContext=this;
        //设置类型，没有这句将调用失败
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        //拍照按钮
        capture=(ImageButton)findViewById(R.id.capture);
        //缩略图
        editPic=(ImageView)findViewById(R.id.editPic);
        bt = (Button)findViewById(R.id.button);
        bt1 = (Button)findViewById(R.id.button1);
        mDraw = (com.supermario.cameratest.SVDraw)findViewById(R.id.mDraw);
        
       sp = (Spinner)findViewById(R.id.spinner1);
        //设置按键监听器    
        capture.setOnClickListener(takePicture);  
        editPic.setOnClickListener(editOnClickListener);		
		bt.setOnClickListener(buttonOnClickListener);
		bt1.setOnClickListener(button1OnClickListener);
		sp.setOnItemSelectedListener(sp1OnItemClickListener);
		
    } 
    
   
   
    public void surfaceChanged(SurfaceHolder holder, int format, int width,    
            int height) {    
        //设置参数
        Camera.Parameters params = myCamera.getParameters();
        
        params.setPictureFormat(PixelFormat.JPEG);
        
        myCamera.setParameters(params);   
        
        myCamera.setDisplayOrientation(90);
        
        //开始预览    
        myCamera.startPreview(); 
       
      
    }    
    public void surfaceCreated(SurfaceHolder holder) {    
        //开启相机    
        if(myCamera == null)    
        {    
            myCamera = Camera.open();    
            try {    
                myCamera.setPreviewDisplay(holder);    
            } catch (IOException e) {    
            	System.err.println(e.getMessage());
                e.printStackTrace(); 
                myCamera.release(); 
            }    
        }    
            
    }    
    public void surfaceDestroyed(SurfaceHolder holder) {    
        //关闭预览并释放资源    
        myCamera.stopPreview();       
        myCamera.release();    
        myCamera = null;                  
    }
    //查看图片
    OnClickListener editOnClickListener=new OnClickListener(){
		public void onClick(View v) {
			
				String picPath=(String) v.getTag();
		        intent=new Intent();
		        //将图片的路径绑定到intent中
		        intent.putExtra("path", picPath);
		        intent.setClass(mContext, Picture.class);
		        //启动查看图片界面
				startActivity(intent);
				//关闭当前界面
				CameraTest.this.finish();
			
		}			
	};     
OnItemSelectedListener sp1OnItemClickListener = new OnItemSelectedListener() {
	
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		Camera.Parameters parameters = myCamera.getParameters();
		switch (arg2) {
		case 0:
			
			parameters.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
			myCamera.setParameters(parameters);
			myCamera.startPreview();
			break;
    case 1:	
	parameters.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_INCANDESCENT);
	myCamera.setParameters(parameters);
	myCamera.startPreview();
	break;
	case 2:
		parameters.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_DAYLIGHT);
		myCamera.setParameters(parameters);
		myCamera.startPreview();
		break;
		case 3:
			parameters.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_CLOUDY_DAYLIGHT);
			myCamera.setParameters(parameters);
			myCamera.startPreview();
			break;
			case 4:
				parameters.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_FLUORESCENT);
				myCamera.setParameters(parameters);
				myCamera.startPreview();
				break;
		default:
			break;
		}
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
};
	OnClickListener button1OnClickListener = new OnClickListener() {
		
		public void onClick(View v) {
			Toast.makeText(CameraTest.this,  
                    "您按了预览按钮",  
                    Toast.LENGTH_SHORT).show();  
            surfaceCreated(holder); /*开启相机*/
            mDraw.setVisibility(View.VISIBLE);            
            mDraw.drawLine(); 
		}
	};
	OnClickListener buttonOnClickListener = new OnClickListener() {
		
		public void onClick(View v) {			
			if (isVideo) {		 				
				  Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);				  
				  intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
				  intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 61);
				  intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 1024 * 1024 * 100);
				  startActivityForResult(intent,11);
				  
			}
		}
	};
	//拍照按键监听器
    OnClickListener takePicture=new OnClickListener(){
		public void onClick(View v) {
	        if(!isClicked)    
	        {    
	        	//自动对焦    
	            myCamera.autoFocus(CameraTest.this);
	            Vibrator vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
	            vibrator.vibrate(100);
	            isClicked = true;    
	        }else    
	        {    
	        	//开启预览  
	            myCamera.startPreview();  
	            isClicked = false;    
	        }
		}   
	};
    //自动对焦时调用
    public void onAutoFocus(boolean success, Camera camera) {    
        if(success)    
        {    
            //获得参数 
            Camera.Parameters params = myCamera.getParameters();    
            //设置参数
            params.setPictureFormat(PixelFormat.JPEG);      
            myCamera.setParameters(params);    
            //拍照
            myCamera.takePicture(null, null, jpeg);    
        }              
    }    
 
    PictureCallback jpeg = new PictureCallback() {               
        public void onPictureTaken(byte[] data, Camera camera) {    
            try    
            {
            // 获得图片    
            Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);     
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");     
            String date = sDateFormat.format(new java.util.Date()); 
            filePath=filePath+date+".jpg";
            File file = new File(filePath);
            
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));  
	          //创建操作图片用的matrix对象
	   		 Matrix matrix = new Matrix();
	   		 matrix.postRotate(90);
	   		 //创建新的图片
	   		 Bitmap rotateBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
            //将图片以JPEG格式压缩到流中    
	   		rotateBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
         	//输出    
            bos.flush();
            //关闭    
            bos.close();
            editPic.setBackgroundDrawable(changeBitmapToDrawable(rotateBitmap));
            editPic.setTag(filePath);
            }catch(Exception e)    
            {    
                e.printStackTrace();    
            }                   
        }    
    };    
    public BitmapDrawable changeBitmapToDrawable(Bitmap bitmapOrg)
    {
    	 int width = bitmapOrg.getWidth();
		 int height = bitmapOrg.getHeight();
		 
		 //定义预转换成的图片的宽和高
		 int newWidth = 100;
		 
		 //计算缩放率，新尺寸除原尺寸
		 float scaleWidth = (float)newWidth/width;
		 float scaleHeight = scaleWidth;
		 //创建操作图片用的matrix对象
		 Matrix matrix = new Matrix();	 
		 //缩放图片动作
		 matrix.postScale(scaleWidth, scaleHeight);
		 //创建新的图片
		 Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0, width, height, matrix, true);		 
		 //将上面创建的Bitmap转换成Drawable对象，使得其可以使用在imageView，imageButton上。
		 BitmapDrawable bitmapDrawable = new BitmapDrawable(resizedBitmap);
		 return bitmapDrawable;
    }    
      }

