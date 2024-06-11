package org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.calculators;

import java.math.BigDecimal;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.PtpNeuralNetwork;

/**
 *
 * @author josep
 */
@SinglePropertyCalculatorInfo(id="linearityDegree", description = "neural avererage of activation function linearity degree")
public class PtpNeuralNetworkActivationFunctionlinearityDegreeCalculator implements PtpNeuralNetworkSinglePropertyCalculator{
    
    @Override
    public BigDecimal calculate(PtpNeuralNetwork agent){
        throw new UnsupportedOperationException("PtpNeuralNetworkActivationFunctionlinearityDegreeCalculator::calculate(PtpNeuralNetwork agent) is not supported yet");
    }
}
