package com.rapidBuilder.annotation.field;

import com.google.auto.service.AutoService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ExecutableType;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@SupportedAnnotationTypes("com.rapidBuilder.annotation.field.FieldBuilderProperty")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class FieldBuilderProcessor extends AbstractProcessor {

    private static final Logger log = LoggerFactory.getLogger(FieldBuilderProcessor.class);

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations) {
            log.debug("getting elements annotated with {}", annotation);
            Set<? extends Element> annotatedElements =
                    roundEnv.getElementsAnnotatedWith(annotation);
            log.debug("...: {}", annotatedElements);
            Map<Element, ? extends List<? extends Element>> resultMap = annotatedElements.stream()
                    .collect(groupingBy(Element::getEnclosingElement));
            for (Map.Entry<Element, ? extends List<? extends Element>> entry : resultMap.entrySet()) {
                annotatedElements = new HashSet<>(entry.getValue());
                annotatedElements.stream()
                        .forEach(element ->
                                log.debug("ClassNameFound: {}", element
                                        .getEnclosingElement().getSimpleName().toString()));
                log.debug("partitioning setter and non-setter methods");
                Map<Boolean, List<Element>> annotatedMethods = annotatedElements.stream().collect(
                        Collectors.partitioningBy(
                                element -> ((ExecutableType) element.asType()).getParameterTypes().size() == 1
                                        && element.getSimpleName().toString().startsWith("set")));
                log.debug("...: {}", annotatedElements);

                List<Element> setters = annotatedMethods.get(true);
                List<Element> builderTrue = new ArrayList<>();
                List<Element> builderTrueRandomizeTrue = new ArrayList<>();

                for(Element setter: setters){
                    if(setter.getAnnotation(FieldBuilderProperty.class).builder()){
                        builderTrue.add(setter);
                    }
                }
                for(Element whereBuilderIsTrue: builderTrue){
                    if(whereBuilderIsTrue.getAnnotation(FieldBuilderProperty.class).randomize()){
                        builderTrueRandomizeTrue.add(whereBuilderIsTrue);
                    }
                }

                List<Element> otherMethods = annotatedMethods.get(false);

                log.debug("notifying non-setter methods");
                otherMethods.forEach(element -> processingEnv.getMessager()
                        .printMessage(Diagnostic.Kind.ERROR,
                                "@FieldBuilderProperty must be applied to a setXxx method with a single argument",
                                element));

                if (setters.isEmpty()) {
                    log.debug("no setters, nothing to do");
                    continue;
                }

                log.debug("getting class name");
                String className = entry.getKey().toString();
                log.debug("...: {}", className);

                log.debug("calculating setters map for builder");
                Map<String, String> builderSetterMap = builderTrue.stream().collect(
                        Collectors.toMap(setter -> setter.getSimpleName().toString(),
                                setter -> ((ExecutableType) setter.asType()).getParameterTypes().get(0)
                                        .toString()));
                log.debug("...: {}", builderSetterMap);
                log.debug("calculating setters map for builderAndRandomize");
                Map<String, String> builderAndRandomize = builderTrueRandomizeTrue.stream().collect(
                        Collectors.toMap(setter -> setter.getSimpleName().toString(),
                                setter -> ((ExecutableType) setter.asType()).getParameterTypes().get(0)
                                        .toString()));
                log.debug("...: {}", builderAndRandomize);
                try {
                    writeBuilderFile(className, builderSetterMap);
                    writeRandomBuilderFile(className, builderAndRandomize);
                } catch (Exception e) {
                    log.error("error", e);
                }
            }
        }

        return true;
    }

    private void writeRandomBuilderFile(String className, Map<String, String> setterMap)
            throws Exception {

        log.debug("creating a new Builder class for {} with setters {}", className, setterMap);

        String packageName = null;
        int lastDot = className.lastIndexOf('.');
        if (lastDot > 0) {
            packageName = className.substring(0, lastDot);
        }

        String simpleClassName = className.substring(lastDot + 1);
        String builderClassName = className + "BuilderRandom";
        String builderSimpleClassName = builderClassName.substring(lastDot + 1);

        JavaFileObject builderFile = processingEnv.getFiler().createSourceFile(builderClassName);
        List<FieldSpecification> fields = setterMap.entrySet()
                .stream()
                .map(e -> new FieldSpecification(e.getKey(), e.getValue()))
                .toList();
        ClassSpecification classSpecification =
                new ClassSpecification(packageName, simpleClassName, builderSimpleClassName, fields);

        try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {
            extractedForRandomizer(classSpecification, out);
        }
    }

    private void extractedForRandomizer(ClassSpecification classSpecification, PrintWriter out)
            throws Exception {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_31);
        configuration.setClassLoaderForTemplateLoading(getClass().getClassLoader(), "");
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setLogTemplateExceptions(false);
        configuration.setWrapUncheckedExceptions(true);

        Map<String, Object> freemarkerDataModel = new HashMap<>();
        freemarkerDataModel.put("classSpecification", classSpecification);

        Template template = configuration.getTemplate("RandomizerFromFields.ftl");
        template.process(freemarkerDataModel, out);
    }    private void writeBuilderFile(String className, Map<String, String> setterMap)
            throws Exception {

        log.debug("creating a new Builder class for {} with setters {}", className, setterMap);

        String packageName = null;
        int lastDot = className.lastIndexOf('.');
        if (lastDot > 0) {
            packageName = className.substring(0, lastDot);
        }

        String simpleClassName = className.substring(lastDot + 1);
        String builderClassName = className + "Builder";
        String builderSimpleClassName = builderClassName.substring(lastDot + 1);

        JavaFileObject builderFile = processingEnv.getFiler().createSourceFile(builderClassName);
        List<FieldSpecification> fields = setterMap.entrySet()
                .stream()
                .map(e -> new FieldSpecification(e.getKey(), e.getValue()))
                .toList();
        ClassSpecification classSpecification =
                new ClassSpecification(packageName, simpleClassName, builderSimpleClassName, fields);

        try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {
            extracted(classSpecification, out);
        }
    }

    private void extracted(ClassSpecification classSpecification, PrintWriter out)
            throws Exception {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_31);
        configuration.setClassLoaderForTemplateLoading(getClass().getClassLoader(), "");
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setLogTemplateExceptions(false);
        configuration.setWrapUncheckedExceptions(true);

        Map<String, Object> freemarkerDataModel = new HashMap<>();
        freemarkerDataModel.put("classSpecification", classSpecification);

        Template template = configuration.getTemplate("BuilderFromFields.ftl");
        template.process(freemarkerDataModel, out);
    }

    public record FieldSpecification(
            String name,
            String type
    ) {
    }


    public record ClassSpecification(
            String packageName,
            String simpleClassName,
            String builderSimpleClassName,
            List<FieldSpecification> fields
    ) {
    }

}
