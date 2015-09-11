package com.alertmass.appalertmass.alertmass.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Comparator;

/**
 * Created by Francisca on 02-09-2015.
 */
public class OrderbyFecha implements Comparator<JSONObject> {
    /*
    * (non-Javadoc)
    *
    * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
    * lhs- 1st message in the form of json object. rhs- 2nd message in the form
    * of json object.
    */
    @Override
    public int compare(JSONObject a, JSONObject b) {
        try {
            /*return lhs.getInt("fecenv") > rhs.getInt("fecenv") ? 1 : (lhs
                    .getInt("fecenv") < rhs.getInt("fecenv") ? -1 : 0);*/
            int valA = Integer.parseInt(a.getString("FechaEnvio").replace("-",""));
            int valB = Integer.parseInt(b.getString("FechaEnvio").replace("-", ""));
            int valA2 = Integer.parseInt(a.getString("HoraEnvio").replace(":",""));
            int valB2 = Integer.parseInt(b.getString("HoraEnvio").replace(":", ""));


            if(valA > valB || valA2 < valB2){
                return 1;
            }
            if(valA < valB || valA2 > valB2){
                return -1;
            }
            return 0;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;

    }
}
