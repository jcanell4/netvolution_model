package org.elsquatrecaps.rsjcb.netvolution.neuralnetwork.actfunctions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author josep
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivationFunctionInfo {
    String id() default "";
    String name();
}
