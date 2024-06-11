package org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.calculators;

import java.math.BigDecimal;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.PtpNeuralNetwork;

/**
 *
 * @author josep
 */
@SinglePropertyCalculatorInfo(id="neuronConnectionDensity", description = "neuron connection density")
public class PtpNeuralNetworkNeuronConnectionDensityCalculator implements PtpNeuralNetworkSinglePropertyCalculator{
    
    @Override
    public BigDecimal calculate(PtpNeuralNetwork agent){
        return BigDecimal.valueOf(agent.getConnectionDensity()); 
    }
}
