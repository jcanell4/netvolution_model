package org.elsquatrecaps.rsjcb.netvolution.neuralnetwork;

import java.util.HashMap;
import org.elsquatrecaps.utilities.tools.Identifiable;

/**
 *
 * @author josep
 */
public enum InputOutputContributionValues implements Identifiable<String>{
    MIXED_CONTRIBUTION("Mixed"),
    ONE_TO_MANY_CONTRIBUTION("One to many" ),
    SEPARATED_CONTRIBUTION ("Separated");
    
    private final String name;
    private static final HashMap<String, InputOutputContributionValues> nameMap = new HashMap<>();
    static {
        nameMap.put("Mixed", MIXED_CONTRIBUTION);
        nameMap.put("One to many", ONE_TO_MANY_CONTRIBUTION);
        nameMap.put("Separated", SEPARATED_CONTRIBUTION);
    }

    private InputOutputContributionValues(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
    
    public String getName(){
        return this.name;
    }

    public static <InputOutputContributionValues extends Enum<InputOutputContributionValues>> InputOutputContributionValues valueOf(Class<InputOutputContributionValues> enumType, String v){
         return (InputOutputContributionValues) nameMap.get(v);
    }
    
    public static InputOutputContributionValues fromName(String v){
        return nameMap.get(v);
    }

    @Override
    public String getId() {
        return this.name();
    }
}
