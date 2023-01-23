import java.util.InputMismatchException;
import java.util.Scanner;
public class RunGame {
    
    //deck var has its own var deck that actually contains the deck.
    //should only be using deck methods to access and alter deck and its cards
    private Deck deck;
    private Player player;
    private Cpu computer;
    Scanner scan = new Scanner(System.in);

    //constructors
    public RunGame(int deckCount, int bal){
        this.deck = new Deck(deckCount);
        this.player = new Player(bal);
        this.computer = new Cpu();
    }

    public RunGame(){
        this.deck = new Deck();
        this.player = new Player();
        this.computer = new Cpu();
    }

    //checks to get an input if input is YN based
    public boolean getResponseYN(String prompt){
        System.out.print(prompt);
        String inp = scan.next().strip();
        if (!inp.equals("y") && !inp.equals("n")){
            System.out.println("Please answer 'y' or 'n'");
            return getResponseYN(prompt);
        }
        if (inp.equals("y")){
            return true;
        }else{
            return false;
        }
    }

    //gets bet size
    public void makeValidBet(){
        System.out.println("Current Balance: " + player.getBal());
        System.out.print("Enter bet size: ");
        int inp = 0;
        try{
            inp = scan.nextInt();
        } catch (InputMismatchException e){
            System.out.println("Please enter valid bet");
            makeValidBet();
        }
        if (!(player.makeBet(inp)) || inp == 0){
            System.out.println("Please enter a bet within balance");
            makeValidBet();
        }    
    }

    //runs the game
    public void play(){
        boolean playerHasMoney = true;
        boolean playerWantsToKeepPlaying = true;
        int handCounter = 0;
        while (playerHasMoney && playerWantsToKeepPlaying){
            player.clearHand();
            computer.clearHand();
            runOneHand();
            playerHasMoney = player.money();
            if (playerHasMoney && handCounter % 10 == 0){
               playerWantsToKeepPlaying = getResponseYN("Would you like to continue playing?[y/n]  ");
               System.out.println("\n");
            }
            handCounter++;
        }
        postGame(playerHasMoney);
    }

    //sleeps for num seconds
    public void sleep(int num){
        try{
            Thread.sleep(num*1000);
        }catch (Exception e){
            
        }
    }

    //determines output at end of game
    public void postGame(boolean playerHasMoney){
        if (playerHasMoney){
            int curr = player.getBal();
            int start = player.getInitBal();
            if (curr > start){
                System.out.println("You won " + (curr - start) + " dollars!");
            } else if (curr == start){
                System.out.println("You broken even!");
            } else{
                System.out.println("You lost " + (start - curr) + " dollars...");
            }
        }
        System.out.println("Thanks for playing!");
    }

    //checking decksize
    public void deckSizeCheck(){
        if (!(deck.checkSize())){
            System.out.println("Deck has been reset and reshuffled!");
        }
    }

    //dealing first two cards
    public void dealFirstTwo(){
        System.out.println("Let's Deal!");
        for (int i = 1; i < 3; i++){
            player.dealInit(deck.dealOneCard());
            computer.dealInit(deck.dealOneCard());
        }
    }

    //checks for splithand
    //num is per split, 0 for first split
    public void splitCheck(int num){
        if (player.splittable(num) && player.doubleDownPossible() && getResponseYN("Would you like to split?[y/n]  ")){
            runSplitHand(num);
        }
    }

    public void doubleDownCheck(){
        if (player.doubleDownPossible()){
            if(getResponseYN("Would you like to double down?[y/n]  ")){
                player.doubleDown();
                System.out.println("Bet is now: " + player.getCurrBet());
            }
        }
    }

    //plays one hand of blackjack
    //notBust is true when player's hand is still valid
    public void runOneHand(){
        sleep(2);
        deckSizeCheck();
        makeValidBet();
        dealFirstTwo();
        boolean cont = true;
        boolean notBust = true;
        System.out.println("Your hand is " + player.toString(0));
        doubleDownCheck();
        splitCheck(0);
        while (cont && notBust){
            sleep(1);
            if (getResponseYN("Would you like to hit?[y for hit, n for stick]  ")){
                player.expandHand(deck.dealOneCard(), 0);
            } else{
                cont = false;
            }
            System.out.println("Your hand is " + player.toString(0));
            notBust = (!(player.isBusted(0))); 
        }
        sleep(1);
        System.out.println();
        int dealerScore = dealerHand();
        checkSplitScores(dealerScore);
        postHand(dealerScore, player.determineScore(0));
    }

    //checks for split score bet evalus
    public void checkSplitScores(int dealerScore){
        if (player.getSA()){
            int[] list = player.getSplitScores();
            for (int i = 0; i<3; i++){
                if (list[i] != 100){
                    System.out.println("For split hand #" + i + ": ");
                    postHand(dealerScore, list[i]);
                }
            }
        }
    }

    //Print output per hand
    public void postHand(int dealerScore, int playerScore){
        sleep(2);
        if (playerScore == dealerScore && playerScore == 0){
            System.out.println("Both players have bust. Bets are returned");
            player.returnBet();
        } else if (playerScore == dealerScore){
            System.out.println("Player and dealer both have " + dealerScore + ". Bets are returned");
            player.returnBet();
        } else if (playerScore > dealerScore){
            System.out.println("Player has won! Bets have been won");
            player.winBet();
        } else {
            System.out.println("Player has lost...Bets have been lost");
            player.clearBet();
        }
    }

    //runs dealer's turn
    public int dealerHand(){
        sleep(2);
        int score = computer.determineScore(0);
        System.out.println("Dealer has " + computer.toString(0));
        if (score == 0){
            System.out.println("Dealer has bust!");
            return 0;
        } else if (score <= 16){
            System.out.println("Dealer must hit");
            computer.expandHand(deck.dealOneCard(), 0);
            return dealerHand();
        } else {
            System.out.println("Dealer must stand");
            return score;
        }
    }

    //called once for each split
    //num is zero for first split
    public void runSplitHand(int num){
        player.runSplit(num);
        player.expandHand(deck.dealOneCard(), num+1);
        splitCheck(1);
        boolean cont = true;
        boolean notBust = true;
        System.out.println("Split hand #" + (num + 1) + ":\n");
        while (cont && notBust){
            sleep(1);
            if (getResponseYN("Would you like to hit?[y for hit, n for stick]  ")){
                player.expandHand(deck.dealOneCard(), num);
            } else{
                cont = false;
            }
            System.out.println("Your hand is " + player.toString(num));
            notBust = (!(player.isBusted(num)));
            
        }
    }





    
    
}
