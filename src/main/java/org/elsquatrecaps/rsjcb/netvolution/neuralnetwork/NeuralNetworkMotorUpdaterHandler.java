package org.elsquatrecaps.rsjcb.netvolution.neuralnetwork;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author josep
 */
public interface NeuralNetworkMotorUpdaterHandler extends Cloneable, Serializable{
    void outputUpdate(List<PtpNeuron> outputNeurons);
    Float[] getValues();
    int sizeOfValues();
    Float getValue(int pos);
}
