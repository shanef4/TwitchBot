import javax.swing.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.*;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class YouTubeViewer extends Application {

	private JFrame yframe;
	private final JFXPanel fxPanel = new JFXPanel();
	private WebView webview;
	
	public YouTubeViewer(){
		yframe = new JFrame("YouTube Frame");
		yframe.add(fxPanel);
		yframe.setSize(1280, 720);
		yframe.setVisible(true);
		yframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		Platform.runLater(() -> {
			webview = new WebView();
			webview.setPrefSize(1280, 720);
			fxPanel.setScene(new Scene(webview));
		});
	}
	
	public void loadSong(String c){
		// Creation of scene and future interactions with JFXPanel
				// should take place on the JavaFX Application Thread
				Platform.runLater(() -> {
					webview.getEngine().load(
							"http://www.youtube.com/embed/" + c +"?autoplay=1"
							);
				});
	}
	
	public void killFrame(){
		Platform.runLater(() -> {
			webview.getEngine().load(
					"http://www.google.com"
					);
		});
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub

	}
}