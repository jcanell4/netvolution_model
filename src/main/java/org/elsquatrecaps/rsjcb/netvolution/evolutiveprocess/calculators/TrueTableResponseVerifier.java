/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.calculators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author josep
 * @param <I>
 * @param <O>
 */
public class TrueTableResponseVerifier<I extends Comparable<I>,O extends Comparable<O>> implements ResponseVerifier<I, O, Boolean> {
    private final Map<I,O> correspondenceTable;
    private final List<I> entries;

    public TrueTableResponseVerifier() {
        correspondenceTable = new HashMap<>();
        entries = new ArrayList<>();
    }

    public TrueTableResponseVerifier(Map<I,O> trueTable){
        correspondenceTable = trueTable;
        entries = new ArrayList<>();
    }
    
    public void addValueCorrespondence(I input, O output){
        correspondenceTable.put(input, output);
        entries.add(input);
    }

    @Override
    public Boolean verify(I input, O output) {
        return correspondenceTable.get(input).compareTo(output)==0;
    }    
    
    public I getEntry(int i){
        return entries.get(i);
    }
    
    public int getEntriesSize(){
        return entries.size();
    }
}
