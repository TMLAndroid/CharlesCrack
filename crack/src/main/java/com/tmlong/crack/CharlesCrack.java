package com.tmlong.crack;



import java.io.File;
import java.io.FileOutputStream;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
/**
 * 破解charles代码
 * 该支持的版本 4.2.1
 */
public class CharlesCrack {


    private static final String PKGNAME = "com.xk72.charles";
    private static final String CLASSNAME = "oFTR";
    private static final String JAR_DIR = "/Users/tmlong/Desktop/";//charles存放的目录
    private static final String JAR_NAME = "charles.jar";

    public static void main(String[] args){

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext(JAR_DIR+JAR_NAME);
            }
        }).map(new Function<String, byte[]>() {
            @Override
            public byte[] apply(String jarPath) throws Exception {
                ClassPool classPool = ClassPool.getDefault();
                classPool.insertClassPath(jarPath);
                CtClass ctClass = classPool.get(PKGNAME+"."+CLASSNAME);
                CtMethod ctMethod = ctClass.getDeclaredMethod("Yuaz", null);
                ctMethod.setBody("{return true;}");
                ctMethod = ctClass.getDeclaredMethod("lktV",null);
                ctMethod.setBody("{return \"Crake by tmlong!!\";}");
                return ctClass.toBytecode();
            }
        }).map(new Function<byte[], String>() {
            @Override
            public String apply(byte[] byteArray) throws Exception {
                String classPath = PKGNAME.replace(".", "/") + "/";
                File dirFile = new File(System.getProperty("user.dir") +"/crack/src/main/java/" +classPath + CLASSNAME + ".class");
                if (!dirFile.getParentFile().exists()){
                    dirFile.getParentFile().mkdirs();
                }
                FileOutputStream output = new FileOutputStream(dirFile);
                output.write(byteArray);
                return dirFile.getAbsolutePath();
            }
        }).map(new Function<String, Integer>() {
            @Override
            public Integer apply(String s) throws Exception {
                String classPath = PKGNAME.replace(".", "/") + "/" + CLASSNAME + ".class";
                Process process = Runtime.getRuntime().exec("jar uvf "+JAR_DIR+JAR_NAME+" "+classPath);
                int status = process.waitFor();
                return status;
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer status) throws Exception {
                System.out.println("status:" + status + Thread.currentThread().getName());
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                System.out.println("throw:"+throwable.getStackTrace().toString()+throwable.getMessage());
            }
        });
    }
}
