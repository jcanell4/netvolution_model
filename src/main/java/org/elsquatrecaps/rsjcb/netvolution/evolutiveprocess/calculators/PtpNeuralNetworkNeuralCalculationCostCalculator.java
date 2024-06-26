package org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.calculators;

import java.math.BigDecimal;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.PtpNeuralNetwork;

/**
 *
 * @author josep
 */
@SinglePropertyCalculatorInfo(id="calculationCost", description = "neural calculation cost")
public class PtpNeuralNetworkNeuralCalculationCostCalculator implements PtpNeuralNetworkSinglePropertyCalculator{
    
    @Override
    public BigDecimal calculate(PtpNeuralNetwork agent){
        float p = 1f/(agent.getMaxLoopsForResults()-1);
        float v = p*(agent.getCompletedCycles()-1);
        return BigDecimal.valueOf(v);
    }
}
