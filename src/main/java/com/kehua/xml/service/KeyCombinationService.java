package com.kehua.xml.service;

import com.kehua.xml.utils.AlertFactory;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KeyCombinationService {

    @Autowired
    private FileService fileService;

    public void initKeyCombination(Scene scene){
        KeyCodeCombination kc1 = new KeyCodeCombination(KeyCode.S,KeyCodeCombination.CONTROL_DOWN);
        scene.getAccelerators().put(kc1, () -> {
            try {
                fileService.save();
            }catch (Exception e) {
                AlertFactory.createAlert("文件保存失败" + e.getMessage());
            }
        });
    }

}
