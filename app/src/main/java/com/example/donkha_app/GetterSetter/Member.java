package com.example.donkha_app.GetterSetter;

public class Member {
    private String
            member_id,
            level_id,
            edu_id,
            std_code,
            member_id_card,
            member_name,
            member_birth_date,
            member_yofadmis,
            address,
            phone_number,
            member_pic,
            member_signa_pic,
            member_regis_date,
            member_title,
            zipcode,
            DISTRICT_NAME,
            AMPHUR_NAME,
            PROVINCE_NAME,
            job_name;
    public Member(){}
    public Member(String member_id, String level_id, String edu_id, String std_code, String member_id_card, String member_name, String member_birth_date, String member_yofadmis, String address, String phone_number, String member_pic, String member_signa_pic, String member_regis_date, String member_title, String zipcode, String DISTRICT_NAME, String AMPHUR_NAME, String PROVINCE_NAME, String job_name) {
        this.member_id = member_id;
        this.level_id = level_id;
        this.edu_id = edu_id;
        this.std_code = std_code;
        this.member_id_card = member_id_card;
        this.member_name = member_name;
        this.member_birth_date = member_birth_date;
        this.member_yofadmis = member_yofadmis;
        this.address = address;
        this.phone_number = phone_number;
        this.member_pic = member_pic;
        this.member_signa_pic = member_signa_pic;
        this.member_regis_date = member_regis_date;
        this.member_title = member_title;
        this.zipcode = zipcode;
        this.DISTRICT_NAME = DISTRICT_NAME;
        this.AMPHUR_NAME = AMPHUR_NAME;
        this.PROVINCE_NAME = PROVINCE_NAME;
        this.job_name = job_name;
    }

    public String getMember_id() { return member_id; }
    public void setMember_id(String member_id) { this.member_id = member_id; }
    public String getLevel_id() { return level_id; }
    public void setLevel_id(String level_id) { this.level_id = level_id; }
    public String getEdu_id() { return edu_id; }
    public void setEdu_id(String edu_id) { this.edu_id = edu_id; }
    public String getStd_code() { return std_code; }
    public void setStd_code(String std_code) { this.std_code = std_code; }
    public String getMember_id_card() { return member_id_card; }
    public void setMember_id_card(String member_id_card) { this.member_id_card = member_id_card; }
    public String getMember_name() { return member_name; }
    public void setMember_name(String member_name) { this.member_name = member_name; }
    public String getMember_birth_date() { return member_birth_date; }
    public void setMember_birth_date(String member_birth_date) { this.member_birth_date = member_birth_date; }
    public String getMember_yofadmis() { return member_yofadmis; }
    public void setMember_yofadmis(String member_yofadmis) { this.member_yofadmis = member_yofadmis; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPhone_number() { return phone_number; }
    public void setPhone_number(String phone_number) { this.phone_number = phone_number; }
    public String getMember_pic() { return member_pic; }
    public void setMember_pic(String member_pic) { this.member_pic = member_pic; }
    public String getMember_signa_pic() { return member_signa_pic; }
    public void setMember_signa_pic(String member_signa_pic) { this.member_signa_pic = member_signa_pic; }
    public String getMember_regis_date() { return member_regis_date; }
    public void setMember_regis_date(String member_regis_date) { this.member_regis_date = member_regis_date; }
    public String getMember_title() { return member_title; }
    public void setMember_title(String member_title) { this.member_title = member_title; }
    public String getZipcode() { return zipcode; }
    public void setZipcode(String zipcode) { this.zipcode = zipcode; }
    public String getDISTRICT_NAME() { return DISTRICT_NAME; }
    public void setDISTRICT_NAME(String DISTRICT_NAME) { this.DISTRICT_NAME = DISTRICT_NAME; }
    public String getAMPHUR_NAME() { return AMPHUR_NAME; }
    public void setAMPHUR_NAME(String AMPHUR_NAME) { this.AMPHUR_NAME = AMPHUR_NAME; }
    public String getPROVINCE_NAME() { return PROVINCE_NAME; }
    public void setPROVINCE_NAME(String PROVINCE_NAME) { this.PROVINCE_NAME = PROVINCE_NAME; }
    public String getJob_name() { return job_name; }
    public void setJob_name(String job_name) { this.job_name = job_name; }
}
