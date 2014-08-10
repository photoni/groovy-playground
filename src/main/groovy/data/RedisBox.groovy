package data

import model.KeyBuilder
import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig

/**
 * Single access point to Redis
 * @author filippo
 *
 */
@Singleton(strict=false)
class RedisBox {

	private static final String REDIS_DEFAULT_HOST = "localhost"

	private JedisPool pool


	private  RedisBox() {
		this.pool = new JedisPool(new JedisPoolConfig(), REDIS_DEFAULT_HOST)
	}

	/**
	 * Store a binary data   
	 * @param key the key under which to store data
	 * @param data
	 * @return
	 */
	void storeBinary(String key,byte[] data){
		Jedis jedis = pool.getResource()
		try {
			jedis.set(key.bytes, data)
		} finally  {
			pool.returnResource(jedis)
		}
	}

	/**
	 * Get a binary data
	 * @param key the key under which to store data
	 * @param data
	 * @return
	 */
	byte[] getBinary(String key){
		Jedis jedis = pool.getResource()
		try {
			jedis.get(key.bytes)
		} finally  {
			pool.returnResource(jedis)
		}
	}
}
