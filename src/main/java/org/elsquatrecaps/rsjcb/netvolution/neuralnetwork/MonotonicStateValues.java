/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package org.elsquatrecaps.rsjcb.netvolution.neuralnetwork;

/**
 *
 * @author josepcanellas
 */
public enum MonotonicStateValues {
    UNDETERMINED(Integer.MAX_VALUE),
    DECREASING(-1),
    CONSTANT(0),
    INCREASING(1);
    
    private final int intValue;
    
    private MonotonicStateValues(int v) {
        this.intValue=v;
    }
    
    public int toInt(){
        return intValue;
    }
    
    public static MonotonicStateValues valueOf(int intValue){
        return (intValue==0?CONSTANT:(intValue<0?DECREASING:INCREASING));
    }

    public static MonotonicStateValues valueOf(float intValue){
        return (intValue==0?CONSTANT:(intValue<0?DECREASING:INCREASING));
    }
    
    public static MonotonicStateValues valueOf(int intValue, int margin){
        MonotonicStateValues ret;
        if(margin==0){
            ret = valueOf(intValue);
        }else if(-margin<intValue && intValue<margin){
            ret = CONSTANT;
        }else{
            ret =  (intValue==0?CONSTANT:(intValue<0?DECREASING:INCREASING));
        }
        return ret;
    }
    
    public static MonotonicStateValues valueOf(float floatValue, float margin){
        MonotonicStateValues ret;
        if(margin==0){
            ret = valueOf(floatValue);
        }else if(-margin<floatValue && floatValue<margin){
            ret = CONSTANT;
        }else{
            ret = (floatValue==0?CONSTANT:(floatValue<0?DECREASING:INCREASING));
        }
        return ret;
    }
}
