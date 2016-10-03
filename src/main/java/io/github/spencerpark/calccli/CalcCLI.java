package io.github.spencerpark.calccli;

import io.github.spencerpark.calccli.command.Command;
import io.github.spencerpark.calccli.expression.Environment;
import io.github.spencerpark.calccli.function.FunctionBank;
import io.github.spencerpark.calccli.parser.*;

import java.util.Scanner;

public class CalcCLI {
    private static EquationLexer lexer;
    private static EquationParser parser;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        FunctionBank functions = new FunctionBank();
        Environment environment = new Environment(functions);

        //Put defaults
        environment.setVariable("pi", Math.PI);
        environment.setVariable("e", Math.E);

        boolean stop = false;
        String command;
        while (!stop) {
            System.out.print("CalcCLI > ");
            if (in.hasNextLine()) {
                command = in.nextLine();
                switch (command.toLowerCase()) {
                    case "stop":
                        stop = true;
                        System.out.println("Stopping...");
                        break;
                    case "env":
                        System.out.println(environment);
                        break;
                    default:
                        if (!command.endsWith(";"))
                            command += ";";
                        handleMathInput(command, environment);
                        break;
                }
            }
        }

        in.close();
    }

    private static void handleMathInput(String in, Environment env) {
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
