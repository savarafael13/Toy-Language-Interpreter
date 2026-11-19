package interpreter.model.type;

import interpreter.model.value.IValue;
import interpreter.model.value.StringValue;

// A3 (§2.1): built-in string type record so equals/hashCode compare by type
public record StringType() implements IType {

    // A3 (Rule S3): default value when declaring "string x;" is ""
    @Override
    public IValue defaultValue() {
        return new StringValue("");
    }

    // A3 (§2.1): used by VarDeclStmt.toString() -> "string varf"
    @Override
    public String toString() {
        return "string";
    }
}