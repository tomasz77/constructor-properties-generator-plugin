package pl.tndsoft.constuctorpropertiesgenerator;

import com.intellij.codeInsight.generation.ClassMember;
import com.intellij.codeInsight.generation.GenerateMembersHandlerBase;
import com.intellij.codeInsight.generation.GenerationInfo;
import com.intellij.codeInsight.generation.PsiGenerationInfo;
import com.intellij.codeInsight.generation.PsiMethodMember;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiClass;
import com.intellij.util.IncorrectOperationException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;

public class UpdateConstructorPropertiesHandler extends GenerateMembersHandlerBase {

    private static final String VALUE_ATTRIBUTE_NAME = "value";

    public UpdateConstructorPropertiesHandler() {
        super("Generate @ConstructorProperties");
    }

    @Override
    protected ClassMember[] getAllOriginalMembers(PsiClass psiClass) {
        return Arrays.stream(psiClass.getConstructors())
                .filter(constructor -> Arrays.stream(constructor.getAnnotations()).anyMatch(
                    psiAnnotation -> Objects.equals(psiAnnotation.getQualifiedName(), "java.beans.ConstructorProperties")))
                .map(PsiMethodMember::new)
                .toArray(PsiMethodMember[]::new);
    }

    @Override
    protected GenerationInfo[] generateMemberPrototypes(PsiClass psiClass, ClassMember classMember) throws IncorrectOperationException {
        PsiMethodMember constructor = (PsiMethodMember) classMember;
        PsiAnnotation annotation = Arrays.stream(constructor.getElement().getAnnotations())
                .filter(psiAnnotation -> Objects.equals(psiAnnotation.getQualifiedName(), "java.beans.ConstructorProperties"))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
        if (!constructor.getElement().hasParameters()) {
            annotation.delete();
        } else {
            Project project = psiClass.getProject();
            PsiAnnotationMemberValue attributeValue = AnnotationUtil.createConstructorPropertiesAnnotation((PsiMethodMember) classMember, project, constructor)
                .findDeclaredAttributeValue(VALUE_ATTRIBUTE_NAME);
            annotation.setDeclaredAttributeValue(VALUE_ATTRIBUTE_NAME, attributeValue);
        }

        return new PsiGenerationInfo[]{new PsiGenerationInfo<>(constructor.getElement())};
    }

}
