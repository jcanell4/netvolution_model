
package org.elsquatrecaps.util.random;

import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
//import java.util.random.RandomGenerator;

/**
 *
 * @author josepcanellas
 */
public class Random /*implements RandomGenerator*/{
    SecureRandom generator;
    
    private static byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }

    public Random(long seed) {
        generator=new SecureRandom(longToBytes(seed));
    }

    public Random() {
        this(true);
    }

    public Random(boolean nativePRNG) {
        if(nativePRNG){
            try {
                generator = SecureRandom.getInstance("NativePRNGNonBlocking");
            } catch (NoSuchAlgorithmException ex) {}
        }else{
            generator = new SecureRandom();
        }
    }

    public boolean nextBoolean() {
        return generator.nextBoolean();
    }

    public double nextDouble() {
        return  generator.nextDouble();
    }
    
    public double nextDouble(double bound) {
        return  generator.nextDouble()*bound;
    }
    
    public double nextDouble(double origin, double bound) {
        return  origin + nextDouble(bound-origin);
    }
    
    public float nextFloat() {
        return generator.nextFloat();
    }

    public float nextFloat(float bound) {
        return generator.nextFloat()*bound;
    }

    public float nextFloat(float origin, float bound) {
        return origin + generator.nextFloat()*(bound-origin);
    }

    public int nextInt() {
        return generator.nextInt();
    }

    public int nextInt(int bound) {
        return generator.nextInt(bound);
    }

    public int nextInt(int origin, int bound) {
        return origin + generator.nextInt(bound-origin);
    }

    public long nextLong() {
        return generator.nextLong();
    }

    public long nextLong(long bound) {
        return (long) (generator.nextDouble()*bound);
    }

    public long nextLong(long origin, long bound) {
        return origin + nextLong(bound-origin);
    }
    
}
