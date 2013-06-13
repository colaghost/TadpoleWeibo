
package org.tadpoleweibo.framework;

import android.widget.ImageButton;

public interface INavBar {
    public INavBar setTitle(String title);

    public INavBar setListener(NavBarListener l);

    public ImageButton getBtnLeft();

    public ImageButton getBtnRight();

}
