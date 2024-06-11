/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.calculators;

import java.math.BigDecimal;
import org.elsquatrecaps.utilities.tools.ComparableArrayOf;

/**
 *
 * @author josep
 */
public class AbsoluteDiferenceTableResponseVerifier extends DiferenceTableResponseVerifier{
    
    @Override
    public BigDecimal verify(ComparableArrayOf<Float> input, Float output) {
        return super.verify(input, output).abs();
    }        
}
