/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.calculators;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.elsquatrecaps.utilities.tools.ComparableArrayOf;

/**
 *
 * @author josep
 */
public class DiferenceTableResponseVerifier implements ResponseVerifier<ComparableArrayOf<Float>, Float, BigDecimal> {
    private final Map<ComparableArrayOf<Float>,Float> correspondenceTable;
    private final List<ComparableArrayOf<Float>> entries;

    public DiferenceTableResponseVerifier() {
        correspondenceTable = new HashMap<>();
        entries = new ArrayList<>();
    }

    public DiferenceTableResponseVerifier(Map<ComparableArrayOf<Float>,Float> trueTable){
        correspondenceTable = trueTable;
        entries = new ArrayList<>();
    }
    
    public void addValueCorrespondence(ComparableArrayOf<Float> input, Float output){
        correspondenceTable.put(input, output);
        entries.add(input);
    }

    @Override
    public BigDecimal verify(ComparableArrayOf<Float> input, Float output) {
        return new BigDecimal(correspondenceTable.get(input)).subtract(new BigDecimal(output));
    }    
    
    public ComparableArrayOf<Float> getEntry(int i){
        return entries.get(i);
    }
    
    public int getEntriesSize(){
        return entries.size();
    }
}
