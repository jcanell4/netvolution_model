package org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.calculators;

import java.util.HashMap;
import org.elsquatrecaps.utilities.tools.ClassGroupItem;
import java.util.List;
import java.util.Map;
import org.elsquatrecaps.utilities.tools.Callback;
import org.elsquatrecaps.utilities.tools.Pair;

/**
 *
 * @author josep
 */
public class SinglePropertyCalculatorItems extends ClassGroupItem<PtpNeuralNetworkSinglePropertyCalculator>{
    private static final Map<String, SinglePropertyCalculatorItems> itemMap=new HashMap<>();
    private String description;

    public SinglePropertyCalculatorItems(String id, String name, Class<PtpNeuralNetworkSinglePropertyCalculator> type) {
        super(id, type);
        this.description=name;
    }

    public SinglePropertyCalculatorItems(String name, Class<PtpNeuralNetworkSinglePropertyCalculator> type) {
        this(name.trim().replace(" ", "_").toUpperCase(), name, type);
    }

    public String getDescription() {
        return description;
    }
    
    public static SinglePropertyCalculatorItems getItem(String key, String packageToSearch){
        if(itemMap.isEmpty()){
            List<SinglePropertyCalculatorItems> l = getItemsList(packageToSearch);
            for(SinglePropertyCalculatorItems si : l){
                itemMap.put(si.getId(), si);
            }
        }
        return itemMap.get(key);
    }
    
    public static SinglePropertyCalculatorItems getItem(String key){
        return getItem(key, "org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.calculators");
    }
    
    public static List<SinglePropertyCalculatorItems> getItemsList(String packageToSearch){
        return ClassGroupItem.getItemsList(packageToSearch, SinglePropertyCalculatorInfo.class, new Callback<Pair<SinglePropertyCalculatorInfo, Class<PtpNeuralNetworkSinglePropertyCalculator>>, SinglePropertyCalculatorItems>() {
            @Override
            public SinglePropertyCalculatorItems call(Pair<SinglePropertyCalculatorInfo, Class<PtpNeuralNetworkSinglePropertyCalculator>> param) {
                return new SinglePropertyCalculatorItems(param.getFirst().id(), param.getFirst().description(), param.getSecond()); 
            }
        });
    }    
}
