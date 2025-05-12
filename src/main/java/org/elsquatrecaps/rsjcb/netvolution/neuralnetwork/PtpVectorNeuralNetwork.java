package org.elsquatrecaps.rsjcb.netvolution.neuralnetwork;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.actfunctions.SigmoidActivationFunction;

/**
 * Class to manage peer to peer neural network implemented with vectors
 * @author josepcanellas
 */
public class PtpVectorNeuralNetwork implements PtpNeuralNetwork {
    private static final long serialVersionUID = 2478541542732890691L;
    
    protected PtpSingleNeuron[] neurons;
    protected float[][] connections;
    protected transient List<PtpNeuron> inputNeurons = new ArrayList<>();
    protected transient List<PtpNeuron> outputNeurons= new ArrayList<>();
    protected PtpVectorNeuralNetworkSensoryMotorHandler smHandler;
    protected boolean isStable=false;
    protected boolean isStableGlob=true;
    protected int loopingTimesToStabilityCheck;
    protected int maxLoopsForResults;
    protected int completedCycles;
    protected int maxCompletedCycles=0;
    protected Set<Integer> neuronsToCheckForStability=new HashSet<>();
    private int actualConnectionsLength;
    private int actualInputToOutputConnectionsLength;
    private int inputNeuronsLength;
    private int outputNeuronsLength;
    protected int efectiveBackwardSize=0;
    protected int efectiveForwardSize=0;    

    public PtpVectorNeuralNetwork() {
    }

    public PtpVectorNeuralNetwork(PtpNeuralNetworkConfiguration config) {
        PtpVectorNeuralNetworkRandomInitializer.initialize(this, config);
    }
    
    public  PtpVectorNeuralNetwork(PtpNeuralNetworkConfiguration config, boolean isForDebugging){
        PtpVectorNeuralNetworkRandomInitializer.initialize(this, config, isForDebugging);
    }
    
    @Override
    public Float[] getLastUpdateSM(){
        return this.smHandler.getValues();
    }
    
    @Override
    public Float[] getLastUpdate(){
        Float[] ret;
        ret = new Float[getOutputNeurons().size()];
        for(int i=0; i<ret.length; i++){
            ret[i] = getOutputNeurons().get(i).getStateValue();
        }
        return ret;
    }
    
    @Override
    public Float[] updateSM(Float[] input){
        updateForCicles(getMaxLoopsForResults(), input);
        this.smHandler.outputUpdate(getOutputNeurons());
        return this.smHandler.getValues();
    }
    
    @Override
    public Float[] update(Float[] input){
        Float[] ret;
        updateForCicles(getMaxLoopsForResults(), input);
        ret = new Float[getOutputNeurons().size()];
        for(int i=0; i<ret.length; i++){
            ret[i] = getOutputNeurons().get(i).getStateValue();
        }
        return ret;
    }
    
    protected void updateForCicles(int forCicles, Float[] input){
        cleanForUpdate();
        updateNetworkOnce(input);
        for(int i=1; !isIsStable() && i<forCicles; i++){
            updateNetworkOnce();
        }
        isStableGlob=isStableGlob&&isStable;
        if(completedCycles>maxCompletedCycles){
            maxCompletedCycles=completedCycles;
        }
    }
    
    protected void cleanForUpdate(){
        completedCycles=0;
        for(PtpNeuron neuron: getNeurons()){
            neuron.cleanForUpdate();
        }
        isStable=false;
    }
    protected void updateNetworkOnce(){
        boolean lIsStable=true;
        for(int i=0; i<getNeurons().length; i++){
            for(int j=0; j<getNeurons().length; j++){
                if(getConnections()[i][j]!=0){
                    if(j>i){
                        getNeuron(i).setUpdateStatus(UpdateStatusValues.UNSTABLE);    
                    }
                    if(getNeuron(j).getUpdateStatus().equals(UpdateStatusValues.UNSTABLE) 
                            || completedCycles==0 && getNeuron(j).getUpdateStatus().equals(UpdateStatusValues.UPDATED)){
                        if(getNeuron(j).getUpdateStatus().equals(UpdateStatusValues.UNSTABLE)){
                            getNeuron(i).setUpdateStatus(UpdateStatusValues.UNSTABLE);
                        }
//                        if(getNeuron(iter).getUpdateStatus().equals(UpdateStatusValues.UPDATED)){
//                            System.out.println("ALERTA");
//                        }
                        getNeuron(i).partialUpdateForSum(getNeurons()[j].getStateValue()*getConnections()[i][j]);
                    }
                }
            }
            getNeurons()[i].endForUpdate(getLoopingTimesToStabilityCheck());            
            if(neuronsToCheckForStability.isEmpty() || neuronsToCheckForStability.contains(i)){
                lIsStable = lIsStable && getNeuron(i).getUpdateStatus().equals(UpdateStatusValues.UPDATED);
            }
        }
        isStable = lIsStable;
        completedCycles++;
    }
    
