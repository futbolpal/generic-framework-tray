package tray;

import java.awt.AWTException;
import java.awt.Font;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;

import launcher.Launcher;
import module_system.Module;
import basic_gui.BasicDialog;
import framework.ui.Window;
import framework.ui.WindowMenuBar;

public class FrameworkTray implements Module
{
  private static FrameworkTray this_;

  private Window window_;

  private SystemTray tray_;

  private TrayIcon icon_;

  private FrameworkTray()
  {
    window_ = Launcher.getFramework().getWindow();
    if(window_ == null)
    {
      throw new NullPointerException();
    }

    // Remove listeners
    for(WindowListener l : window_.getWindowListeners())
    {
      window_.removeWindowListener(l);
    }

    // Add own listener
    window_.addWindowListener(new WindowAdapter()
    {
      public void windowIconified(WindowEvent e)
      {
        if(FrameworkTraySettings.instance().isMinimizeToTray())
        {
          toSystemTray();
        }
      }

      public void windowClosing(WindowEvent e)
      {
        if(FrameworkTraySettings.instance().isCloseToTray())
        {
          toSystemTray();
        }
        else
        {
          Launcher.getFramework().close();
        }
      }
    });

    //
    // Construct popup menu
    //
    PopupMenu menu = new PopupMenu();
    Font font = new JLabel().getFont();

    MenuItem options = new MenuItem("Tray Options...");
    options.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        new FrameworkTrayOptionsDialog();
      }
    });
    options.setFont(font);
    menu.add(options);

    menu.addSeparator();

    MenuItem restore = new MenuItem("Restore Window");
    restore.setFont(font);
    menu.add(restore);

    menu.addSeparator();

    MenuItem exit = new MenuItem("Exit");
    exit.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        Launcher.getFramework().close();
      }
    });
    exit.setFont(font);
    menu.add(exit);
    Image img = window_.getIconImage();
    if(img == null)
    {
      img = Toolkit.getDefaultToolkit().getImage(FrameworkTray.class.getResource("resources/tray.png"));
    }

    //
    // Configure Icon
    //
    tray_ = SystemTray.getSystemTray();
    icon_ = new TrayIcon(img, Launcher.getFramework().getName(), menu);
    icon_.setImageAutoSize(true);
    icon_.addMouseListener(new MouseAdapter()
    {
      public void mouseClicked(MouseEvent e)
      {
        if(SwingUtilities.isLeftMouseButton(e))
        {
          if(e.getClickCount() == FrameworkTraySettings.instance().getClicksToRestore())
          {
            fromSystemTray();
          }
        }
        if(SwingUtilities.isRightMouseButton(e))
        {
        }
      }
    });

    if(FrameworkTraySettings.instance().isIconAlwaysShowing())
    {
      try
      {
        tray_.add(icon_);
      }
      catch(AWTException err)
      {
        err.printStackTrace();
      }
    }

    //
    // Add Tray Option... to options menu in menu bar. If options menu does not
    // exist, create it
    //
    WindowMenuBar bar = window_.getWindowMenuBar();
    if(bar != null)
    {
      JMenu woptions = bar.getOptionsMenu();
      int length = woptions.getMenuComponentCount();
      if(length != 0 && ! (woptions.getMenuComponent(length - 1) instanceof JSeparator))
      {
        woptions.add(new JSeparator());
      }
      JMenuItem toptions = new JMenuItem("Tray Options...");
      toptions.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          new FrameworkTrayOptionsDialog().display();
        }
      });
      woptions.add(toptions);
    }
  }

  public void update()
  {
  }

  public boolean checkForUpdate()
  {
    return false;
  }

  public String getType()
  {
    return "Plugin";
  }

  public boolean showTrayIcon(boolean flag)
  {
    if(flag)
    {
      try
      {
        tray_.add(icon_);
        return true;
      }
      catch(AWTException e)
      {
        return false;
      }
    }
    else
    {
      tray_.remove(icon_);
      return true;
    }
  }

  public void toSystemTray()
  {
    window_.setVisible(false);

    if( ! FrameworkTraySettings.instance().isIconAlwaysShowing())
    {
      showTrayIcon(true);
    }
  }

  public void fromSystemTray()
  {
    window_.setVisible(true);
    if( ! FrameworkTraySettings.instance().isIconAlwaysShowing())
    {
      showTrayIcon(false);
    }
  }

  public void close()
  {
  }

  public BasicDialog getOptionsDialog()
  {
    return new FrameworkTrayOptionsDialog();
  }

  public static FrameworkTray instance()
  {
    if(this_ == null)
    {
      this_ = new FrameworkTray();
    }
    return this_;
  }
}
