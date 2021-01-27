package com.kehua.xml.controller.web;


import com.kehua.xml.control.textformatter.NumberFormatter;
import com.kehua.xml.utils.DataUtils;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FXMLController
public class CollectSettingTcpController {

    @FXML
    public TextField host;
    @FXML
    public TextField port;

    public void initialize() {

        host.setText("127.0.0.1");

        host.setTextFormatter(new TextFormatter<String>(change -> {
            String controlNewText = change.getControlNewText();
            if (!DataUtils.checkHostInput(controlNewText)){
                change.setText("");
            }
            return change;
        }));

        port.setTextFormatter(new NumberFormatter<>(Integer.class).build());

    }

    public String getHost() {
        return host.getText();
    }

    public Integer getPort() {
        try {
            return Integer.parseInt(port.getText());
        }catch (NumberFormatException e){
            return null;
        }
    }

    public void setHost(String host) {
        this.host.setText(host);
    }

    public void setPort(Integer port) {
        this.port.setText((port == null ? "" : port).toString());
    }
}
