package org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.optimization;

import java.math.BigDecimal;
import org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.calculators.PtpNeuralNetworkTrueTableGlobalCalculator;

/**
 *
 * @author josep
 */
public class AgentOptimizationValuesForReproduction implements Comparable<AgentOptimizationValuesForReproduction> {
    
    private int id;
    private BigDecimal performance;
    private BigDecimal reprodutciveAdvantage;
    protected BigDecimal reproductionRate = BigDecimal.ZERO;

    public AgentOptimizationValuesForReproduction(int id) {
        this.id = id;
        this.performance = BigDecimal.ZERO;
        this.reprodutciveAdvantage = BigDecimal.ZERO;
    }

    public AgentOptimizationValuesForReproduction(int id, BigDecimal[] performanceAndReprodutciveAdvantage) {
        this.id = id;
        this.performance = performanceAndReprodutciveAdvantage[0];
        this.reprodutciveAdvantage = performanceAndReprodutciveAdvantage[1];
    }

    public AgentOptimizationValuesForReproduction(int id, BigDecimal performance, BigDecimal reprodutciveAdvantage) {
        this.id = id;
        this.performance = performance;
        this.reprodutciveAdvantage = reprodutciveAdvantage;
    }

    public AgentOptimizationValuesForReproduction(int id, PtpNeuralNetworkTrueTableGlobalCalculator.PerformaceAndReproductiveAdvantage pra) {
        this.id = id;
        this.performance = pra.getPerformace();
        this.reprodutciveAdvantage = pra.getReproductiveAdvantage(true);
    }

    public BigDecimal getReporductiveAdvantageValue(boolean reproductiveAdvantageOnly) {
        BigDecimal ret;
        if (reproductiveAdvantageOnly) {
            ret = reprodutciveAdvantage;
        } else {
            ret = getReporductiveAdvantageValue();
        }
        return ret;
    }

    public BigDecimal getReporductiveAdvantageValue() {
        return performance.add(reprodutciveAdvantage);
    }

    @Override
    public int compareTo(AgentOptimizationValuesForReproduction t) {
        return getReporductiveAdvantageValue().compareTo(t.getReporductiveAdvantageValue());
    }

    public BigDecimal getPerformance() {
        return performance;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the reprodutciveAdvantage
     */
    public BigDecimal getReprodutciveAdvantage() {
        return reprodutciveAdvantage;
    }

    /**
     * @return the reproductionRate
     */
    public BigDecimal getReproductionRate() {
        return reproductionRate;
    }
    
}
