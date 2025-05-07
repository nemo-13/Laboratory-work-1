package org.mephi.b23902.ogorodnikov.lw1;

import org.mephi.b23902.ogorodnikov.lw1.controller.MainController;
import org.mephi.b23902.ogorodnikov.lw1.view.MainView;
import org.mephi.b23902.ogorodnikov.lw1.model.DataModel;

public class LW1 {
    public static void main(String[] args) {
        DataModel model = new DataModel();
        MainView view = new MainView(null);
        MainController controller = new MainController(model, view);
        view.setController(controller);
        view.setVisible(true);
    }
}
