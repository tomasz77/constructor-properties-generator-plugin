package pl.tndsoft.constuctorpropertiesgenerator;

import com.intellij.codeInsight.generation.ClassMember;
import com.intellij.codeInsight.generation.GenerateMembersHandlerBase;
import com.intellij.codeInsight.generation.GenerationInfo;
import com.intellij.codeInsight.generation.PsiGenerationInfo;
import com.intellij.codeInsight.generation.PsiMethodMember;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.util.IncorrectOperationException;
import java.util.Arrays;

public class InsertConstructorPropertiesHandler extends GenerateMembersHandlerBase {

    public InsertConstructorPropertiesHandler() {
        super("Generate @ConstructorProperties");
    }

    @Override
    protected ClassMember[] getAllOriginalMembers(PsiClass psiClass) {
        return Arrays.stream(psiClass.getConstructors())
                .filter(constructor -> !constructor.getParameterList().isEmpty())
                .map(PsiMethodMember::new)
                .toArray(PsiMethodMember[]::new);
    }

    @Override
    protected GenerationInfo[] generateMemberPrototypes(PsiClass psiClass, ClassMember classMember) throws IncorrectOperationException {
        Project project = psiClass.getProject();
        PsiMethodMember constructor = (PsiMethodMember) classMember;
        PsiAnnotation annotation = AnnotationUtil.createConstructorPropertiesAnnotation((PsiMethodMember) classMember, project, constructor);
        PsiElement psiElement = JavaCodeStyleManager.getInstance(project).shortenClassReferences(annotation);
        psiClass.addBefore(psiElement, constructor.getPsiElement());
        return new PsiGenerationInfo[]{new PsiGenerationInfo<>(constructor.getElement())};
    }

}
