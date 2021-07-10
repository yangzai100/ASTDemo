package com.example.plugin;

import com.google.auto.service.AutoService;
import com.sun.source.util.Trees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.Context;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;



@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("*")
@AutoService(Processor.class)
public class SensorsAnalyticsPlugin extends AbstractProcessor {

    private ProcessingEnvironment mProcessingEnvironment;
    private Trees trees;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mProcessingEnvironment = processingEnvironment;
        trees = Trees.instance(processingEnvironment);
        Context context =  ((JavacProcessingEnvironment) processingEnvironment).getContext();

    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        System.out.println("执行完毕");
        if (!roundEnvironment.processingOver()){
            Set<? extends Element> elements = roundEnvironment.getRootElements();
            System.out.println("elements 执行完毕");
            for (Element element : elements) {
                if (element.getKind() == ElementKind.CLASS){
                    //获取Element的抽象语法树
                 JCTree tree = (JCTree) trees.getTree(element);
                 //利用自定义的TreeTanslator处理每一个方法
                    TreeTranslator visitor = new SensorAnalyticsTreeTranslator(mProcessingEnvironment);
                    tree.accept(visitor);
                    System.out.println("TreeTranslator 执行完毕");
                }
            }
        }
        return false;
    }
}
