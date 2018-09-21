package com.example.a41448.huawu.view.sideslip.Shop;

public class CardItem {

    private int mTextResource;
    private int mTitleResource;
    private int mText_coin;
    private int mImageView;
    //项目有点大，尽量使用 uri，好改还不占空间
    private String mImageUri;


    public CardItem(int title, int text,int mText_coin,int mImageView) {
        mTitleResource = title;
        mTextResource = text;
        this.mImageView= mImageView;
        this.mText_coin = mText_coin;
    }

    public CardItem(int title, int text, String imageUri) {
        mTitleResource = title;
        mTextResource = text;
        this.mText_coin = mText_coin;
        this.mImageUri = imageUri;
    }



    public int getText() {
        return mTextResource;
    }

    public int getTitle() {
        return mTitleResource;
    }

    public int getmImageView(){
        return mImageView;
    }
    public int getmText_coin(){
        return mText_coin;
    }

    public String getImageUri() {
        return mImageUri;
    }

    public void setImageUri(String imageUri) {
        mImageUri = imageUri;
    }
}