package playground;

import static org.junit.Assert.*
import model.KeyBuilder;

import org.junit.AfterClass;
import org.junit.BeforeClass
import org.junit.Test

import au.com.bytecode.opencsv.CSVWriter;
import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig

class RedisTest {

	static final String REDIS_DEFAULT_HOST = "localhost"
	static JedisPool pool;
	@BeforeClass
	static void init(){
		pool = new JedisPool(new JedisPoolConfig(), "localhost")
	}

	@Test
	void connectionAndkeyTest() {
		Jedis jedis = pool.getResource()
		try {
			jedis.set("foo", "bar")
			String value = jedis.get("foo")
			assertEquals("bar", value)
		} finally  {
			pool.returnResource(jedis)
		}
	}

	@Test
	void binTest() {
		Jedis jedis = pool.getResource()
		try {
			jedis.connect()
			String key=KeyBuilder.securityCurveKey("GOOGL");
		    String value="binaryV" 
			jedis.set(key.bytes, value.bytes)
			byte[] valueB = jedis.get(key.bytes)
			assertEquals("binaryV", new String(valueB))
		} finally  {
			pool.returnResource(jedis)
		}
	}

	@AfterClass
	static void destroy(){
		if(null!=pool)
			pool.destroy();
	}
}
