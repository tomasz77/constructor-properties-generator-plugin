package pl.tndsoft.constuctorpropertiesgenerator;

import com.intellij.codeInsight.generation.*;
import com.intellij.lang.jvm.JvmNamedElement;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.util.IncorrectOperationException;

import java.util.Arrays;
import java.util.stream.Collectors;

public class InsertConstructorPropertiesHandler extends GenerateMembersHandlerBase {

    public InsertConstructorPropertiesHandler() {
        super("Generate @ConstructorProperties");
    }

    @Override
    protected ClassMember[] getAllOriginalMembers(PsiClass psiClass) {
        return Arrays.stream(psiClass.getConstructors())
                .filter(constructor -> constructor.getParameters().length != 0)
                .map(PsiMethodMember::new)
                .toArray(PsiMethodMember[]::new);
    }

    @Override
    protected GenerationInfo[] generateMemberPrototypes(PsiClass psiClass, ClassMember classMember) throws IncorrectOperationException {
        Project project = psiClass.getProject();
        PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(project);
        JavaCodeStyleManager manager = JavaCodeStyleManager.getInstance(project);

        PsiMethodMember constructor = (PsiMethodMember) classMember;

        String parametersList = Arrays.stream(constructor.getElement().getParameters())
                .map(JvmNamedElement::getName)
                .map(name -> "\"" + name + "\"")
                .collect(Collectors.joining(", "));

        PsiAnnotation annotation =
                elementFactory.createAnnotationFromText("@java.beans.ConstructorProperties({" + parametersList + "})",
                        ((PsiMethodMember) classMember).getPsiElement());
        PsiElement psiElement = manager.shortenClassReferences(annotation);
        psiClass.addBefore(psiElement, constructor.getPsiElement());
        return new PsiGenerationInfo[]{new PsiGenerationInfo(constructor.getElement())};
    }

}
