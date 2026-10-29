package interpreter.view;

import interpreter.model.exp.ArithExp;
import interpreter.model.exp.ArithOp;
import interpreter.model.exp.IExp;
import interpreter.model.exp.ValueExp;
import interpreter.model.exp.VarExp;
import interpreter.model.stmt.AssignStmt;
import interpreter.model.stmt.CompStmt;
import interpreter.model.stmt.IfStmt;
import interpreter.model.stmt.IStmt;
import interpreter.model.stmt.PrintStmt;
import interpreter.model.stmt.VarDeclStmt;
import interpreter.model.type.BoolType;
import interpreter.model.type.IntType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;

/**
 * The three example programs of §2.2, written directly as object trees (§4.9).
 * There is no parser yet, so operator precedence is given by the shape of the
 * tree - which is why 2 + 3 * 5 is an ADD whose right child is a MUL.
 */
public final class Examples {

    private Examples() {           // utility class: not meant to be instantiated
    }

    /** Ex1: int v; v = 2; print(v)  ->  prints 2 */
    public static final IStmt EX1 =
            new CompStmt(new VarDeclStmt("v", new IntType()),
                    new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(2))),
                            new PrintStmt(new VarExp("v"))));

    /**
     * Ex2: int a; int b; a = 2 + 3 * 5; b = a + 1; print(b)  ->  prints 18
     * The MUL is the right-hand child of the ADD, so the tree already encodes
     * the precedence: 2 + (3 * 5) = 17, then b = 17 + 1 = 18.
     */
    public static final IStmt EX2 =
            new CompStmt(new VarDeclStmt("a", new IntType()),
                    new CompStmt(new VarDeclStmt("b", new IntType()),
                            new CompStmt(new AssignStmt("a",
                                    new ArithExp(ArithOp.ADD, new ValueExp(new IntValue(2)),
                                            new ArithExp(ArithOp.MUL,
                                                    new ValueExp(new IntValue(3)),
                                                    new ValueExp(new IntValue(5))))),
                                    new CompStmt(new AssignStmt("b",
                                            new ArithExp(ArithOp.ADD, new VarExp("a"),
                                                    new ValueExp(new IntValue(1)))),
                                            new PrintStmt(new VarExp("b"))))));

    /**
     * Ex3: bool a; int v; a = true; (if a then v = 2 else v = 3); print(v)
     * -> prints 2 (the then-branch is taken)
     */
    public static final IStmt EX3 =
            new CompStmt(new VarDeclStmt("a", new BoolType()),
                    new CompStmt(new VarDeclStmt("v", new IntType()),
                            new CompStmt(new AssignStmt("a", new ValueExp(new BoolValue(true))),
                                    new CompStmt(new IfStmt(new VarExp("a"),
                                            new AssignStmt("v", new ValueExp(new IntValue(2))),
                                            new AssignStmt("v", new ValueExp(new IntValue(3)))),
                                            new PrintStmt(new VarExp("v"))))));
}