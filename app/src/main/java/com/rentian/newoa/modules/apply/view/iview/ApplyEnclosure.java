package com.rentian.newoa.modules.apply.view.iview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.rentian.newoa.R;

import java.io.File;
import java.util.ArrayList;

/**
 * 申请页面 → 添加附件
 */
public class ApplyEnclosure extends FrameLayout implements onResultFileData {

    private Context mContext;

    private ArrayList<File> enclosures;
    private ListView listView;
    private EnclosureAdapter adapter;
    private onStartFilePicker filePicker;

    public ApplyEnclosure(Context context) {
        this(context, null);
    }

    public ApplyEnclosure(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ApplyEnclosure(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.apply_enclosure, this);
        listView = (ListView) findViewById(R.id.apply_enclosure_lv);

        this.mContext = context;

        enclosures = new ArrayList<>();
        adapter = new EnclosureAdapter();
        listView.setAdapter(adapter);

        filePicker = (onStartFilePicker) mContext;

        listener();
    }

    public onResultFileData addResultFileData() {
        return this;
    }

    private void listener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == adapter.getCount() - 1) {
                    filePicker.StartFilePicker();
                } else {
                    enclosures.remove(position);
                    notifyDataSetChanged();
                }
            }
        });
    }

    public void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void resultFileData(File file) {

        if (file != null) {
            // TODO 此方法需要修改
//            ExFilePickerParcelObject object = data.getParcelableExtra(ExFilePickerParcelObject.class.getCanonicalName());
//            if (object.count > 0) {
//                for (int i = 0; i < object.count; i++) {
//                    Log.e("file", object.names.get(i));
//                    File file = new File(object.path,object.names.get(i));
                    enclosures.add(file);
//                }
//            }
        }
    }

    private class EnclosureAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return enclosures.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            return enclosures.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_apply_enclosure, parent, false);
            TextView text = (TextView) convertView
                    .findViewById(R.id.item_apply_enclosure_tv);
            ImageView img = (ImageView) convertView
                    .findViewById(R.id.item_apply_enclosure_iv);
            if (position == enclosures.size()) {
                img.setImageResource(R.drawable.add_clrcle);
                text.setText("添加附件");
                text.setTextColor(Color.GRAY);
            } else {
                img.setImageResource(R.drawable.remove_circle);
                text.setText(((File) getItem(position)).getName());
                text.setTextColor(Color.BLACK);
            }
            return convertView;
        }
    }
}