    protected void updateNetworkOnce(Float[] input){
        for(int i=0; i<getInputNeuronsLength(); i++){
            getNeurons()[i].partialUpdateForSum(smHandler.getInputContribution(i, input));
        }
        updateNetworkOnce();
    }
    
    @Override
    public int getInputLength(){
        return smHandler.inputSize;
    }

    @Override
    public int getOutputLength(){
        return smHandler.posOutputMatrix.size();
    }
    
    @Override
    public Float getNeuronSum(int i){
        return getNeurons()[i].getSumValue();
    }
    
    @Override
    public Float getNeuronValue(int i){
        return getNeurons()[i].getStateValue();
    }
    
    @Override
    public Float getOutputSum(int i){
        return smHandler.outputValues[i];
    }

    @Override
    public Float getOutputValue(int i){
        return smHandler.getValue(i);
    }
    
    @Override
    public Float getWeight(int fromNeuron, int toNeuron){
        return connections[toNeuron][fromNeuron];
    }
    
    @Override
    public String toString(){
        return toString(false);
    }
    
    public String toString(boolean transientValues){
        StringBuilder ret=new StringBuilder();
        ret.append("Is stable: ");
        ret.append((this.isStableGlob?"Y":"N"));
        ret.append("\nNeuron values{\n");
        for(int i=0; i<neurons.length; i++){
            String end = neurons[i].isParticipatingInCalculation()?", IO": (neurons[i].isPathFromInput()?", I":(neurons[i].isPathToOutput()?", O":""));
            if(transientValues){
                ret.append(String.format("N%1s%02d(bias:%9.7f, sum: %12.10f, val:%12.10f, beta:%12.10f%s)",neurons[i].ihoType , i,neurons[i].bias,  neurons[i].getSumValue(), neurons[i].getStateValue(), ((SigmoidActivationFunction)(neurons[i].getActivationFunction())).getBetaTemperature(), end));
            }else{
                ret.append(String.format("N%1s%02d(bias:%9.7f, beta:%12.10f%s)",neurons[i].ihoType, i,neurons[i].bias, ((SigmoidActivationFunction)(neurons[i].getActivationFunction())).getBetaTemperature(), end));                
            }
            ret.append("\n");
        }
        ret.append("}");
        ret.append("\nNeuron connections{\n");
        for(int j=0; j<neurons.length; j++){
            for(int i=0; i<neurons.length; i++){
                if(connections[i][j]!=0){
                    ret.append(String.format("N%02d --[%12.10f] --> N%02d", j, connections[i][j], i));
                    ret.append("\n");
                }
            }
        }
        ret.append("}\n");
        return ret.toString();
    }
        
    public List<Integer> getNeuronsContributedByNeuron(int neuronPos){
        ArrayList<Integer> ret = new ArrayList<>();
        for(int i=0; i<getNeurons().length; i++){
            if(getConnections()[i][neuronPos]!=0){
                ret.add(i);
            }
        }
        return ret;
    }
    
    public List<Integer> getNeuronsContributingToNeuron(int neuronPos){
        ArrayList<Integer> ret = new ArrayList<>();
        for(int j=0; j<getNeurons().length; j++){
            if(getConnections()[neuronPos][j]!=0){
                ret.add(j);
            }
        }
        return ret;
    }
    
    
    /**
     * @return the neurons
     */
    @Override
    public PtpNeuron[] getNeurons() {
        return neurons;
    }

    /**
     * @return the connections
     */
    public float[][] getConnections() {
        return connections;
    }

    /**
     * @return the inputNeurons
     */
    @Override
    public List<PtpNeuron> getInputNeurons() {
        return inputNeurons;
    }

