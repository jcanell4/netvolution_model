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
public class EnvironmentInfoPropertyCalculatorItems extends ClassGroupItem<EnvironmentInfoPropertyCalculator>{
    private static final Map<String, EnvironmentInfoPropertyCalculatorItems> itemMap=new HashMap<>();
    private String description;

    public EnvironmentInfoPropertyCalculatorItems(String id, String name, Class<EnvironmentInfoPropertyCalculator> type) {
        super(id, type);
        this.description=name;
    }

    public EnvironmentInfoPropertyCalculatorItems(String name, Class<EnvironmentInfoPropertyCalculator> type) {
        this(name.trim().replace(" ", "_").toUpperCase(), name, type);
    }

    public String getDescription() {
        return description;
    }
    
    public static EnvironmentInfoPropertyCalculatorItems getItem(String key, String packageToSearch){
        if(itemMap.isEmpty()){
            List<EnvironmentInfoPropertyCalculatorItems> l = getItemsList(packageToSearch);
            for(EnvironmentInfoPropertyCalculatorItems si : l){
                itemMap.put(si.getId(), si);
            }
        }
        return itemMap.get(key);
    }
    
    public static EnvironmentInfoPropertyCalculatorItems getItem(String key){
        return getItem(key, "org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.calculators");
    }
    
    public static List<EnvironmentInfoPropertyCalculatorItems> getItemsList(String packageToSearch){
        return ClassGroupItem.getItemsList(packageToSearch, EnvironmentInfoPropertyCalculatorInfo.class, new Callback<Pair<EnvironmentInfoPropertyCalculatorInfo, Class<EnvironmentInfoPropertyCalculator>>, EnvironmentInfoPropertyCalculatorItems>() {
            @Override
            public EnvironmentInfoPropertyCalculatorItems call(Pair<EnvironmentInfoPropertyCalculatorInfo, Class<EnvironmentInfoPropertyCalculator>> param) {
                return new EnvironmentInfoPropertyCalculatorItems(param.getFirst().id(), param.getFirst().description(), param.getSecond()); 
            }
        });
    }    
}
