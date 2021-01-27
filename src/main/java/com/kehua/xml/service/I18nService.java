package com.kehua.xml.service;

import com.kehua.xml.data.CommonDataManager;
import com.kehua.xml.model.I18n;
import com.kehua.xml.utils.AlertFactory;
import com.kehua.xml.utils.ReflectionUtil;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class I18nService {

    private Map<String, I18n> i18nCache = new HashMap<>();

    public String getLocal(String remark, String local) {
        if ("zh-CN".equals(local) || i18nCache.get(remark) == null) {
            return remark;
        }
        Object o = ReflectionUtil.getValue(i18nCache.get(remark), "remark" + upperCase(local));
        return o.toString();
    }

    public void cacheI18n() {
        CommonDataManager.I_18_NS.forEach(x -> i18nCache.put(x.getRemark(), x));
    }

    public void clearCache() {
        i18nCache.clear();
    }

    public String upperCase(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public void checkLocals(String locals) {
        for (String local : locals.split(",")) {
            if ("zh-CN".equals(local)) continue;
            CommonDataManager.I_18_NS.forEach(x -> {
                Object o = ReflectionUtil.getValue(x, "remark" + upperCase(local));
                if (o == null || StringUtils.isBlank(o.toString())) {
                    throw new RuntimeException(local + "国际化存在未配置数据");
                }
            });
        }
    }

    public I18n getExist(String remark) {
        for (I18n i18n : CommonDataManager.I_18_NS) {
            if (Objects.equals(i18n.getRemark(), remark)) {
                return i18n;
            }
        }
        return null;
    }

    @AllArgsConstructor
    private static class ExtractI18nConflictItem {
        String en;
        String pl;

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof ExtractI18nConflictItem)) {
                return false;
            }
            return Objects.equals(en, ((ExtractI18nConflictItem) obj).en) && Objects.equals(pl, ((ExtractI18nConflictItem) obj).pl);
        }

        String get(String type){
            if ("en".equals(type)){
                return en;
            }
            if ("pl".equals(type)){
                return pl;
            }
            return null;
        }
    }

    public void extractI18n() {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("请选择要打开的文件");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML", "*-*.xml"));
        stage.initModality(Modality.WINDOW_MODAL);
        List<File> files = fileChooser.showOpenMultipleDialog(stage);
        if (files == null || files.size() == 0) {
            return;
        }
        boolean hasCn = false;
        for (File file1 : files) {
            String fileName = file1.getName();
            String type = fileName.substring(fileName.lastIndexOf("-") + 1, fileName.lastIndexOf(".xml"));
            if (type.equals("cn")) {
                hasCn = true;
                break;
            }
        }
        if (!hasCn) {
            AlertFactory.createAlert("所选文件必须包含*-cn.xml");
            return;
        }
        Map<String, List<String>> allRemarks = new HashMap<>();
        files.forEach(file -> {
            List<String> remarks = new ArrayList<>();
            String fileName = file.getName();
            String type = fileName.substring(fileName.lastIndexOf("-") + 1, fileName.lastIndexOf(".xml"));
            allRemarks.put(type, remarks);
            try {
                Document document = new SAXReader().read(file);
                Element rootElement = document.getRootElement();
                addRemark(rootElement, remarks);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        });
        Map.Entry<String, List<String>> last = null;
        int size = -1;
        for (Map.Entry<String, List<String>> entry : allRemarks.entrySet()) {
            if (last == null) {
                last = entry;
                size = entry.getValue().size();
                continue;
            }
            if (last.getValue().size() != entry.getValue().size()) {
                AlertFactory.createAlert("文件[" + last.getKey() + "]与文件[" + entry.getKey() + "]点位描述数量不一致！！！");
                return;
            }
        }
        if (size == 0) {
            return;
        }
        int count = 0;
        Map<String, List<ExtractI18nConflictItem>> conflict = new HashMap<>();
        for (int i = 0; i < size; i++) {
            String remark = allRemarks.get("cn").get(i).trim();
            I18n i18n = getExist(remark);
            if (i18n == null) {
                i18n = new I18n();
                i18n.setRemark(remark);
                CommonDataManager.I_18_NS.add(i18n);
            }
            String remarkEn = null, remarkPl = null;
            if (allRemarks.get("en") != null) {
                remarkEn = allRemarks.get("en").get(i).trim();
            }
            if (allRemarks.get("pl") != null) {
                remarkPl = allRemarks.get("pl").get(i).trim();
            }
            String i18nRemarkEn = i18n.getRemarkEn();
            String i18nRemarkPl = i18n.getRemarkPl();
            //  检查冲突
            boolean isConflict = false;
            if (remarkEn != null && !remarkEn.equals(i18nRemarkEn)) {
                if (i18nRemarkEn != null) {
                    isConflict = true;
                }
                i18n.setRemarkEn(remarkEn);
            }
            if (remarkPl != null && !remarkPl.equals(i18nRemarkPl)) {
                if (i18nRemarkPl != null) {
                    isConflict = true;
                }
                i18n.setRemarkPl(remarkPl);
            }
            if (isConflict) {
                List<ExtractI18nConflictItem> list = conflict.computeIfAbsent(remark, k -> new ArrayList<>());
                ExtractI18nConflictItem item = new ExtractI18nConflictItem(remarkEn, remarkPl);
                ExtractI18nConflictItem i18nItem = new ExtractI18nConflictItem(i18nRemarkEn, i18nRemarkPl);
                if (!list.contains(item)) {
                    list.add(item);
                }
                if (!list.contains(i18nItem)) {
                    list.add(i18nItem);
                }
                count++;
            }
        }

        extractI18nConflictAlter(conflict, count, allRemarks.keySet());
    }

    private void extractI18nConflictAlter(Map<String, List<ExtractI18nConflictItem>> conflict, int count, Set<String> types) {
        String msg = "已提取" + count + "个点位翻译";
        HBox box = null;
        if (conflict.size() > 0) {
            msg += "，其中" + conflict.size() + "个点位翻译冲突";
            box = new HBox();
            box.getStyleClass().add("extract-i18n-conflict");
            box.getStylesheets().add("/css/sample.css");
            Map<String, ObservableList<Node>> children = getChildren(box, types);
            conflict.forEach((key, conf) -> {
                AtomicBoolean hasName = new AtomicBoolean(false);
                conf.forEach(item -> {
                    Map<String, Label> labels = new HashMap<>();
                    types.forEach(type -> labels.put(type, new Label(item.get(type))));
                    if (!hasName.get()) {
                        labels.get("cn").setText(key);
                        children.forEach((type, child) -> child.add(new Separator()));
                        hasName.set(true);
                    }
                    children.forEach((type, child) -> child.add(labels.get(type)));
                });
            });
        }
        AlertFactory.createAlert(msg, box, null);
    }

    private Map<String, ObservableList<Node>> getChildren(HBox box, Set<String> types) {
        Map<String, ObservableList<Node>> children = new HashMap<>();
        types.forEach(type -> {
            VBox vbox = new VBox();
            box.getChildren().add(vbox);
            ObservableList<Node> nodes = vbox.getChildren();
            nodes.add(new Label(type));
            children.put(type, nodes);
        });
        return children;
    }

    private void addRemark(Element element, List<String> remarks) {
        if (element == null) {
            return;
        }
        String value = element.attributeValue("remark");
        if (value != null) {
            remarks.add(value);
        }
        element.elements().forEach(e -> {
            addRemark((Element) e, remarks);
        });
    }
}
