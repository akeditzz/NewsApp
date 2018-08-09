package com.akeditzz.newsapp.Main.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akeditzz.newsapp.Main.interfaces.INews;
import com.akeditzz.newsapp.Main.model.NewsModel;
import com.akeditzz.newsapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    //declaration
    private Context context;
    private ArrayList<NewsModel> list;
    private INews mCallback;

    public NewsAdapter(Context context, ArrayList<NewsModel> list, INews mCallback) {
        this.context = context;
        this.list = list;
        this.mCallback = mCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_news_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (!TextUtils.isEmpty(list.get(position).getWebTitle())) {
            holder.tv_title.setText(list.get(position).getWebTitle());
        } else {
            holder.tv_title.setText(R.string.label_news_title);
        }

        if (!TextUtils.isEmpty(list.get(position).getSectionName())) {
            holder.tv_section.setText(list.get(position).getSectionName());
        } else {
            holder.tv_section.setText(R.string.label_section);
        }

        if (!TextUtils.isEmpty(list.get(position).getWebPublicationDate())) {
            holder.tv_date.setText(getFormatedDate(list.get(position).getWebPublicationDate()));
        } else {
            holder.tv_date.setText(R.string.label_date);
        }

    }

    /**
     * Convert date coming to api to required date format
     *
     * @param dateString date string form api
     * @return formated date string
     */
    private String getFormatedDate(String dateString) {
        try {
            SimpleDateFormat apiDateFormat = new SimpleDateFormat(context.getString(R.string.api_date_format));
            SimpleDateFormat requiredDateFormat = new SimpleDateFormat(context.getString(R.string.req_date_format));
            Date date = apiDateFormat.parse(dateString);
            return requiredDateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        //declaration
        TextView tv_title;
        TextView tv_section;
        TextView tv_date;

        ViewHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_section = itemView.findViewById(R.id.tv_section);
            tv_date = itemView.findViewById(R.id.tv_date);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.openUrl(list.get(getAdapterPosition()).getWebUrl());
                }
            });
        }
    }
}
