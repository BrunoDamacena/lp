package syntatic;

import interpreter.command.AssignCommand;
import interpreter.command.Command;
import interpreter.command.CommandsBlock;
import interpreter.command.IfCommand;
import interpreter.command.WhileCommand;
import interpreter.expr.*;
import interpreter.util.AccessPath;
import interpreter.util.StdFunction;
import interpreter.value.FunctionValue;
import interpreter.value.IntegerValue;
import interpreter.value.StringValue;
import lexical.Lexeme;
import lexical.LexicalAnalysis;
import lexical.TokenType;

import java.io.IOException;

public class SyntaticAnalysis {

    private LexicalAnalysis lex;
    private Lexeme current;

    public SyntaticAnalysis(LexicalAnalysis lex) throws IOException {
        this.lex = lex;
        this.current = lex.nextToken();
    }

    public Command start() throws IOException {
        Command c = procCode();
        matchToken(TokenType.END_OF_FILE);
        return c;
    }

    private void matchToken(TokenType type) throws IOException {
        // System.out.println("Match token: " + current.type + " == " + type + "?");
        if (type == current.type) {
            current = lex.nextToken();
        } else {
            showError();
        }
    }

    private void showError() {
        System.out.printf("%02d: ", lex.getLine());

        switch (current.type) {
            case INVALID_TOKEN:
                System.out.printf("Lexema inválido [%s]\n", current.token);
                break;
            case UNEXPECTED_EOF:
            case END_OF_FILE:
                System.out.printf("Fim de arquivo inesperado\n");
                break;
            default:
                System.out.printf("Lexema não esperado [%s]\n", current.token);
                break;
        }

        System.exit(1);
    }

    //<code> ::= { <statement> }
    private CommandsBlock procCode() throws IOException {
        CommandsBlock cb = new CommandsBlock();
        while (current.type == TokenType.IF
                || current.type == TokenType.WHILE
                || current.type == TokenType.SYSTEM
                || current.type == TokenType.ARGS
                || current.type == TokenType.SELF
                || current.type == TokenType.NAME) {
            Command c = procStatement();
            cb.addCommand(c);
        }
        return cb;
    }

    //<statement> ::= <if> | <while> | <cmd>
    private Command procStatement() throws IOException {
        Command cmd = null;
        if(current.type == TokenType.IF) {
            cmd = procIf(); // TODO: verificar isso
        }
        else if(current.type == TokenType.WHILE) {
            cmd = procWhile();
        }
        else {
            cmd = procCmd();
        }
        return cmd;
    }

    //<if> ::= if '(' <boolexpr> ')' '{' <code> '}' [else '{' <code> '}' ]
    private IfCommand procIf() throws IOException {
        int line = lex.getLine();
        matchToken(TokenType.IF);
        matchToken(TokenType.OPEN_PAR);
        BoolExpr be = procBoolExpr();
        matchToken(TokenType.CLOSE_PAR);
        matchToken(TokenType.OPEN_CUR);
        CommandsBlock doThen = procCode();
        CommandsBlock doElse = null;
        matchToken(TokenType.CLOSE_CUR);
        if(current.type == TokenType.ELSE) {
            matchToken(TokenType.ELSE);
            matchToken(TokenType.OPEN_CUR);
            doElse = procCode();
            matchToken(TokenType.CLOSE_CUR);
        }
        IfCommand ic = new IfCommand(be, doThen, doElse, line);
        return ic;
    }

    // <while> ::= while '(' <boolexpr> ')' '{' <code> '}'
    private WhileCommand procWhile() throws IOException {
        int line = lex.getLine();
        matchToken(TokenType.WHILE);
        matchToken(TokenType.OPEN_PAR);
        BoolExpr be = procBoolExpr();
        matchToken(TokenType.CLOSE_PAR);
        matchToken(TokenType.OPEN_CUR);
        CommandsBlock cb = procCode();
        matchToken(TokenType.CLOSE_CUR);
        WhileCommand wc = new WhileCommand(be, cb, line);
        return wc;
    }

    // <cmd> ::= <access> ( <assign> | <call> ) ';'
    private AssignCommand procCmd() throws IOException {
        AccessPath path = procAccess();
        AssignCommand ac = null;
        if(current.type == TokenType.ASSIGN) {
            ac = procAssign(path);
        }
        else if(current.type == TokenType.OPEN_PAR) {
            int line = lex.getLine();
            FunctionCallExpr fce = procCall(path);
            ac = new AssignCommand(null, fce, line);
        }
        else {
            showError();
        }
        matchToken(TokenType.DOT_COMMA);
        return ac;
    }

