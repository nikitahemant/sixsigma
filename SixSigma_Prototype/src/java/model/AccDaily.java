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
public class AccDaily {
    private String date;
    private double amount;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public AccDaily(String date, double amount) {
        this.date = date;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "AccDaily{" + "date=" + date + ", amount=" + amount + '}';
    }
}
