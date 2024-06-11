package org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.actfunctions;

import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.PtpSingleNeuron;

/**
 *
 * @author josepcanellas
 */
@ActivationFunctionInfo(id="SIGM", name="Sigmoid")
public class SigmoidActivationFunction implements ActivationFunction{
    private static final long serialVersionUID = 2478541542732890015L;        
    private float betaFactor;
    private float beta;

    public SigmoidActivationFunction(float betaFactor, float beta) {
        this.betaFactor = betaFactor;
        this.beta = beta;
    }

    public SigmoidActivationFunction(float beta) {
        this.beta = beta;
        this.betaFactor=1;
    }

    public SigmoidActivationFunction() {
        beta=0.01f;
        betaFactor=1;
    }
    
    

    /**
     * @return the betaFactor
     */
    public float getBetaFactor() {
        return betaFactor;
    }

    /**
     * @param betaFactor the betaFactor to set
     */
    public void setBetaFactor(float betaFactor) {
        this.betaFactor = betaFactor;
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
        return this.beta*this.betaFactor;
    }

    @Override
    public Float getResult(Float x) {
        return (float)(1/(1+Math.exp(-x.doubleValue()*getBetaTemperature())));
    }

    @Override
    public void changeLinearity(Float l) {
        this.beta+=l;
        if(this.beta<0){
            this.beta=0;
        }
        if(this.beta>20){
            this.beta=20;
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        SigmoidActivationFunction ret = new SigmoidActivationFunction(betaFactor, beta);
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
        hash = 89 * hash + Float.floatToIntBits(this.betaFactor);
        hash = 89 * hash + Float.floatToIntBits(this.beta);
        return hash;
    }

    @Override
    public String toString() {
        return String.format("AF.B:%5.3f", getBetaTemperature());
    }
}
