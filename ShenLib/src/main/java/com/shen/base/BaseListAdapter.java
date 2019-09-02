package com.shen.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Administrator on 2018/3/13 0013.
 */
public abstract class BaseListAdapter<T> extends BaseAdapter implements AdapterView.OnItemClickListener,AdapterView.OnItemSelectedListener{
    protected  int          selectPosition=-1;                                                      //当前选择的偏移量
    protected  List<T>      dataList;                                                               //当前数据源
    public BaseListAdapter(){
        super();
    }
    public BaseListAdapter(List<T> data) {
        super();
        this.dataList=data;
    }
    public void setDataList(List<T> data){
        this.dataList=data;
    }
    @Override
    public int getCount() {
        if(dataList==null)
            return 0;
        else
            return dataList.size();
    }
    @Override
    public Object getItem(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);

//    {
//        BaseHolder holder;
//        if (convertView == null) {
//            holder=new BaseHolder(getActivityThis());
//            convertView=holder.getView();
//            convertView.setTag(holder);
//        }else {
//            holder = (BaseHolder)convertView.getTag();
//        }
//        try {
//        }catch (Exception e){
//        }
//        return holder.getView();
//    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        selectPosition=-1;
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectPosition=position;
    }

    public void setSelectPositionDef(int def){
        selectPosition=def;
    }

}

