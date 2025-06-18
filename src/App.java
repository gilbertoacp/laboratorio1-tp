import javax.swing.SwingUtilities;

import ui.PanelManager;

public class App {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new PanelManager();
			}
		});	
	}
}
