package activitystarter.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import activitystarter.Arg;
import activitystarter.MakeActivityStarter;

@AutoService(Processor.class)
public final class ActivityStarterProcessor extends AbstractProcessor {

    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        filer = env.getFiler();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(Arg.class.getCanonicalName());
        return types;
    }

    @Override
    public boolean process(Set<? extends TypeElement> elements, RoundEnvironment env) {
        Map<TypeElement, BindingSet> bindingMap = findAndParseTargets(env);
        processTargets(bindingMap);
        return false;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private Map<TypeElement, BindingSet> findAndParseTargets(RoundEnvironment env) {
        Map<TypeElement, BindingSet> builderMap = new LinkedHashMap<>();
        ElementsParser parser = new ElementsParser(processingEnv.getMessager());

        for (Element element : env.getElementsAnnotatedWith(activitystarter.Arg.class)) {
            try {
                parser.parseArg(element, builderMap);
            } catch (Exception e) {
                logParsingError(element, activitystarter.Arg.class, e);
            }
        }

        for (Element element : env.getElementsAnnotatedWith(MakeActivityStarter.class)) {
            try {
                parser.parseClass(element, builderMap);
            } catch (Exception e) {
                logParsingError(element, MakeActivityStarter.class, e);
            }
        }

        return builderMap;
    }

    private void processTargets(Map<TypeElement, BindingSet> bindingMap) {
        for (Map.Entry<TypeElement, BindingSet> entry : bindingMap.entrySet()) {
            TypeElement typeElement = entry.getKey();
            BindingSet binding = entry.getValue();

            JavaFile javaFile = binding.brewJava();
            try {
                javaFile.writeTo(filer);
            } catch (IOException e) {
                error(typeElement, "Unable to write binding for type %s: %s", typeElement, e.getMessage());
            }
        }
    }

    private void logParsingError(Element element, Class<? extends Annotation> annotation, Exception e) {
        StringWriter stackTrace = new StringWriter();
        e.printStackTrace(new PrintWriter(stackTrace));
        error(element, "Unable to parse @%s binding.\n\n%s", annotation.getSimpleName(), stackTrace);
    }

    private void error(Element element, String message, Object... args) {
        printMessage(Diagnostic.Kind.ERROR, element, message, args);
    }

    private void printMessage(Diagnostic.Kind kind, Element element, String message, Object[] args) {
        if (args.length > 0) message = String.format(message, args);
        processingEnv.getMessager().printMessage(kind, message, element);
    }
}