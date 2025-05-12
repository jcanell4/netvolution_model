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

    Float[] getLastUpdateSM();

    Float[] getLastUpdate();
    
    Float[] updateSM(Float[] input);

    Float[] update(Float[] input);
    
    int getMaxNeuronsLength();
    
    PtpNeuron getNeuron(int id);
    
    int getActualNeuronsLength();
  
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
    
    void train(Float[][] inputs, Float[][] outputs,  int iterations);

    void train(Float[][] inputs, Float[][] outputs,  int iterations, float learningRate);
    
//    int getEfectiveBackwardSize();
//    
//     int getEfectiveForwadSize();
}
