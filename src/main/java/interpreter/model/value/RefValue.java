package interpreter.model.value;

import interpreter.model.type.IType;
import interpreter.model.type.RefType;

// A4 (§1.1, §2.1): pairs a heap address (0 = null) with the type stored at that location
public record RefValue(int address, IType locationType) implements IValue {

    // A4 (§2.1): returns Ref(locationType) so AssignStmt and NewStmt can verify types
    @Override
    public IType getType() {
        return new RefType(locationType);
    }

    // A4 (§2.1): formats as "(address,locationType)", e.g. "(1,int)" or "(2,Ref(int))"
    @Override
    public String toString() {
        return "(" + address + "," + locationType + ")";
    }
}