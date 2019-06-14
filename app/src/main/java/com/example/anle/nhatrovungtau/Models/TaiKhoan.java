package com.example.anle.nhatrovungtau.Models;

public class TaiKhoan extends ChuTro {
    private String mTK;
    private String mMK;
    private String mSDT;

    public TaiKhoan(){

    }

    public TaiKhoan(String mHoten, String mCMND, String mSinhnhat, String mGioitinh, String mEmail,String mDiachi, String mTK, String mMK, String mSDT) {
        super(mHoten, mCMND, mSinhnhat, mGioitinh, mEmail,mDiachi);
        this.mTK = mTK;
        this.mMK = mMK;
        this.mSDT = mSDT;
    }


    public String getmTK() {
        return mTK;
    }

    public String getmMK() {
        return mMK;
    }

    public String getmSDT() {
        return mSDT;
    }

    public void setmTK(String mTK) {
        this.mTK = mTK;
    }

    public void setmMK(String mMK) {
        this.mMK = mMK;
    }

    public void setmSDT(String mSDT) {
        this.mSDT = mSDT;
    }

}
