package lexico;

import exceptions.PscLexicalException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TScanner {

    private char[] content;
    private int state;
    private int position;
    private String[] keywords = {
                     "char", "do", "program", "begin", "if", "then", "else",
                     "while", "const", "integer", "String", "read", "write",
                     "writeln", "var", "print", "end", "absolute", "array",
                     "case", "div", "and", "or", "not"};

    public TScanner(String filename) {
        try {
            String fileContent;
            fileContent = new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8);
            System.out.println("..............Arquivo fonte..............");
            System.out.println(fileContent);
            System.out.println(".........................................");
            content = fileContent.toCharArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public Tokens nextToken() {
        Tokens token;
        String word = "";
        char currentChar;
        state = 0;

        if (isEOF()) {
            return null;
        } else {
            while (true) {
                currentChar = nextChar();
                switch (state) {
                    case 0:
                        if (isSpace(currentChar)) {
                            state = 0;
                        } else if (isChar(currentChar)) {
                            word += currentChar;
                            state = 1;
                        } else if (isDigit(currentChar)) {
                            word += currentChar;
                            state = 3;
                        } else if (currentChar == '"') {
                            state = 10;
                        } else if (currentChar == '.' && (position == content.length-1)) {
                            word += currentChar;
                            state = 9;
                        } else if (currentChar == '<' || currentChar == '>') {
                            word += currentChar;
                            state = 6;
                        } else if (currentChar == '=') {
                            word += currentChar;
                            state = 6;
                        } else if (currentChar == '/') {
                            word += currentChar;
                            state = 12;
                        } else if (currentChar == ':') {
                            word += currentChar;
                            state = 7;
                        } else if ((isOperator(currentChar))) {
                            word += currentChar;
                            state = 5;
                        } else {
                            System.out.println("ERRO LEXICO - Simbolo desconhecido: \"" + word + currentChar + "\"");
                        }
                        break;

                    case 1:
                        if (isChar(currentChar) || isDigit(currentChar)) {
                            word += currentChar;
                            state = 1;
                        } else if (isSpace(currentChar) || isOperator(currentChar)) {
                            state = 2;
                            back();
                        } else {
                            token = new Tokens();
                            token.setType("");
                            token.setText("");
                            System.out.println("ERRO LEXICO - identificador mal formado: \"" + word + currentChar + "\"");
                            return token;
                        }
                        break;

                    case 2:
                        int j = 0;
                        token = new Tokens();
                        for (int i = 0; i < keywords.length; i++) {
                            if (keywords[i].equals(word)) {
                                j++;
                            }
                        }
                        if (j > 0) {
                            token.setType(Tokens.T_KEYWORD);
                        } else {
                            token.setType(Tokens.T_ID);
                        }
                        token.setText(word);
                        back();
                        return token;

                    case 3:
                        if (isDigit(currentChar)) {
                            word += currentChar;
                            state = 3;
                        } else if (currentChar == '.' && isDigit(nextChar())) {
                            word += currentChar;
                            state = 16;
                            back();
                        } else if (!isChar(currentChar)) {
                            state = 4;
                            back();
                        } else {
                            token = new Tokens();
                            token.setType("");
                            token.setText("");
                            System.out.println("ERRO LEXICO - Numero mal formado: \"" + word + currentChar + "\"");
                            return token;
                        }
                        break;

                    case 4:
                        token = new Tokens();
                        token.setType(Tokens.T_NUM);
                        token.setText(word);
                        back();
                        return token;

                    case 5:
                        token = new Tokens();
                        token.setType(Tokens.T_SPECIAL);
                        token.setText(word);
                        back();
                        return token;

                    case 6:
                        if (currentChar == '=') {
                            word += currentChar;
                            state = 5;
                        } else if (isSpace(currentChar)) {
                            state = 6;
                        } else if (isDigit(currentChar)) {
                            state = 5;
                            back();
                        } else if (isChar(currentChar)) {
                            state = 5;
                            back();
                        } else if (isOperator(currentChar)) {
                            state = 5;
                            back();
                        } else {
                            token = new Tokens();
                            token.setType("");
                            token.setText("");
                            System.out.println("ERRO LEXICO - Operador mal formado: \"" + word + currentChar + "\"");
                            return token;
                        }
                        break;
                        
                    case 7:
                        if (currentChar == '=') {
                            word += currentChar;
                            state = 8;
                        } else {
                            state = 5;
                            back();
                        }
                        break;
                        
                    case 8:
                        token = new Tokens();
                        token.setType(Tokens.T_ASSIGN);
                        token.setText(word);
                        back();
                        return token;
                        
                    case 9:
                        token = new Tokens();
                        token.setType(Tokens.T_END);
                        token.setText(word);
                        return token;
                        
                    case 10:
                        if (currentChar != '"') {
                            word += currentChar;
                            state = 10;
                        } else if (currentChar == '"') {
                            state = 11;
                        } else {
                            token = new Tokens();
                            token.setType("");
                            token.setText("");
                            System.out.println("ERRO LEXICO - String mal formada: \"" + word + currentChar + "\"");
                            return token;
                        }
                        break;
                        
                    case 11:
                        token = new Tokens();
                        token.setType(Tokens.T_STR);
                        token.setText(word);
                        back();
                        return token;
                        
                    case 12:
                        if (currentChar == '*') {
                            state = 13;
                        } else if (!isOperator(currentChar)) {
                            state = 5;
                            back();
                        } else {
                            token = new Tokens();
                            token.setType("");
                            token.setText("");
                            System.out.println("ERRO LEXICO - Operador mal formado: \"" + word + currentChar + "\"");
                            return token;
                        }
                        break;
                        
                    case 13:
                        if (currentChar != '*') {
                            state = 13;
                        } else {
                            state = 14;
                        }
                        break;
                        
                    case 14:
                        if (currentChar != '/') {
                            state = 13;
                            back();
                        } else {
                            state = 15;
                        }
                        break;
                        
                    case 15:
                        word = "";
                        state = 0;
                        break;
                        
                    case 16:
                        if (isDigit(currentChar)) {
                            word += currentChar;
                            state = 16;
                        } else {
                            state = 17;
                            back();
                        }
                        break;
                        
                    case 17:
                        token = new Tokens();
                        token.setType(Tokens.T_REAL_NUM);
                        token.setText(word);
                        back();
                        return token;
                }
            }
        }
    }

    private boolean isDigit(char digit) {
        return digit >= '0' && digit <= '9';
    }

    private boolean isChar(char letter) {
        return (letter >= 'a' && letter <= 'z')
                || (letter >= 'A' && letter <= 'Z');
    }

    private boolean isOperator(char op) {
        return op == '#' || op == '+' || op == '(' || op == ')' || op == '/'
                         || op == '-' || op == ';' || op == '>' || op == '{'
                         || op == '}' || op == '<' || op == '=' || op == '!'
                         || op == ':' || op == '*' || op == ',' || op == '.';
    }

    private boolean isSpace(char tab) {
        return tab == ' ' || tab == '\t' || tab == '\n' || tab == '\r';
    }

    private char nextChar() {
        return content[position++];
    }

    private void back() {
        position--;
    }

    private boolean isEOF() {
        return position == content.length;
    }
}
