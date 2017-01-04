package io.github.spencerpark.calccli;

import io.github.spencerpark.calccli.command.Command;
import io.github.spencerpark.calccli.expression.Environment;
import io.github.spencerpark.calccli.expression.objects.CalcDecimal;
import io.github.spencerpark.calccli.expression.objects.CalcObject;
import io.github.spencerpark.calccli.function.FunctionBank;
import io.github.spencerpark.calccli.parser.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

public class CalcCLI {
    private static EquationLexer lexer;
    private static EquationParser parser;
    private static boolean stop = false;
    private static boolean verbose = false;

    public static void main(String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("verbose")) verbose = true;
        Scanner in = new Scanner(System.in);

        FunctionBank functions = new FunctionBank();
        Environment environment = new Environment("global", functions);

        //Put defaults
        environment.setVariable("pi", new CalcDecimal(Math.PI));
        environment.setVariable("e", new CalcDecimal(Math.E));

        environment.registerCommand("env", (env, cmdArgs) -> System.out.println(env));
        environment.registerCommand("stop", (env, cmdArgs) -> {
            stop = true;
            System.out.println("Stopping...");
        });
        environment.registerCommand("run", CalcCLI::runCommand);

        String command;
        while (!stop) {
            System.out.print("CalcCLI > ");
            if (in.hasNextLine()) {
                command = in.nextLine();
                if (!command.endsWith(";"))
                    command += ";";
                handleInput(command, environment);
            }
        }

        in.close();
    }

    private static void printErr(String msg) {
        System.out.println("err > " + msg);
    }

    private static void handleInput(String in, Environment env) {
        CharStream chars = new StringCharStream(in);

        if (lexer == null) lexer = new EquationLexer(chars);
        else               lexer.setInput(chars);

        if (parser == null) parser = new EquationParser(lexer, env);
        else                parser.reset();

        try {
            for (Command cmd : parser.parseCommands()) {
                cmd.execute(env);
            }
        } catch (Exception e) {
            if (verbose) e.printStackTrace();
            printErr(e.getMessage());
        }
    }

    private static void runCommand(Environment env, String[] args) {
        if (args.length != 1) {
            printErr("Expected 1 argument: the file to load");
        } else {
            File file = new File(args[0]);
            InputStream inScript = null;
            try {
                if (file.isFile()) {
                    System.out.println("Loading \"" + args[0] + "\"");
                    inScript = new FileInputStream(file);
                } else {
                    inScript = CalcCLI.class.getClassLoader().getResourceAsStream(args[0]);
                    if (inScript == null) {
                        printErr("Cannot find resource \"" + args[0] + "\"");
                        return;
                    } else {
                        System.out.println("Loading standard lib \"" + args[0] + "\"");
                    }
                }

                CharStream charsIn = new ReaderCharStream(inScript);
                EquationLexer lexer = new EquationLexer(charsIn);
                EquationParser parser = new EquationParser(lexer, env);

                List<Command> cmds = parser.parseCommands();
                for (Command cmd : cmds) {
                    try {
                        cmd.execute(env);
                    } catch (Exception e) {
                        printErr(e.getMessage());
                    }
                }

                System.out.println("Loaded \"" + args[0] + "\"");
            } catch (Exception e) {
                printErr(e.getMessage());
                if (inScript != null) try {
                    inScript.close();
                } catch (IOException e1) {
                    printErr("closing input script: "+e1.getMessage());
                }
            }

        }
    }
}
