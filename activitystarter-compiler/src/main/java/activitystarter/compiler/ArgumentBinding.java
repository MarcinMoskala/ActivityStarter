package activitystarter.compiler;

import com.squareup.javapoet.TypeName;

import javax.lang.model.type.TypeMirror;

import activitystarter.compiler.FieldAccessableHelper.FieldVeryfyResult;

final class ArgumentBinding {
    private final String name;
    private final TypeName type;
    private final TypeMirror elementType;
    private final boolean required;
    private final FieldVeryfyResult settingType;

    ArgumentBinding(String name, TypeName type, TypeMirror elementType, boolean required, FieldVeryfyResult settingType) {
        this.name = name;
        this.type = type;
        this.elementType = elementType;
        this.required = required;
        this.settingType = settingType;
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
