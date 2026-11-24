package interpreter.model.type;

import interpreter.model.value.IValue;
import interpreter.model.value.RefValue;

// A4 (§2.1): recursive reference type built from any IType (including another RefType).
// Declared as a record so equals/hashCode compare inner types recursively.
public record RefType(IType inner) implements IType {

    // A4 (Rule S3, §2.1): declaring "Ref T x;" initializes x to (0, T), where 0 means null
    @Override
    public IValue defaultValue() {
        return new RefValue(0, inner);
    }

    // A4 (§2.1): prints "Ref(int)", "Ref(Ref(int))", etc.
    @Override
    public String toString() {
        return "Ref(" + inner + ")";
    }
}