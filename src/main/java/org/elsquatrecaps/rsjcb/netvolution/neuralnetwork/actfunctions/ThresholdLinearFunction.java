package org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.actfunctions;

import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.PtpSingleNeuron;

/**
 *
 * @author josepcanellas
 */
@ActivationFunctionInfo(id="LIN_THRES", name="Linear with threshold")
public class ThresholdLinearFunction implements ActivationFunction{
    private static final long serialVersionUID = 2478541542732890016L;        
    private float hiddenThreshold;
    private float thresholdFactor;

    public ThresholdLinearFunction() {
        hiddenThreshold=Float.MAX_VALUE;
        thresholdFactor=1;
    }

    public ThresholdLinearFunction(float threshold, float thresholtFactor) {
        this.hiddenThreshold = threshold;
        this.thresholdFactor = thresholtFactor;
    }
    
    public ThresholdLinearFunction(float threshold) {
        this.hiddenThreshold = threshold;
        this.thresholdFactor = 1;
    }
    
    
    
    @Override
    public Float getResult(Float x) {
        Float ret;
        if(x>this.getThreshold()){
            ret = 1f;
        }else if(x<=-this.getThreshold()){
            ret = 0f;
        }else{
            ret = x/this.getThreshold();
        }
        return ret;
    }   

    /**
     * @return the threshold
     */
    public float getThreshold() {
        return hiddenThreshold*thresholdFactor;
    }

    /**
     * @param hthreshold the threshold to set
     */
    public void setHiddenThreshold(float hthreshold) {
        this.hiddenThreshold = hthreshold;
    }

    /**
     * @return the thresholtFactor
     */
    public float getThresholdFactor() {
        return thresholdFactor;
    }

    /**
     * @param thresholtFactor the thresholtFactor to set
     */
    public void setThresholdFactor(float thresholtFactor) {
        this.thresholdFactor = thresholtFactor;
    }
    
    public float getHiddenThreshold() {
        return hiddenThreshold;
    }

    @Override
    public void changeLinearity(Float l) {
        this.hiddenThreshold += l;
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        ThresholdLinearFunction ret = new ThresholdLinearFunction(hiddenThreshold, thresholdFactor);
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
        int hash = 3;
        hash = 73 * hash + Float.floatToIntBits(this.hiddenThreshold);
        hash = 73 * hash + Float.floatToIntBits(this.thresholdFactor);
        return hash;
    }

    @Override
    public String toString() {
        return String.format("Thr: %f", getThreshold());
    }
}
