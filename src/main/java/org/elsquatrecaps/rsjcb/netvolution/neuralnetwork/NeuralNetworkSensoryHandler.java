package org.elsquatrecaps.rsjcb.netvolution.neuralnetwork;

import java.io.Serializable;

/**
 *
 * @author josep
 */
public interface NeuralNetworkSensoryHandler extends Cloneable, Serializable{
    public Float getInputContribution(int neuron, Float input[]);
}
