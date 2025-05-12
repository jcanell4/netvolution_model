package org.elsquatrecaps.rsjcb.netvolution.metrics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.PtpNeuralNetwork;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.PtpNeuron;

/**
 *
 * @author josep
 */
public class PtpFeatureNeuralNetwokVector implements FeatureNeuralNetworkVector{
    private List<Double> basicNetworkFeatures;
    private List<Double> singleTopologicalFeatures;
//    private List<List<Integer>> allStronglyConnectedComponents;
    private List<List<Integer>> functionalStronglyConnectedComponents;
    private List<List<Integer>> connetions;

    public PtpFeatureNeuralNetwokVector() {
    }
    
    public void init(PtpNeuralNetwork nn){
        int components; 
        int numCycles;

        basicNetworkFeatures = new ArrayList<>();
        singleTopologicalFeatures = new ArrayList<>();
        connetions = new ArrayList<>();
//        singleStructuralFeatures = new ArrayList<>();

        List<Float> biasValues = new ArrayList<>();
        List<Float> sigmoidScales = new ArrayList<>();
        // 1. Bias i Sigmoid
        for(PtpNeuron n: nn.getNeurons()){
            if(n.isPathToOutput()){
               biasValues.add(n.getBias());
               sigmoidScales.add(n.getActivationFunction().getTemperature());
            }
        }
        basicNetworkFeatures.add(mean(biasValues));
        basicNetworkFeatures.add(stdDev(biasValues));
        basicNetworkFeatures.add(mean(sigmoidScales));
        basicNetworkFeatures.add(stdDev(sigmoidScales));

        // 2. Pesos de les arestes
        List<Float> weights = new ArrayList<>();
        for(int i=0; i<nn.getMaxNeuronsLength(); i++){
            connetions.add(new ArrayList<>());
            for(int j=0; j<nn.getMaxNeuronsLength(); j++){
                if(i!=j && nn.getNeuron(j).isPathToOutput() 
                        && nn.getWeight(i, j)!=0){
                    weights.add(nn.getWeight(i, j));
                    connetions.get(i).add(1);
                }else{
                    connetions.get(i).add(0);
                }
            }
        }

        basicNetworkFeatures.add(mean(weights));
        basicNetworkFeatures.add(stdDev(weights));

        // 3. Informació de la connectivitat
//        numNodes = biasValues.size();
//        numEdges = weights.size();
//        density = (double) numEdges / (numNodes * (numNodes - 1));
//
//        basicNetworkFeatures.add((double) numEdges);
//        basicNetworkFeatures.add(density);
        basicNetworkFeatures.add((double) nn.getConnectionDensity());

        // 4. Nombre de cicles independents 
//        allStronglyConnectedComponents = kosajaruForAllNodes(nn);
        functionalStronglyConnectedComponents = kosajaruForJustFunctionalNodes(nn);
//        components = allStronglyConnectedComponents.size();
//        numCycles = numEdges - numNodes + components;
//        singleTopologicalFeatures.add((double) numCycles);
        components = functionalStronglyConnectedComponents.size(); 
        numCycles = nn.getActualConnectionsLength() - nn.getActualNeuronsLength() + components;
        singleTopologicalFeatures.add((double) numCycles);
        double fsize = 0;
        double in = 0;
        double on = 0;
        for(List<Integer>cycles: functionalStronglyConnectedComponents){
            fsize += cycles.size();
            for(Integer idn: cycles){
                if(idn < nn.getInputNeuronsLength()){
                    in += 1;
                }
                if(idn>=(nn.getMaxNeuronsLength()-nn.getOutputLength())){
                    on += 1;
                }
            }
        }
        singleTopologicalFeatures.add(fsize/functionalStronglyConnectedComponents.size());
        singleTopologicalFeatures.add(in);
        singleTopologicalFeatures.add(on);
        
//        for(int i=0; i<weights.; i++){
//            connetions.add(new ArrayList<>());
//            for(int j=0; j<numNodes; j++){
//                if(nn.getWeight(i, j)!=0){
//                    connetions.get(i).add(1);
//                }else{
//                    connetions.get(i).add(0);
//                }
//            }
//        }
    }
    
    private double mean(List<Float> vals) {
        return vals.stream().mapToDouble(v -> v).average().orElse(0.0);
    }

    private double stdDev(List<Float> vals) {
        double mean = mean(vals);
        return Math.sqrt(vals.stream().mapToDouble(v -> (v - mean) * (v - mean)).average().orElse(0.0));
    }
    
    private double structuralSquaredDiference(List<List<Integer>> connections2){
        double ret=0;
        for(int i=0; i<connetions.size(); i++){
            for(int j=0; j<connetions.get(i).size(); j++){
                ret = connetions.get(i).get(j)-connections2.get(i).get(j);
            }
        }
        return ret*ret;
    }
    
    private void dfsForward(int pos, List<Boolean> visited, PtpNeuralNetwork nn, Stack<Integer> stack){
        visited.set(pos, Boolean.TRUE);
        for(int j=0; j< nn.getMaxNeuronsLength(); j++){
            if(!visited.get(j) && nn.getWeight(pos, j)!=0){
                dfsForward(j, visited, nn, stack);
            }
        }
        stack.push(pos);
    }
    
