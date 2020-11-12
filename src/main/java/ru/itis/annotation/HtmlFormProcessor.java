package ru.itis.annotation;

import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

@AutoService(Processor.class)
@SupportedAnnotationTypes("ru.itis.annotation.HtmlForm")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class HtmlFormProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> annotatedElements =  roundEnv.getElementsAnnotatedWith(HtmlForm.class);
        Set<? extends Element> annotatedFields =  roundEnv.getElementsAnnotatedWith(FormField.class);
        annotatedFields.forEach(e -> processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE,
                "Field " + e.getSimpleName() + " marked with @FormField"));
        annotatedElements.forEach(e -> processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE,
                "Class " + e.getSimpleName() + " marked with @HtmlForm"));

        for (Element element : annotatedElements) {

            String path = HtmlFormProcessor.class.getProtectionDomain().getCodeSource().getLocation().getPath();

            path = path.substring(1) + element.getSimpleName().toString() + ".html";

            Path out = Paths.get(path);

            try {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "CREATING FILE " + out.toString());
                BufferedWriter writer = new BufferedWriter(new FileWriter(out.toFile()));
                HtmlForm annotation = element.getAnnotation(HtmlForm.class);
                writer.write("<form action='" + annotation.action() +
                        "' method='" + annotation.method() + "'>\n");
                for(Element field : annotatedFields) {
                    FormField fieldAnnotation =  field.getAnnotation(FormField.class);
                    String toWrite = String.format("<input type=\"%s\" name=\"%s\" placeholder=\"%s\">\n",
                            fieldAnnotation.type(),
                            fieldAnnotation.name(),
                            fieldAnnotation.placeholder()
                    );
                    writer.write(toWrite);
                }
                writer.write("</form>");
                writer.close();
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        }
        return true;
    }
}
