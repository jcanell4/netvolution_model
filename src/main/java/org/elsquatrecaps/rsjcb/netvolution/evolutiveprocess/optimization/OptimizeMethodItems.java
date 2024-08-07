package org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.optimization;

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
public class OptimizeMethodItems extends ClassGroupItem<OptimizationMethod>{
    private static final Map<String, OptimizeMethodItems> itemMap=new HashMap<>();
    private String[] forInitalizingInstanceFromConfig;
    
    public OptimizationMethod getInstance(Object... v) {
        OptimizationMethod inst = super.getInstance();
        inst.initInstance(v);
        return inst;
    }
public OptimizeMethodItems(String id, Class<OptimizationMethod> type, String[] forInitalizingInstanceFromConfig) {
        super(id, type);
        this.forInitalizingInstanceFromConfig = forInitalizingInstanceFromConfig;
    }
    
    public static OptimizeMethodItems getItem(String key, String packageToSearch){
        if(itemMap.isEmpty()){
            List<OptimizeMethodItems> l = getItemsList(packageToSearch);
            for(OptimizeMethodItems si : l){
                itemMap.put(si.getId(), si);
            }
        }
        return itemMap.get(key);
    }
    
    public static OptimizeMethodItems getItem(String key){
        return getItem(key, "org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.optimization");
    }
    
    public static List<OptimizeMethodItems> getItemsList(String packageToSearch){
        return ClassGroupItem.getItemsList(packageToSearch, OptimizationMethodInfo.class, new Callback<Pair<OptimizationMethodInfo, Class<OptimizationMethod>>, OptimizeMethodItems>() {
            @Override
            public OptimizeMethodItems call(Pair<OptimizationMethodInfo, Class<OptimizationMethod>> param) {
                return new OptimizeMethodItems(
                        param.getFirst().id().getValue(), 
                        param.getSecond(), 
                        param.getFirst().forInitalizingInstanceFromConfig()
                ); 
            }
        });
    }    

    /**
     * @return the forInitalizingInstanceFromConfig
     */
    public String[] getForInitalizingInstanceFromConfig() {
        return forInitalizingInstanceFromConfig;
    }
}
