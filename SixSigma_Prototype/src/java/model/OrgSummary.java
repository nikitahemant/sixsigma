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
public class OrgSummary {
    private static List<OrgDaily> daily;
    private static List<OrgMonthly> monthly;
    private static List<OrgCategory> category;

    public OrgSummary(List<OrgDaily> d, List<OrgMonthly> m, List<OrgCategory> c) {
        this.daily = d;
        this.monthly = m;
        this.category = c;
    }
}
