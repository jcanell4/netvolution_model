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

    public ThresholdLinearFunction() {
        hiddenThreshold=1000;
    }
    
    public ThresholdLinearFunction(float threshold) {
        this.hiddenThreshold = threshold;
    }
    
    
    
    @Override
    public Float getResult(Float x) {
        Float ret;
        if(x>=this.getThreshold()){
            ret = 1f;
        }else if(x<=-this.getThreshold()){
            ret = 0f;
        }else{
            ret = (x+this.getThreshold())/(this.getThreshold()+this.getThreshold());
        }
        return ret;
    }   

    @Override
    public Float getXDerivative(Float x) {
        Float ret;
        if(x>=this.getThreshold()){
            ret = 0f;
        }else if(x<=-this.getThreshold()){
            ret = 0f;
        }else{
            ret = 1/(this.getThreshold()+this.getThreshold());
        }
        return ret;
    }   

    @Override
    public Float getYDerivative(Float y) {
        Float ret;
        if(1f == y){
            ret = 0f;
        }else if(y== 0f){
            ret = 0f;
        }else{
            ret = 1/(this.getThreshold()+this.getThreshold());
        }
        return ret;
    }   

    /**
     * @return the threshold
     */
    public float getThreshold() {
        return hiddenThreshold;
    }

    /**
     * @param hthreshold the threshold to set
     */
    public void setHiddenThreshold(float hthreshold) {
        this.hiddenThreshold = hthreshold;
    }

    public float getHiddenThreshold() {
        return hiddenThreshold;
    }

    @Override
    public void changeLinearity(Float l) {
        this.hiddenThreshold += l;
        if(hiddenThreshold<0){
            hiddenThreshold=0f;
        }
        if(hiddenThreshold>1000){
            hiddenThreshold=1000;
        }
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        ThresholdLinearFunction ret = new ThresholdLinearFunction(hiddenThreshold);
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
        return hash;
    }

    @Override
    public String toString() {
        return String.format("Thr: %f", getThreshold());
    }
    
    @Override
    public Float getTemperature(){
        return getXDerivative(0f);
    }
}
