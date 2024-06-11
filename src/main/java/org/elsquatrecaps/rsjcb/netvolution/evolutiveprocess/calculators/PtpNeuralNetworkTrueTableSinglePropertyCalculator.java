/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.calculators;

import org.elsquatrecaps.utilities.tools.ComparableArrayOf;

/**
 *
 * @author josep
 */
public interface PtpNeuralNetworkTrueTableSinglePropertyCalculator extends PtpNeuralNetworkSinglePropertyCalculator{
    void addValueCorrespondence(ComparableArrayOf<Float> input, ComparableArrayOf<Float> output);
}
