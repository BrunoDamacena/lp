package interpreter.util;

import interpreter.value.FunctionValue;
import java.util.Random;
import java.util.Scanner;

import interpreter.value.Value;
import interpreter.value.IntegerValue;
import interpreter.value.StringValue;
import interpreter.value.InstanceValue;

public class SpecialFunction extends Function {

    private FunctionType type;
    private Scanner in;
    private Random rand;

    public SpecialFunction(FunctionType type) {
        this.type = type;
        this.in = new Scanner(System.in);
        this.rand = new Random();
    }

    public Value<?> call(Instance self, Arguments args) {
        Value<?> v;

        switch (type) {
            case Print:
                v = this.print(args);
                break;
            case Println:
                v = this.println(args);
                break;
            case Read:
                v = this.read(args);
                break;
            case Random:
                v = this.random(args);
                break;
            case Clone:
                v = this.clone(args);
                break;
            case Get:
                v = this.get(args);
                break;
            case Set:
                v = this.set(args);
                break;
            case Abort:
                v = this.abort(args);
                break;
            case Type:
                v = this.type(args);
                break;
            case Length:
                v = this.length(args);
                break;
            case Substring:
                v = this.substring(args);
                break;
            
            default:
                throw new RuntimeException("FIXME: implement me!");
        }

        return v;
    }

    private Value<?> print(Arguments args) {
        if (args.contains("arg1")) {
            Value<?> v = args.getValue("arg1");
            if (v instanceof IntegerValue) {
                IntegerValue iv = (IntegerValue) v;
                System.out.print(v.value());
            } else if (v instanceof StringValue) {
                StringValue sv = (StringValue) v;
                System.out.print(sv.value());
            } else {
                throw new RuntimeException("FIXME: Implement me!");
            }
        }

        return IntegerValue.Zero;
    }

    private Value<?> println(Arguments args) {
        Value<?> v = print(args);
        System.out.println();
        return v;
    }

    private Value<?> read(Arguments args) {
        // Print the argument.
        this.print(args);

        String str = in.nextLine();
        try {
           int n = Integer.parseInt(str);
           IntegerValue iv = new IntegerValue(n);
           return iv;
        } catch (Exception e) {
           StringValue sv = new StringValue(str);
           return sv;
        }
    }

    private Value<?> random(Arguments args) {
        if (!args.contains("arg1"))
            InterpreterError.abort("system.random: primeiro argumento inexistente");
        else if (!args.contains("arg2"))
            InterpreterError.abort("system.random: segundo argumento inexistente");

        Value<?> v1 = args.getValue("arg1");
        Value<?> v2 = args.getValue("arg2");

        if (!(v1 instanceof IntegerValue))
            InterpreterError.abort("system.random: primeiro argumento não é inteiro");
	else if (!(v2 instanceof IntegerValue))
            InterpreterError.abort("system.random: segundo argumento não é inteiro");

        int n1 = ((IntegerValue) v1).value();
        int n2 = ((IntegerValue) v2).value();

        if (n2 <= n1)
            InterpreterError.abort("system.random: segundo argumento menor/igual ao primeiro");

        int r = rand.nextInt(n2 - n1 + 1) + n1;
        IntegerValue iv = new IntegerValue(r);
        return iv;
    }

    private Value<?> clone(Arguments args) {
        if (!args.contains("arg1"))
            InterpreterError.abort("clone: primeiro argumento inexistente");

        Value<?> v = args.getValue("arg1");
        if (!(v instanceof InstanceValue))
            InterpreterError.abort("clone: primeiro argumento não é instance");

        Instance i = ((InstanceValue) v).value();
        InstanceValue iv = new InstanceValue(i.dup());
        return iv;
    }
    
