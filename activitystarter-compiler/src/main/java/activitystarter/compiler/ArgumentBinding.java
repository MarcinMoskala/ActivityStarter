package activitystarter.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import javax.lang.model.type.TypeMirror;

final class ArgumentBinding {
    private final String name;
    private final TypeName type;
    private final TypeMirror elementType;
    private final boolean required;
    private final boolean bySetter;
    private final boolean nullable;

    ArgumentBinding(String name, TypeName type, TypeMirror elementType, boolean required, boolean bySetter,  boolean nullable) {
        this.name = name;
        this.type = type;
        this.elementType = elementType;
        this.required = required;
        this.bySetter = bySetter;
        this.nullable = nullable;
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

    public boolean isBySetter() {
        return bySetter;
    }

    public boolean isNullable() {
        return nullable;
    }
}
