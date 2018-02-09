package com.mintosoft.moneymanager.AllExpanse;

/**
 * Created by MahadiurJaman on 10/22/2017.
 */

public class model {

    String year;
    String budget;
    String expanse;
    String status;

    public model(String year, String budget, String expanse, String status) {
        this.year = year;
        this.budget = budget;
        this.expanse = expanse;
        this.status = status;
    }

    public model(String budget, String expanse, String status) {
        this.budget = budget;
        this.expanse = expanse;
        this.status = status;
    }

    public String getYear() {
        return year;
    }

    public String getBudget() {
        return budget;
    }

    public String getExpanse() {
        return expanse;
    }

    public String getStatus() {
        return status;
    }
}
