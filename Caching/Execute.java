package Caching;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import Caching.CacheFactory;
import Caching.CacheOperations;

class Execute {
	private final FileOperations fileParser;
	private final CacheOperations cacheOperations;

	//@TODO: make this relative path
	private static final String FILE_DIRECTORY = "/Users/sairaghava/Documents/CacheAssignment/Caching/";
	private final int BLOCK_SIZE;
	private final int CACHE_SIZE_L1;
	private final int ASSOCIATION_L1;
	private final int CACHE_SIZE_L2;
	private final int ASSOCIATION_L2;
	private final int TOTAL_BITS = 32;
	private final String REPLACEMENT_POLICY;
	private final int CACHE_BLOCKS_L1;//sets
	private final int INDEX_BITS_L1;
	private final int OFFSET_BITS_L1;
	private final int TAG_BITS_L1;
	private int CACHE_BLOCKS_L2=0;//sets
	private int INDEX_BITS_L2=0;
	private int OFFSET_BITS_L2=0;
	private int TAG_BITS_L2=0;
	
	public Execute(int blockSize, int cacheSizel1, int associationl1,
		int cacheSizel2, int associationl2, String replacementPolicy, 
		String traceFile) throws FileNotFoundException {
			this.BLOCK_SIZE = blockSize;
			this.CACHE_SIZE_L1 = cacheSizel1;
			this.ASSOCIATION_L1 = associationl1;
			this.CACHE_SIZE_L2 = cacheSizel2;
			this.ASSOCIATION_L2 = associationl2;
			this.REPLACEMENT_POLICY = replacementPolicy;
			this.fileParser = new FileOperations(new File(FILE_DIRECTORY+traceFile));
			CacheFactory cacheFactory = new CacheFactory();
			this.CACHE_BLOCKS_L1 = CACHE_SIZE_L1/(ASSOCIATION_L1*BLOCK_SIZE);//sets
			this.INDEX_BITS_L1 = (int)(Math.log(CACHE_BLOCKS_L1)/Math.log(2));
			this.OFFSET_BITS_L1 = (int)(Math.log(BLOCK_SIZE)/Math.log(2));
			this.TAG_BITS_L1 = TOTAL_BITS-INDEX_BITS_L1-OFFSET_BITS_L1;
			if(ASSOCIATION_L2>0 && CACHE_SIZE_L2>0) {
				CACHE_BLOCKS_L2 = CACHE_SIZE_L2/(ASSOCIATION_L2*BLOCK_SIZE);//sets
				INDEX_BITS_L2 = (int)(Math.log(CACHE_BLOCKS_L2)/Math.log(2));
				OFFSET_BITS_L2 = (int)(Math.log(BLOCK_SIZE)/Math.log(2));
				TAG_BITS_L2 = TOTAL_BITS-INDEX_BITS_L2-OFFSET_BITS_L2;
			}
			int capacity1 = CACHE_SIZE_L1/(ASSOCIATION_L1*BLOCK_SIZE);
			int capacity2 = CACHE_SIZE_L2==0 ? 0 : CACHE_SIZE_L2/(ASSOCIATION_L2*BLOCK_SIZE);
			this.cacheOperations = cacheFactory.getCache(REPLACEMENT_POLICY, capacity1, capacity2,
					INDEX_BITS_L1, OFFSET_BITS_L1, TAG_BITS_L1, ASSOCIATION_L1,
					INDEX_BITS_L2, OFFSET_BITS_L2, TAG_BITS_L2, ASSOCIATION_L2);
	}


	public void run() throws IOException, Exception {
		String line;
		while((line = fileParser.getNextLine())!=null) {
			String[] words = line.split(" ");
			if('r' == words[0].charAt(words[0].length()-1)) {
				cacheOperations.read(words[1]);
			} else if('w' == words[0].charAt(words[0].length()-1)) {
				cacheOperations.write(words[1]);
			} else {
				String msg = String.format("Invalid Cache Operation: %s %s", words[0], words[1]);
				throw new Exception(msg);
			}
		}
		int readsl1 = cacheOperations.getReads("l1");
		int readMissesl1 = cacheOperations.getReadMisses("l1");
		int writesl1 = cacheOperations.getWrites("l1");
		int writeMissesl1 = cacheOperations.getWriteMisses("l1");
		int writeBacksl1 = cacheOperations.getWriteBacks("l1");
		int readsl2 = cacheOperations.getReads("l2");
		int readMissesl2 = cacheOperations.getReadMisses("l2");
		int writesl2 = cacheOperations.getWrites("l2");
		int writeMissesl2 = cacheOperations.getWriteMisses("l2");
		int writeBacksl2 = cacheOperations.getWriteBacks("l2");
		int totalMemoryTraffic = cacheOperations.getTotalMemoryTraffic();
		System.out.println("===== L1 contents =====");
		cacheOperations.printCache("l1");
		System.out.println("===== L2 contents =====");
		cacheOperations.printCache("l2");

		System.out.println("readsl1: "+readsl1);
		System.out.println("readMissesl1: "+readMissesl1);
		System.out.println("writesl1: "+writesl1);
		System.out.println("writeMissesl1: "+writeMissesl1);
		System.out.println("miss rate l1: "+(double)(readMissesl1+writeMissesl1)/(double)(readsl1+writesl1));
		System.out.println("writeBacksl1: "+writeBacksl1);

		System.out.println("readsl2: "+readsl2);
		System.out.println("readMissesl2: "+readMissesl2);
		System.out.println("writesl2: "+writesl2);
		System.out.println("writeMissesl2: "+writeMissesl2);
		System.out.println("miss rate l2: "+(double)(readMissesl2+writeMissesl2)/(double)(readsl2+writesl2));
		System.out.println("writeBacksl2: "+writeBacksl2);
		System.out.println("total traffic: "+totalMemoryTraffic);
	}

	
}