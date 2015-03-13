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

class ListViewAdapter extends BaseAdapter {

	private List<PackageInfo> packageInfos = null;
	private LayoutInflater inflater = null;
	private  Context context = null;

	public ListViewAdapter(List<PackageInfo>  packageInfos , Context context){
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
		View view = inflater.inflate(R.layout.listviewitem, null);
		TextView appName = (TextView) view.findViewById(R.id.lv_item_appname);
		TextView packageName = (TextView) view.findViewById(R.id.lv_item_packagename);
		ImageView iv = (ImageView) view.findViewById(R.id.lv_icon);

		appName.setText(packageInfos.get(position).applicationInfo.loadLabel(context.getPackageManager()));

		packageName.setText(packageInfos.get(position).packageName);

		iv.setImageDrawable(packageInfos.get(position).applicationInfo.loadIcon(context.getPackageManager()));	
		return view;
	}	
}
