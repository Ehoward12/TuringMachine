package ads2;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

public class DrawingCanvas extends JPanel implements ComponentListener
{
  private static final long ANIMATE_TIME=500;
  
  private TuringMachineInterface machine;
  
  private int curtapecell;
  private int nooftapesymbols;
  private char tapesymbols[];
  
  private BufferedImage stateimages[];
  private String statenames[];
  
  private BufferedImage backbuffer;
  private final Font fonts[];
  private FontMetrics fontmetrics[];
  private final RenderingHints renderhints;
  private final Stroke linethickness;
  private int cellsize[];
  private final Color cellcolour, headcolour;
  
  private boolean playing;
  private char animate;
  private long animstart;
  private RedrawTimer timer;
  
  public DrawingCanvas()
  {
    curtapecell=0;
    nooftapesymbols=0;
    tapesymbols=new char[10];
    
    cellsize=new int[2];
    
    playing=false;
    
    machine=new TuringMachine();
    machine.Load("InvertNumberEx.txt");
    //machine.Load("AlternateTuringMachine.txt");
    
    InitMiniTape();
    
    fonts=new Font[2];
    fontmetrics=new FontMetrics[2];
    
    fonts[0]=new Font("Arial", Font.BOLD, 36);
    fonts[1]=new Font("Arial", Font.BOLD, 20);
    renderhints=new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    renderhints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    renderhints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    linethickness=new BasicStroke(2);
    cellcolour=new Color(0xFFFCC488, true);
    headcolour=new Color(0x80006537, true);
    
    animate=TuringMachineInterface.MOVE_HEAD_NONE;
    timer=new RedrawTimer(this);
    
    addComponentListener(this);
  }
  
  private void InitMiniTape()
  {
    int count=0;
    int index=0;
    
    curtapecell=0;
    nooftapesymbols=0;
    
    TuringTapeInterface tape=machine.GetTape();
    if (tape!=null)
    {
      final char blank=tape.GetBlankSymbol();

      while (count<3 && nooftapesymbols<10000)  // if no of non-blank symbols if greater than 10,000 then this is probably an error
      {
        char symbol=tape.GetSymbol(index++);
        if (symbol==blank)
          ++count;
        else
          count=0;
        AddTapeSymbol(symbol);
      }    
    }
  }
  
  private void AddTapeSymbol(char symbol)
  {
    if (nooftapesymbols==tapesymbols.length)
    {
      char[] tmp=new char[(int) (nooftapesymbols*1.5)];
      System.arraycopy(tapesymbols, 0, tmp, 0, nooftapesymbols);
      tapesymbols=tmp;
    }
    tapesymbols[nooftapesymbols++]=symbol;
  }
  
  public void paint(Graphics graphics) 
  {    
    if (backbuffer!=null) // make sure we have a back buffer to draw to
    {
      // Draw the entire scene to the back buffer first - this stops flickering
      DrawScene((Graphics2D) backbuffer.getGraphics());

      // copy the back buffer to the main screen in one go
      graphics.drawImage(backbuffer, 0, 0, null);
      
      if (playing && animate==TuringMachine.MOVE_HEAD_NONE)
        OnExecute();
    }
  }
  
