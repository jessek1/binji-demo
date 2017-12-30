package binji.demo.core.caching;

import java.util.concurrent.Callable;

import org.springframework.cache.Cache;

/**
 * D360 cache manager interface
 * 
 * @author jesse keane
 *
 */
public interface D360Cache extends Cache {

	/**
	 * Gets the item from cache.
	 * 
	 * 
	 * @param key
	 *            the key with which the specified value is to be associated
	 * @param valueLoader
	 * @return
	 */
	<T> T get(Object key, long expiration, Callable<T> valueLoader);

	/**
	 * Gets the item from cache and provides a setKey for the set that should
	 * have this key added to.
	 * 
	 * @see #evictSet(String setKey)
	 * 
	 * @param key
	 *            the key with which the specified value is to be associated
	 * @param setKey
	 *            the key of the set with which the specified key will be added
	 *            to
	 * @param valueLoader
	 * @return
	 */
	<T> T get(Object key, String setKey, long expiration, Callable<T> valueLoader);

	/**
	 * Puts the item into cache while also inserting the item's key into the set
	 * specified by the setKey.
	 * 
	 * @param key
	 *            the key with which the specified value is to be associated
	 * @param setKey
	 *            the key of the set with which the specified key will be added
	 *            to
	 * @param expiration
	 *            time to live in seconds
	 * @param value
	 */
	void put(Object key, String setKey, long expiration, Object value);

	/**
	 * Deletes all the cache items by their keys that are members of the set and
	 * deletes the set itself.
	 * 
	 * Here a set is used as an optimization to delete key variations from a
	 * single requested entity. e.g. getDeviceById(deviceId, includeSomething);
	 * key= byid-{deviceId}-{includeSomething} setKey = byid-{deviceId}
	 * 
	 * If we update the device entity, we need to ensure that we delete any
	 * cache items that contain the older version of the device entity. The set
	 * acts as an index of cache keys for a particular request that may have had
	 * variations to the request. By evicting the set, we can ensure we delete
	 * all cache keys that had different variations and also ensure that we
	 * perform the delete operation in a performant.
	 * 
	 * For example
	 * 
	 * @see net.totempower.totemnet.services.devices.DeviceServiceImpl#updateDevice(net.totempower.totemnet.common.dto.devices.Device
	 *      device)
	 * 
	 * @param setKey
	 *            the key of the set
	 */
	void evictSet(String setKey);

	/**
	 * Adds key to a set.
	 * 
	 * Used for the purpose of maintaining keys in a set that may be defined by
	 * some grouping of a property that prevents the specific object id from
	 * being easily discoverable for eviction when needing to purge cache.
	 * 
	 * For example. A list of devices by property id can be maintained by
	 * creating a set key using the property id such that when eviction is
	 * required, you can get the propertyid from the device object. However, by
	 * querying for a list of devices by a list of property ids, you can no
	 * longer generate a set key that can be determined when modifying a single
	 * device object. To work around this issue, putToSet allows the adding of
	 * the multi propertyid key to all of the single id sets such that eviction
	 * will be handled correctly.
	 * 
	 * @param key
	 * @param setKey
	 */
	void putToSet(String key, String setKey);
}