    //<access> ::= <var> { '.' <name> }
    private AccessPath procAccess() throws IOException {
        int line = lex.getLine();
        String name = procVar();
        AccessPath path = new AccessPath(name, line);
        while(current.type == TokenType.DOT) {
            matchToken(TokenType.DOT);
            name = procName();
            path.addName(name);
        }
        return path;
    }

    //<assign> ::= '=' <rhs>
    private AssignCommand procAssign(AccessPath path) throws IOException {
        int line = lex.getLine();
        matchToken(TokenType.ASSIGN);
        Rhs rhs = procRhs();
        AssignCommand ac = new AssignCommand(path, rhs, line);
        return ac;
    }

    //<call> ::= '(' [ <rhs> { ',' <rhs> } ] ')'
    private FunctionCallExpr procCall(AccessPath path) throws IOException {
        FunctionCallExpr fce = new FunctionCallExpr(path, lex.getLine());
        matchToken(TokenType.OPEN_PAR);
        if(current.type == TokenType.FUNCTION || current.type == TokenType.NUMBER
                || current.type == TokenType.STRING || current.type == TokenType.OPEN_PAR
                || current.type == TokenType.SYSTEM || current.type == TokenType.SELF
                || current.type == TokenType.ARGS || current.type == TokenType.NAME) {
            Rhs rhs = procRhs();
            fce.addParam(rhs);
            while(current.type == TokenType.COMMA) {
                matchToken(TokenType.COMMA);
                rhs = procRhs();
                fce.addParam(rhs);
            }
        }
        matchToken(TokenType.CLOSE_PAR);
        return fce;
    }

    //<boolexpr> ::= [ '!' ] <cmpexpr> [ ('&' | '|') <boolexpr> ]
    private BoolExpr procBoolExpr() throws IOException {
        BoolExpr be;
        if(current.type == TokenType.NOT) {
            int line = lex.getLine();
            matchToken(TokenType.NOT);
            be = new NotBoolExpr(procCmpExpr(), lex.getLine());
        }
        else {
            be = procCmpExpr();
        }

        if(current.type == TokenType.AND) {
            int line = lex.getLine();
            matchToken(TokenType.AND);
            BoolExpr rbe = procBoolExpr();
            be = new CompositeBoolExpr(be, BoolOp.And, rbe, line);
        }

        if(current.type == TokenType.OR) {
            int line = lex.getLine();
            matchToken(TokenType.OR);
            BoolExpr rbe = procBoolExpr();
            return new CompositeBoolExpr(be, BoolOp.Or, rbe, line);
        }
        return be;
    }

    //<cmpexpr> ::= <expr> <relop> <expr>
    private SingleBoolExpr procCmpExpr() throws IOException {
        Expr left = procExpr();
        int line = lex.getLine();
        RelOp rop = procRelop();
        Expr right = procExpr();
        SingleBoolExpr sbe = new SingleBoolExpr(left, rop, right, line);
        return sbe;
    }

    //<relop> ::= '==' | '!=' | '<' | '>' | '<=' | '>='
    private RelOp procRelop() throws IOException {
        if(current.type == TokenType.EQUAL) {
            matchToken(current.type);
            return RelOp.Equal;
        }
        if(current.type == TokenType.DIFF) {
            matchToken(current.type);
            return RelOp.Diff;
        }
        if(current.type == TokenType.LOWER) {
            matchToken(current.type);
            return RelOp.LowerThan;
        }
        if(current.type == TokenType.GREATER) {
            matchToken(current.type);
            return RelOp.GreaterThan;
        }
        if(current.type == TokenType.LOWER_EQUAL) {
            matchToken(current.type);
            return RelOp.LowerEqual;
        }
        if(current.type == TokenType.GREATER_EQUAL) {
            matchToken(current.type);
            return RelOp.GreaterEqual;
        }
        return null;
    }

    //<rhs> ::= <function> | <expr>
    private Rhs procRhs() throws IOException {
        Rhs rhs = null;
        if(current.type == TokenType.FUNCTION) {
            rhs = procFunction();
        }
        else {
            rhs = procExpr();
        }
        return rhs;
    }

