package pl.tndsoft.constuctorpropertiesgenerator;

import com.intellij.codeInsight.generation.PsiMethodMember;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiParameter;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

class AnnotationUtil {

    private AnnotationUtil() {}

    @NotNull
    static PsiAnnotation createConstructorPropertiesAnnotation(PsiMethodMember classMember, Project project, PsiMethodMember constructor) {
        PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(project);

        String parametersList = Arrays.stream(constructor.getElement().getParameterList().getParameters())
            .map(PsiParameter::getName)
            .map(name -> "\"" + name + "\"")
            .collect(Collectors.joining(", "));

        return elementFactory.createAnnotationFromText("@java.beans.ConstructorProperties({" + parametersList + "})",
                classMember.getPsiElement());
    }

}
