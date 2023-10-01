package com.example.piece1timer.school;

public class mid_fin_date {
    String lecture_name;
    String mid_term_date;
    String fin_term_date;

    mid_fin_date (String lecture_name){
        this.lecture_name = lecture_name;
    }

    public String getFin_term_date() {
        return fin_term_date;
    }

    public String getMid_term_date() {
        return mid_term_date;
    }

    public void setFin_term_date(String fin_term_date) {
        this.fin_term_date = fin_term_date;
    }

    public void setMid_term_date(String mid_term_date) {
        this.mid_term_date = mid_term_date;
    }
}
