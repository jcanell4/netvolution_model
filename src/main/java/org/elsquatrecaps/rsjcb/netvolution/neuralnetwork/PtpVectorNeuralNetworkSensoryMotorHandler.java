package org.elsquatrecaps.rsjcb.netvolution.neuralnetwork;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.actfunctions.ThresholdLinearFunction;

/**
 *
 * @author josep
 */
public class PtpVectorNeuralNetworkSensoryMotorHandler implements NeuralNetworkSensoryHandler, NeuralNetworkMotorUpdaterHandler{
    private static final long serialVersionUID = 2478541542732890012L;    
    List<List<Integer>> posInputMatrix;
    List<List<Integer>> posOutputMatrix;
    ThresholdLinearFunction resultFunction;
    Float[] outputValues;
    int inputSize;
    InputOutputContributionValues inputContribution;
    InputOutputContributionValues outputContribution;

    public PtpVectorNeuralNetworkSensoryMotorHandler(int inputNeuronsSize, int inputSize, int outputSize) {
        this.posInputMatrix = new ArrayList<>();
        this.posOutputMatrix = new ArrayList<>();
        this.outputValues = new Float[outputSize];
        this.resultFunction = new ThresholdLinearFunction(0);
        for(int i=0; i<inputNeuronsSize; i++){
            posInputMatrix.add(new ArrayList<>());
        }
        for(int i=0; i<outputSize; i++){
            posOutputMatrix.add(new ArrayList<>());
        }
        this.inputSize = inputSize;
    }
    
    
    @Override
    public void outputUpdate(List<PtpNeuron> outputNeurons) {
        for(int i=0; i<outputValues.length; i++){
            outputValues[i] = 0f;
            for(int j=0; j<posOutputMatrix.get(i).size(); j++){
                outputValues[i] += outputNeurons.get(posOutputMatrix.get(i).get(j)).getStateValue() - 0.5f;
            }
        }
    }
    
    @Override
    public Float getInputContribution(int neuron, Float input[]){
        Float ret = 0f;
        if(neuron<posInputMatrix.size()){
            for(int j=0; j<posInputMatrix.get(neuron).size(); j++){
                ret += input[posInputMatrix.get(neuron).get(j)];
            }
        }
        return ret;
    }

    @Override
    public int sizeOfValues() {
        return this.outputValues.length;
    }

    @Override
    public Float getValue(int pos) {
        return resultFunction.getResult(outputValues[pos]);
    }

    @Override
    public Float[] getValues() {
        Float[] ret = new Float[outputValues.length];
        for(int i=0; i<outputValues.length; i++){
            ret[i] = getValue(i);
        }
        return ret;
    }
    
    public Float[] getNeuronValues(){
        return outputValues;
    }

    public int getInputSize() {
        return inputSize;
    }

    /**
     * @return the inputContribution
     */
    public InputOutputContributionValues getInputContribution() {
        return inputContribution;
    }

    /**
     * @param inputContribution the inputContribution to set
     */
    public void setInputContribution(InputOutputContributionValues inputContribution) {
        this.inputContribution = inputContribution;
    }

    public InputOutputContributionValues getOutputContribution() {
        return outputContribution;
    }

    public void setOutputContribution(InputOutputContributionValues outputContribution) {
        this.outputContribution = outputContribution;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        PtpVectorNeuralNetworkSensoryMotorHandler ret = new PtpVectorNeuralNetworkSensoryMotorHandler(posInputMatrix.size(), inputSize, outputValues.length);
        ret.resultFunction=(ThresholdLinearFunction) resultFunction.clone();
        ret.inputContribution=inputContribution;
        ret.outputContribution=outputContribution;
        for(int i=0; i<posInputMatrix.size(); i++){
            for(int j=0; j<posInputMatrix.get(i).size(); j++){
                ret.posInputMatrix.get(i).add(posInputMatrix.get(i).get(j));
            }
        }
        for(int i=0; i<posOutputMatrix.size(); i++){
            for(int j=0; j<posOutputMatrix.get(i).size(); j++){
                ret.posOutputMatrix.get(i).add(posOutputMatrix.get(i).get(j));
            }
        }
        return ret;
    }
    
    @Override
    public boolean equals(Object obj){
        boolean ret = false;
        if(obj instanceof PtpVectorNeuralNetworkSensoryMotorHandler){
            ret = this.hashCode() == obj.hashCode();
        }
        return ret;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.posInputMatrix);
        hash = 47 * hash + Objects.hashCode(this.posOutputMatrix);
        hash = 47 * hash + Objects.hashCode(this.resultFunction);
        hash = 47 * hash + Arrays.deepHashCode(this.outputValues);
        hash = 47 * hash + this.inputSize;
        hash = 47 * hash + Objects.hashCode(this.inputContribution);
        hash = 47 * hash + Objects.hashCode(this.outputContribution);
        return hash;
    }
}
