/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter.util;

import interpreter.command.CommandsBlock;
import interpreter.expr.Rhs;
import interpreter.value.IntegerValue;
import interpreter.value.Value;

/**
 *
 * @author brunodamacena
 */
public class StdFunction extends Function {
    
    private CommandsBlock cb;
    private Rhs rhs;
    
    public StdFunction(CommandsBlock cb) {
        this.cb = cb;
    }
    
    public StdFunction(CommandsBlock cb, Rhs rhs) {
        this.cb = cb;
        this.rhs = rhs;
    }
    
    @Override
    public Value<?> call(Instance self, Arguments args) {
        cb.execute(self, args);
        if (rhs == null) {
            return new IntegerValue(0);
        }
        return rhs.rhs(self, args);
    }
    
    
}
