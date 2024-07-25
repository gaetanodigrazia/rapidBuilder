package ${classSpecification.packageName()};

<#-- Template per BuilderSimpleRecord -->
public class ${recordClassName} {
${recordClassName} ${recordObjectName};

<#-- Template per Builder -->
public static final class Builder {

<#list attributes as attr>
${attr.type} ${attr.name};
</#list>

public Builder() {
}

<#-- Costruttore con attributi obbligatori -->
public Builder(<#list mandatoryAttributes as attr>${attr.type} ${attr.name}<#if attr_has_next>, </#if></#list>) {
<#list mandatoryAttributes as attr>
this.${attr.name} = ${attr.name};
            </#list>
        }

        <#-- Costruttore con tutti gli attributi -->
        public Builder(<#list attributes as attr>${attr.type} ${attr.name}<#if attr_has_next>, </#if></#list>) {
<#list attributes as attr>
this.${attr.name} = ${attr.name};
            </#list>
        }

        <#-- Metodi per impostare gli attributi opzionali -->
        <#list optionalAttributes as attr>
        public Builder ${attr.name}(${attr.type} ${attr.name}) {
this.${attr.name} = ${attr.name};
            return this;
        }
        </#list>

        <#-- Metodo per costruire un Builder da un oggetto SimpleRecord esistente -->
        public Builder buildFrom(${recordClassName} ${recordObjectName}) {
return new Builder(<#list attributes as attr>${recordObjectName}.${attr.name}<#if attr_has_next>, </#if></#list>);
}

        <#-- Metodo per costruire l'oggetto SimpleRecord -->
        public ${recordClassName} build() {
return new ${recordClassName}(<#list attributes as attr>${attr.name}<#if attr_has_next>, </#if></#list>);
        }

        @Override
        public boolean equals(Object o) {
if (this == o) return true;
if (o == null || getClass() != o.getClass()) return false;
Builder builder = (Builder) o;
return <#list attributes as attr>Objects.equals(${attr.name}, builder.${attr.name})<#if attr_has_next> && </#if></#list>;
}

        @Override
        public int hashCode() {
return Objects.hash(<#list attributes as attr>${attr.name}<#if attr_has_next>, </#if></#list>);
}

        @Override
        public String toString() {
return "Builder{" +
<#list attributes as attr>"${attr.name}='" + ${attr.name} + "', " </#list>
'}';
        }
    }
}

