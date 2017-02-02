package activitystarter.compiler;

import com.squareup.javapoet.TypeName;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Messager;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;

import activitystarter.Arg;
import activitystarter.MakeActivityStarter;

import static activitystarter.compiler.ElementsParser.FieldVeryfyResult.Accessible;
import static activitystarter.compiler.ElementsParser.FieldVeryfyResult.BySetter;
import static activitystarter.compiler.ElementsParser.FieldVeryfyResult.Inaccessible;
import static activitystarter.compiler.IsSubtypeHelper.isSubtypeOfType;
import static javax.lang.model.element.ElementKind.CLASS;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.STATIC;

public class ElementsParser {

    private static final String ACTIVITY_TYPE = "android.app.Activity";
    private static final String SERIALIZABLE_TYPE = "java.io.Serializable";
    private static final String PARCELABLE_TYPE = "android.os.Parcelable";

    private static final String NULLABLE_ANNOTATION_NAME = "Nullable";
    private static final List<TypeKind> SUPPORTED_BASE_TYPES = Arrays.asList(
            TypeKind.BOOLEAN, TypeKind.INT, TypeKind.FLOAT, TypeKind.DOUBLE, TypeKind.CHAR
    );
    private Messager messager;

    public ElementsParser(Messager messager) {
        this.messager = messager;
    }

    void parseArg(Element element, Map<TypeElement, BindingSet> builderMap) {
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();

        if (isInaccessibleViaGeneratedCode(Arg.class, "fields", element))
            return;

        TypeMirror elementType = getElementType(element);

        if (veryfyFieldType(element, enclosingElement, elementType))
            return;

        FieldVeryfyResult fieldVeryfyResult = getFieldAccessibility(element);

        if(fieldVeryfyResult == Inaccessible) {
            error(enclosingElement, "@%s %s Inaccessable element. (%s.%s)",
                    Arg.class.getSimpleName(), element, enclosingElement.getQualifiedName(),
                    element.getSimpleName());
            return;
        }

        if (!builderMap.containsKey(enclosingElement)) {
            builderMap.put(enclosingElement, new BindingSet(enclosingElement));
        }

        BindingSet bindingSet = builderMap.get(enclosingElement);

        String name = element.getSimpleName().toString();
        TypeName type = TypeName.get(elementType);
        boolean required = isFieldRequired(element);
        boolean bySetter = fieldVeryfyResult == BySetter;

        ArgumentBinding argumentBinding = new ArgumentBinding(name, type, elementType, required, bySetter);
        bindingSet.addArgumentBinding(argumentBinding);
    }

    enum FieldVeryfyResult {
        Accessible,
        BySetter,
        Inaccessible
    }

    private FieldVeryfyResult getFieldAccessibility(Element element) {
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();

        Set<Modifier> modifiers = element.getModifiers();
        if (modifiers.contains(PRIVATE)) {
            if (hasMethodNamed(enclosingElement, element.getSimpleName().toString()))
                return BySetter;
        } else {
            return Accessible;
        }

        return Inaccessible;
    }

    private boolean hasMethodNamed(TypeElement enclosingElement, String fieldName) {
        for (ExecutableElement e : ElementFilter.methodsIn(enclosingElement.getEnclosedElements())) {
            if (e.getSimpleName().contentEquals("get" + Utills.capitalizeFirstLetter(fieldName)))
                return true;
        }
        return false;
    }

    private boolean isInaccessibleViaGeneratedCode(Class<? extends Annotation> annotationClass, String targetThing, Element element) {
        boolean hasError = false;
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();

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

    void parseClass(Element element, Map<TypeElement, BindingSet> builderMap) {
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

    private void error(Element element, String message, Object... args) {
        printMessage(Diagnostic.Kind.ERROR, element, message, args);
    }

    private void printMessage(Diagnostic.Kind kind, Element element, String message, Object[] args) {
        if (args.length > 0) message = String.format(message, args);
        messager.printMessage(kind, message, element);
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
