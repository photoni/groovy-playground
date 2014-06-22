package model

/**
 * Implement the key schema
 * @author filippo
 *
 */
class KeyBuilder {
	static final String SCHEMA_KEY_PREFIX = "model"
	static final String SECURITY_KEY_PREFIX = "securities"
	static final String KEY_LEVEL_SEPARATOR = ":"
	static final String CURVE_KEY_MARKER = "curve"
	/**
	 * @param stock
	 * @return the key of the curve for the specified security
	 */
	static String securityCurveKey(String stock){
		StringBuilder sb=new StringBuilder(SECURITY_KEY_PREFIX).append(SCHEMA_KEY_PREFIX).append(KEY_LEVEL_SEPARATOR)
		sb.append(KEY_LEVEL_SEPARATOR).append(CURVE_KEY_MARKER).append(KEY_LEVEL_SEPARATOR).append(stock)
		return sb.toString()
	}
}
