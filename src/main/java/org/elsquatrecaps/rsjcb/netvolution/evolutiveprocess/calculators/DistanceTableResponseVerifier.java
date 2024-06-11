package org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.calculators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.elsquatrecaps.utilities.tools.CalculableDistanceArrayOf;

/**
 *
 * @author josep
 * @param <I>
 * @param <O>
 */
public class DistanceTableResponseVerifier<I extends CalculableDistanceArrayOf,O extends CalculableDistanceArrayOf> implements ResponseVerifier<I, O, Float> {
    private final Map<I,O> correspondenceTable;
    private final List<I> entries;

    public DistanceTableResponseVerifier() {
        correspondenceTable = new HashMap<>();
        entries = new ArrayList<>();
    }

    public DistanceTableResponseVerifier(Map<I,O> trueTable){
        correspondenceTable = trueTable;
        entries = new ArrayList<>();
    }
    
    public void addTrueCorespondence(I input, O output){
        correspondenceTable.put(input, output);
        entries.add(input);
    }

    @Override
    public Float verify(I input, O output) {
        return correspondenceTable.get(input).getDistance(output);
    }    
    
    public I getEntry(int i){
        return entries.get(i);
    }
    
    public int getEntriesSize(){
        return entries.size();
    }
}
