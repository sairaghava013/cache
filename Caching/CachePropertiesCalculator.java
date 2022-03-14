package Caching;

class CachePropertiesCalculator {
    

    public CacheProperties getCacheProperties(String hexNum, int indexBits, int offsetBits, int tagBits) {
        //Get decimal to convert into other bases
        int convertedDecimal = Integer.parseInt(hexNum, 16);
        // System.out.println("converted number to decimal:"+convertedDecimal);
        String convertedBinary = Integer.toBinaryString(convertedDecimal);
        while(convertedBinary.length()<32) {
            convertedBinary="0"+convertedBinary;
        }
        // System.out.println("converted number to binary:"+convertedBinary);

        //Calculate values of index and tag
        // System.out.println("tagbits:"+convertedBinary.substring(0,tagBits));
        int tagInDecimal = Integer.parseInt(convertedBinary.substring(0,tagBits),2);
        String tag = Integer.toHexString(tagInDecimal);
        int index = Integer.parseInt(convertedBinary.substring(tagBits, tagBits+indexBits),2);
        int offset=0;
        // int offset = Integer.parseInt(convertedBinary.substring(tagBits+indexBits, 32),2);
        // System.out.println("tag: "+tag+ " index: " + index + " offset:"+offset);
        
        return new CacheProperties(index, offset, tag);
    }
}