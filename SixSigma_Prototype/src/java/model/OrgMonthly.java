/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Nikita
 */
public class OrgMonthly {
    
    private String month;
    private double amount;

    public String getMonth() {
        return month;
    }

    public void setMonth(String date) {
        this.month = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public OrgMonthly(String month, double amount) {
        this.month = month;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "OrgMonthly{" + "month=" + month + ", amount=" + amount + '}';
    }

    
    
}
