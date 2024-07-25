package ${classSpecification.packageName()};

import java.util.Objects;
import com.github.javafaker.Faker;

public class ${classSpecification.builderSimpleClassName()} {

private ${classSpecification.simpleClassName()} object = new ${classSpecification.simpleClassName()}();
private Faker faker = new Faker();

    public ${classSpecification.simpleClassName()} build() {
        return object;
    }

    <#list classSpecification.fields() as field>
    public ${classSpecification.builderSimpleClassName()} ${field.name()}(${field.type()} value) {
        object.${field.name()}(value);
        return this;
    }
    </#list>

    public ${classSpecification.builderSimpleClassName()} fromObject(${classSpecification.simpleClassName()} object) {
        ${classSpecification.builderSimpleClassName()} builder = new ${classSpecification.builderSimpleClassName()}();
    <#list classSpecification.fields() as field>
    <#assign fieldName = field.name()>
    <#assign getterName = fieldName?replace("^set", "get","r")>
        builder.${field.name()}(object.${getterName}());
    </#list>
    return builder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ${classSpecification.builderSimpleClassName()} builder = (${classSpecification.builderSimpleClassName()}) o;
        return Objects.equals(object, builder.object);
    }

    @Override
    public int hashCode() {
        return Objects.hash(object);
    }

    @Override
    public String toString() {
    return "Builder{" +
        "object=" + object +
        '}';
    }
}
