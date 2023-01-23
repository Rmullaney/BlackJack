import java.lang.Math;
public class Cpu implements Comparable<Cpu>{

    //most splithand variables will never be initialized, but they are there
    //just in case there are that many split hands
    //hand will have 43 indices: 0-10 belong to hand, 11-21 belong to splithand1
    //22-32 belong to splithand2, and 33-43 belong to splithand4
    protected Card[] hand;

    //constructor
    public Cpu(){
        hand = new Card[43];
    }

    public void clearHand(){
        for (int i = 0; i<43; i++){
            if (hand[i] != null){
                hand[i] = null;
            }
        }
    }

    //deals the init two cards; must be called twice
    public void dealInit(Card card){
        if (hand[0] == null){
            hand[0] = card;
        } else{
            hand[1] = card;
        }
    }

    //returns true if the player has busted
    //num is the current split - 0 for normal
    public boolean isBusted(int num){
        if (determineScore(num) == 0){
            System.out.println("You have bust!");
            return true;
        }
        return false;
    }

    //determines if the hand is splittable
    //int num is per split, num = 0 for first split
    public boolean splittable(int num){
        return hand[num*11].getValue() == hand[(num*11)+1].getValue();
    }

    //runs a split; splitCount is how many times a split has occurred; =0for first
    //by runs a split, I just mean that it sets up the card situation
    public void runSplit(int splitCount){
        Card cardSplit = hand[11*splitCount];
        hand[(11*splitCount)+1] = null;
        hand[11*(splitCount+1)] = cardSplit;
    }

    //adds a card to the player's hand
    //handNum = zero for normal; +1 for each split
    public void expandHand(Card card, int handNum){
        int startIndex = handNum*11;
        while (startIndex < 100){
            if (hand[startIndex] == null){
                hand[startIndex] = card;
                startIndex += 100;
            }
            startIndex++;
        }
    }

    //compares one player to another to see who wins
    //-1: (this) player wins
    //0: tie
    //1: (other) player wins
    //does not work with any splitting
    public int compareTo(Cpu other){
        if (this.determineScore(0) > other.determineScore(0)){
            return -1;
        } else if (this.determineScore(0) == other.determineScore(0)){
            return 0;
        } else{
            return 1;
        }
    }

    //better valueList function
    //count being if we are calling for a split hand
    public int[] determineValueList(int count){
        int i = (count + 1)*11;
        int k = 0;
        for (int j = i - 11; j < i; j++){
            if (hand[j] == null){
                k = j - (count*11);
                j += 100;
            }
        }
        int[] valueList = new int[k];
        for (int l = k-1; l >= i-11; l--){
            valueList[l] = hand[l+(count*11)].getIntValue();
        }
        return valueList;
    }

    //returns score. If bust, returns zero
    //count is for multiple hands - normal hand passes zero, splithands pass their
    //splithand number (Ex. splithand2 passes 2)
    public int determineScore(int count){
        int[] valueList;
        valueList = determineValueList(count);
        return reRoutePerAces(valueList);
    }

    //determines output based on AceCount
    public int reRoutePerAces(int[] valueList){
        int aceCount = 0;
        for (int i = 0; i<valueList.length; i++){
            if (valueList[i] == 1){
                aceCount++;
            }
        }
        if (aceCount == 0){
            return noAceLogic(valueList);
        } else if (aceCount == 1){
            return oneAceLogic(valueList);
        } else if (aceCount == 2){
            return twoPlusAceLogic(valueList, 0);
        } else if (aceCount == 3){
            return twoPlusAceLogic(valueList, 1);
        } else{
            return twoPlusAceLogic(valueList, 2);
        }
    }

    //evaluation logic for a hand w no aces
    public int noAceLogic(int[] valueList){
        int sum = 0;
        for (int i = 0; i<valueList.length; i++){
            sum += valueList[i];
        }
        if (sum > 21){
            return 0;
        }
        return sum;
    }

    //evaluation logic for a hand w one ace
    public int oneAceLogic(int[] valueList){
        int sum1 = 0;
        int sum2 = 0;
        for (int i = 0; i< valueList.length; i++){
            if (valueList[i] == 1){
                sum1 += 11;
                sum2 += 1;
            } else{
                sum1 += valueList[i];
                sum2 += valueList[i];
            }
        }
        return sumWinner(sum1, sum2);
    }

    //returns the winner of two hand total counts
    public int sumWinner(int sum1, int sum2){
        if (sum1 > 21 && sum2 > 21){
            return 0;
        } else if (sum1 > 21 && sum2 < 21){
            return sum2;
        } else if (sum1 < 21 && sum2 > 21){
            return sum1;
        } else{
            return Math.max(sum1, sum2);
        }
    }

    //evaluation logic for a hand w two aces or more
    //surplusAces = AceCount - 2
    public int twoPlusAceLogic(int[] valueList, int surplusAces){
        int sum1 = 2;
        int sum2 = 12;
        int aceCounter = 0;
        for (int i = 0; i<valueList.length; i++){
            if (valueList[i] != 1){
                sum1 += valueList[i];
                sum2 += valueList[i];
            } else{
                if (aceCounter != surplusAces){
                    aceCounter++;
                } else{
                    sum1 += 1;
                    sum2 += 1;
                }
            }
        }
        return sumWinner(sum1, sum2);
    }

    public String toString(int whichHand){
        int startIndex = (whichHand*11) + 1;
        String ret = "" + hand[startIndex-1].getStringForVal() + " of " + hand[startIndex-1].getSuit();
        for (int i = startIndex; i < 100; i++){
            if (hand[i] == null){
                i += 1000;
            } else{
                ret+=", " + hand[i].getStringForVal() + " of " + hand[i].getSuit();
            }
        }
        ret+="";
        return ret;
    }

    
}
