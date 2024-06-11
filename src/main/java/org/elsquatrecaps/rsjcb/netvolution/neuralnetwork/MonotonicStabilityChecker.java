package org.elsquatrecaps.rsjcb.netvolution.neuralnetwork;

import java.io.Serializable;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Objects;

/**
 *
 * @author josepcanellas
 */
public class MonotonicStabilityChecker implements StabilityChecker{
    private static final long serialVersionUID = 2478541542732890010L;    
    private Float lastValue;
    private final LinkedList<MinMaxValue> minMaxValues = new LinkedList<>();
    private Float absoluteMax;
    private Float absoluteMin;
    private Float equalityRange;
    private Comparator<Float> valueComparator=null;


    public MonotonicStabilityChecker() {
        this(0f);
    }
    
    public MonotonicStabilityChecker(Float equalityRange) {
        this.equalityRange = equalityRange;
    }
    
    public MonotonicStabilityChecker(Comparator<Float> comparator) {
        this.valueComparator = comparator;
    }
    
    @Override
    public void updateValue(Float value){
       if(minMaxValues.isEmpty()){
            absoluteMax=absoluteMin=value;
            minMaxValues.add(new MinMaxValue(MonotonicStateValues.UNDETERMINED, value));
            lastValue=value;
        }else{
            MonotonicStateValues current;
            if(valueComparator==null){
                current = MonotonicStateValues.valueOf(value-lastValue, equalityRange);
            }else{
                current = MonotonicStateValues.valueOf(valueComparator.compare(value, lastValue));
            }
            lastValue = value;   
            if(current==minMaxValues.getLast().getPrevioiusMonotonicState()){
                if(current==MonotonicStateValues.CONSTANT){
                    minMaxValues.getLast().setValue(value);
                }else if(current==MonotonicStateValues.DECREASING && value<minMaxValues.getLast().getValue()){
                    minMaxValues.getLast().setValue(value);
                    if(value<absoluteMin){
                        absoluteMin=value;
                    }
                }else if(current==MonotonicStateValues.INCREASING && value>minMaxValues.getLast().getValue()){
                    minMaxValues.getLast().setValue(value);
                    if(value>absoluteMax){
                        absoluteMax=value;
                    }
                }
            }else{
                minMaxValues.add(new MinMaxValue(current, value));
            }
        }            
    }
    
    @Override
    public boolean valuesAreStabilized(int loopingTimesToCheck){
        return !minMaxValues.isEmpty() 
                && minMaxValues.getLast().updatedTimes>=(loopingTimesToCheck-1) 
                && minMaxValues.getLast().getPrevioiusMonotonicState()==MonotonicStateValues.CONSTANT;
    }
    
    @Override
    public Float getAverageMinMaxValues(){
        float sum=0;
        for(MinMaxValue mm: minMaxValues){
            sum+=mm.getValue();
        }
        return sum/minMaxValues.size();
    }

    private void updateMinMax(Float value){
        if(minMaxValues.isEmpty()){
            absoluteMax=absoluteMin=value;
            minMaxValues.add(new MinMaxValue(MonotonicStateValues.UNDETERMINED, value));
        }else{
            MonotonicStateValues current = MonotonicStateValues.valueOf(value-lastValue, equalityRange);
            if(current==minMaxValues.getLast().getPrevioiusMonotonicState()){
                if(current==MonotonicStateValues.DECREASING && value<minMaxValues.getLast().getValue()){
                    minMaxValues.getLast().setValue(value);
                    if(value<absoluteMin){
                        absoluteMin=value;
                    }
                }else if(current==MonotonicStateValues.INCREASING && value>minMaxValues.getLast().getValue()){
                    minMaxValues.getLast().setValue(value);
                    if(value>absoluteMax){
                        absoluteMax=value;
                    }
                }
            }else{
                minMaxValues.add(new MinMaxValue(current, value));
            }
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        MonotonicStabilityChecker ret = new MonotonicStabilityChecker();
        ret.equalityRange=equalityRange;
        ret.valueComparator = valueComparator;
        return ret;
    }
    
    @Override
    public void clean(){
        this.absoluteMax = null;
        this.absoluteMin = null;
        this.lastValue = null;
        this.minMaxValues.clear();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.lastValue);
        hash = 17 * hash + Objects.hashCode(this.minMaxValues);
        hash = 17 * hash + Objects.hashCode(this.absoluteMax);
        hash = 17 * hash + Objects.hashCode(this.absoluteMin);
        hash = 17 * hash + Objects.hashCode(this.equalityRange);
        hash = 17 * hash + Objects.hashCode(this.valueComparator);
        return hash;
    }
    
    @Override
    public boolean equals(Object obj){
        boolean ret = false;
        if(obj instanceof MonotonicStabilityChecker){
            ret = this.hashCode() == obj.hashCode();
        }
        return ret;
    }
    
    
    private static class MinMaxValue implements Serializable{
        private static final long serialVersionUID = 2478541542732890011L;            
        private final MonotonicStateValues previoiusMonotonicState;
        private Float value;
        private int updatedTimes;

        protected MinMaxValue(MonotonicStateValues previoiusMonotonicState, Float value) {
            this.previoiusMonotonicState = previoiusMonotonicState;
            this.value = value;
            this.updatedTimes=1;
        }

        protected MonotonicStateValues getPrevioiusMonotonicState() {
            return previoiusMonotonicState;
        }

        protected Float getValue() {
            return value;
        }

        protected void setValue(Float value) {
            this.value = value;
            updatedTimes++;
        }

        protected int getUpdatedTimes() {
            return updatedTimes;
        }
        
        @Override
        public boolean equals(Object obj){
            boolean ret = false;
            if(obj instanceof MinMaxValue){
                ret = this.hashCode() == obj.hashCode();
            }
            return ret;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 73 * hash + Objects.hashCode(this.previoiusMonotonicState);
            hash = 73 * hash + Objects.hashCode(this.value);
            hash = 73 * hash + this.updatedTimes;
            return hash;
        }
        
    }
    
}
