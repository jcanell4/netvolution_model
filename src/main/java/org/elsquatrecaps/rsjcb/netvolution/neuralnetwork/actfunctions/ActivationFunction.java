package org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.actfunctions;

import java.io.Serializable;

/**
 *
 * @author josepcanellas
 */
public interface ActivationFunction extends Cloneable, Serializable{
    Float getResult(Float x);
    void changeLinearity(Float factor);
    Object clone() throws CloneNotSupportedException;
    Float getXDerivative(Float x);
    Float getYDerivative(Float y);
    Float getTemperature();
}
