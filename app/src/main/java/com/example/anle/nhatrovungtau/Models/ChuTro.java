package com.example.anle.nhatrovungtau.Models;

public class ChuTro {
    private int mID;
    private String mHoten;
    private String mCMND;
    private String mSinhnhat;
    private String mGioitinh;
    private String mEmail;
    private String mDiachi;
    public ChuTro() {

    }

    public ChuTro(String mHoten, String mCMND, String mSinhnhat, String mGioitinh, String mEmail,String mDiachi) {
        this.mHoten = mHoten;
        this.mCMND = mCMND;
        this.mSinhnhat = mSinhnhat;
        this.mGioitinh = mGioitinh;
        this.mEmail = mEmail;
        this.mDiachi=mDiachi;
    }

    public ChuTro(int mID, String mHoten, String mCMND, String mSinhnhat, String mGioitinh, String mEmail,String mDiachi) {
        this.mID = mID;
        this.mHoten = mHoten;
        this.mCMND = mCMND;
        this.mSinhnhat = mSinhnhat;
        this.mGioitinh = mGioitinh;
        this.mEmail = mEmail;
        this.mDiachi=mDiachi;
    }

    public int getmID() {
        return mID;
    }

    public String getmHoten() {
        return mHoten;
    }

    public String getmCMND() {
        return mCMND;
    }

    public String getmSinhnhat() {
        return mSinhnhat;
    }

    public String getmGioitinh() {
        return mGioitinh;
    }

    public String getmEmail() {
        return mEmail;
    }

    public String getmDiachi(){return mDiachi;}


    public void setmID(int mID) {
        this.mID = mID;
    }

    public void setmHoten(String mHoten) {
        this.mHoten = mHoten;
    }

    public void setmCMND(String mCMND) {
        this.mCMND = mCMND;
    }

    public void setmSinhnhat(String mSinhnhat) {
        this.mSinhnhat = mSinhnhat;
    }

    public void setmGioitinh(String mGioitinh) {
        this.mGioitinh = mGioitinh;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public void setmDiachi(String mDiachi){ this.mDiachi=mDiachi;}
}
