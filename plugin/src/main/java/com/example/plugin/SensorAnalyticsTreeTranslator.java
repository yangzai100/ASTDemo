package com.example.plugin;

import com.sun.source.util.Trees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic;

public class SensorAnalyticsTreeTranslator extends TreeTranslator {

    private final Messager messager;
    private final Trees trees;
    private final Context context;
    private final TreeMaker make;
    private final Name.Table names;

    public SensorAnalyticsTreeTranslator(ProcessingEnvironment env) {
        messager = env.getMessager();
        trees = Trees.instance(env);
        context = ((JavacProcessingEnvironment) env).getContext();
        make = TreeMaker.instance(context);
        names = Names.instance(context).table;
    }

    void log(String message){
        messager.printMessage(Diagnostic.Kind.NOTE,message);
    }

    @Override
    public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
        super.visitClassDef(jcClassDecl);
    }

    //TODO  完全看不懂
    private void insertAfterMethod(JCTree.JCMethodDecl jcMethodDecl){
        JCTree.JCExpression printlnExpression = make.Ident(names.fromString("com"));
        printlnExpression  = make.Select(printlnExpression,names.fromString("example"));
//        printlnExpression = make.Select(printlnExpression,names.fromString("analytics"));
//        printlnExpression = make.Select(printlnExpression,names.fromString("android"));
        printlnExpression = make.Select(printlnExpression,names.fromString("sdk"));
        printlnExpression = make.Select(printlnExpression,names.fromString("SensorsDataAutoTrackHelper"));
        printlnExpression = make.Select(printlnExpression,names.fromString("trackViewOnClick"));

        JCTree.JCStatement statement = make.Exec(
                make.Apply(
                        List.<JCTree.JCExpression>nil(),
                        printlnExpression,
                        List.<JCTree.JCExpression>of(make.Ident(jcMethodDecl.getParameters().get(0).name))
                )
        );



        jcMethodDecl.body.stats = jcMethodDecl.body.stats.appendList(List.of(statement));

    }

    private boolean isButtonOnClick(JCTree.JCMethodDecl jcMethodDecl){
        List<JCTree.JCAnnotation> annotationList = jcMethodDecl.getModifiers().annotations;
        System.out.println("jcMethodDecl 执行完毕");
        System.out.println("jcMethodDecl 执行完毕4" +jcMethodDecl.getName().toString());
        if (jcMethodDecl.getName().toString().equals("onClick")){
            if (jcMethodDecl.getReturnType().toString().equals("void")){
                if (jcMethodDecl.getParameters().size() == 1){
                    System.out.println("jcMethodDecl 执行完毕6");
                    System.out.println("jcMethodDecl 执行完毕1" + jcMethodDecl.getParameters().get(0).toString());
                    System.out.println("jcMethodDecl 执行完毕2" + jcMethodDecl.getParameters().toString());
                    System.out.println("jcMethodDecl 执行完毕3" + jcMethodDecl.getParameters().get(0).vartype.toString());


                    return jcMethodDecl.getParameters().get(0).vartype.toString().equals("View") ||
                            jcMethodDecl.getParameters().get(0).vartype.toString().equals("android.view.View");
                }
            }
        }

        return  false;
    }


    @Override
    public void visitMethodDef(JCTree.JCMethodDecl jcMethodDecl) {
        super.visitMethodDef(jcMethodDecl);
        System.out.println("visitMethodDef 执行完毕");

        if (isButtonOnClick(jcMethodDecl)){
            System.out.println("isButton 执行完毕");
            insertAfterMethod(jcMethodDecl);
        }
    }
}
