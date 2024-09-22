import javax.swing.*;
public class App {
    public static void main(String[] args) throws Exception {
      int boardWidth= 360;
      int boardHeight= 640;
      JFrame frame =new JFrame("FLAPPY BIRD");
      
      frame.setSize(boardWidth,boardHeight);
      frame.setLocationRelativeTo(null);
      frame.setResizable(false);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      flappybirdd flappybird=new flappybirdd();
      frame.add(flappybird);
      frame.pack();
      flappybird.requestFocus();
      frame.setVisible(true);

    }
}
