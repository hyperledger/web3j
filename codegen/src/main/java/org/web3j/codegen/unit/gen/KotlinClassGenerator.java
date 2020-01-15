package org.web3j.codegen.unit.gen;

import com.squareup.kotlinpoet.FileSpec;
import com.squareup.kotlinpoet.TypeSpec;
import org.web3j.commons.JavaVersion;

import java.io.File;

import static org.web3j.codegen.unit.gen.utills.NameUtils.toCamelCase;

public class KotlinClassGenerator implements UnitClassGenerator {


    private final Class theContract;
    private final String packageName;
    private final String writePath;


    public KotlinClassGenerator(final Class theContract, final String packageName, String writePath) {
        this.theContract = theContract;
        this.packageName = packageName;
        this.writePath = writePath;
    }

    @Override
    public void writeClass() throws Exception {
        com.squareup.kotlinpoet.ClassName EVM_ANNOTATION = new com.squareup.kotlinpoet.ClassName("org.web3j", "EVMTest");
        com.squareup.kotlinpoet.AnnotationSpec.Builder annotationSpec = com.squareup.kotlinpoet.AnnotationSpec.builder(EVM_ANNOTATION);
        if (JavaVersion.getJavaVersionAsDouble() < 11) {
            com.squareup.kotlinpoet.ClassName gethContainer = new com.squareup.kotlinpoet.ClassName("org.web3j", "NodeType");
            annotationSpec.addMember("type = %T.GETH", gethContainer);
        }
        TypeSpec testClass = TypeSpec.classBuilder(theContract.getSimpleName() + "Test")
                .addFunctions(MethodFilter.generateFunctionSpecsForEachTest(theContract))
                .addAnnotation((annotationSpec).build()).addProperty(
                        toCamelCase(theContract), theContract)
                .build();
        FileSpec kotlinFile = FileSpec.builder(packageName, "Test").addType(testClass).build();
        //  kotlinFile.writeTo(new File(writePath));
        kotlinFile.writeTo(System.out);
    }

}
