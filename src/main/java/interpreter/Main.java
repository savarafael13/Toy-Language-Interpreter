package interpreter;

import interpreter.controller.Controller;
import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.stmt.IStmt;
import interpreter.repository.Repository;
import interpreter.view.Examples;

import java.util.Scanner;

/**
 * Entry point (§1.3: "view start-up").
 * §4.8: a text menu with two options - select a program and run it completely.
 * Errors are caught here and shown as messages, so the menu never crashes.
 */
public class Main {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        while (true) {
            System.out.println();
            System.out.println("1. Ex1: int v; v = 2; print(v)");
            System.out.println("2. Ex2: int a; int b; a = 2 + 3 * 5; b = a + 1; print(b)");
            System.out.println("3. Ex3: bool a; int v; a = true; if a then v = 2 else v = 3; print(v)");
            System.out.println("0. exit");
            System.out.print("> ");

            String choice = in.nextLine().trim();

            if (choice.equals("0"))
                return;

            IStmt program = switch (choice) {
                case "1" -> Examples.EX1;
                case "2" -> Examples.EX2;
                case "3" -> Examples.EX3;
                default -> null;
            };

            if (program == null) {
                System.out.println("invalid option, try again");
                continue;
            }

            try {
                // A fresh state per run: new ADTs, so nothing leaks between runs.
                // PrgState's constructor pushes the whole program - do not push it here.
                PrgState state = new PrgState(new MyStack<>(), new MyDictionary<>(),
                        new MyList<>(), program);
                // displayFlag = true -> the trace of §3.3 is printed after every step
                new Controller(new Repository(state), true).allStep();
                System.out.println("Program finished. Output: " + state.getOut());
            } catch (MyException e) {
                System.out.println("Error: " + e.getMessage());   // §4.8: menu must not crash
            }
        }
    }
}