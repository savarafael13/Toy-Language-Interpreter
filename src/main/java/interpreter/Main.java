package interpreter;

import interpreter.controller.Controller;
import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.stmt.IStmt;
import interpreter.repository.Repository;
import interpreter.view.Examples;

import java.util.Scanner;

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
                PrgState state = PrgState.of(program);
                // A3 (§2.6): pass temporary log file path until Step 6 adds TextMenu
                new Controller(new Repository(state, "log" + choice + ".txt"), true).allStep();
                System.out.println("Program finished. Output: " + state.getOut());
            } catch (MyException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}