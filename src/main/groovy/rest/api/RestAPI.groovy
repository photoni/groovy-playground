package rest.api;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType

import model.Security
import service.SecurityService
import ta.TechnicalAnalisys

@Path("api")
public class RestAPI {
	static final String TICKER = "GOOGL"
	Security s;double[] prices;TechnicalAnalisys ta;SecurityService ss;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String get() {
		return "\n This is PF REST API via HTTPServer"
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("echoEntries")
	public List<String> getEchoEntries(	@QueryParam(value = "entry")
			final List<String> entries) {
		return entries;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("series")
	public Map<String,Object> getSeries(@QueryParam(value = "type")
			final List<String> types,@DefaultValue(value="false") @QueryParam(value = "cut") final boolean cut) {
		Map<String,Object> result= new HashMap<String,Object>();
		/* Getting ticker */
		ss= SecurityService.instance
		s=ss.getSecurity(TICKER)
		int historyLength=s.getHistory().size()
		prices=new double[historyLength]
		s.getHistory().eachWithIndex { obj, i -> prices[i]=obj.adjClose}
		result.put("prices", prices)

		ta=TechnicalAnalisys.instance
		Map<String,double[]> multi=ta.multi(prices, types,cut)
		//    	obj.put("a", 1.1);
		//    	JSONObject outputJsonObj = new JSONObject();
		//        outputJsonObj.put("output", obj);
		result.putAll(multi);
		return result;
	}
			
			
}
