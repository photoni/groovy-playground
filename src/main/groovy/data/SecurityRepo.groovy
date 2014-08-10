package data

import model.KeyBuilder;

/**
 * Single access point for security repository
 * @author filippo
 *
 */
@Singleton(strict=false)
class SecurityRepo {
	protected RedisBox rb;

	private SecurityRepo(){
		rb=RedisBox.instance;
	}

	void storeBinaryCurve(String symbol,byte[] curve){
		String key=KeyBuilder.securityCurveKey(symbol)
		rb.storeBinary(key, curve);		
	}
	
	byte[] getBinaryCurve(String symbol){
		String key=KeyBuilder.securityCurveKey(symbol)
		return rb.getBinary(key);
	}
}
