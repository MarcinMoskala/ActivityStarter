package activitystarter.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeName;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.tools.Diagnostic.Kind;

import activitystarter.Arg;
import activitystarter.MakeActivityStarter;

import static activitystarter.compiler.IsSubtypeHelper.isSubtypeOfType;
import static javax.lang.model.element.ElementKind.CLASS;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.STATIC;

@AutoService(Processor.class)
public final class ActivityStarterProcessor extends AbstractProcessor {

    private static final String ACTIVITY_TYPE = "android.app.Activity";
    private static final String SERIALIZABLE_TYPE = "java.io.Serializable";
    private static final String PARCELABLE_TYPE = "android.os.Parcelable";

    private static final String NULLABLE_ANNOTATION_NAME = "Nullable";
    private static final List<TypeKind> SUPPORTED_BASE_TYPES = Arrays.asList(
            TypeKind.BOOLEAN,TypeKind.INT,TypeKind.FLOAT,TypeKind.DOUBLE,TypeKind.CHAR
    );

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

    private Map<TypeElement, BindingSet> findAndParseTargets(RoundEnvironment env) {
        Map<TypeElement, BindingSet> builderMap = new LinkedHashMap<>();

        for (Element element : env.getElementsAnnotatedWith(activitystarter.Arg.class)) {
            // TODO DELETE debug
//            processingEnv.getMessager().printMessage(Kind.ERROR, "Znalazłem coś 0.0", element);
            try {
                parseArg(element, builderMap);
            } catch (Exception e) {
                logParsingError(element, activitystarter.Arg.class, e);
            }
        }

        for (Element element : env.getElementsAnnotatedWith(MakeActivityStarter.class)) {
            // TODO DELETE debug
//            processingEnv.getMessager().printMessage(Kind.ERROR, "Znalazłem klasę :))", element);
            try {
                parseClass(element, builderMap);
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

    private void parseArg(Element element, Map<TypeElement, BindingSet> builderMap) {
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();

        if (isInaccessibleViaGeneratedCode(Arg.class, "fields", element))
            return;

        TypeMirror elementType = getElementType(element);

        if (veryfyFieldType(element, enclosingElement, elementType))
            return;

        if (!builderMap.containsKey(enclosingElement)) {
            builderMap.put(enclosingElement, new BindingSet(enclosingElement));
        }

        BindingSet bindingSet = builderMap.get(enclosingElement);

        String name = element.getSimpleName().toString();
        TypeName type = TypeName.get(elementType);
        boolean required = isFieldRequired(element);

        ArgumentBinding argumentBinding = new ArgumentBinding(name, type, elementType, required);
        bindingSet.addArgumentBinding(argumentBinding);
    }

    private boolean isInaccessibleViaGeneratedCode(Class<? extends Annotation> annotationClass, String targetThing, Element element) {
        boolean hasError = false;
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();

        // Verify method modifiers.
        Set<Modifier> modifiers = element.getModifiers();
        if (modifiers.contains(PRIVATE) || modifiers.contains(STATIC)) {
            error(element, "@%s %s must not be private or static. (%s.%s)",
                    annotationClass.getSimpleName(), targetThing, enclosingElement.getQualifiedName(),
                    element.getSimpleName());
            hasError = true;
        }

        // Verify containing type.
        if (enclosingElement.getKind() != CLASS) {
            error(enclosingElement, "@%s %s may only be contained in classes. (%s.%s)",
                    annotationClass.getSimpleName(), targetThing, enclosingElement.getQualifiedName(),
                    element.getSimpleName());
            hasError = true;
        }

        // Verify containing class visibility is not private.
        if (enclosingElement.getModifiers().contains(PRIVATE)) {
            error(enclosingElement, "@%s %s may not be contained in private classes. (%s.%s)",
                    annotationClass.getSimpleName(), targetThing, enclosingElement.getQualifiedName(),
                    element.getSimpleName());
            hasError = true;
        }

        return hasError;
    }

    private TypeMirror getElementType(Element element) {
        TypeMirror elementType = element.asType();
        if (elementType.getKind() == TypeKind.TYPEVAR) {
            TypeVariable typeVariable = (TypeVariable) elementType;
            elementType = typeVariable.getUpperBound();
        }
        return elementType;
    }

    private boolean veryfyFieldType(Element element, TypeElement enclosingElement, TypeMirror elementType) {
        if (!isFieldValidType(elementType)) {
            error(element, "@%s fields must extend from Serializable, Parcelable or beof type String, int, float, double, char or boolean. (%s.%s)",
                    Arg.class.getSimpleName(), enclosingElement.getQualifiedName(), element.getSimpleName());
            return true;
        }
        return false;
    }

    private boolean isFieldValidType(TypeMirror elementType) {
        return SUPPORTED_BASE_TYPES.contains(elementType.getKind()) ||
                TypeName.get(elementType).equals(TypeName.get(String.class)) ||
                isSubtypeOfType(elementType, SERIALIZABLE_TYPE) ||
                isSubtypeOfType(elementType, PARCELABLE_TYPE);
    }

    private void parseClass(Element element, Map<TypeElement, BindingSet> builderMap) {
        TypeElement typeElement = (TypeElement) element;
        TypeMirror elementType = getElementType(element);

        if (veryfyClassType(element, elementType))
            return;

        if (!builderMap.containsKey(typeElement)) {
            builderMap.put(typeElement, new BindingSet(typeElement));
        }
    }

    private boolean veryfyClassType(Element element, TypeMirror elementType) {
        if (!isSubtypeOfType(elementType, ACTIVITY_TYPE)) {
            error(element, "@%s must be addede before Activity class definition. (%s)",
                    MakeActivityStarter.class.getSimpleName(), element.getSimpleName());
            return true;
        }
        return false;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private void error(Element element, String message, Object... args) {
        printMessage(Kind.ERROR, element, message, args);
    }

    private void printMessage(Kind kind, Element element, String message, Object[] args) {
        if (args.length > 0) message = String.format(message, args);
        processingEnv.getMessager().printMessage(kind, message, element);
    }

    private static boolean hasAnnotationWithName(Element element, String simpleName) {
        for (AnnotationMirror mirror : element.getAnnotationMirrors()) {
            String annotationName = mirror.getAnnotationType().asElement().getSimpleName().toString();
            if (simpleName.equals(annotationName)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isFieldRequired(Element element) {
        return !hasAnnotationWithName(element, NULLABLE_ANNOTATION_NAME);
    }
}