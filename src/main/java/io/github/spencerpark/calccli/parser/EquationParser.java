package io.github.spencerpark.calccli.parser;

import io.github.spencerpark.calccli.command.*;
import io.github.spencerpark.calccli.expression.*;
import io.github.spencerpark.calccli.expression.objects.CalcDecimal;
import io.github.spencerpark.calccli.function.UserDefinedFunction;

import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Parse a stream of tokens emitted from {@link EquationLexer}.
 * <p>
 * command ::= ( assign | expr ) SEMI_COLON
 * <p>
 * assign ::= (ID | ID L_PAREN expr (COMMA expr) R_PAREN) ASSIGN expr
 * <p>
 * expr ::= (PLUS | MINUS)? term ((PLUS | MINUS) term)*
 * term ::= factor (( TIMES | DIVIDE) factor)*
 * factor ::= NUMBER | L_PAREN expr R_PAREN | function | DOLLAR ID
 * function ::= ID L_PAREN expr (COMMA expr) R_PAREN
 */
public class EquationParser {
    private static final BitSet PLUS_OR_MINUS = new BitSet();
    private static final BitSet TIMES_OR_DIVIDE = new BitSet();
    private static final BitSet FIRST_EXPRESSION = new BitSet();

    static {
        PLUS_OR_MINUS.set(SymbolType.PLUS.ordinal());
        PLUS_OR_MINUS.set(SymbolType.MINUS.ordinal());

        TIMES_OR_DIVIDE.set(SymbolType.TIMES.ordinal());
        TIMES_OR_DIVIDE.set(SymbolType.DIVIDE.ordinal());

        FIRST_EXPRESSION.set(SymbolType.PLUS.ordinal());
        FIRST_EXPRESSION.set(SymbolType.MINUS.ordinal());
        FIRST_EXPRESSION.set(SymbolType.NUMBER.ordinal());
        FIRST_EXPRESSION.set(SymbolType.L_PAREN.ordinal());
        FIRST_EXPRESSION.set(SymbolType.IDENTIFIER.ordinal());
        FIRST_EXPRESSION.set(SymbolType.DOLLAR.ordinal());
    }

    private static <T> T throwUnexpectedToken(Token problem, SymbolType... expected) {
        StringBuilder exp = new StringBuilder("[");
        for (int i = 0; i < expected.length; i++) {
            exp.append(expected[i].name());
            if (i + 1 != expected.length) exp.append(", ");
        }
        exp.append(']');

        throw new ParseException(String.format("Unexpected token %s. Expected one of %s", problem, exp.toString()));
    }

    private static final int REQUIRED_LOOKAHEAD = 3;

    private final TokenStream tokens;
    private final Environment global;

    public EquationParser(Supplier<Token> tokens, Environment global) {
        this.tokens = new TokenStream(REQUIRED_LOOKAHEAD, tokens);
        this.global = global;
    }

    public void reset() {
        this.tokens.reset();
    }

    public List<Command> parseCommands() {
        List<Command> cmds = new LinkedList<>();
        while (tokens.peek().getType() != SymbolType.EOS) {
            cmds.add(parseCommand());
        }
        return cmds;
    }

    /**
     * Parse
     * <pre>
     * command ::= ( assign | expr | ID STRING* ) SEMI_COLON
     * </pre>
     *
     * @return an expression representing the parsed expression
     */
    public Command parseCommand() {
        Command cmd = null;
        Token la1 = tokens.lookahead(1);
        if (tokens.peek().getType() == SymbolType.IDENTIFIER) {
            if (la1.getType() == SymbolType.ASSIGN) {
                cmd = parseAssign();
            } else if (la1.getType() == SymbolType.L_PAREN) {
                Token la2 = tokens.lookahead(2);
                if (la2.getType() == SymbolType.IDENTIFIER) {
                    cmd = parseAssign();
                }
            } else if (la1.getType() == SymbolType.SEMI_COLON
                    || la1.getType() == SymbolType.STRING) {

                String cmdName = tokens.consume().getText();
                List<String> args = new LinkedList<>();
                while (tokens.peek().getType() == SymbolType.STRING) {
                    args.add(tokens.consume().getText());
                }

                cmd = new ControlCommand(cmdName, args.toArray(new String[args.size()]));
            }
        }

        if (cmd == null) {
            Expression expr = parseExpression();
            cmd = new PrintCommand(expr);
        }

        if (tokens.peek().getType() != SymbolType.SEMI_COLON)
            return throwUnexpectedToken(tokens.peek(), SymbolType.SEMI_COLON);
        tokens.consume();

        return cmd;
    }

