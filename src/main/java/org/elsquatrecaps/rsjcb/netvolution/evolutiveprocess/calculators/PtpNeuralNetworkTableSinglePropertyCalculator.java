package org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.calculators;

import org.elsquatrecaps.utilities.tools.ComparableArrayOf;

/**
 *
 * @author josep
 */
public interface PtpNeuralNetworkTableSinglePropertyCalculator extends PtpNeuralNetworkSinglePropertyCalculator{
    void addValueCorrespondence(ComparableArrayOf<Float> input, ComparableArrayOf<Float> output);
}
