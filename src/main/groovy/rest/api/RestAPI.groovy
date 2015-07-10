package rest.api

import util.ArrayUtil;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType

import model.Security
import service.SecurityService
import ta.TechnicalAnalisys
import util.DateUtil;

@Path("api")
public class RestAPI {
	static final String TICKER = "GOOGL"
	Security s;double[] prices;double[] highs;double[] lows;long[] dates;TechnicalAnalisys ta;SecurityService ss;

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
		highs=new double[historyLength]
		lows=new double[historyLength]
		dates=new double[historyLength]
		s.getHistory().eachWithIndex { obj, i ->
			prices[i]=obj.adjClose
			highs[i]=obj.high
			lows[i]=obj.low
			dates[i]=obj.date
		}
		//result.put("prices", prices)

		ta=TechnicalAnalisys.instance
		Map<String,Map> multi=ta.multi(prices,highs,lows,types,cut)
		multi.get("o").put("prices", prices);
		//    	obj.put("a", 1.1);
		//    	JSONObject outputJsonObj = new JSONObject();
		//        outputJsonObj.put("output", obj);
		result.put("results",multi);
		result.put("dates", dates);
		return result;
	}
}
