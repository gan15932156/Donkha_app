package com.example.donkha_app.GetterSetter;

public class Account  {
    private String account_id,
            member_id,
            staff_open_id,
            staff_close_id,
            account_open_date,
            account_close_date,
            account_name,
            account_status,
            account_balance,
            passbook_line,
            interest_update;

    public Account(){}
    public Account(String account_id, String member_id, String staff_open_id, String staff_close_id, String account_open_date, String account_close_date, String account_name, String account_status, String account_balance, String passbook_line, String interest_update) {
        this.account_id = account_id;
        this.member_id = member_id;
        this.staff_open_id = staff_open_id;
        this.staff_close_id = staff_close_id;
        this.account_open_date = account_open_date;
        this.account_close_date = account_close_date;
        this.account_name = account_name;
        this.account_status = account_status;
        this.account_balance = account_balance;
        this.passbook_line = passbook_line;
        this.interest_update = interest_update;
    }

    public Account(String account_id, String account_name, String account_status, String account_balance) {
        this.account_id = account_id;
        this.account_name = account_name;
        this.account_status = account_status;
        this.account_balance = account_balance;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getStaff_open_id() {
        return staff_open_id;
    }

    public void setStaff_open_id(String staff_open_id) {
        this.staff_open_id = staff_open_id;
    }

    public String getStaff_close_id() {
        return staff_close_id;
    }

    public void setStaff_close_id(String staff_close_id) {
        this.staff_close_id = staff_close_id;
    }

    public String getAccount_open_date() {
        return account_open_date;
    }

    public void setAccount_open_date(String account_open_date) {
        this.account_open_date = account_open_date;
    }

    public String getAccount_close_date() {
        return account_close_date;
    }

    public void setAccount_close_date(String account_close_date) {
        this.account_close_date = account_close_date;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getAccount_status() {
        return account_status;
    }

    public void setAccount_status(String account_status) {
        this.account_status = account_status;
    }

    public String getAccount_balance() {
        return account_balance;
    }

    public void setAccount_balance(String account_balance) {
        this.account_balance = account_balance;
    }

    public String getPassbook_line() {
        return passbook_line;
    }

    public void setPassbook_line(String passbook_line) {
        this.passbook_line = passbook_line;
    }

    public String getInterest_update() {
        return interest_update;
    }

    public void setInterest_update(String interest_update) {
        this.interest_update = interest_update;
    }
}