    /**
     * @return the outputNeurons
     */
    @Override
    public List<PtpNeuron> getOutputNeurons() {
        return outputNeurons;
    }

    /**
     * @return the isStable
     */
    @Override
    public boolean isIsStable() {
        return isStable;
    }

    /**
     * @return the loopingTimesToStabilityCheck
     */
    @Override
    public int getLoopingTimesToStabilityCheck() {
        return loopingTimesToStabilityCheck;
    }

    /**
     * @return the maxLoopsForResults
     */
    @Override
    public int getMaxLoopsForResults() {
        return maxLoopsForResults;
    }

    /**
     * @return the completedCycles
     */
    @Override
    public int getCompletedCycles() {
        return maxCompletedCycles;
    }

    @Override
    public int getMaxNeuronsLength() {
        return neurons.length;
    }

        @Override
    public int getActualNeuronsLength() {
        int ret =0;
        for(int i=0; i< neurons.length; i++){
            if(neurons[i].isPathToOutput()){
                ret++;
            }
        }
        return ret;
    }
    
    @Override
    public PtpNeuron getNeuron(int id) {
        return neurons[id];
    }

    @Override
    public void setWeight(int fromNeuron, int toNeuron, Float v) {
        if(this.connections[toNeuron][fromNeuron]==0 && v!=0){
            actualConnectionsLength++;
            if(fromNeuron<inputNeurons.size() && toNeuron>=neurons.length-outputNeurons.size()){
                actualInputToOutputConnectionsLength++;
            }
        }
        if(this.connections[toNeuron][fromNeuron]!=0 && v==0){
            actualConnectionsLength--;
            if(fromNeuron<inputNeurons.size() && toNeuron>=neurons.length-outputNeurons.size()){
                actualInputToOutputConnectionsLength--;
            }
        }
        this.connections[toNeuron][fromNeuron] = v;
        
    }

    @Override
    public void changeNeuronActivationFunctionLinearity(int id, Float incDec) {
        this.neurons[id].getActivationFunction().changeLinearity(incDec);
    }

    @Override
    public int getActualConnectionsLength() {
        return actualConnectionsLength;
    }

    @Override
    public Object clone() throws CloneNotSupportedException{
        PtpVectorNeuralNetwork ret = new PtpVectorNeuralNetwork();
        ret.connections = new float[this.connections.length][this.connections.length];
        for(int i=0; i<connections.length; i++){
            for(int j=0; j<connections[i].length; j++){
                ret.connections[i][j]=connections[i][j];
            }
        }
        ret.loopingTimesToStabilityCheck=loopingTimesToStabilityCheck;
        ret.maxLoopsForResults=maxLoopsForResults;
        ret.neurons = new PtpSingleNeuron[neurons.length];
        for(int i=0; i<neurons.length; i++){
            ret.neurons[i]=(PtpSingleNeuron) neurons[i].clone();
        }
        ret.actualConnectionsLength = actualConnectionsLength;
        ret.actualInputToOutputConnectionsLength = actualInputToOutputConnectionsLength;
        ret.neuronsToCheckForStability = neuronsToCheckForStability;
        ret.inputNeuronsLength = inputNeuronsLength;
        ret.outputNeuronsLength = outputNeuronsLength;

        
        ret.inputNeurons=new ArrayList<>();
        for(int i=0; i<inputNeurons.size(); i++){
            ret.inputNeurons.add(ret.neurons[i]);
        }
        ret.isStableGlob=isStableGlob;
        ret.maxCompletedCycles = maxCompletedCycles;
        
        int oPos=neurons.length-outputNeurons.size();
        ret.outputNeurons=new ArrayList<>();
        for(int i=0; i<outputNeurons.size(); i++){
            ret.outputNeurons.add(ret.neurons[oPos+i]);
        }
        ret.smHandler = (PtpVectorNeuralNetworkSensoryMotorHandler) smHandler.clone();
        ret.efectiveBackwardSize = this.efectiveBackwardSize;
        ret.efectiveForwardSize = this.efectiveForwardSize;        
        return ret;
    }

    @Override
    public float getConnectionDensity() {
        float c;
        float maxConnections = this.neurons.length*(this.neurons.length-1);
        float minConnections = Math.max(this.inputNeurons.size(), this.outputNeurons.size());
        minConnections = Math.min(minConnections, this.getActualInputToOutputConnectionsLength());
        c = Math.abs(this.getActualConnectionsLength()-minConnections);
        return c/(maxConnections-minConnections);
    }

