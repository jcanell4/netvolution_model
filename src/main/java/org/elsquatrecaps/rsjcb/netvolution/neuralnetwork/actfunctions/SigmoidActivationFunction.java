package org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.actfunctions;

import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.PtpSingleNeuron;

/**
 *
 * @author josepcanellas
 */
@ActivationFunctionInfo(id="SIGM", name="Sigmoid")
public class SigmoidActivationFunction implements ActivationFunction{
    private static final long serialVersionUID = 2478541542732890015L;        
    private float beta;

    public SigmoidActivationFunction(float beta) {
        this.beta = beta;
    }

    public SigmoidActivationFunction() {
        beta=0.01f;
    }

    /**
     * @return the beta
     */
    public float getBeta() {
        return beta;
    }

    /**
     * @param beta the beta to set
     */
    public void setBeta(float beta) {
        this.beta = beta;
    }
    
    public float getBetaTemperature(){
        return this.beta;
    }

    @Override
    public Float getResult(Float x) {
        return (float)(1/(1+Math.exp(-x.doubleValue()*getBetaTemperature())));
    }

    @Override
    public Float getXDerivative(Float x) {
        Float s = getResult(x);
        return getYDerivative(s);
    }

    @Override
    public Float getYDerivative(Float y) {
        return getBetaTemperature() * y * (1-y);
    }

    @Override
    public void changeLinearity(Float l) {
        this.beta+=l;
        if(this.beta<0){
            this.beta=0;
        }
        if(this.beta>100){
            this.beta=100;
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        SigmoidActivationFunction ret = new SigmoidActivationFunction(beta);
        return ret;
    }
    
    @Override
    public boolean equals(Object obj){
        boolean ret = false;
        if(obj instanceof PtpSingleNeuron){
            ret = this.hashCode() == obj.hashCode();
        }
        return ret;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Float.floatToIntBits(this.beta);
        return hash;
    }

    @Override
    public String toString() {
        return String.format("AF.B:%5.3f", getBetaTemperature());
    }
    
    @Override
    public Float getTemperature(){
        return getXDerivative(0f);
    }
}
