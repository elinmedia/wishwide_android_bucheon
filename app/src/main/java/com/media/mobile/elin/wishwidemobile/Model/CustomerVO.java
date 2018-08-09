package com.media.mobile.elin.wishwidemobile.Model;

import java.sql.Timestamp;
import java.util.List;

public class CustomerVO {
    private int no;   //통학고객번호
    private String birth; //통학고객생일
    private String email; //통학고객이메일
    private String name;  //통학고객이름
    private String phone; //통학고객전화번호
    private String memo;  //통학고객메모
    private int gender;  //통학고객성별
    private int visibleCode; //통학고객활성여부

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getVisibleCode() {
        return visibleCode;
    }

    public void setVisibleCode(int visibleCode) {
        this.visibleCode = visibleCode;
    }

    private List<MembershipCustomerVO> membershipCustomers;


    public List<MembershipCustomerVO> getMembershipCustomers() {
        return membershipCustomers;
    }

    public void setMembershipCustomers(List<MembershipCustomerVO> membershipCustomers) {
        this.membershipCustomers = membershipCustomers;
    }

    @Override
    public String toString() {
        return "CustomerVO{" +
                "no=" + no +
                ", birth='" + birth + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", memo='" + memo + '\'' +
                ", gender=" + gender +
                ", visibleCode=" + visibleCode +
                '}';
    }
}
