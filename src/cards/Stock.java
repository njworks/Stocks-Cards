package cards;

public enum Stock {
    Apple("A"), BP("B"), Cisco("C"), Dell("D"), Ericsson("E");

    Stock(String symbol) {
        this.symbol = symbol;
    }

    public final String symbol;

    public static Stock parse(char c) {
        switch (Character.toUpperCase(c)) {
            case 'A':
                return Apple;
            case 'B':
                return BP;
            case 'C':
                return Cisco;
            case 'D':
                return Dell;
            case 'E':
                return Ericsson;
        }
        throw new RuntimeException("Stock parsing failed");
    }

    public static Stock parse(String s) {
        return parse(s.charAt(0));
    }

    //Checks stock and returns the id numbers
    public static int checkStock(String stock) {
        switch (stock) {
            case "APPLE":
                return 0;
            case "A":
                return 0;
            case "BP":
                return 1;
            case "B":
                return 1;
            case "CISCO":
                return 2;
            case "C":
                return 2;
            case "DELL":
                return 3;
            case "D":
                return 3;
            case "ERICSSON":
                return 4;
            case "E":
                return 4;
            default:
                return 6;
        }
    }
}
