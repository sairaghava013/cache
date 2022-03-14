package Caching;

public class CacheFactory {
    public CacheOperations getCache(String cacheType, int capacityl1, int capacityl2,
        int indexBitsl1, int offsetBitsl1, int tagBitsl1, int associationl1,
        int indexBitsl2, int offsetBitsl2, int tagBitsl2, int associationl2) {
        if (cacheType.equals("Lru")) {
            return new LruCache(capacityl1, capacityl2, 
            indexBitsl1, offsetBitsl1, tagBitsl1, associationl1,
            indexBitsl2, offsetBitsl2, tagBitsl2, associationl2);
        }
        return null;
    }
}
