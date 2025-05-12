package org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.elsquatrecaps.rsjcb.netvolution.events.ErrorOnProcessEvolution;
import org.elsquatrecaps.rsjcb.netvolution.events.EvolutionaryEvent;
import org.elsquatrecaps.rsjcb.netvolution.events.EvolutionaryProcesSubscriptor;
import org.elsquatrecaps.rsjcb.netvolution.events.EvolutionaryProcessEventStore;
import org.elsquatrecaps.rsjcb.netvolution.events.EvolutionaryProcessInfoEditor;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.PtpNeuralNetwork;
import org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.PtpNeuralNetworkMutationProcessor;
import org.elsquatrecaps.utilities.tools.Pair;

/**
 * Conté agents que fa evolucionar sotmetent-los una pressió evolutiva. Els agents es reprodueixen amb certes mutacions.
 * fa evolucionar 
 * @author josep
 */
public class RunnablePtpVectorNeuralNetworkTrueTableEvolutionaryEnvironment extends PtpNeuralNetworkTrueTableEvolutionaryEnvironment implements Runnable{
    EvolutionaryProcessInfoEditor editor;
    List<Pair<String, Consumer<EvolutionaryEvent>>> consumers = new ArrayList<>();
    List<Pair<String, EvolutionaryProcesSubscriptor>> subscriptors = new ArrayList<>();
    
    protected RunnablePtpVectorNeuralNetworkTrueTableEvolutionaryEnvironment(
            PtpNeuralNetwork[] population,
            PtpNeuralNetworkMutationProcessor mutationProcessor){
        super(population, mutationProcessor);        
    }
    
    protected RunnablePtpVectorNeuralNetworkTrueTableEvolutionaryEnvironment(PtpNeuralNetworkTrueTableEvolutionaryCycleProcessor cycleProcessor){
        super(cycleProcessor);        
    }
    
//    public RunnablePtpVectorNeuralNetworkTrueTableEvolutionaryEnvironment(int populationSize, PtpNeuralNetworkConfiguration nnConfig, 
//            Float[][] environmentInputSet, Float[][] environmentOutputSet, List<String> propertiesToFollow) {
//        this( populationSize, nnConfig, environmentInputSet, environmentOutputSet, new String[0], new String[] {"neuronConnectionDensity"}, propertiesToFollow);
//        
//    }
//    private RunnablePtpVectorNeuralNetworkTrueTableEvolutionaryEnvironment(int populationSize, PtpNeuralNetworkConfiguration nnConfig, 
//            Float[][] environmentInputSet, Float[][] environmentOutputSet, String[] performance, String[] repAdv, List<String> propertiesToFollow) {
//        this(populationSize, nnConfig, environmentInputSet, environmentOutputSet, Arrays.asList(performance), Arrays.asList(repAdv), propertiesToFollow, SurviveOptimizationMethodValues.DYNAMIC_RATE, 50);
//    }
//    public RunnablePtpVectorNeuralNetworkTrueTableEvolutionaryEnvironment(
//            int populationSize, 
//            PtpNeuralNetworkConfiguration nnConfig, 
//            Float[][] environmentInputSet, 
//            Float[][] environmentOutputSet, 
//            List<String> performance, List<String> repAdv, List<String> propertiesToFollow, 
//            OptimizationMethod optimizationMethod,
////            SurviveOptimizationMethodValues deathRateType, 
//            double survivalRate,
//            boolean keepProgenyLines) {
//        super(new PtpVectorNeuralNetwork[populationSize], 
//                new PtpNeuralNetworkTrueTableGlobalCalculator(
//                        performance, 
//                        repAdv, 
//                        environmentInputSet, 
//                        environmentOutputSet), 
//                new PtpVectorNeuralNetworkMutationProcessor(), 
//                propertiesToFollow,
//                optimizationMethod,
////                deathRateType,
//                survivalRate,
//                keepProgenyLines);
//        for(int i=0; i<populationSize; i++){
//            PtpVectorNeuralNetwork net = new PtpVectorNeuralNetwork();
//            PtpVectorNeuralNetworkRandomInitializer.initialize(net, nnConfig);
//            this.getPopulation()[i] = net;
//        }
//        this.getMutationProcessor().setConnectionMutationRate(nnConfig.getConnectionMutationRate());
//        this.getMutationProcessor().setDisconnectionMutationRate(nnConfig.getDisconnectionMutationRate());
//        this.getMutationProcessor().setMaxThresholdExchangeFactorValue(nnConfig.getMaxThresholdExchangeFactorValue());
//        this.getMutationProcessor().setMaxWeightExchangevalue(nnConfig.getMaxWeightExchangevalue());
//        this.getMutationProcessor().setReceiverNeuronNumberMutationRate(nnConfig.getReceiverNeuronNumberMutationRate());
//        this.getMutationProcessor().setResponseNeuronNumberMutationRate(nnConfig.getResponseNeuronNumberMutationRate());
//        this.getMutationProcessor().setThresholdMutationRate(nnConfig.getThresholdMutationRate());
//        this.getMutationProcessor().setWeightsMutationRate(nnConfig.getWeightsMutationRate());
//        this.getMutationProcessor().setInputContributionrobability(nnConfig.getInputContributionrobability());
//        if(environmentInputSet.length!=environmentOutputSet.length){
//            throw new RuntimeException("The input array must be the same length as the output array");
//        }
//    }

    @Override
    public void init(float averagePerformanceForStopping, float desiredPerformance, int maxTimes) {
        EvolutionaryProcessEventStore eventStore = new EvolutionaryProcessEventStore("RUNNABLE-ENVIRONMENT-1", Math.max(100, maxTimes/1000));
        super.init(averagePerformanceForStopping, desiredPerformance, maxTimes); 
        this.setEvolutionaryProcessObserver(eventStore);
        this.editor = new EvolutionaryProcessInfoEditor(eventStore);
        if(!consumers.isEmpty()){
            for(Pair<String, Consumer<EvolutionaryEvent>> p: consumers){
                this.editor.subscribe(p.getFirst(), p.getSecond());
            }
            consumers.clear();
        }
        if(!subscriptors.isEmpty()){
            for(Pair<String, EvolutionaryProcesSubscriptor> p: subscriptors){
                this.editor.subscribe(p.getFirst(), p.getSecond());
            }
            subscriptors.clear();
        }
        
    }
    
    public void addEventHandler(String tyop, EvolutionaryProcesSubscriptor subs){
        if(editor==null){
            subscriptors.add(new Pair<>(tyop, subs));
        }else{
            editor.subscribe(tyop, subs);
        }
    }
    
    public void addEventHandler(String tyop, Consumer<EvolutionaryEvent> c){
        if(editor!=null){
            editor.subscribe(tyop, c);
        }else{
            consumers.add(new Pair<>(tyop, c));
        }
    }
    
    public void start() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        try{
//            Thread consumerThread = new Thread(this.editor);
//            consumerThread.setName("Editor of Evolution Events");
//            consumerThread.start();
            this.editor.start("Editor of Evolution Events");
            this.process();
        }catch(Exception e){
//            this.editor.finish();
            this.getObserver().updateEvent(new ErrorOnProcessEvolution(e)); 
            this.getObserver().notifyLastEvent();
            //notifyLast
        }
    }
    
}
