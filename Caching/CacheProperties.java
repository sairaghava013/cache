package Caching;

public class CacheProperties {
    int index;
    int offset;
    String tag;

    CacheProperties() {
        this.index=-1;
        this.offset=-1;
        this.tag="";
    }

    CacheProperties(int index, int offset, String tag) {
        this.index = index;
        this.offset = offset;
        this.tag = tag;
    }
}