    @Override
    public float getInternalConnectionDensity() {
        int nl = this.neurons.length-this.inputNeurons.size()-this.outputNeurons.size();
        float maxConnections = nl*(nl-1)-this.inputNeurons.size();
        return (float)this.getActualInternalConnectionsLength()/(  float)maxConnections;
    }

    /**
     * @return the actualInputToOutputConnectionsLength
     */
    @Override
    public int getActualInputToOutputConnectionsLength() {
        return actualInputToOutputConnectionsLength;
    }
    
    @Override
    public int getActualInternalConnectionsLength(){
        return getActualConnectionsLength()-getActualInputToOutputConnectionsLength();
    }

    /**
     * @return the inputNeuronsLength
     */
    @Override
    public int getInputNeuronsLength() {
        return inputNeuronsLength;
    }

    @Override
    public void updateInputNeuronsLength() {
        this.inputNeuronsLength = this.getInputNeurons().size();
    }

    @Override
    public void updateInputNeurons() {
        if(this.getInputNeurons()==null){
            this.inputNeurons = new ArrayList<>();
        }else{
            this.getInputNeurons().clear();
        }
        for(int i=0; i<this.inputNeuronsLength; i++){
            this.getInputNeurons().add((PtpSingleNeuron) this.getNeuron(i));
        }
    }

    /**
     * @return the outputNeuronsLength
     */
    @Override
    public int getOutputNeuronsLength() {
        return outputNeuronsLength;
    }

    @Override
    public void updateOutputNeuronsLength() {
        this.outputNeuronsLength = this.getOutputNeurons().size();
    }
    
    @Override
    public void updateOutputNeurons() {
        int oPos=this.getNeurons().length-this.outputNeuronsLength;
        if(this.getOutputNeurons()==null){
            this.outputNeurons = new ArrayList<>();
        }else{
            this.getOutputNeurons().clear();
        }
        for(int i=0; i<this.outputNeuronsLength; i++){
            this.getOutputNeurons().add((PtpSingleNeuron) this.getNeuron(oPos+i));
        }
    }
    
    @Override
    public boolean equals(Object obj){
        boolean ret = false;
        if(obj instanceof PtpVectorNeuralNetwork){
            ret = this.hashCode() == obj.hashCode();
        }
        return ret;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Arrays.deepHashCode(this.neurons);
        hash = 83 * hash + Arrays.deepHashCode(this.connections);
        hash = 83 * hash + Objects.hashCode(this.smHandler);
        hash = 83 * hash + (this.isStable ? 1 : 0);
        hash = 83 * hash + this.loopingTimesToStabilityCheck;
        hash = 83 * hash + this.maxLoopsForResults;
        hash = 83 * hash + this.completedCycles;
        hash = 83 * hash + Objects.hashCode(this.neuronsToCheckForStability);
//        hash = 83 * hash + this.actualConnectionsLength;
        hash = 83 * hash + this.efectiveForwardSize;
        hash = 83 * hash + this.efectiveBackwardSize;
        hash = 83 * hash + this.actualInputToOutputConnectionsLength;
        hash = 83 * hash + this.inputNeuronsLength;
        hash = 83 * hash + this.outputNeuronsLength;
        hash = 83 * hash + Objects.hashCode(this.inputNeurons);
        hash = 83 * hash + Objects.hashCode(this.outputNeurons);
        return hash;
    }

    @Override
    public void train(Float[][] inputs, Float[][] outputs, int iterations) {
        train(inputs, outputs, iterations, 0.1f);
    }
    
    @Override
    public void train(Float[][] inputs, Float[][] outputs, int iterations, float learningRate) {
        for(int iter=0; iter<iterations; iter++){
            trainOneEpoche(inputs, outputs, learningRate);
        }
    }
    
    public void trainOneEpoche(Float[][] inputs, Float[][] outputs, float learningRate){
        for(int i=0; i<inputs.length; i++){
            trainOneEntry(inputs[i], outputs[i], learningRate);
        }
    }
    
