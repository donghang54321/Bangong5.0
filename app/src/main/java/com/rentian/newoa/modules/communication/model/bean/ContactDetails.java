package com.rentian.newoa.modules.communication.model.bean;

public class ContactDetails {

    private String name;
    private String station;
    private String imgUrl;
    private String mobilePhoneNum;
    private String officePhoneNum;
    private String eMail;
    private String department;
    private String address;

    @Override
    public String toString() {
        return "ContactDetails{" +
                "name='" + name + '\'' +
                ", station='" + station + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", mobilePhoneNum='" + mobilePhoneNum + '\'' +
                ", officePhoneNum='" + officePhoneNum + '\'' +
                ", eMail='" + eMail + '\'' +
                ", department='" + department + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getMobilePhoneNum() {
        return mobilePhoneNum;
    }

    public void setMobilePhoneNum(String mobilePhoneNum) {
        this.mobilePhoneNum = mobilePhoneNum;
    }

    public String getOfficePhoneNum() {
        return officePhoneNum;
    }

    public void setOfficePhoneNum(String officePhoneNum) {
        this.officePhoneNum = officePhoneNum;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
