package io.github.spencerpark.calccli;

import io.github.spencerpark.calccli.command.Command;
import io.github.spencerpark.calccli.expression.Environment;
import io.github.spencerpark.calccli.function.FunctionBank;
import io.github.spencerpark.calccli.parser.CharStream;
import io.github.spencerpark.calccli.parser.EquationLexer;
import io.github.spencerpark.calccli.parser.EquationParser;
import io.github.spencerpark.calccli.parser.StringCharStream;

import java.util.Arrays;
import java.util.Scanner;

public class CalcCLI {
    private static EquationLexer lexer;
    private static EquationParser parser;
    private static boolean stop = false;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        FunctionBank functions = new FunctionBank();
        Environment environment = new Environment(functions);

        //Put defaults
        environment.setVariable("pi", Math.PI);
        environment.setVariable("e", Math.E);

        environment.registerCommand("env", (env, cmdArgs) -> System.out.println(env));
        environment.registerCommand("stop", (env, cmdArgs) -> {
            stop = true;
            System.out.println("Stopping...");
        });

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

    private static void handleInput(String in, Environment env) {
        CharStream chars = new StringCharStream(in);

        if (lexer == null) lexer = new EquationLexer(chars);
        else               lexer.setInput(chars);

        if (parser == null) parser = new EquationParser(lexer);
        else                parser.reset();

        try {
            for (Command cmd : parser.parseCommands()) {
                cmd.execute(env);
            }
        } catch (Exception e) {
            System.out.println("err > " + e.getMessage());
        }
    }
}
