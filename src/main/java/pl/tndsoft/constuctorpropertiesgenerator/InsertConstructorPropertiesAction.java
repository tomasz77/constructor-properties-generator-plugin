package pl.tndsoft.constuctorpropertiesgenerator;

import com.intellij.codeInsight.generation.actions.BaseGenerateAction;

public class InsertConstructorPropertiesAction extends BaseGenerateAction {
    public InsertConstructorPropertiesAction() {
        super(new InsertConstructorPropertiesHandler());
    }
}
