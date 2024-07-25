package ${classSpecification.packageName()};

import java.util.Objects;
import com.github.javafaker.Faker;

public class ${classSpecification.builderSimpleClassName()} {

private ${classSpecification.simpleClassName()}Builder object = new ${classSpecification.simpleClassName()}Builder();
private Faker faker = new Faker();

    public ${classSpecification.simpleClassName()}Builder buildNullModel() {
    <#list classSpecification.fields() as field>
    <#assign randomValue = ''>
    <#if field.type() == 'java.lang.String'>
    <#assign randomValue = "null">
    <#elseif field.type() == 'int'>
    <#assign randomValue = "0">
    <#elseif field.type() == 'boolean'>
    <#assign randomValue = "false">
    <#elseif field.type() == 'float'>
    <#assign randomValue = "0.0f">
    <#elseif field.type() == 'double'>
    <#assign randomValue = "0.0d">
    <#else>
    <#-- For other types, assume it's a complex object (class) -->
    <#assign fieldName = field.name()>
    <#assign randomValue = "null">
    </#if>
        object.${field.name()}(${randomValue});
    </#list>
    return object;
    }

    public ${classSpecification.simpleClassName()}Builder randomize() {
    <#list classSpecification.fields() as field>
    <#assign randomValue = ''>
    <#if field.type() == 'java.lang.String'>
    <#assign randomValue = "faker.lorem().characters(faker.random().nextInt(5, 15))">
    <#elseif field.type() == 'int'>
    <#assign randomValue = "faker.random().nextInt(5, 15)">
    <#elseif field.type() == 'boolean'>
    <#assign randomValue = "faker.bool().bool()">
    <#elseif field.type() == 'float'>
    <#assign randomValue = "faker.number().randomDouble(2, 0, 100).floatValue()">
    <#elseif field.type() == 'double'>
    <#assign randomValue = "faker.number().randomDouble(2, 0, 100)">
    <#else>
    <#-- For other types, assume it's a complex object (class) -->
    <#assign fieldName = field.name()>
    <#assign customObject = fieldName?replace("^set", "","r")?cap_first>
    <#assign randomValue = "new ${customObject}BuilderRandom().randomize().build()">
    </#if>
        object.${field.name()}(${randomValue});
    </#list>
    return object;
    }
}
