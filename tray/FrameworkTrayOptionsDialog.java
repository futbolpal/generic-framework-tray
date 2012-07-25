package tray;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import basic_gui.BasicDialog;

public class FrameworkTrayOptionsDialog extends BasicDialog
{
  private static final String TITLE = "FrameworkTray Options";

  private static final int WIDTH = 500;

  private static final int HEIGHT = 500;

  private static final String INSTRUCTIONS = "Configure FrameworkTray options";
  
  private JCheckBox close_;
  private JCheckBox minimize_;
  private JCheckBox show_icon_;
  private JSpinner clicks_;

  public FrameworkTrayOptionsDialog()
  {
    super(TITLE, WIDTH, HEIGHT, INSTRUCTIONS);

    close_ = new JCheckBox("When I click the close button, minimize to System Tray");
    close_.setSelected(FrameworkTraySettings.instance().isCloseToTray());
    this.addComponent(close_, 10, 10);

    minimize_ = new JCheckBox("When I click the minimize button, minimize to System Tray");
    minimize_.setSelected(FrameworkTraySettings.instance().isMinimizeToTray());
    this.addComponent(minimize_, 10, 40);
    
    show_icon_ = new JCheckBox("Always show framework icon in System Tray");
    show_icon_.setSelected(FrameworkTraySettings.instance().isIconAlwaysShowing());
    this.addComponent(show_icon_,10,70);
    
    int min = 1;
    int max = 5;
    int step = 1;
    int initValue = FrameworkTraySettings.instance().getClicksToRestore();
    SpinnerModel model = new SpinnerNumberModel(initValue, min, max, step);
    clicks_ = new JSpinner(model);
    this.addFormItem("Clicks to restore",clicks_,100,100);

    JButton ok = new JButton("OK");
    ok.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        apply();
        dispose();        
      }
    });
    JButton cancel = new JButton("Cancel");
    cancel.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        dispose();
      }
    });
    
    this.addButton(cancel,RIGHT_ALIGNMENT,false);
    this.addButton(ok,RIGHT_ALIGNMENT,true);
  }
  
  public void apply()
  {
    FrameworkTraySettings.instance().setCloseToTray(close_.isSelected());
    FrameworkTraySettings.instance().setMinimizeToTray(minimize_.isSelected());
    FrameworkTraySettings.instance().setClicksToRestore((Integer)clicks_.getValue());    
    FrameworkTraySettings.instance().setIconAlwaysShowing(show_icon_.isSelected());
  }
}
