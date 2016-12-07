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
public class intelRebate {
    private String category;
    private double indAmount;
    private double orgAmount;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getIndAmount() {
        return indAmount;
    }

    public void setIndAmount(double indAmount) {
        this.indAmount = indAmount;
    }

    public double getOrgAmount() {
        return orgAmount;
    }

    public void setOrgAmount(double orgAmount) {
        this.orgAmount = orgAmount;
    }

    public intelRebate(String category, double indAmount, double orgAmount) {
        this.category = category;
        this.indAmount = indAmount;
        this.orgAmount = orgAmount;
    }

    @Override
    public String toString() {
        return "intelRebate{" + "category=" + category + ", indAmount=" + indAmount + ", orgAmount=" + orgAmount + '}';
    }

    

    
}
