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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Nikita
 */
public class AccSummary {
    private static List<AccDaily> daily;
    private static List<AccMonthly> monthly;
    private static List<AccCategory> category;

    public AccSummary(List<AccDaily> d, List<AccMonthly> m, List<AccCategory> c) {
        this.daily = new ArrayList<AccDaily>(d);
        this.monthly = new ArrayList<AccMonthly>(m);
        this.category = new ArrayList<AccCategory>(c);
    }
    
    public String toJSON() throws IOException
    {
        JSONObject output = new JSONObject();;
        
        JSONArray dailyobj = new JSONArray();
        for (AccDaily daily1 : daily) {
            JSONObject da = new JSONObject();
            da.put("date", daily1.getDate());
            da.put("amount", daily1.getAmount());
            dailyobj.add(da);
        }
        output.put("daily",dailyobj);
        
        JSONArray monthlyobj = new JSONArray();
        for (AccMonthly monthly1 : monthly) {
            JSONObject mon = new JSONObject();
            mon.put("month",monthly1.getMonth());
            mon.put("amount", monthly1.getAmount());
            monthlyobj.add(mon);
        }
        output.put("monthly",monthlyobj);
        
        JSONArray catobj = new JSONArray();
        for (AccCategory cat1 : category) {
            JSONObject cat = new JSONObject();
            cat.put("category", cat1.getCategory());
            cat.put("amount", cat1.getAmount());
            catobj.add(cat);
        }
        output.put("category",catobj);
        
        StringWriter out = new StringWriter();
        output.writeJSONString(out);
      
      return out.toString();
    }
}
