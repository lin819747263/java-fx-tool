package com.kehua.xml;

import de.felixroske.jfxsupport.SplashScreen;
import javafx.scene.Parent;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.springframework.stereotype.Component;

@Component
public class CustomSplash extends SplashScreen {


    @Override
    public Parent getParent() {
        final ImageView imageView = new ImageView(getClass().getResource(getImagePath()).toExternalForm());

        final VBox vbox = new VBox();
        vbox.getChildren().addAll(imageView);

        return vbox;
    }

    @Override
    public String getImagePath() {
        return "/splash.jpg";
    }

    @Override
    public boolean visible() {
        return true;
    }
}
