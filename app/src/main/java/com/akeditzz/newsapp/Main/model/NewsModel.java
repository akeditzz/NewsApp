package com.akeditzz.newsapp.Main.model;

import java.util.ArrayList;

public class NewsModel {
    private String mSectionName;
    private String mWebPublicationDate;
    private String mWebTitle;
    private String mWebUrl;
    private ArrayList<ContributorModel> mContributorList;

    public NewsModel(String sectionName, String webPublicationDate, String webTitle, String webUrl, ArrayList<ContributorModel> contributorList) {
        this.mSectionName = sectionName;
        this.mWebPublicationDate = webPublicationDate;
        this.mWebTitle = webTitle;
        this.mWebUrl = webUrl;
        this.mContributorList = contributorList;
    }

    public String getmSectionName() {
        return mSectionName;
    }

    public String getmWebPublicationDate() {
        return mWebPublicationDate;
    }

    public String getmWebTitle() {
        return mWebTitle;
    }

    public String getmWebUrl() {
        return mWebUrl;
    }

    public ArrayList<ContributorModel> getmContributorList() {
        return mContributorList;
    }
}
