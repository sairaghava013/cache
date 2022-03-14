package Caching;

import java.util.ArrayList;
import java.util.List;

public class OptimizedCache implements CacheOperations{

    private final List<List<String>> l1;
    private final List<List<String>> l2;
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

    public OptimizedCache(int capacityl1, int capacityl2) {
        this.l1 = new ArrayList<>();
        this.l2 = new ArrayList<>();
        for(int i=0;i<capacityl1;i++) {
            this.l1.add(i, new ArrayList<>());
            dirtyBitsl1.add(i, false);
        }
        for(int i=0;i<capacityl2;i++) {
            this.l2.add(i, new ArrayList<>());
            dirtyBitsl2.add(i, false);
        }
    }

    @Override
    public void read(int index, String tag, int association) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void write(int index, String tag, int association) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void printCache() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public int getTotalMemoryTraffic() {
        if(l2.size()!=0) {
            return readMissesl2 + writeMissesl2 + writeBacksl2;
        }
        return readMissesl1 + writeMissesl1 + writeBacksl1;
    }

    public int getReads() {
        return this.readsl1;
    }

    public int getReadMisses() {
        return this.readMissesl1;
    }

    public int getWrites() {
        return this.writesl1;
    }

    public int getWriteMisses() {
        return this.writeMissesl1;
    }

    public int getWriteBacks() {
        return this.writeBacksl1;
    }
    
}
