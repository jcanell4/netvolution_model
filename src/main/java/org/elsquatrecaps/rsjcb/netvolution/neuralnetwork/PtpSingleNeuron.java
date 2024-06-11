package org.elsquatrecaps.rsjcb.netvolution.neuralnetwork;

import java.util.Objects;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.actfunctions.ActivationFunction;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.actfunctions.SigmoidActivationFunction;

/**
 *
 * @author josepcanellas
 */
public class PtpSingleNeuron implements PtpNeuron {
    private static final long serialVersionUID = 2478541542732890014L;        
    int id;
    StabilityChecker stabilityChecker;
    transient Float sumValue = 0f;
    transient UpdateStatusValues updateStatus = UpdateStatusValues.UNSTABLE;
    ActivationFunction activationFunction;
    Float bias = 0f;
    boolean pathToOutput=false;
    boolean pathFromInput=false;
    char ihoType='h';

//    public SingleNeuron(int id, float equalityRange) {
//        this.id = id;
//        this.activationFunction=new SigmoidActivationFunction();
//        stabilityChecker = new PtpVectorNeuralNetworkStabilityChecker(equalityRange);
//    }
    
    public PtpSingleNeuron(int id, StabilityChecker checker) {
        this(id, new SigmoidActivationFunction(), checker);
    }

    public PtpSingleNeuron(int id, ActivationFunction activationFunction, StabilityChecker checker) {
        this(id, activationFunction, checker, 0);
    }
    
    public PtpSingleNeuron(int id, ActivationFunction activationFunction, StabilityChecker checker, float bias) {
        this.id = id;
        this.activationFunction=activationFunction;
        stabilityChecker = checker;
        this.bias = bias;
    }

    public PtpSingleNeuron(int id, ActivationFunction activationFunction, 
            StabilityChecker checker, float bias, boolean pTOut, boolean pFromIn, char ihoType) {
        this(id, activationFunction, checker, bias);
        this.pathFromInput = pFromIn;
        this.pathToOutput = pTOut;
        this.ihoType = ihoType;
    }
    
//    public SingleNeuron(int id) {
//        this(id, 0f);
//    }

//    public SingleNeuron(int id, ActivationFunction activationFunction) {
//        this(id, activationFunction, 0);
//    }
//
//    public SingleNeuron(int id, ActivationFunction activationFunction, float equalityRange) {
//        this.id = id;
//        this.activationFunction = activationFunction;
//        stabilityChecker = new PtpVectorNeuralNetworkStabilityChecker(equalityRange);
//    }


    @Override
    public void cleanForUpdate() {
        sumValue = 0f;
        stabilityChecker.clean();
        updateStatus = UpdateStatusValues.UPDATING;
    }

//    @Override
//    public void startForUpdate() {
//        updateStatus = UpdateStatusValues.UPDATING;
//    }

    @Override
    public void endForUpdate(int loopingTimesToCheck){
        if(!updateStatus.equals(UpdateStatusValues.UPDATED)){
            stabilityChecker.updateValue(getStateValue());
            if(updateStatus.equals(UpdateStatusValues.UPDATING)){
                updateStatus = UpdateStatusValues.UPDATED;
            }else if(stabilityChecker.valuesAreStabilized(loopingTimesToCheck)){
                updateStatus = UpdateStatusValues.UPDATED;
            }
        }
    }
    
    @Override
    public boolean isStabilized(int loopingTimesToCheck){
        return stabilityChecker.valuesAreStabilized(loopingTimesToCheck);
    }

    @Override
    public void partialUpdateForSum(Float partialValue) {
        if(updateStatus==UpdateStatusValues.UPDATED){
            throw new RuntimeException("Error trying to update partial contribution value from UPDATED status");
        }
        sumValue += partialValue;
    }
    
    @Override
    public Float getSumValue(){
        return sumValue + bias;
    }
    
    @Override
    public Float getStateValue(){
        Float ret;
//        if(updateStatus==UpdateStatusValues.UNSTABLE){
//            ret = 0f;
//        }else{
            ret = activationFunction.getResult(getSumValue());
//        }
        return ret;
    }

    @Override
    public ActivationFunction getActivationFunction() {
        return this.activationFunction;
    }        

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return new PtpSingleNeuron(id, 
                (ActivationFunction) activationFunction.clone(), 
                (StabilityChecker) stabilityChecker.clone(), 
                this.bias,
                this.pathToOutput,
                this.pathFromInput,
                this.ihoType);
    }
    
    @Override
    public void changeBias(Float increment){
        this.bias += increment;
    }
    
    /**
     *
     * @param net
     * @return
     */
    @Override
    public boolean equals(Object net){
        boolean ret = false;
        if(net instanceof PtpSingleNeuron){
            ret = this.hashCode() == net.hashCode();
        }
        return ret;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + this.id;
        hash = 37 * hash + Objects.hashCode(this.stabilityChecker);
        hash = 37 * hash + Objects.hashCode(this.activationFunction);
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder stb = new StringBuilder();
        stb.append("N");
        stb.append(id);
        stb.append(String.format("-b:%4.2f", this.bias));
        stb.append("\n(");
        stb.append(this.getActivationFunction().toString());
        stb.append(")");
        return stb.toString();
    }

    @Override
    public boolean isParticipatingInCalculation() {
        return pathFromInput && pathToOutput;
    }

    /**
     * @return the bias
     */
    @Override
    public Float getBias() {
        return bias;
    }

    /**
     * @return the pathToOutput
     */
    @Override
    public boolean isPathToOutput() {
        return pathToOutput;
    }

    /**
     * @param pathToOutput the pathToOutput to set
     */
    @Override
    public void setPathToOutput(boolean pathToOutput) {
        if(ihoType!='o'){
            this.pathToOutput = pathToOutput;
        }
    }

    /**
     * @return the pathFromInput
     */
    @Override
    public boolean isPathFromInput() {
        return pathFromInput;
    }

    /**
     * @param pathFromInput the pathFromInput to set
     */
    @Override
    public void setPathFromInput(boolean pathFromInput) {
        if(ihoType!='i'){
            this.pathFromInput = pathFromInput;
        }
    }

    /**
     * @param ihoType the ihoType to set
     */
    public void setIhoType(char ihoType) {
        this.ihoType = ihoType;
        switch (ihoType) {
            case 'i':
                this.pathFromInput=true;
                break;
            case 'o':
                this.pathToOutput=true;
                break;
        }
    }

    @Override
    public UpdateStatusValues getUpdateStatus() {
        return updateStatus;
    }

    @Override
    public void setUpdateStatus(UpdateStatusValues v) {        
        updateStatus = v;
    }

    public void setUpdateStatus(UpdateStatusValues v, boolean isValueStabilized) {        
        if(!v.equals(UpdateStatusValues.UNSTABLE)){
            updateStatus = v;
        }else if(isValueStabilized){        
            updateStatus = UpdateStatusValues.UPDATED;
        }else{
            updateStatus = v;
        }
    }
}
