package com.example.sample.holders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dblib.bean.FilePath;
import com.example.sample.R;

/**
 *
 * @author YGX
 */
public class FilePathHolder extends ViewHolder<FilePath> {
    private ImageView mIvIcon;
    private TextView mTvName;

    public FilePathHolder(Context ctx) {
        super(ctx);
    }

    @Override
    protected View onCreateView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.item_filepath,null);
    }

    @Override
    protected void onViewCreated(View v) {
        mIvIcon = v.findViewById(R.id.filepath_iv_icon);
        mTvName = v.findViewById(R.id.filepath_tv_name);
    }

    @Override
    public void bindData(FilePath data) {
        mIvIcon.setImageResource(data.isDir == FilePath.TYPE_DIR ? R.mipmap.ic_folder : R.mipmap.ic_file);
        mTvName.setText(data.getName());
    }
}
