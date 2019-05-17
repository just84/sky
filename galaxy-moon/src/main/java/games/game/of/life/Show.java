package games.game.of.life;

import javax.swing.*;
import java.awt.*;

public class Show extends JFrame {

    private int [][] data = null;
    private int size = 0;
    private static int length = 2;

    public Show(int size){
        setSize(size * length , size * length+20);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        add(new JPanel(){
            @Override
            public void paint(Graphics g){
                if(data == null){
                    return;
                }

                super.paint(g);
                g.setColor(Color.BLACK);

                int j = 0;
                for(int [] dat : data){
                    int i = 0;
                    for (int da : dat){
                        if(da == 0){
                            draw0(g,i,j);
                        } else {
                            draw1(g,i,j);
                        }
                        i++;
                    }
                    j++;
                }
            }
        });
        setVisible(true);
    }

    public void paintData(int[][] data) {
        this.data = data;
        repaint();
    }

    private void draw0(Graphics g, int i, int j){
        // 画普通矩形框
        g.drawRect(i * length, j * length, length, length);
    }

    private void draw1(Graphics g, int i, int j){
        // 填充普通矩形
        g.drawRect( i * length, j * length, length, length);
        g.fillRect( i * length, j * length, length, length);
    }
}
