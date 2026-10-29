package interpreter.model.exp;
import interpreter.model.adt.IDictionary;
import interpreter.model.value.IValue;
//a constat evaluates to istself . component is IValue not int value so bool constats use the same class
public record ValueExp(IValue value) implements IExp {
    @Override
    public IValue eval(IDictionary<String , IValue> symTable){
        return value;
    }
    // needed for trace 3.3 "v = 2 " not " v = valueExp[value=iNT..[val=2]"
    @Override
    public String toString(){
        return value.toString();
    }
}
