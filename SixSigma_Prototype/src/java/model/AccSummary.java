/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.List;

/**
 *
 * @author Nikita
 */
public class AccSummary {
    private static List<AccDaily> daily;
    private static List<AccMonthly> monthly;
    private static List<AccCategory> category;

    public AccSummary(List<AccDaily> d, List<AccMonthly> m, List<AccCategory> c) {
        this.daily = d;
        this.monthly = m;
        this.category = c;
    }
}
