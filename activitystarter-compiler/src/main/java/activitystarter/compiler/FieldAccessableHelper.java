package activitystarter.compiler;

import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;

import static javax.lang.model.element.Modifier.PRIVATE;

public class FieldAccessableHelper {

    enum FieldVeryfyResult {
        Accessible,
        BySetter,
        Inaccessible
    }

    public static FieldVeryfyResult getFieldAccessibility(Element element) {
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();

        Set<Modifier> modifiers = element.getModifiers();
        if (modifiers.contains(PRIVATE)) {
            if (hasMethodNamed(enclosingElement, element.getSimpleName().toString()))
                return FieldVeryfyResult.BySetter;
        } else {
            return FieldVeryfyResult.Accessible;
        }

        return FieldVeryfyResult.Inaccessible;
    }

    private static boolean hasMethodNamed(TypeElement enclosingElement, String fieldName) {
        for (ExecutableElement e : ElementFilter.methodsIn(enclosingElement.getEnclosedElements())) {
            if (e.getSimpleName().contentEquals("get" + Utills.capitalizeFirstLetter(fieldName)))
                return true;
        }
        return false;
    }
}
