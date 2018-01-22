package com.kw2.kw2.sit;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by SAMSUNG on 2018-01-21.
 */

public class ListAdapter extends ArrayAdapter<ListViewItem> {

    public interface ListBtnClickListener {
        void onListBtnClick(int id) ;
    }

    private ListBtnClickListener listBtnClickListener ;
    private final Activity context;
    int resourceId ;
    private DBHelper helper;

    public ListAdapter(Activity context, int resource , ArrayList<ListViewItem> list, ListBtnClickListener clickListener){
        super(context, resource, list);
        this.context = context;
        this.resourceId = resource;
        this.listBtnClickListener = clickListener;
        helper = new DBHelper(context);
    }

    @Override
    public android.view.View getView(int position, @Nullable android.view.View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(this.resourceId, parent, false);
        }

        final ListViewItem listViewItem = (ListViewItem) getItem(position);

        TextView timeName = (TextView) convertView.findViewById(R.id.item_timeName);

        Button deleteBtn = (Button) convertView.findViewById(R.id.item_deleteBtn);

        timeName.setText(listViewItem.getTimeName());


        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 삭제 작업
                listBtnClickListener.onListBtnClick(listViewItem.getId()) ;
            }
        });

        return convertView;
    }

}

