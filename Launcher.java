public class Launcher{

    
    public static void main(String[] args){
        System.out.println("*******WELCOME TO BLACKJACK*******\n");
        RunGame game = new RunGame(2, 1000);
        game.play();
    }
}