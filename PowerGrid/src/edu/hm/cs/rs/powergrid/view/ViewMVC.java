package edu.hm.cs.rs.powergrid.view;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** View der MVC-Architektur.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-05-12
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewMVC {
    String type() default "text";

}
