package com.example.sample.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.dblib.bean.FilePath;
import com.example.sample.holders.FilePathHolder;

import java.io.File;
import java.util.List;


public class FilePathAdapter extends BaseAdapter {

    private final List<FilePath> paths;

    public FilePathAdapter(List<FilePath> paths){
        this.paths = paths;
    }

    @Override
    public int getCount() {
        return paths.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FilePathHolder holder = null;
        if(convertView == null){
            holder = new FilePathHolder(parent.getContext());
        }else{
            holder = (FilePathHolder) convertView.getTag();
        }
        holder.bindData(paths.get(position));
        return holder.getView();
    }
}
