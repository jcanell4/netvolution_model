package org.elsquatrecaps.rsjcb.netvolution.neuralnetwork;

import java.util.HashMap;
import org.elsquatrecaps.utilities.tools.Identifiable;

/**
 *
 * @author josepcanellas
 */
public enum NeuronTypesForStabilityCheckingValues implements Identifiable<String>{
    ALL("All"),
    OUTPUT_ONLY("Outputs only");
    
    private final String name;
    private static final HashMap<String, NeuronTypesForStabilityCheckingValues> nameMap = new HashMap<>();
    static {
        nameMap.put("All", ALL);
        nameMap.put("Outputs only", OUTPUT_ONLY);
    }    

    private NeuronTypesForStabilityCheckingValues(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return this.name;
    }
    
    public String getName(){
        return this.name;
    }

    public static <NeuronsForStabilityCheckingValues extends Enum<NeuronsForStabilityCheckingValues>> NeuronsForStabilityCheckingValues valueOf(Class<NeuronsForStabilityCheckingValues> enumType, String v){
         return (NeuronsForStabilityCheckingValues) nameMap.get(v);
    }
    
    public static NeuronTypesForStabilityCheckingValues fromName(String v){
        return nameMap.get(v);
    }

    @Override
    public String getId() {
        return this.name();
    }
    
}
