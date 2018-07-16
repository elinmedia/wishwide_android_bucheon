package com.media.mobile.elin.wishwidemobile.Model;

import android.util.Log;

public class CouponVO {
    private static CouponVO mCouponVO;
    private int couponNo;
    private int membershipCustomerNo;
    private String wideManagerId;
    private String movePage;
    private String couponDiscountTypeCode;
    private int couponDiscountTypeValue;

    private CouponVO() {

    }

    public static CouponVO getInstance() {
        if (mCouponVO == null) {
            mCouponVO = new CouponVO();
            Log.d("CouponVO", "CouponVO 객체 생성");
        }

        return mCouponVO;
    }

    public static void clearInstance() {
        if (mCouponVO != null) {
            mCouponVO = null;
        }
    }

    public int getCouponNo() {
        return couponNo;
    }

    public void setCouponNo(int couponNo) {
        this.couponNo = couponNo;
    }

    public int getMembershipCustomerNo() {
        return membershipCustomerNo;
    }

    public void setMembershipCustomerNo(int membershipCustomerNo) {
        this.membershipCustomerNo = membershipCustomerNo;
    }

    public String getWideManagerId() {
        return wideManagerId;
    }

    public void setWideManagerId(String wideManagerId) {
        this.wideManagerId = wideManagerId;
    }

    public String getMovePage() {
        return movePage;
    }

    public void setMovePage(String movePage) {
        this.movePage = movePage;
    }

    public String getCouponDiscountTypeCode() {
        return couponDiscountTypeCode;
    }

    public void setCouponDiscountTypeCode(String couponDiscountTypeCode) {
        this.couponDiscountTypeCode = couponDiscountTypeCode;
    }

    public int getCouponDiscountTypeValue() {
        return couponDiscountTypeValue;
    }

    public void setCouponDiscountTypeValue(int couponDiscountTypeValue) {
        this.couponDiscountTypeValue = couponDiscountTypeValue;
    }

    @Override
    public String toString() {
        return "CouponVO{" +
                "couponNo=" + couponNo +
                ", membershipCustomerNo=" + membershipCustomerNo +
                ", wideManagerId='" + wideManagerId + '\'' +
                ", movePage='" + movePage + '\'' +
                ", couponDiscountTypeCode='" + couponDiscountTypeCode + '\'' +
                ", couponDiscountTypeValue=" + couponDiscountTypeValue +
                '}';
    }
}
