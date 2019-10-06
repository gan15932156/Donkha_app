package com.example.donkha_app.GetterSetter;

public class Statement {
    private String account_detail_id;
    private String trans_id;
    private String accoint_id;
    private String staff_record_id;
    private String action;
    private String record_date;
    private String record_time;
    private Double account_detail_balance;
    private Double trans_money;
    private String account_tranfer;

    public Statement(){ }

    public Statement(String account_id,String account_detail_id, String action, String record_date, String record_time, Double trans_money) {
        this.accoint_id = account_id;
        this.account_detail_id = account_detail_id;
        this.action = action;
        this.record_date = record_date;
        this.record_time = record_time;
        this.trans_money = trans_money;
    }
    public Statement(
            String account_detail_id,
            String trans_id,
            String accoint_id,
            String staff_record_id,
            String action,
            String record_date,
            String record_time,
            Double account_detail_balance,
            Double trans_money,
            String account_tranfer)
    {
        this.account_detail_id = account_detail_id;
        this.trans_id = trans_id;
        this.accoint_id = accoint_id;
        this.staff_record_id = staff_record_id;
        this.action = action;
        this.record_date = record_date;
        this.record_time = record_time;
        this.account_detail_balance = account_detail_balance;
        this.trans_money = trans_money;
        this.account_tranfer = account_tranfer;

    }

    public String getAccount_detail_id() { return account_detail_id; }
    public void setAccount_detail_id(String account_detail_id) { this.account_detail_id = account_detail_id; }
    public String getTrans_id() { return trans_id; }
    public void setTrans_id(String trans_id) { this.trans_id = trans_id; }
    public String getAccoint_id() { return accoint_id; }
    public void setAccoint_id(String accoint_id) { this.accoint_id = accoint_id; }
    public String getStaff_record_id() { return staff_record_id; }
    public void setStaff_record_id(String staff_record_id) { this.staff_record_id = staff_record_id; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getRecord_date() { return record_date; }
    public void setRecord_date(String record_date) { this.record_date = record_date; }
    public String getRecord_time() { return record_time; }
    public void setRecord_time(String record_time) { this.record_time = record_time; }
    public Double getAccount_detail_balance() { return account_detail_balance; }
    public void setAccount_detail_balance(Double account_detail_balance) { this.account_detail_balance = account_detail_balance; }
    public Double getTrans_money() { return trans_money; }
    public void setTrans_money(Double trans_money) { this.trans_money = trans_money; }

    public String getAccount_tranfer() {
        return account_tranfer;
    }

    public void setAccount_tranfer(String account_tranfer) {
        this.account_tranfer = account_tranfer;
    }
}