    /**
     * Parse
     * <pre>
     * assign ::= (ID | ID L_PAREN ID (COMMA ID) R_PAREN) ASSIGN expr SEMI_COLON
     * </pre>
     *
     * @return a command representing the assignment command
     */
    public Command parseAssign() {
        Token id = tokens.peek();
        if (id.getType() != SymbolType.IDENTIFIER)
            return throwUnexpectedToken(id, SymbolType.IDENTIFIER);
        tokens.consume();

        if (tokens.peek().getType() == SymbolType.L_PAREN) {
            tokens.consume();

            List<String> params = new LinkedList<>();
            if (tokens.peek().getType() == SymbolType.IDENTIFIER) {
                params.add(tokens.consume().getText());
                while (tokens.peek().getType() == SymbolType.COMMA) {
                    tokens.consume();
                    if (tokens.peek().getType() != SymbolType.IDENTIFIER)
                        return throwUnexpectedToken(tokens.peek(), SymbolType.IDENTIFIER);
                    params.add(tokens.consume().getText());
                }
            } else {
                return throwUnexpectedToken(tokens.peek(), SymbolType.IDENTIFIER);
            }

            if (tokens.peek().getType() != SymbolType.R_PAREN)
                return throwUnexpectedToken(tokens.peek(), SymbolType.R_PAREN);
            tokens.consume();

            if (tokens.peek().getType() != SymbolType.ASSIGN)
                return throwUnexpectedToken(tokens.peek(), SymbolType.ASSIGN);
            tokens.consume();

            Expression expr = parseExpression();
            UserDefinedFunction fn = new UserDefinedFunction(this.global, id.getText(), params.toArray(new String[params.size()]), expr);

            return new FunctionDefinitionCommand(fn);
        } else {
            if (tokens.peek().getType() != SymbolType.ASSIGN)
                return throwUnexpectedToken(id, SymbolType.ASSIGN);
            tokens.consume();

            Expression expr = parseExpression();

            return new AssignmentCommand(id.getText(), expr);
        }
    }

    /**
     * Parse
     * <pre>
     * expr ::= (PLUS | MINUS)? term ((PLUS | MINUS) term)*
     * </pre>
     *
     * @return an expression representing the parsed expression
     */
    public Expression parseExpression() {
        Token next = tokens.peek();
        boolean unaryMinus = false;
        if (next.getType() == SymbolType.PLUS) tokens.consume();
        else if (next.getType() == SymbolType.MINUS) {
            tokens.consume();
            unaryMinus = true;
        }

        Expression expr = parseTerm();
        if (unaryMinus) expr = new UnaryMinus(expr);

        BinaryOperator operator;
        Expression right;
        next = tokens.peek();
        while (PLUS_OR_MINUS.get(next.getType().ordinal())) {
            if (next.getType() == SymbolType.PLUS) operator = BinaryOperator.PLUS;
            else operator = BinaryOperator.MINUS;
            tokens.consume();

            right = parseTerm();
            expr = new BinaryOperation(expr, operator, right);

            next = tokens.peek();
        }

        return expr;
    }

    /**
     * Parse
     * <pre>
     * term ::= factor (( TIMES | DIVIDE) factor)*
     * </pre>
     *
     * @return an expression representing the parsed expression
     */
    public Expression parseTerm() {
        Expression term = parseFactor();

        BinaryOperator operator;
        Expression right;
        Token next = tokens.peek();
        while (TIMES_OR_DIVIDE.get(next.getType().ordinal())) {
            if (next.getType() == SymbolType.TIMES) operator = BinaryOperator.TIMES;
            else operator = BinaryOperator.DIVIDE;
            tokens.consume();

            right = parseFactor();
            term = new BinaryOperation(term, operator, right);

            next = tokens.peek();
        }

        return term;
    }

    /**
     * Parse
     * <pre>
     * factor ::= NUMBER | L_PAREN expr R_PAREN | function | ID
     * </pre>
     *
     * @return an expression representing the parsed expression
     */
    public Expression parseFactor() {
        Token next = tokens.peek();
        if (next.getType() == SymbolType.NUMBER) {
            tokens.consume();
            return new Constant(new CalcDecimal(Double.parseDouble(next.getText())));
        } else if (next.getType() == SymbolType.L_PAREN) {
            tokens.consume();
            Expression expr = parseExpression();
            next = tokens.peek();
            if (next.getType() == SymbolType.R_PAREN) tokens.consume();
            else return throwUnexpectedToken(next, SymbolType.R_PAREN);
            return expr;
        } else if (next.getType() == SymbolType.DOLLAR) {
             tokens.consume();
            if (tokens.peek().getType() != SymbolType.IDENTIFIER)
                return throwUnexpectedToken(tokens.peek(), SymbolType.IDENTIFIER);
            return new VariableResolution(tokens.consume().getText());
        } else if (next.getType() == SymbolType.IDENTIFIER) {
            return parseFunction();
        } else {
            return throwUnexpectedToken(next, SymbolType.NUMBER, SymbolType.L_PAREN, SymbolType.IDENTIFIER);
        }
    }

    /**
     * Parse
     * <pre>
     * function ::= ID L_PAREN expr (COMMA expr) R_PAREN
     * </pre>
     *
     * @return an expression representing the parsed expression
     */
    public Expression parseFunction() {
        Token id = tokens.peek();
        if (id.getType() != SymbolType.IDENTIFIER)
            return throwUnexpectedToken(id, SymbolType.IDENTIFIER);
        tokens.consume();

        if (tokens.peek().getType() != SymbolType.L_PAREN)
            return throwUnexpectedToken(tokens.peek(), SymbolType.L_PAREN);
        tokens.consume();

        List<Expression> args = new LinkedList<>();
        if (FIRST_EXPRESSION.get(tokens.peek().getType().ordinal())) {
            args.add(parseExpression());
            while (tokens.peek().getType() == SymbolType.COMMA) {
                tokens.consume();
                args.add(parseExpression());
            }
        } else {
            return throwUnexpectedToken(tokens.peek(),
                    SymbolType.PLUS, SymbolType.MINUS, SymbolType.NUMBER,
                    SymbolType.L_PAREN, SymbolType.IDENTIFIER);
        }

        if (tokens.peek().getType() != SymbolType.R_PAREN)
            return throwUnexpectedToken(tokens.peek(), SymbolType.R_PAREN);
        tokens.consume();

        return new FunctionCall(id.getText(), args.toArray(new Expression[args.size()]));
    }
}
