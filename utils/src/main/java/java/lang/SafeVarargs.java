/*
 * Copyright (c) 2010, 2013, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package java.lang;

import java.lang.annotation.*;

/**
 * A programmer assertion that the body of the annotated method or
 * constructor does not perform potentially unsafe operations on its
 * varargs parameter.  Applying this annotation to a method or
 * constructor suppresses unchecked warnings about a
 * <i>non-reifiable</i> variable arity (vararg) type and suppresses
 * unchecked warnings about parameterized array creation at call
 * sites.
 *
 * <p> In addition to the usage restrictions imposed by its {@link
 * Target @Target} meta-annotation, compilers are required to implement
 * additional usage restrictions on this annotation type; it is a
 * compile-time error if a method or constructor declaration is
 * annotated with a {@code @SafeVarargs} annotation, and either:
 * <ul>
 * <li>  the declaration is a fixed arity method or constructor
 *
 * <li> the declaration is a variable arity method that is neither
 * {@code static} nor {@code final}.
 *
 * </ul>
 *
 * <p> Compilers are encouraged to issue warnings when this annotation
 * type is applied to a method or constructor declaration where:
 *
 * <ul>
 *
 * <li> The variable arity parameter has a reifiable element type,
 * which includes primitive types, {@code Object}, and {@code String}.
 * (The unchecked warnings this annotation type suppresses already do
 * not occur for a reifiable element type.)
 *
 * <li> The body of the method or constructor declaration performs
 * potentially unsafe operations, such as an assignment to an element
 * of the variable arity parameter's array that generates an unchecked
 * warning.  Some unsafe operations do not trigger an unchecked
 * warning.  For example, the aliasing in
 *
 * <blockquote><pre>
 * &#64;SafeVarargs // Not actually safe!
 * static void m(List&lt;String&gt;... stringLists) {
 *   Object[] array = stringLists;
 *   List&lt;Integer&gt; tmpList = Arrays.asList(42);
 *   array[0] = tmpList; // Semantically invalid, but compiles without warnings
 *   String s = stringLists[0].get(0); // Oh no, ClassCastException at runtime!
 * }
 * </pre></blockquote>
 *
 * leads to a {@code ClassCastException} at runtime.
 *
 * <p>Future versions of the platform may mandate compiler errors for
 * such unsafe operations.
 *
 * </ul>
 *
 * @since 1.7
 */

// The annotation SafeVarargs does not exist in Java 1.6, but it is being used
// in the auto-generated type classes (the Android branch is still on Java 1.6).
// Rather than having two different ways of generating these classes, the class
// SafeVarargs was copied from the Java source code (>= 1.7), and the retention
// changed from RUNTIME to CLASS, so it is not causing any problems at runtime.

@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD})
public @interface SafeVarargs {}
