package com.gob.rewmobile.util;

import java.util.ArrayList;

import com.gob.rewmobile.R;
import com.gob.rewmobile.objects.BaseClass;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BloqueAdapter extends BaseAdapter {
	
	private LayoutInflater inflater = null;
	private ArrayList<BaseClass> baseClass;
	public static String ITEM_MOZO = "ITEM_MOZO";
	public static String ITEM_PRODUCTO = "ITEM_PRODUCTO";
	private String GRID_ITEM = null;
    
    public BloqueAdapter(Context context, ArrayList<BaseClass> baseClass, String item){
        this.inflater = LayoutInflater.from(context);
        this.baseClass = baseClass;
        Log.i("SIZE", "SIZE: "+baseClass.size());
        this.GRID_ITEM = item;
    }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView tv;
    	if(convertView==null){
    		convertView = this.inflater.inflate(R.layout.layout_bloque, null);
            if(this.GRID_ITEM.equals(BloqueAdapter.ITEM_MOZO)){
            	tv = (TextView)convertView.findViewById(R.id.grid_item);
            } else if(this.GRID_ITEM.equals(BloqueAdapter.ITEM_PRODUCTO)) {
            	tv = (TextView)convertView.findViewById(R.id.grid_item_producto);
            } else {
            	tv = (TextView)convertView.findViewById(R.id.grid_item);
            }
            
            convertView.setTag(tv);
            
        } else {
        	Log.i("convertView", "convertView");
        	tv = (TextView) convertView.getTag();
        }
    	
    	BaseClass baseClass = (BaseClass) getItem(position);
        tv.setVisibility(View.VISIBLE);
        Log.i("PRODUCTO", baseClass.getName()+" POSICION: "+position);
        tv.setText(baseClass.getName());
        
		return convertView;
	}
    
	@Override
	public int getCount() {return baseClass.size();}

	@Override
	public Object getItem(int position) {return baseClass.get(position);}

	@Override
	public long getItemId(int position) {return position;} //return baseClass.get(position).getId();
}