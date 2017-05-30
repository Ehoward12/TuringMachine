package ads2;

public class RedrawTimer extends Thread
{
  private boolean alive, paused;
  private DrawingCanvas parent;
  
  private static final int DELAY=40;  // 25 fps
  
  public RedrawTimer(DrawingCanvas parent)
  {
    this.parent=parent;
    alive=true;
    paused=true;
    
    start();
  }
  
  public synchronized void run()
  {
    while (alive)
    {
      try
      {
        sleep(DELAY);
        if (paused)
          wait();
        if (alive)
          parent.OnRedraw();
      }
      catch (InterruptedException e) { }
    }
  }
  
  public void Pause()
  {
    paused=true;
  }
  
  public synchronized void Resume()
  {
    paused=false;
    notifyAll();
  }

  public synchronized void Kill()
  {
    alive=false;
    notifyAll();
  }
}
