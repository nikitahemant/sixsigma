/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONObject;

/**
 *
 * @author Nikita
 */
public class OrgSummary {
    private static List<OrgDaily> daily;
    private static List<OrgMonthly> monthly;
    private static List<OrgCategory> category;

    public OrgSummary(List<OrgDaily> d, List<OrgMonthly> m, List<OrgCategory> c) {
        this.daily = new ArrayList<OrgDaily>(d);
        this.monthly = new ArrayList<OrgMonthly>(m);
        this.category = new ArrayList<OrgCategory>(c);
    }
    
    public String toJSON() throws IOException
    {
        JSONObject output = new JSONObject();;
        
        JSONObject dailyobj = new JSONObject();
        for (OrgDaily daily1 : daily) {
            dailyobj.put(daily1.getDate(), daily1.getAmount());
        }
        output.put("daily",dailyobj);
        
        JSONObject monthlyobj = new JSONObject();
        for (OrgMonthly monthly1 : monthly) {
            monthlyobj.put(monthly1.getMonth(), monthly1.getAmount());
        }
        output.put("monthly",monthlyobj);
        
        JSONObject catobj = new JSONObject();
        for (OrgCategory cat1 : category) {
            catobj.put(cat1.getCategory(), cat1.getAmount());
        }
        output.put("category",category);
        
        StringWriter out = new StringWriter();
        output.writeJSONString(out);
      
      return out.toString();
    }
            
}
