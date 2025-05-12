package org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.calculators;

import java.math.BigDecimal;
import org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.EvolutionaryCycleInfo;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.PtpNeuralNetwork;

/**
 *
 * @author josep
 */
@EnvironmentInfoPropertyCalculatorInfo(id="replacedAgentsRatio", description = "replaced agents ratio")
public class EnvironmentInfoReplacedAgentsCalculator implements EnvironmentInfoPropertyCalculator{

    @Override
    public BigDecimal calculate(EvolutionaryCycleInfo info, PtpNeuralNetwork[] population) {
        BigDecimal ret = new  BigDecimal(info.getReplacedAgents() / (double) population.length);
        return ret;
    }
}