    public void trainOneEntry(Float[] inputs, Float[] outputs, float learningRate){
        update(inputs);
        
        // Backpropagation       
        double[] error = new double[getNeurons().length];
        double[] delta = new double[getNeurons().length];
        final double epsilon = 1e-6;
        final int maxIterations = getNeurons().length;            
        for(int i = getNeurons().length-getOutputNeuronsLength(); i< getNeurons().length; i++) { // Només les neurones de sortida contribueixen a l'error
            error[i] = outputs[i - (getNeurons().length - getOutputLength())] - getNeuronValue(i);
            double derivative = getNeuron(i).getActivationFunction().getYDerivative(getNeuronValue(i));
            delta[i] = error[i] * derivative;
        }
        for(int i=getNeurons().length-getOutputNeuronsLength()-1; i>=0; i--){// De sortida a entrada
                double sum = 0;
                for (int j = 0; j < getNeurons().length; j++) {
                    sum = getWeight(j, i) * delta[j]; // Ponderem l'error per la força de connexió
                }
                error[i] = sum;
                double derivative = getNeuron(i).getActivationFunction().getYDerivative(getNeuronValue(i));
                delta[i] = sum * derivative; // Propagació del gradient            
        }
        for(int iteration = 1; !errorConverged(error, epsilon) && iteration<getNeurons().length; iteration++){
            for (int i = 0; i <getNeurons().length; i++) { //Repetim per a cada neurona o fins que l'error sigui massa petit
                double sum = 0;
                for (int j = 0; j < getNeurons().length; j++) {
                    sum += getWeight(j, i) * delta[j]; // Ponderem l'error per la força de connexió
                }
                error[i] += sum;
                double derivative = getNeuron(i).getActivationFunction().getYDerivative(getNeuronValue(i));
                delta[i] = error[i] * derivative; // Propagació del gradient
            }
        }

            // ACTUALITZACIÓ DELS PESOS
            for (int i = 0; i < getNeurons().length; i++) {
                for (int j = 0; j < getNeurons().length; j++) {
                    double w = getWeight(i, j) + learningRate * delta[i] * getNeuronSum(j);
                    setWeight(i, j, (float) w);
                }
            }

            // Actualitza biaixos (només per a neurones no d'entrada)
            for (int i = 0; i <getNeurons().length ; i++) {
                double b = learningRate * delta[i];
                getNeuron(i).changeBias((float)b);
            }            
    }
    
    private boolean errorConverged(double[] error, double epsilon){
        for (double error1 : error) {
            if (Math.abs(error1) > epsilon) {
                return false; // Encara no ha convergit
            }
        }
        return true;        
    }
    
//    public float[] forward(float[] input){
//        int numNeurons = this.getNeurons().length;
//        float[] output = new float[numNeurons];
//        for (int i = 0; i < numNeurons; i++) {
//            double sum = 0;
//            for (int j = 0; j < numNeurons; j++) {
//                sum += W[i][j] * input[j];
//            }
//            output[i] = sigmoid(sum, beta[i]);
//        }
//        return output;        
//    }
//    
//    public SimpleMatrix forward(SimpleMatrix inputs) {
//        SimpleMatrix mweights = new SimpleMatrix(this.connections);
//        SimpleMatrix mbias = new SimpleMatrix(this.getNeurons().length, 1);
//        for(int i=0; i<this.getNeurons().length; i++){
//            mbias.set(i, this.getNeuron(i).getBias());
//        }        
//        return forward(inputs, mweights, mbias);        
//    }
//    
//    public SimpleMatrix forward(SimpleMatrix inputs, SimpleMatrix W, SimpleMatrix b) {
//        SimpleMatrix h = new SimpleMatrix(W.getNumRows(), 1);
//        
//        // Inicialitza les entrades
//        for (int i = 0; i < getInputNeuronsLength(); i++) {
//            h.set(i, 0, inputs.get(i));
//        }
//
//        // Propaga fins a convergència (màxim 100 iteracions)
//        for (int step = 0; step < 100; step++) {
//            SimpleMatrix newH = W.mult(h).plus(b);
//            // Aplica la sigmoide modificada a cada neurona (excepte entrades)
//            for (int neuronId = getInputNeuronsLength(); neuronId < newH.getNumRows(); neuronId++) {
//                double z = newH.get(neuronId, 0);
//                newH.set(neuronId, 0, this.getNeuron(neuronId).getActivationFunction().getResult( (float) z));
//            }
//            if (newH.minus(h).normF() < 1e-6) break;  // Convergència
//            h = newH;
//        }
//        return h;
//    }
}
