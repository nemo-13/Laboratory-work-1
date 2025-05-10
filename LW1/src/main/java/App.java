import controller.MainController;
import model.DataModel;
import view.MainView;


public class App {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            DataModel model = new DataModel();
            MainView view = new MainView();
            new MainController(model, view);
            view.setVisible(true);
        });
    }
}