import java.awt.*;
import java.awt.event.*;
import java.nio.channels.Pipe;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class flappybirdd extends JPanel implements ActionListener, KeyListener {
    int boardWidth = 360;
    int boardHeight = 640;
    // images
    Image backgroundImg;
    Image birdImg;
    Image topPipeImg;
    Image bottomPipeImg;
    // bird
    int birdx = boardWidth / 2;
    int birdy = boardHeight / 2;
    int birdWidth = 34;
    int birdHeight = 24;

    class Bird {
        int x = birdx;
        int y = birdy;
        int width = birdWidth;
        int height = birdHeight;
        Image img;

        Bird(Image img) {
            this.img = img;
        }
    }

    // pipes
    int pipeX = boardWidth;
    int pipeY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;

    class pipe {
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean passed = false;

        pipe(Image img) {
            this.img = img;

        }

    
    }

    // game logic
    Bird bird;
    int velocityX = -4; // move pipes to the left speed(simulate bird moving right)
    Timer gameLoop;
    int velocityY = 0; // move bird up and down
    int gravity = 1;
    ArrayList<pipe> pipes;
    Timer placepipeTimer;
    Random random=new Random();
    boolean gameover=false;
    double score = 0;

    flappybirdd() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setFocusable(true);
        addKeyListener(this);
        // setBackground(Color.blue);
        backgroundImg = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();
        // bird
        bird = new Bird(birdImg);
        pipes = new ArrayList<pipe>();
        // placepipestimer
        placepipeTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placepipe();
            }
        });
        placepipeTimer.start();
        // game timer
        gameLoop = new Timer(1000 / 60, this);
        gameLoop.start();

    }

    public void placepipe() {
        int randompipeY=(int) (pipeY - pipeHeight/4 - Math.random()*(pipeHeight/2));
int openspace=boardHeight/4;
        pipe topPipe = new pipe(topPipeImg);
        topPipe.y=randompipeY;
        pipes.add(topPipe);
        //bottompipe                  
        pipe bottomPipe = new pipe(bottomPipeImg);
        bottomPipe.y=topPipe.y+openspace+pipeHeight;
        pipes.add(bottomPipe);   
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, this.boardWidth, this.boardHeight, null);
        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);
         
   
    for(int i = 0;i<pipes.size();i++)
    {
        pipe pipe = pipes.get(i);
        g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
    }
    g.setColor(Color.white);
    g.setFont(new Font("Arial",Font.PLAIN,32));
    if (gameover) {
        g.drawString("Game Over: " + String.valueOf((int) score), 10, 35);
    }
    else {
        g.drawString(String.valueOf((int) score), 10, 35);
    }
    
}

    public void move(){
        velocityY+=gravity;
        bird.y += velocityY ;
        bird.y = Math.max(bird.y,0);
        //pipes
        for(int i=0;i<pipes.size();i++){
            pipe pipe=pipes.get(i);
            pipe.x+=velocityX;
            if(!pipe.passed && bird.x> pipe.x+ pipe.width){
                score += 0.5;
                pipe.passed =true;
            
            }
            if(collision(bird,pipe)){
                gameover=true;
            }
        }
        if(bird.y>boardHeight){
            gameover=true;
        }
    }
    public boolean collision(Bird a , pipe b){
        return a.x < b.x + b.width &&//a's top left corner doesn't reach b's top right corner
        a.x  + a.width > b.x &&//a's top right corner passes b's top left corner
        a.y < b.y+ b.width && //a's top left corner doesn't reach b's bottom left corner
        a.y  + a.width > b.y; //a's bottom left corner passes b's top left corner
        


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();                        
        repaint();
        if(gameover){
            placepipeTimer.stop();
            gameLoop.stop();

        }
    }

    @Override
     public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                // System.out.println("JUMP!");
                velocityY = -9;
                if(gameover){
                    bird.y=birdy;
                    velocityY=0;
                    pipes.clear();
                    score=0;
                    gameover=false;
                    gameLoop.start();
                    placepipeTimer.start();

                }

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
}

    @Override
    public void keyTyped(KeyEvent e) {
    }

}