package org.elsquatrecaps.rsjcb.netvolution.metrics;

import java.util.List;

/**
 *
 * @author josep
 */
public interface FeatureNeuralNetworkVector {
    double getSingleDistance(FeatureNeuralNetworkVector other);
    double getTopologicalDistance(FeatureNeuralNetworkVector other);
    double getStrictTopologicalDistance(FeatureNeuralNetworkVector other);
    double getStructuralDistance(FeatureNeuralNetworkVector other);
    double getStrictStructuralDistance(FeatureNeuralNetworkVector other);
    List<Double> getBasicNetworkFeatures();
    List<Double> getSingleTopologicalFeatures();
//    List<List<Integer>> getAllStronglyConnectedComponents();
//    List<List<Integer>> getFunctionalStronglyConnectedComponents();
    List<List<Integer>> getConnetions();
}
