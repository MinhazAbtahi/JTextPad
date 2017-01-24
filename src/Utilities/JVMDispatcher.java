/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import java.io.*;
import java.net.URISyntaxException;

/**
 *
 * @author abtahi
 */
public class JVMDispatcher {

    private static void printLines(String name, InputStream ins) throws Exception {
        String line = null;
        BufferedReader in = new BufferedReader(
                new InputStreamReader(ins));
        while ((line = in.readLine()) != null) {
            System.out.println(name + " " + line);
        }
    }

    private static int runProcess(String command) throws Exception {
        Process pro = Runtime.getRuntime().exec(command);
        printLines(command + " stdout:", pro.getInputStream());
        printLines(command + " stderr:", pro.getErrorStream());
        pro.waitFor();
        System.out.println(command + " exitValue() " + pro.exitValue());
        return pro.exitValue();
    }

    public static void compileCode(String command) {
        try {
            runProcess(command);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void runProgram(String command) {
        try {
            runProcess(command);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void buildAndRun() {

        try {
            int k = runProcess("javac MainT.java");
            if (k == 0) {
                k = runProcess("java MainT.java");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void buildAndRun(String className) {
        try {
            int k = runProcess("javac " + className + ".java");
            if (k == 0) {
                k = runProcess("java " + className);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
