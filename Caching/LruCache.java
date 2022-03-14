package Caching;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LruCache implements CacheOperations {

    private final List<List<String>> l1;
    private final List<List<String>> l2;
    private final Map<String, String> l1TagMap = new HashMap<>();
    private final Map<String, String> l2TagMap = new HashMap<>();
    private final List<List<Boolean>> dirtyBitsl1 = new ArrayList<>();
    private final List<List<Boolean>> dirtyBitsl2 = new ArrayList<>();
    private final List<Integer> lruIndex1 = new ArrayList<>();
    private final List<Integer> lruIndex2 = new ArrayList<>();
    private final int indexBitsl1;
    private final int offsetBitsl1;
    private final int tagBitsl1;
    private final int associationl1;
    private int indexBitsl2 = 0;
    private int offsetBitsl2 = 0;
    private int tagBitsl2 = 0;
    private int associationl2 = 0;
    private int readsl1 = 0;
	private int readMissesl1 = 0;
	private int writesl1 = 0;
	private int writeMissesl1 = 0;
    private int writeBacksl1 = 0;
    private int readsl2 = 0;
	private int readMissesl2 = 0;
	private int writesl2 = 0;
	private int writeMissesl2 = 0;
    private int writeBacksl2 = 0;

    public LruCache(int capacityl1, int capacityl2,
                    int indexBitsl1, int offsetBitsl1, int tagBitsl1, int associationl1,
                    int indexBitsl2, int offsetBitsl2, int tagBitsl2, int associationl2) {
        this.l1 = new ArrayList<>();
        this.l2 = new ArrayList<>();
        for(int i=0;i<capacityl1;i++) {
            this.l1.add(i, new ArrayList<>());
            List<Boolean> dirtyBits = new ArrayList<>();
            for(int j=0;j<associationl1;j++) {
                dirtyBits.add(false);
            }
            this.dirtyBitsl1.add(i, dirtyBits);
            this.lruIndex1.add(i, 0);
        }
        for(int i=0;i<capacityl2;i++) {
            this.l2.add(i, new ArrayList<>());
            List<Boolean> dirtyBits = new ArrayList<>();
            for(int j=0;j<associationl2;j++) {
                dirtyBits.add(false);
            }
            this.dirtyBitsl2.add(i, dirtyBits);
            this.lruIndex2.add(i, 0);
        }
        this.indexBitsl1 = indexBitsl1;
        this.offsetBitsl1 = offsetBitsl1;
        this.tagBitsl1 = tagBitsl1;
        this.associationl1 = associationl1;
        if(capacityl2>0) {
            this.indexBitsl2 = indexBitsl2;
            this.offsetBitsl2 = offsetBitsl2;
            this.tagBitsl2 = tagBitsl2;
            this.associationl2 = associationl2;
        }
    }

    @Override
    public void read(String hexcode) {
        readOrWrite(hexcode,false);
    }

    @Override
    public void write(String hexcode) {
        readOrWrite(hexcode,true);
    }

    public void readOrWrite(String hexcode, boolean isWrite) {
        CachePropertiesCalculator cachePropertiesCalculator = new CachePropertiesCalculator();
        CacheProperties properties = cachePropertiesCalculator.getCacheProperties(hexcode, indexBitsl1, offsetBitsl1, tagBitsl1);
        l1TagMap.put(properties.tag, hexcode);
        String writeBackHexcode = modifyCache("l1", isWrite, properties);
        if(!"".equals(writeBackHexcode) && tagBitsl2!=0) {
            properties = cachePropertiesCalculator.getCacheProperties(writeBackHexcode, indexBitsl2, offsetBitsl2, tagBitsl2);
            l2TagMap.put(properties.tag, writeBackHexcode);
            modifyCache("l2", isWrite, properties);
        }
    }

    private String modifyCache(String type, boolean isWrite, CacheProperties properties) {
        String writebackHexcode = "";
        if(isWrite) {
            if("l1".equals(type)){
                writesl1++;
            } else {
                writesl2++;
            }
        } else {
            if("l1".equals(type)) {
                readsl1++;
            } else {
                readsl2++;
            }
        }
        List<String> currAssociation;
        if("l1".equals(type)) {
            currAssociation = l1.get(properties.index);
        } else {
            currAssociation = l2.get(properties.index);
        }
        String tag = properties.tag;
        int index = properties.index;

        // if l1 contains the tag, then update dirty bits and lru index
        if(currAssociation.contains(tag)) {
            int tagIndex = currAssociation.indexOf(tag);
            List<Boolean> dirtyBits;
            if("l1".equals(type)) {
                lruIndex1.set(index, (tagIndex+1)%associationl1);
            } else {
                lruIndex2.set(index, (tagIndex+1)%associationl2);
            }
        } else {// in case of read/write misses
            int tagIndex=0;
            if("l1".equals(type)) {
                if(currAssociation.size()==associationl1) {
                    tagIndex = lruIndex1.get(index);
                    List<Boolean> dirtyBits = dirtyBitsl1.get(index);
                    if(isWrite) {
                        writeMissesl1++;
                        if (dirtyBitsl1.get(index).get(tagIndex)==true) {
                            writeBacksl1++;
                        }
                        dirtyBits.set(tagIndex, true);
                    } else {
                        readMissesl1++;
                        dirtyBits.set(tagIndex, false);
                    }
                    String writebackTag = currAssociation.remove(tagIndex);
                    writebackHexcode = l1TagMap.get(writebackTag);
                    currAssociation.add(tagIndex, tag);
                    lruIndex1.set(index, (tagIndex+1)%associationl1 );
                } else {
                    tagIndex = currAssociation.size();
                    List<Boolean> dirtyBits = dirtyBitsl1.get(index);
                    if(isWrite) {
                        writeMissesl1++;
                        dirtyBits.set(tagIndex, true);
                    } else {
                        readMissesl1++;
                        dirtyBits.set(tagIndex, false);
                    }
                    currAssociation.add(tag);
                }
            } else {
                if(currAssociation.size()==associationl2) {
                    tagIndex = lruIndex2.get(index);
                    List<Boolean> dirtyBits = dirtyBitsl2.get(index);
                    if(isWrite) {
                        writeMissesl2++;
                        if (dirtyBitsl2.get(index).get(tagIndex)==true) {
                            writeBacksl2++;
                        }
                        dirtyBits.set(tagIndex, true);
                    } else {
                        readMissesl2++;
                        dirtyBits.set(tagIndex, false);
                    }
                    String writebackTag = currAssociation.remove(tagIndex);
                    writebackHexcode = l2TagMap.get(writebackTag);
                    currAssociation.add(tagIndex, tag);
                    lruIndex2.set(index, (tagIndex+1)%associationl2);
                } else {
                    tagIndex = currAssociation.size();
                    List<Boolean> dirtyBits = dirtyBitsl2.get(index);
                    if(isWrite) {
                        writeMissesl2++;
                        dirtyBits.set(tagIndex, true);
                    } else {
                        readMissesl2++;
                        dirtyBits.set(tagIndex, false);
                    }
                    currAssociation.add(tag);
                }
            }
        }
        return writebackHexcode;
    }

    @Override
    public void printCache(String type) {
        List<List<String>> cache = "l1".equals(type) ? l1: l2;
        for(int i=0;i<cache.size();i++) {
            System.out.print("Set    "+i+":    ");
            for(int j=0;j<cache.get(0).size();j++) {
                System.out.print(cache.get(i).get(j)+" ");
                List<List<Boolean>> dirtyBits = "l1".equals(type) ? dirtyBitsl1: dirtyBitsl2;
                if (dirtyBits.get(i).get(j) == true) {
                    System.out.print("D         ");
                }
            }
            System.out.println();
        }
    }

    @Override
    public int getTotalMemoryTraffic() {
        if(l2.size()!=0) {
            return readMissesl2 + writeMissesl2 + writeBacksl2;
        }
        return readMissesl1 + writeMissesl1 + writeBacksl1;
    }

    public int getReads(String type) {
        return "l1".equals(type) ? this.readsl1 : this.readsl2;
    }

    public int getReadMisses(String type) {
        return "l1".equals(type) ? this.readMissesl1 : this.readMissesl2;
    }

    public int getWrites(String type) {
        return "l1".equals(type) ? this.writesl1 : this.writesl2;
    }

    public int getWriteMisses(String type) {
        return "l1".equals(type) ? this.writeMissesl1 : this.writeMissesl2;
    }

    public int getWriteBacks(String type) {
        return "l1".equals(type) ? this.writeBacksl1 : this.writeBacksl2;
    }
    
}
