package model

/**
 * Implement the key schema
 * @author filippo
 *
 */
class KeyBuilder {
	private static final String SCHEMA_KEY_PREFIX = "model"
	private static final String SECURITY_KEY_PREFIX = "securities"
	private static final String KEY_LEVEL_SEPARATOR = ":"
	private static final String CURVE_KEY_MARKER = "curve"
	/**
	 * @param symbol 
	 * @return the key of the curve for the specified security
	 */
	static String securityCurveKey(String symbol){
		StringBuilder sb=new StringBuilder(SCHEMA_KEY_PREFIX).append(SECURITY_KEY_PREFIX).append(KEY_LEVEL_SEPARATOR)
		sb.append(KEY_LEVEL_SEPARATOR).append(CURVE_KEY_MARKER).append(KEY_LEVEL_SEPARATOR).append(symbol)
		return sb.toString()
	}
	
}