  private void DrawScene(Graphics2D graphics)
  {
    final int width=getWidth();
    final int height=getHeight();
    final TuringTapeInterface tape=machine.GetTape();
    
    long diff=0;
    if (animate!=TuringMachineInterface.MOVE_HEAD_NONE)
    {
      diff=System.currentTimeMillis()-animstart;
      if (diff>ANIMATE_TIME)
      {
        timer.Pause();
        diff=0;
        animate=TuringMachineInterface.MOVE_HEAD_NONE;
        
        if (statenames[1]!=null)
          SwapStateImages();
      }
    }
    
    int dx=0;
    int tapeoffset=0;
    if (animate==TuringMachineInterface.MOVE_HEAD_LEFT)
    {
      tapeoffset=1;
      dx=(int) ((cellsize[0]*diff)/ANIMATE_TIME);
    }
    else if (animate==TuringMachineInterface.MOVE_HEAD_RIGHT)
    {
      tapeoffset=-1;
      dx=(int) -((cellsize[0]*diff)/ANIMATE_TIME);
    }
    
    graphics.setFont(fonts[0]);
    graphics.setColor(Color.white);
    graphics.setRenderingHints(renderhints);

    graphics.fillRect(0, 0, width, height);
    
    int cx=(width-cellsize[0])/2;
    int cells=1+(cx+cellsize[0]-1)/cellsize[0];
    graphics.setStroke(linethickness);
    
    int x1=dx+cx;
    int x2=dx+cx;
    int y1=cellsize[0]/2;
    int y2=y1+fontmetrics[0].getAscent();
    
    for (int i=0; i<cells; i++)
    {
      x1-=cellsize[0];
      x2+=cellsize[0];
      graphics.setColor(cellcolour);
      graphics.fillRect(x1, y1, cellsize[0], cellsize[0]);
      graphics.fillRect(x2, y1, cellsize[0], cellsize[0]);

      graphics.setColor(Color.BLACK);
      graphics.drawRect(x1, y1, cellsize[0], cellsize[0]);
      graphics.drawRect(x2, y1, cellsize[0], cellsize[0]);
      
      int t=i+1;
      if (tape!=null)
      {
        DrawSymbol(graphics, tape.GetSymbol(tapeoffset-t), x1, y2);
        DrawSymbol(graphics, tape.GetSymbol(tapeoffset+t), x2, y2);
      }
    }
    
    x1=cx+dx;
    graphics.setColor(cellcolour);
    graphics.fillRect(x1, y1, cellsize[0], cellsize[0]);

    graphics.setColor(Color.BLACK);
    graphics.drawRect(x1, y1, cellsize[0], cellsize[0]);
    
    if (tape!=null)
      DrawSymbol(graphics, tape.GetSymbol(tapeoffset), x1, y2);

    // draw the head overlay
    graphics.setColor(headcolour);
    graphics.fillRect(cx-8, y1-8, cellsize[0]+16, cellsize[0]+16);
    graphics.setColor(Color.BLACK);
    graphics.drawRect(cx-8, y1-8, cellsize[0]+16, cellsize[0]+16);

    DrawMiniTape(graphics, y1+cellsize[0]+20, width);
    
    int drop=y1+cellsize[0]+16;
    if (statenames[1]==null)
      graphics.drawImage(stateimages[0], (width-stateimages[0].getWidth())/2, drop+(height-drop-stateimages[0].getHeight())/2, this);
    else
    {
      int offx=(width-stateimages[0].getWidth())/2;
      int tx=offx-(int) ((offx+stateimages[0].getWidth())*diff/ANIMATE_TIME);
      graphics.drawImage(stateimages[0], tx, drop+(height-drop-stateimages[0].getHeight())/2, this);

      tx=width-(int) ((offx+stateimages[0].getWidth())*diff/ANIMATE_TIME);
      graphics.drawImage(stateimages[1], tx, drop+(height-drop-stateimages[1].getHeight())/2, this);
    }
  }
  
  private void CreateStateImage(BufferedImage image)
  {
    Graphics2D graphics=(Graphics2D) image.getGraphics();
    
    graphics.setFont(fonts[0]);
    graphics.setStroke(linethickness);
    graphics.setRenderingHints(renderhints);
    
    machine.DrawCurrentState(graphics, image.getWidth(), image.getHeight());
  }
  
  private void DrawSymbol(Graphics2D graphics, char symbol, int x, int y)
  {
    int tw=fontmetrics[0].charWidth(symbol);
    graphics.drawString(""+symbol, x+(cellsize[0]-tw)/2, y);
  }
  
  private void DrawMiniTape(Graphics2D graphics, int offy, int width)
  {
    graphics.setColor(Color.black);
    graphics.setFont(fonts[1]);
    
    int x=10;
    int y=offy;
    for (int i=0; i<nooftapesymbols; i++)
    {
      if (x+cellsize[1]>width)
      {
        x=10;
        y+=cellsize[1];
      }
      
      graphics.setColor(i==curtapecell?Color.black:Color.white);
      graphics.fillRect(x, y, cellsize[1], cellsize[1]);

      graphics.setColor(Color.black);
      graphics.drawRect(x, y, cellsize[1], cellsize[1]);

      graphics.setColor(i==curtapecell?Color.white:Color.black);
      int tw=fontmetrics[1].charWidth(tapesymbols[i]);
      graphics.drawString(""+tapesymbols[i], x+(cellsize[1]-tw)/2, y+fontmetrics[1].getAscent()+2);
      
      x+=cellsize[1];
    }
  }

