public class Card {
    private String suit;
    private char value;

    //constructor
    public Card(String suit, char value){
        this.suit = suit;
        this.value = value;
    }

    //getter for suit
    public String getSuit(){
        return this.suit;
    }

    //getter for value
    public char getValue(){
        return this.value;
    }

    //getter for int value of card
    public int getIntValue(){
        return convert(this.value);
    }

    //static int value getter
    public static int getIntValueStatic(char value){
        return convert(value);
    }

    //method to get int value
    //Aces will always be returned as a 1
    //tens are stored as '0'
    public static int convert(char value){
        if (value == 'A'){
            return 1;
        } else if (value == '2'){
            return 2;
        } else if (value == '3'){
            return 3;
        } else if (value == '4'){
            return 4;
        } else if (value == '5'){
            return 5;
        } else if (value == '6'){
            return 6;
        } else if (value == '7'){
            return 7;
        } else if (value == '8'){
            return 8;
        } else if (value == '9'){
            return 9;
        } else {
            return 10;
        }
    }

    public String getStringForVal(){
        if (convert(value) < 10 && convert(value) > 1){
            char[] list = {value};
            String str = new String(list);
            return str;
        } else if (value == '0'){
            return "10";
        } else if (value == 'J'){
            return "Jack";
        } else if (value == 'Q'){
            return "Queen";
        } else if (value == 'K'){
            return "King";
        } else{
            return "Ace";
        }
    }

    //get char from int value
    //cannot return J, Q, or K. All have value 10
    public static char revert(int value){
        if (value == 1){
            return 'A';
        } else if (value == 2){
            return '2';
        } else if (value == 3){
            return '3';
        } else if (value == 4){
            return '4';
        } else if (value == 5){
            return '5';
        } else if (value == 6){
            return '6';
        } else if (value == 7){
            return '7';
        } else if (value == 8){
            return '8';
        } else if (value == 9){
            return '9';
        } else {
            return '0';
        }
    }

}
