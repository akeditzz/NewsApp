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
        if (!TextUtils.isEmpty(list.get(position).getmWebTitle())) {
            holder.tvTitle.setText(list.get(position).getmWebTitle());
        } else {
            holder.tvTitle.setText(R.string.label_news_title);
        }

        if (!TextUtils.isEmpty(list.get(position).getmSectionName())) {
            holder.tvSection.setText(list.get(position).getmSectionName());
        } else {
            holder.tvSection.setText(R.string.label_section);
        }

        if (!TextUtils.isEmpty(list.get(position).getmWebPublicationDate())) {
            holder.tvDate.setText(getFormatedDate(list.get(position).getmWebPublicationDate()));
        } else {
            holder.tvDate.setText(R.string.label_date);
        }

        if (list.get(position).getmContributorList() != null && !list.get(position).getmContributorList().isEmpty()) {
            holder.tvAuthor.setVisibility(View.VISIBLE);
            StringBuilder authors = new StringBuilder();
            authors.append(context.getString(R.string.label_hyphen));
            for (int i = 0; i < list.get(position).getmContributorList().size(); i++) {
                if (i == 0) {
                    authors.append(list.get(position).getmContributorList().get(i).getmWebTitle());
                } else {
                    authors.append(context.getString(R.string.label_comma));
                    authors.append(list.get(position).getmContributorList().get(i).getmWebTitle());
                }
            }

            if (!TextUtils.isEmpty(authors)) {
                holder.tvAuthor.setText(authors);
            } else {
                holder.tvAuthor.setVisibility(View.GONE);
            }
        } else {
            holder.tvAuthor.setVisibility(View.GONE);
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
        TextView tvTitle;
        TextView tvSection;
        TextView tvDate;
        TextView tvAuthor;

        ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvSection = itemView.findViewById(R.id.tv_section);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvAuthor = itemView.findViewById(R.id.tv_authors);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.openUrl(list.get(getAdapterPosition()).getmWebUrl());
                }
            });
        }
    }
}
