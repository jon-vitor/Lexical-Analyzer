
package lexico;

public class Tokens {
    public static final String T_ID = "[IDENTIFICADOR]";
    public static final String T_NUM = "[NUMERO]";
    public static final String T_SPECIAL = "[SIMBOLO/OPERADOR]";
    public static final String T_KEYWORD = "[PALAVRA RESERVADA]";
    public static final String T_ASSIGN = "[ATRIBUICAO]";
    public static final String T_STR = "[STRING]";
    public static final String T_REAL_NUM = "[NUMERO REAL]";
    public static final String T_END = "[FIM]";
    
    private String type;
    private String text;

    public Tokens(String type, String text) {
        super();
        this.type = type;
        this.text = text;
    }

    public Tokens() {
        super();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "\n" + type + ", Lexema --> " + text;
    }
}
