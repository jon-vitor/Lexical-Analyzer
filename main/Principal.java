package main;

import exceptions.PscLexicalException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import lexico.TScanner;
import lexico.Tokens;

public class Principal {

    public static void main(String[] args) throws IOException {
        FileWriter file = new FileWriter("C:/Users/jvsno/Documents/NetBeansProjects/LexicalAnalizer/src/output.txt");
        PrintWriter burn = new PrintWriter(file);
        try {
            TScanner scan = new TScanner("C:/Users/jvsno/Documents/NetBeansProjects/LexicalAnalizer/src/maior.p");
            Tokens token = null;
            
            do {
                token = scan.nextToken();
                if (token != null) {
                    System.out.println(token);
                    burn.println(token);
                }
            } while (token != null);
            
            System.out.println("\nSalvo em arquivo \"output.txt\"");
        }
        catch (PscLexicalException ex) {
            System.out.println("Erro Lexico - " + ex.getMessage());
        }
        catch (Exception ex) {
            System.out.println("Erro desconhecido");
        }
        file.close();
    }
}
