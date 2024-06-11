
package org.elsquatrecaps.rsjcb.netvolution.evolutiveprocess.calculators;

/**
 *
 * @author josep
 * @param <I>
 * @param <O>
 */
public class BooleanFunctionResponseVerifier<I extends Comparable<I>, O extends Comparable<O>> extends AbstractFunctionResponseVerifier<I, O, Boolean>{

    public BooleanFunctionResponseVerifier(ResponseFunction<I, O> function) {
        super(function);
    }

    public BooleanFunctionResponseVerifier() {
    }

    @Override
    public Boolean verify(I input, O output) {
        return output.compareTo(this.getFunction().responseCalulate(input))==0;
    }
}
