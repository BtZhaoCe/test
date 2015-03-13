package com.guo.apkmanager;
import java.util.List; 
import android.content.Context;
import android.content.pm.PackageInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

class GridViewAdapter extends BaseAdapter {
	private List<PackageInfo> packageInfos = null;
	private LayoutInflater inflater = null;//inflater的作用是将xml文件转换成视图
	private  Context context = null;
	public GridViewAdapter(List<PackageInfo>  packageInfos , Context context){
		this.packageInfos = packageInfos; 
		this.context = context ; 
		inflater = LayoutInflater.from(context);
	}
	public int getCount() {
		return packageInfos.size();
	}

	public Object getItem(int arg0) {
		return packageInfos.get(arg0);
	}

	public long getItemId(int arg0) {
		return arg0;
		
	}

	public View getView(int position, View contentView, ViewGroup parent) {
		View view = inflater.inflate(R.layout.gridviewitem, null);
		TextView tv = (TextView) view.findViewById(R.id.gv_item_appname);
		ImageView iv = (ImageView) view.findViewById(R.id.gv_item_icon);

		tv.setText(packageInfos.get(position).applicationInfo.loadLabel(context.getPackageManager()));

		iv.setImageDrawable(packageInfos.get(position).applicationInfo.loadIcon(context.getPackageManager()));
		return view;
	}	
}
