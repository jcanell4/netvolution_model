/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.calculators;

import java.math.BigDecimal;
import org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.EvolutionaryCycleInfo;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.PtpNeuralNetwork;

/**
 *
 * @author josep
 */
public interface EnvironmentInfoPropertyCalculator {
    BigDecimal calculate(EvolutionaryCycleInfo info, PtpNeuralNetwork[] population);
}
