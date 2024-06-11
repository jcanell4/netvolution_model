package org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.calculators;

import java.math.BigDecimal;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.PtpNeuralNetwork;

/**
 *
 * @author josep
 */
@SinglePropertyCalculatorInfo(id="neuronConnectionDensityComp", description = "1 - neuron connection density rate")
public class PtpNeuralNetworkNeuronConnectionDensityCompCalculator extends PtpNeuralNetworkNeuronConnectionDensityCalculator implements PtpNeuralNetworkSinglePropertyCalculator{
    
    @Override
    public BigDecimal calculate(PtpNeuralNetwork agent){
        return BigDecimal.ONE.subtract(super.calculate(agent)); 
    }
}
