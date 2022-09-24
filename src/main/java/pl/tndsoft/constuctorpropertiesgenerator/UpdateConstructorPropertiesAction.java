package pl.tndsoft.constuctorpropertiesgenerator;

import com.intellij.codeInsight.generation.actions.BaseGenerateAction;

public class UpdateConstructorPropertiesAction extends BaseGenerateAction {
    public UpdateConstructorPropertiesAction() {
        super(new UpdateConstructorPropertiesHandler());
    }
}
