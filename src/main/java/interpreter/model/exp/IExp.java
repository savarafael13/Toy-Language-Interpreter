package interpreter.model.exp;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.value.IValue;
//every expression can be evaluated against the symtable and yield an IValue
//eval only READS the symtable expressions cant change the state
public interface IExp {
    IValue eval(IDictionary<String , IValue> symtable) throws MyException;
}
