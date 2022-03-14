package Caching;

import java.io.IOException;

class SimCache {
	//Lru /PseudoLru
	public static void main(String[] args) throws IOException, Exception {
		Execute cacheSimulator = new Execute(16, 1024, 2, 8192, 4, "Lru", "gcc_trace.txt");
		cacheSimulator.run();
	}
}