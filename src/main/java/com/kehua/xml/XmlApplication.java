package com.kehua.xml;

import com.kehua.xml.view.HomeView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author admin
 */
@SpringBootApplication
public class XmlApplication extends AbstractJavaFxApplicationSupport {

    public static void main(String[] args) {
        // 参数分别是Application的主类，主界面的UI类，闪屏对象还有args
        // 不想要自定义闪屏的可以调用另一个不带闪屏对象的launch方法
            launch(XmlApplication.class, HomeView.class, new CustomSplash(), args);
    }
}