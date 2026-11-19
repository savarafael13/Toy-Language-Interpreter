package interpreter.model.value;

import interpreter.model.type.IType;
import interpreter.model.type.StringType;

// A3 (§2.1): record gives content-based equals/hashCode required for FileTable map keys
public record StringValue(String val) implements IValue {

    // A3 (§2.1): lets AssignStmt verify StringValue matches a StringType variable
    @Override
    public IType getType() {
        return new StringType();
    }

    // A3 (§2.1, §2.6): wraps value in quotes for SymTable logs (varf --> "test.in")
    @Override
    public String toString() {
        return "\"" + val + "\"";
    }
}