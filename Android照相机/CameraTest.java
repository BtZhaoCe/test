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
	//surfaceView����    
   private SurfaceView mySurfaceView;
   private SVDraw mDraw;
    //surfaceHolder���� 
    SurfaceHolder holder;   
    //�������    
    Camera myCamera;
   
    //��Ƭ����·��    
    String filePath="/sdcard/Pictures/";
    //�Ƿ�����־λ
    boolean isClicked = false; 
    boolean isVideo = true;
    //���հ�ť
    ImageButton capture;
    //��Ƭ����ͼ
    ImageView editPic;
    Context mContext;
    Intent intent;
    Button bt;
    Button bt1;
    Spinner sp;
    
    //����jpegͼƬ�ص����ݶ���    
    /** Called when the activity is first created. */    
    @Override    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      
	//�ޱ���      mContainer = (LinearLayout) findViewById(R.id.container);
       ;
        requestWindowFeature(Window.FEATURE_NO_TITLE);         
        //�������㷽��    
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);   
        setContentView(R.layout.main);    
        //��ÿؼ�    
        mySurfaceView = (SurfaceView)findViewById(R.id.surfaceView1);    
        //��þ��    
        holder = mySurfaceView.getHolder();    
        //��ӻص�    
        holder.addCallback(this);   
        mContext=this;
        //�������ͣ�û����佫����ʧ��
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        //���հ�ť
        capture=(ImageButton)findViewById(R.id.capture);
        //����ͼ
        editPic=(ImageView)findViewById(R.id.editPic);
        bt = (Button)findViewById(R.id.button);
        bt1 = (Button)findViewById(R.id.button1);
        mDraw = (com.supermario.cameratest.SVDraw)findViewById(R.id.mDraw);
        
       sp = (Spinner)findViewById(R.id.spinner1);
        //���ð���������    
        capture.setOnClickListener(takePicture);  
        editPic.setOnClickListener(editOnClickListener);		
		bt.setOnClickListener(buttonOnClickListener);
		bt1.setOnClickListener(button1OnClickListener);
		sp.setOnItemSelectedListener(sp1OnItemClickListener);
		
    } 
    
   
   
    public void surfaceChanged(SurfaceHolder holder, int format, int width,    
            int height) {    
        //���ò���
        Camera.Parameters params = myCamera.getParameters();
        
        params.setPictureFormat(PixelFormat.JPEG);
        
        myCamera.setParameters(params);   
        
        myCamera.setDisplayOrientation(90);
        
        //��ʼԤ��    
        myCamera.startPreview(); 
       
      
    }    
    public void surfaceCreated(SurfaceHolder holder) {    
        //�������    
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
        //�ر�Ԥ�����ͷ���Դ    
        myCamera.stopPreview();       
        myCamera.release();    
        myCamera = null;                  
    }
    //�鿴ͼƬ
    OnClickListener editOnClickListener=new OnClickListener(){
		public void onClick(View v) {
			
				String picPath=(String) v.getTag();
		        intent=new Intent();
		        //��ͼƬ��·���󶨵�intent��
		        intent.putExtra("path", picPath);
		        intent.setClass(mContext, Picture.class);
		        //�����鿴ͼƬ����
				startActivity(intent);
				//�رյ�ǰ����
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
                    "������Ԥ����ť",  
                    Toast.LENGTH_SHORT).show();  
            surfaceCreated(holder); /*�������*/
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
	//���հ���������
    OnClickListener takePicture=new OnClickListener(){
		public void onClick(View v) {
	        if(!isClicked)    
	        {    
	        	//�Զ��Խ�    
	            myCamera.autoFocus(CameraTest.this);
	            Vibrator vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
	            vibrator.vibrate(100);
	            isClicked = true;    
	        }else    
	        {    
	        	//����Ԥ��  
	            myCamera.startPreview();  
	            isClicked = false;    
	        }
		}   
	};
    //�Զ��Խ�ʱ����
    public void onAutoFocus(boolean success, Camera camera) {    
        if(success)    
        {    
            //��ò��� 
            Camera.Parameters params = myCamera.getParameters();    
            //���ò���
            params.setPictureFormat(PixelFormat.JPEG);      
            myCamera.setParameters(params);    
            //����
            myCamera.takePicture(null, null, jpeg);    
        }              
    }    
 
    PictureCallback jpeg = new PictureCallback() {               
        public void onPictureTaken(byte[] data, Camera camera) {    
            try    
            {
            // ���ͼƬ    
            Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);     
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");     
            String date = sDateFormat.format(new java.util.Date()); 
            filePath=filePath+date+".jpg";
            File file = new File(filePath);
            
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));  
	          //��������ͼƬ�õ�matrix����
	   		 Matrix matrix = new Matrix();
	   		 matrix.postRotate(90);
	   		 //�����µ�ͼƬ
	   		 Bitmap rotateBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
            //��ͼƬ��JPEG��ʽѹ��������    
	   		rotateBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
         	//���    
            bos.flush();
            //�ر�    
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
		 
		 //����Ԥת���ɵ�ͼƬ�Ŀ�͸�
		 int newWidth = 100;
		 
		 //���������ʣ��³ߴ��ԭ�ߴ�
		 float scaleWidth = (float)newWidth/width;
		 float scaleHeight = scaleWidth;
		 //��������ͼƬ�õ�matrix����
		 Matrix matrix = new Matrix();	 
		 //����ͼƬ����
		 matrix.postScale(scaleWidth, scaleHeight);
		 //�����µ�ͼƬ
		 Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0, width, height, matrix, true);		 
		 //�����洴����Bitmapת����Drawable����ʹ�������ʹ����imageView��imageButton�ϡ�
		 BitmapDrawable bitmapDrawable = new BitmapDrawable(resizedBitmap);
		 return bitmapDrawable;
    }    
      }

