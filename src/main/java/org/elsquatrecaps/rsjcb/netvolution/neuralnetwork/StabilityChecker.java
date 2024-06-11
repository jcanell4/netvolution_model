/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.elsquatrecaps.rsjcb.netvolution.neuralnetwork;

import java.io.Serializable;

/**
 *
 * @author josep
 */
public interface StabilityChecker extends Cloneable, Serializable{

    Float getAverageMinMaxValues();

    void updateValue(Float value);

    boolean valuesAreStabilized(int loopingTimesToCheck);
    
    void clean();
    
    Object clone() throws CloneNotSupportedException;
    
}