    //<function> ::= function '{' <code> [ return <rhs> ';' ] '}'
    private FunctionRhs procFunction() throws IOException {
        int line = lex.getLine();
        matchToken(TokenType.FUNCTION);
        matchToken(TokenType.OPEN_CUR);
        CommandsBlock cmds = procCode();
        Rhs ret = null;
        if(current.type == TokenType.RETURN) {
            matchToken(TokenType.RETURN);
            ret = procRhs();
            matchToken(TokenType.DOT_COMMA);
        }
        StdFunction sf;
        if(ret == null) {
            sf = new StdFunction(cmds);
        }
        else {
            sf = new StdFunction(cmds, ret);
        }
        matchToken(TokenType.CLOSE_CUR);
        FunctionValue fv = new FunctionValue(sf);
        return new FunctionRhs(fv, line);
    }

    //<expr> ::= <term> { ('+' | '-') <term> }
    private Expr procExpr() throws IOException {
        Expr e = procTerm();
        while(current.type == TokenType.ADD || current.type == TokenType.SUB) {
            int line = lex.getLine();
            if(current.type == TokenType.ADD) {
                matchToken(TokenType.ADD);
                Expr rightTerm = procTerm();
                CompositeExpr ce = new CompositeExpr(e, CompOp.Add, rightTerm, line);
                e = ce;
            }
            
            if(current.type == TokenType.SUB) {
                matchToken(TokenType.SUB);
                Expr rightTerm = procTerm();
                CompositeExpr ce = new CompositeExpr(e, CompOp.Sub, rightTerm, line);
                e = ce;
            }
        }
        return e;
    }

    //<term> ::= <factor> { ('*' | '/' | '%') <factor> }
    private Expr procTerm() throws IOException {
        Expr e = procFactor();
        while(current.type == TokenType.MUL || current.type == TokenType.DIV || current.type == TokenType.MOD) {
            int line = lex.getLine();
            if(current.type == TokenType.MUL) {
                matchToken(TokenType.MUL);
                Expr rightFactor = procFactor();
                CompositeExpr ce = new CompositeExpr(e, CompOp.Mul, rightFactor, line);
                e = ce;
            }
            if(current.type == TokenType.DIV) {
                matchToken(TokenType.DIV);
                Expr rightFactor = procFactor();
                CompositeExpr ce = new CompositeExpr(e, CompOp.Div, rightFactor, line);
                e = ce;
            }
            if(current.type == TokenType.MOD) {
                matchToken(TokenType.MOD);
                Expr rightFactor = procFactor();
                CompositeExpr ce = new CompositeExpr(e, CompOp.Mod, rightFactor, line);
                e = ce;
            }
        }
        return e;
    }

    //<factor> ::= <number> | <string> | <access> [ <call> ] | '(' <expr> ')'
    private Expr procFactor() throws IOException {
        Expr e = null;
        if(current.type == TokenType.NUMBER) {
            e = procNumber();
        }
        else if(current.type == TokenType.STRING) {
            e = procString();
        }
        else if(current.type == TokenType.OPEN_PAR) {
            matchToken(TokenType.OPEN_PAR);
            e = procExpr();
            matchToken(TokenType.CLOSE_PAR);
        }
        else if(current.type == TokenType.SYSTEM || current.type == TokenType.SELF ||
                current.type == TokenType.ARGS || current.type == TokenType.NAME) {
            int line = lex.getLine();
            AccessPath path = procAccess();
            if(current.type == TokenType.OPEN_PAR) {
                e = procCall(path);
            }
            else {
                e = new AccessExpr(path, line);
            }
        }
        return e;
    }

    //<var> ::= system | self | args | <name> 	
    private String procVar() throws IOException {
        String var = null;
        if(current.type == TokenType.SYSTEM) {
            var = current.token;
            matchToken(TokenType.SYSTEM);
        }
        else if(current.type == TokenType.SELF) {
            var = current.token;
            matchToken(TokenType.SELF);
        }
        else if(current.type == TokenType.ARGS) {
            var = current.token;
            matchToken(TokenType.ARGS);
        }
        else if(current.type == TokenType.NAME) {
            var = procName();
        }
        return var;
    }
    
    private ConstExpr procNumber() throws IOException {
        int line = lex.getLine();
        String tmp = current.token;
        matchToken(TokenType.NUMBER);
        int n = Integer.parseInt(tmp);
        IntegerValue iv = new IntegerValue(n);
        ConstExpr ce = new ConstExpr(iv, line);
        return ce;
    }

    private ConstExpr procString() throws IOException {
        int line = lex.getLine();
        String tmp = current.token;
        matchToken(TokenType.STRING);
        StringValue sv = new StringValue(tmp);
        ConstExpr ce = new ConstExpr(sv, line);
        return ce;
    }
    
    private String procName() throws IOException {
        String name = current.token;
        if(current.type == TokenType.NAME) {
            matchToken(TokenType.NAME);
            return name;
        }
        return null;
    }

}