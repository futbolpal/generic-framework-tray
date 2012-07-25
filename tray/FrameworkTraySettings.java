package tray;

import framework.FrameworkProperties;

public class FrameworkTraySettings
{
  private static final String CLICKS_TO_RESTORE = "framework.plugin.tray.clicks-to-restore";

  private static final String CLOSE_TO_TRAY = "framework.plugin.tray.close-to-tray";

  private static final String MINIMIZE_TO_TRAY = "framework.plugin.tray.minimize-to-tray";

  private static final String SHOW_ICON = "framework.plugin.tray.show-icon";

  private static FrameworkTraySettings this_;

  private int clicks_to_restore_;

  private boolean close_to_tray_;

  private boolean minimize_to_tray_;

  private boolean show_icon_;

  private FrameworkProperties p_;

  protected FrameworkTraySettings()
  {
    p_ = FrameworkProperties.instance();
    clicks_to_restore_ = Integer.valueOf(p_.getProperty(CLICKS_TO_RESTORE, "2"));
    close_to_tray_ = Boolean.valueOf(p_.getProperty(CLOSE_TO_TRAY, "true"));
    minimize_to_tray_ = Boolean.valueOf(p_.getProperty(MINIMIZE_TO_TRAY, "true"));
    show_icon_ = Boolean.valueOf(p_.getProperty(SHOW_ICON, "true"));
  }

  public int getClicksToRestore()
  {
    return clicks_to_restore_;
  }

  public boolean isCloseToTray()
  {
    return close_to_tray_;
  }

  public boolean isMinimizeToTray()
  {
    return minimize_to_tray_;
  }

  public boolean isIconAlwaysShowing()
  {
    return show_icon_;
  }

  public void setClicksToRestore(int c)
  {
    p_.setProperty(CLICKS_TO_RESTORE, String.valueOf(c));
    clicks_to_restore_ = c;
  }

  public void setCloseToTray(boolean flag)
  {
    p_.setProperty(CLOSE_TO_TRAY, String.valueOf(flag));
    close_to_tray_ = flag;
  }

  public void setMinimizeToTray(boolean flag)
  {
    p_.setProperty(MINIMIZE_TO_TRAY, String.valueOf(flag));
    minimize_to_tray_ = flag;
  }

  public void setIconAlwaysShowing(boolean flag)
  {
    if(show_icon_ == flag)
      return;
    p_.setProperty(SHOW_ICON, String.valueOf(flag));
    show_icon_ = flag;
    FrameworkTray.instance().showTrayIcon(show_icon_);
  }

  public static FrameworkTraySettings instance()
  {
    if(this_ == null)
    {
      this_ = new FrameworkTraySettings();
    }
    return this_;
  }
}
