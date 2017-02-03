package activitystarter.compiler;

import java.util.Set;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;

import static javax.lang.model.element.Modifier.PRIVATE;

public class FieldAccessableHelper {

    enum FieldVeryfyResult {
        Accessible,
        BySetter,
        ByIsSetter,
        ByNoIsSetter,
        Inaccessible
    }

    public static FieldVeryfyResult getFieldAccessibility(Element element) {
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();

        Set<Modifier> modifiers = element.getModifiers();
        if (modifiers.contains(PRIVATE)) {
            String fieldName = element.getSimpleName().toString();
            String fieldNameCapitalized = Utills.capitalizeFirstLetter(fieldName);
            if (hasMethodNamed(enclosingElement, "get" + fieldNameCapitalized))
                return FieldVeryfyResult.BySetter;
            if (fieldName.substring(0, 2).equals("is") && hasMethodNamed(enclosingElement, "set" + fieldName.substring(2)))
                return FieldVeryfyResult.ByNoIsSetter;
            else
                return FieldVeryfyResult.Inaccessible;
        } else {
            return FieldVeryfyResult.Accessible;
        }
    }

    public static String getSetter(FieldVeryfyResult result, String fieldName) {
        String fieldNameCapitalized = Utills.capitalizeFirstLetter(fieldName);
        switch (result) {
            case BySetter:
                return "set" + fieldNameCapitalized;
            case ByNoIsSetter:
                return "set" + fieldName.substring(2);
        }
        return null;
    }

    private static boolean hasMethodNamed(TypeElement enclosingElement, String fieldName) {
        for (ExecutableElement e : ElementFilter.methodsIn(enclosingElement.getEnclosedElements())) {
            if (e.getSimpleName().contentEquals(fieldName))
                return true;
        }
        return false;
    }
}
