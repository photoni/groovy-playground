package rest.api;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import service.SecurityService;
import ta.TechnicalAnalisys;
import model.Security;
import net.sf.json.JSONObject;
 
@Path("api")
public class RestAPI {
	static final String TICKER = "GOOGL"
	SecurityService ss;	Security s;double[] prices;TechnicalAnalisys ta;
 
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String get() {
        return "\n This is PF REST API via HTTPServer";
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("series")
    public JSONObject getSeries() {
		Map<String,?> result= new HashMap<String,Object>();
		/* Getting ticker */
		ss= SecurityService.instance
		s=ss.getSecurity(TICKER)
		int historyLength=s.getHistory().size()
		prices=new double[historyLength]
		s.getHistory().eachWithIndex { obj, i -> prices[i]=obj.adjClose}
		result.put("prices", prices)		
    	
		ta=TechnicalAnalisys.instance
		double[] sma= ta.sma(prices)
		result.put("sma", sma)
//    	obj.put("a", 1.1);
//    	JSONObject outputJsonObj = new JSONObject();  
//        outputJsonObj.put("output", obj);  
        return result;
    }
 
}