  // When the window is resized, we create a back buffer that is large enough
  // to hold a copy of the entire area - this will be drawn to first and then
  // copied to the screen to avoid flickering
  public void componentResized(ComponentEvent e) 
  {
    int width=getWidth();
    int height=getHeight();
    if (backbuffer==null || backbuffer.getWidth()<width || backbuffer.getHeight()<height)
    {
      backbuffer=new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
      Graphics2D graphics=(Graphics2D) backbuffer.getGraphics();
      graphics.setRenderingHints(renderhints);

      graphics.setFont(fonts[1]);
      fontmetrics[1]=graphics.getFontMetrics();
      
      graphics.setFont(fonts[0]);
      fontmetrics[0]=graphics.getFontMetrics();
      
      for (int i=0; i<2; i++)
      {
        cellsize[i]=fontmetrics[i].charWidth('M');
        if (cellsize[i]<fontmetrics[i].getHeight())
          cellsize[i]=fontmetrics[i].getHeight();
        cellsize[i]=(int) (cellsize[i]*1.1f);
      }
      
      int tw=(2*width)/3;
      int th=(2*height)/3;
      stateimages=new BufferedImage[2];
      stateimages[0]=new BufferedImage(tw, th, BufferedImage.TYPE_INT_ARGB);
      stateimages[1]=new BufferedImage(tw, th, BufferedImage.TYPE_INT_ARGB);
      
      statenames=new String[2];
      statenames[0]=machine.GetCurrentStateName();
      CreateStateImage(stateimages[0]);
    }
  }

  public void componentMoved(ComponentEvent e) { }

  public void componentShown(ComponentEvent e) { }

  public void componentHidden(ComponentEvent e) { }  
  
  public void OnExecute()
  {
    if (animate==TuringMachineInterface.MOVE_HEAD_NONE)
    {
      if (!machine.IsCurrentStateHalt())
      {
        animate=machine.ExecuteNextStep();
        
        String name=machine.GetCurrentStateName();
        if (statenames[0]!=null && !statenames[0].equalsIgnoreCase(name))
        {
          statenames[1]=name;
          CreateStateImage(stateimages[1]);
        }
                
        if (animate!=TuringMachineInterface.MOVE_HEAD_NONE)
        {
          if (animate==TuringMachineInterface.MOVE_HEAD_RIGHT)
          {
            if (curtapecell>=0)
              tapesymbols[curtapecell]=machine.GetTape().GetSymbol(-1);
            ++curtapecell;
          }
          else
          {
            if (curtapecell>=0)
              tapesymbols[curtapecell]=machine.GetTape().GetSymbol(1);
            --curtapecell;
          }
          
          animstart=System.currentTimeMillis();
          timer.Resume();
        }
        else
        {
          if (curtapecell>=0)
            tapesymbols[curtapecell]=machine.GetTape().GetSymbol(0);
          
          if (statenames[1]!=null)
            SwapStateImages();
        }
        
        if (curtapecell>nooftapesymbols)
          ++nooftapesymbols;

        if (nooftapesymbols==tapesymbols.length)
        {
          char[] tmp=new char[(int) (nooftapesymbols*1.5)];
          System.arraycopy(tapesymbols, 0, tmp, 0, nooftapesymbols);
          tapesymbols=tmp;
        }
        
        paint(getGraphics());
      }
    }
    else
      playing=false;
  }

  private void SwapStateImages()
  {
    statenames[0]=statenames[1];
    statenames[1]=null;

    BufferedImage swap=stateimages[1];
    stateimages[1]=stateimages[0];
    stateimages[0]=swap;
  }
  
  public void OnRestart()
  {
    if (animate!=TuringMachineInterface.MOVE_HEAD_NONE)
    {
      timer.Pause();
      animate=TuringMachineInterface.MOVE_HEAD_NONE;
    }
    
    playing=false;

    machine.Restart();
    statenames[0]=machine.GetCurrentStateName();
    statenames[1]=null;
    
    InitMiniTape();
    
    CreateStateImage(stateimages[0]);
    paint(getGraphics());
  }
  
  public void OnRedraw()
  {
    paint(getGraphics());
  }
  
  public void OnPlayStop()
  {
    playing^=true;
    OnExecute();
  }
}
