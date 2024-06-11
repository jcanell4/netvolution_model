package org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.calculators;

import java.math.BigDecimal;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.PtpNeuralNetwork;

/**
 *
 * @author josep
 */
@SinglePropertyCalculatorInfo(id="calculationCostComp", description = "1 - neural calculation cost rate")
public class PtpNeuralNetworkNeuralCalculationCostCompCalculator extends PtpNeuralNetworkNeuralCalculationCostCalculator implements PtpNeuralNetworkSinglePropertyCalculator{
    
    @Override
    public BigDecimal calculate(PtpNeuralNetwork agent){        
        return BigDecimal.ONE.subtract(super.calculate(agent)); 
    }
}
