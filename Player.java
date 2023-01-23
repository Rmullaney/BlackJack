public class Player extends Cpu{
    
    private int bal;
    private static final int DEFAULT_BAL = 100;
    private int initialBal;
    private int currentBet = 0;
    private int[] splitScores = {100, 100, 100};
    private boolean splitsActive = false;

    public Player(){
        this(DEFAULT_BAL);
    }

    public Player(int bal){
        if (bal > 0){
            this.bal = bal; 
            this.initialBal = bal;
        } else{
            this.bal = DEFAULT_BAL;
            this.initialBal = DEFAULT_BAL;
        }
    }

    public boolean editBal(int change){
        if (bal + change < 0){
            return false;
        }
        bal += change;
        return true;
    }

    public int getBal(){
        return this.bal;
    }

    public int getInitBal(){
        return this.initialBal;
    }

    public int getCurrBet(){
        return this.currentBet;
    }

    //returns false if bal is 0
    public boolean money(){
        return (bal != 0);
    }

    //returns true if bet goes through, false if bet is larger than bal
    public boolean makeBet(int sum){
        if (editBal(-sum)){
            currentBet += sum;
            return true;
        }
        return false;
    }

    //returns true if bet goes through, false if double down not possible w sum
    public boolean doubleDown(){
        return makeBet(currentBet);
    }

    //returns true if a double down if possible
    public boolean doubleDownPossible(){
        return (currentBet <= bal);
    }

    public void clearBet(){
        this.currentBet = 0;
    }

    public void returnBet(){
        bal += currentBet;
        clearBet();
    }

    public void winBet(){
        bal += currentBet*2;
        clearBet();
    }

    public int[] getSplitScores(){
        return splitScores;
    }

    public boolean getSA(){
        return this.splitsActive;
    }

    public void appendToSplitScores(int score){
        for (int i = 0; i < 3; i++){
            if (splitScores[i] == 100){
                splitScores[i] = score;
            }
        }
        splitsActive = true;
    }

    public void resetSplitScors(){
        for (int i = 0; i<3; i++){
            splitScores[i] = 100;
        }
        splitsActive = false;
    }

}
