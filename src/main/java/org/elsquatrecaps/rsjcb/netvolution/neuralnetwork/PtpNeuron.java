package org.elsquatrecaps.rsjcb.netvolution.neuralnetwork;

import java.io.Serializable;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.actfunctions.ActivationFunction;

/**
 *
 * @author josep
 */
public interface PtpNeuron extends Cloneable, Serializable{

    Float getStateValue();

    Float getSumValue();

    boolean isStabilized(int loopingTimesToCheck);
    
    ActivationFunction getActivationFunction();
    
    int getId();

    boolean isParticipatingInCalculation();
    
    Object clone() throws CloneNotSupportedException;
    
    void changeBias(Float increment);
    
    Float getBias();
    
    boolean isPathToOutput();

    void setPathToOutput(boolean pathToOutput);

    boolean isPathFromInput();

    void setPathFromInput(boolean pathFromInput);
    
    void setIhoType(char ihoType);
    
    boolean isInputType();

    boolean isOutputType();
    
    UpdateStatusValues getUpdateStatus();
    
    void setUpdateStatus(UpdateStatusValues v);
    
    //void setUpdateStatus(UpdateStatusValues v, boolean isValueStabilized);
    
    void cleanForUpdate();
    
//    void startForUpdate();
    
    void endForUpdate(int loopingTimesToCheck);
    
    void partialUpdateForSum(Float partialValue);
    
//    void updateForChecking();
    
}