    private Value<?> get(Arguments args) {
        if(!args.contains("arg1")) {            
            InterpreterError.abort("get: primeiro argumento inexistente");
        }
        
        if(!args.contains("arg2")){
            InterpreterError.abort("get: primeiro argumento inexistente");
        }
        
        Value<?> objInstance = args.getValue("arg1");
        Value<?> attrString = args.getValue("arg2");
        
        if(!(objInstance instanceof InstanceValue)) {
            InterpreterError.abort("get: primeiro argumento não é instance");
        }
        
        if(!(attrString instanceof StringValue)) {
            InterpreterError.abort("get: segundo argumento não é string");
        }
        
        String attr = (String) attrString.value();

        Instance pureInstance = ((InstanceValue) objInstance).value();
        Value<?> attrValue = pureInstance.getValue(attr);

        return attrValue;
    }
    
    private Value<?> set(Arguments args) {
        if(!args.contains("arg1")) {
            InterpreterError.abort("set: argumento inexistente");
        }
        
        Value<?> objInstance = args.getValue("arg1");
        Value<?> attrString = args.getValue("arg2");
        Value<?> value = args.getValue("arg3");
        
        if(!(objInstance instanceof InstanceValue)) {
            InterpreterError.abort("set: primeiro argumento não é instance");
        }
        
        if(!(attrString instanceof StringValue)) {
            InterpreterError.abort("set: segundo argumento não é string");
        }
        
        String attr = (String) attrString.value();

        Instance pureInstance = ((InstanceValue) objInstance).value();
        pureInstance.setValue(attr, value);
        
        return new IntegerValue(0);
    }
    
    private Value<?> abort(Arguments args) {
        Value<?> argumentOne;
        
        if(args.contains("arg1")) {
            argumentOne = args.getValue("arg1");
            
            if(!(argumentOne instanceof StringValue)) {
                InterpreterError.abort("abort: argumento não é string");
            }
           
            this.println(args);   
        }
        
        System.exit(0);
        
        return new IntegerValue(0);
    }
    
    private Value<?> type(Arguments args) {
        if(!(args.contains("arg1"))) {
            InterpreterError.abort("type: argumento inexistente");
        }
        
        Value<?> argumentOne = args.getValue("arg1");
        StringValue ret = null;
        
        if(argumentOne instanceof IntegerValue) {
            ret = new StringValue("integer");
        }
        else if (argumentOne instanceof StringValue) {
            ret = new StringValue("string");
        }
        else if (argumentOne instanceof FunctionValue) {
            ret = new StringValue("function");
        }
        else if (argumentOne instanceof InstanceValue) {
            ret = new StringValue("instance");
        }
        
        return ret;
    }
    
    private Value<?> length(Arguments args) {
        Value<?> argumentOne = null;
        
        if(!(args.contains("arg1"))) {
            InterpreterError.abort("length: argumento inexistente");
        }
        
        argumentOne = args.getValue("arg1");
        
        if(!(argumentOne instanceof StringValue)) {
            InterpreterError.abort("length: argumento não é string");
        } 
        
        String value = (String) argumentOne.value();
        int len = value.length();
        IntegerValue strLen = new IntegerValue(len);
        
        return strLen;
    }
    
    private Value<?> substring(Arguments args) {
        if(!(args.contains("arg1"))) {           
            InterpreterError.abort("substring: primeiro argumento inexistente");
        }
        
        if(!(args.contains("arg2"))){
            InterpreterError.abort("substring: segundo argumento inexistente");
        }
        if(!(args.contains("arg3"))) {
            InterpreterError.abort("substring: terceiro argumento inexistente");
        }
        
        Value<?> argumentOne = args.getValue("arg1");
        Value<?> argumentTwo = args.getValue("arg2");
        Value<?> argumentThree = args.getValue("arg3");
        
        if (!(argumentOne instanceof StringValue)) {
            InterpreterError.abort("substring: primeiro argumento inválido");
        }
        
        if(!(argumentTwo instanceof IntegerValue)) {
            InterpreterError.abort("substring: segundo argumento inválido");
        }
        if(!(argumentThree instanceof IntegerValue)) {
            InterpreterError.abort("substring: terceiro argumento inválido");
        }
        
        String value = (String) argumentOne.value();
        int indexI = ((IntegerValue) argumentTwo).value();
        int indexF = ((IntegerValue) argumentThree).value();
        
        
        String strRet = value.substring(indexI, indexF);
        StringValue subStr = new StringValue(strRet);
        
        return subStr;
    }    
    
}