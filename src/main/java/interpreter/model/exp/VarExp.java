package interpreter.model.exp;
import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.value.IValue;
public record VarExp(String id) implements IExp {
    @Override
    public IValue eval(IDictionary<String , IValue> symTable) throws MyException{
        //e2 error is fixed by "var id is not defined"
        //dictionary throws adtexception(" key not in dict"
        // which is adtlevel failure so its expression-level
        if(!symTable.isDefined(id)){
            throw new ExpressionException("variable" +id +" is not defined");

        }
        return symTable.lookup(id);
    }
    //for trace "print(v) , a = ...
    @Override
    public String toString(){
        return id;
    }
}
