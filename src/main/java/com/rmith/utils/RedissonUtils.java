package com.rmith.utils;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.redisson.api.RBucket;
import org.redisson.api.RKeys;
import org.redisson.api.RList;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
//</editor-fold>

/**
 *
 * @author Le Tan Phuc
 */
@Component
@Lazy
public class RedissonUtils {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    @Lazy
    private CacheManager cacheManager;

    //hash map
    public String getHashByKey(String strKey, String strField) {
        String result = "";
        if (redissonClient != null) {
            RMap<String, String> rMap = redissonClient.getMap(strKey);
            String value = rMap.get(strField);
            result = (value == null ? "" : value);
        }
        return result;
    }

    //hash map
    public void addHashKey(String strKey, String strField, String strValue) {
        if (redissonClient != null && strValue != null) {
            RMap<String, String> rMap = redissonClient.getMap(strKey);
            rMap.put(strField, strValue);
        }
    }

    //hash map
    public void delFieldInHash(String strKey, String strField) {
        if (redissonClient != null) {
            RMap<String, String> rMap = redissonClient.getMap(strKey);
            rMap.fastRemove(strField);
        }
    }

    //Map String
    public Map<String, String> getAllFileByKey(String strKey) {
        Map<String, String> result = new HashMap<>();
        if (redissonClient != null) {
            RMap<String, String> rMap = redissonClient.getMap(strKey);
            rMap.keySet().forEach((value) -> {
                result.put(value, rMap.get(value));
            });
        }
        return result;
    }

    //get member key
    public List<String> getMemberByKey(String strKey) {
        List<String> listRows = new ArrayList<>();
        if (redissonClient != null) {
            listRows = redissonClient.getList(strKey);
        }
        return listRows;
    }

    //add member key
    public void addMemberKey(String strKey, String strValue) {
        if (redissonClient != null && strValue != null) {
            RList<String> rList = redissonClient.getList(strKey);
            rList.add(strValue);
        }
    }

    //get keys pattern
    public List<String> getKeysByPattern(String pattern) {
        List<String> keys = new ArrayList<>();
        if (redissonClient != null) {
            RKeys rKeys = redissonClient.getKeys();
            keys = (List<String>) rKeys.findKeysByPattern(pattern);
        }
        return keys;
    }

    //set and get
    public void setKey(String strKey, String strValue) {
        if (redissonClient != null) {
            RBucket rBucket = redissonClient.getBucket(strKey);
            rBucket.set(strValue);
        }
    }

    public void setKeyWithTTL(String strKey, String strValue, int ttlMinutes) {
        if (redissonClient != null) {
            RBucket rBucket = redissonClient.getBucket(strKey);
            rBucket.set(strValue, ttlMinutes, TimeUnit.MINUTES);
        }
    }

    public String getKey(String strKey) {
        String result = "";
        if (redissonClient != null) {
            RBucket rBucket = redissonClient.getBucket(strKey);
            if (rBucket.get() == null) {
                return result;
            }
            String value = rBucket.get().toString();
            result = (value == null ? "" : value);
        }
        return result;
    }

    //delete key
    public void delKey(String strKey) {
        if (redissonClient != null) {
            RKeys rKeys = redissonClient.getKeys();
            rKeys.delete(strKey);
        }
    }

    public void deleteCacheSpring(String cacheName, String key) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.evict(key);
        }
    }

    public void cacheEvict(String cacheName, int key) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.evict(key);
        }
    }
}
