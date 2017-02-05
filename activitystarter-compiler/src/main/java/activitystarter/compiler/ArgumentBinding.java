package activitystarter.compiler;

import com.squareup.javapoet.TypeName;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;

import activitystarter.Optional;
import activitystarter.compiler.FieldAccessableHelper.FieldVeryfyResult;

import static activitystarter.compiler.FieldAccessableHelper.getFieldAccessibility;

final class ArgumentBinding {
    private final String name;
    private final TypeName type;
    private final TypeMirror elementType;
    private final boolean required;
    private final FieldVeryfyResult settingType;

    ArgumentBinding(Element element) {
        elementType = Utills.getElementType(element);
        name = element.getSimpleName().toString();
        type = TypeName.get(elementType);
        required = element.getAnnotation(Optional.class) == null;
        settingType = getFieldAccessibility(element);
    }

    public static List<List<ArgumentBinding>> getArgumentBindingVariants(List<ArgumentBinding> argumentBindings) {
        ArrayList<List<ArgumentBinding>> list = new ArrayList<>();
        list.add(new ArrayList<ArgumentBinding>());

        for (ArgumentBinding b : argumentBindings) {
            if (b.isRequired()) {
                for (List<ArgumentBinding> sublist : list) {
                    sublist.add(b);
                }
            } else {
                List<List<ArgumentBinding>> newElements = new ArrayList<>();
                for (List<ArgumentBinding> sublist : list) {
                    List<ArgumentBinding> sublistCopy = copy(sublist);
                    sublistCopy.add(b);
                    newElements.add(sublistCopy);
                }
                list.addAll(newElements);
            }
        }
        return list;
    }

    public static List<ArgumentBinding> copy(List<ArgumentBinding> oldList) {
        List<ArgumentBinding> list = new ArrayList<>();
        list.addAll(oldList);
        return list;
    }

    public String getName() {
        return name;
    }

    public TypeName getType() {
        return type;
    }

    public boolean isRequired() {
        return required;
    }

    public TypeMirror getElementType() {
        return elementType;
    }

    public FieldVeryfyResult getSettingType() {
        return settingType;
    }
}
