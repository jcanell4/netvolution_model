package org.elsquatrecaps.rsjcb.netvolution.neuralnetwork;

import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.actfunctions.ActivationFunction;

/**
 *
 * @author josepcanellas
 */
public class PtpConnectedNeuron extends PtpSingleNeuron {
    private static final long serialVersionUID = 2478541542732890013L;    

    public PtpConnectedNeuron(int id, StabilityChecker checker) {
        super(id, checker);
    }

    public PtpConnectedNeuron(int id, ActivationFunction activationFunction, StabilityChecker checker) {
        super(id, activationFunction, checker);
    }
    
//    List<PtpConnectedNeuron> propagateTo = new ArrayList<>();
//    List<Float> weights = new ArrayList<>();
//    List<Boolean> aportacions = new ArrayList<>();
//    List<PtpConnectedNeuron> valueFrom = new ArrayList<>();
    
    @Override
    public Object clone() throws CloneNotSupportedException{
        throw new CloneNotSupportedException("This method needs to be implemented");
    }    
}
