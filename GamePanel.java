import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int delay = 75;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 6;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH , SCREEN_HEIGHT));
        this.setBackground(Color.LIGHT_GRAY);
        this.setFocusable(true);
        this.addKeyListener(new myKeyAdapter());
        startGame();

    }
    public void startGame(){
        newApple();
        running = true;
        timer = new Timer(delay, this);
        timer.start();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);

    }
    public void draw(Graphics g){
        if(running) {
            for (int i = 0; i < (SCREEN_HEIGHT / UNIT_SIZE); i++) {
                g.drawLine(i*UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }
            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.BLUE);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(Color.green);
                    g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.BLACK);
            g.setFont(new Font("Ink Free",Font.BOLD,25));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("SCORE: " + applesEaten,(SCREEN_WIDTH - metrics.stringWidth("SCORE: " + applesEaten))/2,g.getFont().getSize());
        }else{
            gameOver(g);
        }
    }
    public void newApple(){
        appleX = random.nextInt((int)SCREEN_WIDTH/UNIT_SIZE) * UNIT_SIZE;
        appleY = random.nextInt((int)SCREEN_HEIGHT/UNIT_SIZE) * UNIT_SIZE;
    }
    public void move(){
        for(int i = bodyParts; i > 0 ; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        if(direction == 'U'){
            y[0] = y[0] - UNIT_SIZE;
        }else if(direction == 'D'){
            y[0] = y[0] + UNIT_SIZE;
        }else if(direction == 'R') {
            x[0] = x[0] + UNIT_SIZE;
        }else if(direction == 'L') {
            x[0] = x[0] - UNIT_SIZE;
        }
    }
    public void checkApple(){
        if(x[0] == appleX && y[0] == appleY){
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }
    public void checkCollisions(){
        //Checks if Head collided with body
        for (int i = bodyParts; i>0; i--){
            if((x[0] == x[i]) && y[0] == y[i]){
                running = false;
            }
        }
        // see if equals works better
        //Check if we hit left border
        if(x[0] < 0){
            running = false;
        }
        // right
        if(x[0] > SCREEN_WIDTH){
            running = false;
        }
        // top
        if(y[0] < 0){
            running = false;
        }
        //bottom
        if(y[0] > SCREEN_HEIGHT){
            running = false;
        }
        if(!running){
            timer.stop();
        }
    }
    public void gameOver(Graphics g){
        g.setColor(Color.BLACK);
        g.setFont(new Font("Ink Free",Font.BOLD,25));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("SCORE: " + applesEaten,(SCREEN_WIDTH - metrics1.stringWidth("SCORE: " + applesEaten))/2,g.getFont().getSize());
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free",Font.BOLD,75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("GAMEOVER",(SCREEN_WIDTH - metrics2.stringWidth("GAMEOVER"))/2,SCREEN_HEIGHT/2);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();

    }

    public class myKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(direction != 'R'){
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L'){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U'){
                        direction = 'D';
                    }
                    break;
            }

        }

    }

}
