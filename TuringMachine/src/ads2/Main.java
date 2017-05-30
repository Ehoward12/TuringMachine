package ads2;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Main extends JFrame implements ActionListener, ComponentListener
{
  public static void main(String[] args)
  {
    Main tm=new Main();
    tm.setVisible(true);
  }
  
  private static final int SCREEN_WIDTH=800;
  private static final int SCREEN_HEIGHT=600;

  private DrawingCanvas canvas;
  private JButton play, execute, restart, exit;
    
  public Main()
  {
    setTitle("ADS2 Turning Maching");
    setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
    
    Dimension dim=Toolkit.getDefaultToolkit().getScreenSize();
    setLocation((dim.width-SCREEN_WIDTH)/2, (dim.height-SCREEN_HEIGHT)/2);

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    canvas=new DrawingCanvas();
    getContentPane().setLayout(new BorderLayout());


    JPanel toolbar=new JPanel();
    toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.Y_AXIS));
    play=new JButton("Play/Stop");
    play.addActionListener(this);
    toolbar.add(play);
    
    execute=new JButton("Execute");
    execute.addActionListener(this);
    toolbar.add(execute);

    restart=new JButton("Restart");
    restart.addActionListener(this);
    toolbar.add(restart);
    
    exit=new JButton("Exit");
    exit.addActionListener(this);
    toolbar.add(exit);

    getContentPane().add("Center", canvas);
    getContentPane().add("West", toolbar);

    addComponentListener(this);
  }

  public void actionPerformed(ActionEvent e) 
  {
    Object src=e.getSource();
    if (src==play)
      canvas.OnPlayStop();
    else if (src==execute)
      canvas.OnExecute();
    else if (src==restart)
      canvas.OnRestart();
    else if (src==exit)
      System.exit(0);
  }

  public void componentResized(ComponentEvent e) {}

  public void componentMoved(ComponentEvent e) {}

  public void componentShown(ComponentEvent e) {}

  public void componentHidden(ComponentEvent e) {}
  
}