    private void dfsBackward(int pos, List<Boolean> visited, PtpNeuralNetwork nn, List<List<Integer>> components, int currentComponent){
        if(currentComponent== components.size()){
            components.add(new ArrayList<>());
        }
        components.get(pos).add(pos);
        visited.set(pos, Boolean.TRUE);
        for(int i=0; i<nn.getMaxNeuronsLength(); i++){
            if(!visited.get(i) && nn.getWeight(i, pos)!=0){
                dfsBackward(i, visited, nn, components, currentComponent);
            }
        }
    }
    
    private List<List<Integer>> kosaraju(PtpNeuralNetwork nn, List<Boolean> visitedOriginal){
        List<Boolean> visited = new ArrayList<>(visitedOriginal);
        Stack<Integer> stack = new Stack<>();
        for(int i=0; i<nn.getMaxLoopsForResults(); i++){
            if(!visited.get(i)){
                dfsForward(i, visited, nn, stack);
            }
        }
        visited = new ArrayList<>(visitedOriginal);
        int componentsSize = 0;
        List<List<Integer>> components = new ArrayList<>();
        while(!stack.empty()){
            Integer j = stack.pop();
            if(!visited.get(j)){
                dfsBackward(j, visited, nn, components, componentsSize);
                componentsSize++;
            }
        }
        return components;
    }
    
//    private List<List<Integer>> kosajaruForAllNodes(PtpNeuralNetwork nn){
//        List<Boolean> visited = Arrays.asList(nn.getNeurons()).stream().map(n -> false).toList(); 
//        return kosaraju(nn, visited);
//    }

    private List<List<Integer>> kosajaruForJustFunctionalNodes(PtpNeuralNetwork nn){
        List<Boolean> visited = Arrays.asList(nn.getNeurons()).stream().map(n -> !n.isPathToOutput()).toList();
        return kosaraju(nn, visited);
    }

    @Override
    public double getTopologicalDistance(FeatureNeuralNetworkVector other) {
        List<Double> data1 = new ArrayList<>(basicNetworkFeatures);
        List<Double> data2 = new ArrayList<>(other.getBasicNetworkFeatures());
        data1.addAll(singleTopologicalFeatures);
        data2.addAll(other.getSingleTopologicalFeatures());
        double diff_square_sum = 0.0;
        for (int i = 0; i < data1.size(); i++) {
            diff_square_sum += (data1.get(i) - data2.get(i)) * (data1.get(i) - data2.get(i));
        }
        return Math.sqrt(diff_square_sum);    
    }

    @Override
    public double getStructuralDistance(FeatureNeuralNetworkVector other) {
        double diff_square_sum = 0.0;
        for (int i = 0; i < basicNetworkFeatures.size(); i++) {
            diff_square_sum += (basicNetworkFeatures.get(i) - other.getBasicNetworkFeatures().get(i)) 
                    * (basicNetworkFeatures.get(i) - other.getBasicNetworkFeatures().get(i));
        }
        diff_square_sum += structuralSquaredDiference(other.getConnetions());
        return Math.sqrt(diff_square_sum);   
    }

    @Override
    public double getSingleDistance(FeatureNeuralNetworkVector other) {
        double diff_square_sum = 0.0;
        for (int i = 0; i < basicNetworkFeatures.size(); i++) {
            diff_square_sum += (basicNetworkFeatures.get(i) - other.getBasicNetworkFeatures().get(i)) 
                    * (basicNetworkFeatures.get(i) - other.getBasicNetworkFeatures().get(i));
        }
        return Math.sqrt(diff_square_sum);    
    }

    @Override
    public double getStrictTopologicalDistance(FeatureNeuralNetworkVector other) {
        double diff_square_sum = 0.0;
        for (int i = 0; i < this.singleTopologicalFeatures.size(); i++) {
            diff_square_sum += (singleTopologicalFeatures.get(i) - other.getSingleTopologicalFeatures().get(i)) 
                    * (singleTopologicalFeatures.get(i) - other.getSingleTopologicalFeatures().get(i));
        }
        return Math.sqrt(diff_square_sum);  
    }

    @Override
    public double getStrictStructuralDistance(FeatureNeuralNetworkVector other) {
        double diff_square_sum = 0.0;
        diff_square_sum += structuralSquaredDiference(other.getConnetions());
        return Math.sqrt(diff_square_sum);   
    }

    @Override
    public List<Double> getBasicNetworkFeatures() {
        return basicNetworkFeatures;
    }

    @Override
    public List<Double> getSingleTopologicalFeatures() {
        return singleTopologicalFeatures;
    }

//    @Override
//    public List<List<Integer>> getAllStronglyConnectedComponents() {
//        return allStronglyConnectedComponents;
//    }

//    @Override
//    public List<List<Integer>> getFunctionalStronglyConnectedComponents() {
//        return functionalStronglyConnectedComponents;
//    }

    @Override
    public List<List<Integer>> getConnetions() {
        return connetions;
    }
}
