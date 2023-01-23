import java.util.Random;
public class Deck {

    
    private Card[] deck;
    private static final int STANDARD_DECK_COUNT = 2;
    private Random rand = new Random();
    private int size;
    private static final int REQUIRED_CARDS_PER_HAND = 12;

    //constructors
    public Deck(int deckCount){
        if (deckCount < 7){
            deck = new Card[deckCount*52];  
        } else{
            deck = new Card[deckCount*STANDARD_DECK_COUNT];
        }
        size = deckCount*52;
        initRandDeck(deckCount);
    }

    public Deck(){
        this(STANDARD_DECK_COUNT);
    }

    //deals out one card
    public Card dealOneCard(){
        size--;
        return deck[size];
    }

    //checks to make sure deck is big enough for another hand
    //if return == false, deck is automatically reset and shuffled
    public boolean checkSize(){
        if (size>=REQUIRED_CARDS_PER_HAND){
            return true;
        } else{
            shuffle();
            size = deck.length;
            return false;
        }
    }

    //determines output for initdeck per card
    public Card determineCard(int valueNum, int suitNum){
        String suit;
        if (suitNum == 1){
            suit = "Clubs";
        } else if (suitNum == 2){
            suit = "Hearts";
        } else if (suitNum == 3){
            suit = "Spades";
        } else{
            suit = "Diamonds";
        }
        if (valueNum <= 10){
            return new Card(suit, Card.revert(valueNum));
        } else{
            if (valueNum == 11){
                return new Card(suit, 'J');
            } else if (valueNum == 12){
                return new Card(suit, 'Q');
            } else{
                return new Card(suit, 'K');
            }
        }
    }
    
    //initializes the deck
    //I want this to change the deck variable, and to be able to be called to 
    //reset the deck midgame too
    public void initRandDeck(int deckCount){
        int counter = 0;
        for (int value = 1; value < 14; value++){
            for (int suit = 1; suit < 5; suit++){
                for (int count = deckCount; count>0; count--){
                    deck[counter] = determineCard(value, suit);
                    counter++;
                }
            }
        }
        shuffle();
    }

    //does the whole shuffle process
    public void shuffle(){
        for (int i = 0; i<25; i++){
            for (int j = rand.nextInt(3); j<3; j++){
                riffle();
            }
            if (rand.nextInt(3) == 1){
                swap();
            }
            if (rand.nextInt(4) == 2 || rand.nextInt(4) == 1){
                cut();
            }
        }
    }

    //riffle-shuffles the deck
    public void riffle(){
        Card[] temp = new Card[deck.length];
        for (int i = 0; i < deck.length; i++){
            if (i%2 == 0){
                temp[i] =  deck[i/2];
            }else{
                temp[i] = deck[(deck.length/2) + i/2];
            }
        }
        deck = temp;
    }

    //cuts the deck
    public void cut(){
        Card[] temp = new Card[deck.length];
        for (int i = 0; i<deck.length; i++){
            if (i < deck.length/2){
                temp[i] = deck[i+deck.length/2];
            } else{
                temp[i] = deck[i-deck.length/2];
            }
        }
        deck = temp;
    }

    //swaps all indexes of list, maybe...
    public void swap(){
        Card temp;
        for (int i = 0; i<deck.length/2; i++){
            if (rand.nextInt(2) == 1){
                temp = deck[deck.length-1-i];
                deck[deck.length-1-i] = deck[i];
                deck[i] = temp;
            }
        }
    }

    public int[] remove(int index, int[] list){
        int[] newlist = new int[list.length-1];
        for (int i = 0; i<index; i++){
            newlist[i] = list[i];
        }
        for (int i = list.length-1; i>index; i--){
            newlist[i-1] = list[i];
        }
        return newlist;
    }

    //returns a string representation
    public String toString(){
        int counter = 1;
        String ret = "[";
        for (int i = 0; i<deck.length - 1; i++){
            ret += deck[i].getValue() + " " + deck[i].getSuit() + ",\n";
            counter++;
        }
        ret+=deck[deck.length-1].getValue() + " " + deck[deck.length-1].getSuit() + "]";
        System.out.println(counter);
        return ret;
    }


}
