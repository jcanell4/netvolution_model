package org.elsquatrecaps.rsjcb.netvolution.neuralnetwork;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author josep
 */
public interface PtpNeuralNetwork extends Cloneable, Serializable{

    int getCompletedCycles();

    int getInputLength();

    List<PtpNeuron> getInputNeurons();

    int getLoopingTimesToStabilityCheck();

    int getMaxLoopsForResults();

    Float getNeuronSum(int i);

    Float getNeuronValue(int i);

    PtpNeuron[] getNeurons();

    int getOutputLength();

    List<PtpNeuron> getOutputNeurons();

    Float getOutputSum(int i);

    Float getOutputValue(int i);

    Float getWeight(int fromNeuron, int toNeuron);

    void setWeight(int fromNeuron, int toNeuron, Float v);
    
    void changeNeuronActivationFunctionLinearity(int id, Float incDec);

    boolean isIsStable();

    Float[] updateSM(Float[] input);

    Float[] update(Float[] input);
    
    int getMaxNeuronsLength();
    
    PtpNeuron getNeuron(int id);
    
    int getActualConnectionsLength();
    
    int getActualInputToOutputConnectionsLength();
    
    int getActualInternalConnectionsLength();

    float getConnectionDensity();
    
    float getInternalConnectionDensity();
    
    Object clone() throws CloneNotSupportedException;
    
    void updateOutputNeuronsLength();
    
    void updateInputNeuronsLength();
    
    void updateOutputNeurons();
    
    void updateInputNeurons();
    
    int getOutputNeuronsLength();
    
    int getInputNeuronsLength();
    
//    int getEfectiveBackwardSize();
//    
//     int getEfectiveForwadSize();
}
