package com.myapplication;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by alan on 17-6-28.
 */
public class ImageFolderPopWindow extends PopupWindow {

    HashMap<String, ImageDirs> dirMap;
    LayoutInflater inflaotor;
    ListView lvDir;
    Context context;
    LocalImageLoader imageLoader;

    public OnClickListener onItemClickListner;

    public void setOnPopClickListener(OnClickListener listener){
        onItemClickListner=listener;
    }

    public ImageFolderPopWindow(Context context,int width,int height) {
        imageLoader=new LocalImageLoader();
        inflaotor = LayoutInflater.from(context);
        setContentView(initView());
        setHeight(height);
        setWidth(width);
        setFocusable(true); // 设置PopupWindow可获得焦点
        setTouchable(true); // 设置PopupWindow可触摸
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
        this.context=context;
    }

    private View initView() {
        View view = inflaotor.inflate(R.layout.popwindow_img_dir, null);
        lvDir = (ListView) view.findViewById(R.id.lv_img_dir);
        return view;
    }

    public void popWindow(HashMap<String, ImageDirs> dirMap, View view) {
        this.dirMap = dirMap;
        lvDir.setAdapter(new ImageDirAdapter(dirMap));
        showAsDropDown(view);
    }

    class ImageDirAdapter extends BaseAdapter {
        ImageDirs[] dirs;

        public ImageDirAdapter(HashMap<String, ImageDirs> dirMaps) {
            dirs = dirMaps.values().toArray(new ImageDirs[dirMaps.size()]);
        }

        @Override
        public int getCount() {
            return dirs.length;
        }

        @Override
        public ImageDirs getItem(int position) {
            return dirs[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ImageDirs dir=getItem(position);
            ViewHolder viewHolder = null;
            if(convertView==null){
                convertView=inflaotor.inflate(R.layout.list_item_dir_pop_window, null);
                viewHolder=new ViewHolder();
                viewHolder.imgHeader=(ImageView) convertView.findViewById(R.id.img_dir_header);
                viewHolder.tvTitle=(TextView) convertView.findViewById(R.id.tv_dir_title);
                viewHolder.imgDot=(ImageView) convertView.findViewById(R.id.img_dir_dot);
                convertView.setTag(viewHolder);
            }else{
                viewHolder=(ViewHolder) convertView.getTag();
            }
            viewHolder.tvTitle.setText(dir.getDirName()+" ("+dir.getFiles().size()+")");
            imageLoader.displayImage(viewHolder.imgHeader, dir.firstImagePath);
            if(dir.selectedFiles.size()>0){
                viewHolder.imgDot.setVisibility(View.VISIBLE);
            }else{
                viewHolder.imgDot.setVisibility(View.INVISIBLE);
            }

            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    v.setTag(dir);
                    onItemClickListner.onClick(v);
                }
            });

            return convertView;
        }

        public class ViewHolder{
            TextView tvTitle;
            ImageView  imgHeader;
            ImageView imgDot;
        }
    }
}
