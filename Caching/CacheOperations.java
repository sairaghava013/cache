package Caching;

public interface CacheOperations {
    void read(String hexcode);
    void write(String hexcode);
    void printCache(String type);
    int getReads(String type);
    int getReadMisses(String type);
    int getWrites(String type);
    int getWriteMisses(String type);
    int getWriteBacks(String type);
    int getTotalMemoryTraffic();
}
