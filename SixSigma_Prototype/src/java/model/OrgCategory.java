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
public class OrgCategory {
    private String category;
    private double amount;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public OrgCategory(String category, double amount) {
        this.category = category;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "OrgCategory{" + "category=" + category + ", amount=" + amount + '}';
    }
    
}
