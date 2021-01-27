package com.kehua.xml.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.kehua.xml.data.AppTableDataManager;
import com.kehua.xml.data.CommonDataManager;
import com.kehua.xml.data.GloableSetting;
import com.kehua.xml.exception.CheckException;
import com.kehua.xml.model.Device;
import com.kehua.xml.model.I18n;
import com.kehua.xml.model.app.*;
import com.kehua.xml.utils.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import javafx.util.Pair;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AppFileService {

    @Autowired
    TableDataService tableDataService;
    @Autowired
    AppI18nService appI18nService;
    @Autowired
    I18nService i18nService;

    public void open() {
        if(!beforeOpenCheck()) {
            return;
        }

        File file = UIFactory.chooseFile();
        if (file != null) {
            Boolean res = checkFile(file);
            if (!res) {
                throw new RuntimeException("文件格式有误，请重新选择");
            }
            CommonDataManager.currentFile = file;
            openFile(file);
            AlertFactory.createAlert("文件打开成功");
        }
    }

    private Boolean beforeOpenCheck(){
        if(AppTableDataManager.DEVICES.size() != 0){
            Optional<ButtonType> result = AlertFactory.createAlert("打开文件会导致当前文件丢失，请确认保存", Alert.AlertType.CONFIRMATION);
            if(result.isPresent() && result.get().equals(ButtonType.CANCEL)){
                return false;
            }
        }
        return true;
    }

    public void openFile(File file){

        List<Device> device = EasyExcel.read(file).registerConverter(new SimpleStringPropertyConvert()).registerConverter(new SimpleBooleanPropertyConvert()).registerConverter(new SimpleIntegerPropertyConvert()).head(Device.class).sheet("设备").doReadSync();
        List<AppBlockPoint> blocks = EasyExcel.read(file).registerConverter(new SimpleStringPropertyConvert()).registerConverter(new SimpleBooleanPropertyConvert()).registerConverter(new SimpleIntegerPropertyConvert()).head(AppBlockPoint.class).sheet("查询指令").doReadSync();
        List<AppYxPoint> yxList = EasyExcel.read(file).registerConverter(new SimpleStringPropertyConvert()).registerConverter(new SimpleBooleanPropertyConvert()).registerConverter(new SimpleIntegerPropertyConvert()).head(AppYxPoint.class).sheet("YX").doReadSync();
        List<AppYcPoint> ycList = EasyExcel.read(file).registerConverter(new SimpleStringPropertyConvert()).registerConverter(new SimpleBooleanPropertyConvert()).registerConverter(new SimpleIntegerPropertyConvert()).head(AppYcPoint.class).sheet("YC").doReadSync();
        List<AppYkPoint> ykList = EasyExcel.read(file).registerConverter(new SimpleStringPropertyConvert()).registerConverter(new SimpleBooleanPropertyConvert()).registerConverter(new SimpleIntegerPropertyConvert()).head(AppYkPoint.class).sheet("YK").doReadSync();
        List<RecordPoint> records = EasyExcel.read(file).registerConverter(new SimpleStringPropertyConvert()).registerConverter(new SimpleBooleanPropertyConvert()).registerConverter(new SimpleIntegerPropertyConvert()).head(RecordPoint.class).sheet("记录").doReadSync();
        List<I18n> i18ns = EasyExcel.read(file).registerConverter(new SimpleStringPropertyConvert()).registerConverter(new SimpleBooleanPropertyConvert()).registerConverter(new SimpleIntegerPropertyConvert()).head(I18n.class).sheet("国际化").doReadSync();

        tableDataService.clearAllAppData();

        AppTableDataManager.DEVICES.addAll(device);
        AppTableDataManager.BLOCKS.addAll(blocks);
        AppTableDataManager.YX_POINTS.addAll(yxList);
        AppTableDataManager.YC_POINTS.addAll(ycList);
        AppTableDataManager.YK_POINTS.addAll(ykList);
        AppTableDataManager.RECORDS.addAll(records);
        CommonDataManager.I_18_NS.addAll(i18ns);
    }

    public void save(){
        File file = CommonDataManager.currentFile;
        if (file == null) {
            String fileName = "新建文件";
            if(AppTableDataManager.DEVICES.size() != 0) {
                fileName = AppTableDataManager.DEVICES.get(0).getProtocolName();
            }
            file = UIFactory.saveFile(fileName);
            if (file == null) return;
            CommonDataManager.currentFile = file;
        }
        saveFile(file);
    }

    public void saveAs(){
        String fileName = "新建文件";
        if(AppTableDataManager.DEVICES.size() != 0) {
            fileName = AppTableDataManager.DEVICES.get(0).getProtocolName();
        }

        File file = UIFactory.saveFile(fileName);
        if (file == null) return;

        saveFile(file);
        CommonDataManager.currentFile = file;
        AlertFactory.createAlert("文件保存成功");
    }

    public void saveFile(File file){
        ExcelWriter excelWriter = null;
        try {
            // 这里 指定文件
            excelWriter = EasyExcel.write(file)
                    .registerConverter(new SimpleStringPropertyConvert())
                    .registerConverter(new SimpleIntegerPropertyConvert())
                    .registerConverter(new SimpleBooleanPropertyConvert()).build();

            WriteSheet deviceSheet = EasyExcel.writerSheet( "设备").head(Device.class).build();
            WriteSheet blockSheet = EasyExcel.writerSheet( "查询指令").head(AppBlockPoint.class).build();
            WriteSheet yxSheet = EasyExcel.writerSheet( "YX").head(AppYxPoint.class).build();
            WriteSheet ycSheet = EasyExcel.writerSheet( "YC").head(AppYcPoint.class).build();
            WriteSheet ykSheet = EasyExcel.writerSheet( "YK").head(AppYkPoint.class).build();
            WriteSheet recordSheet = EasyExcel.writerSheet( "记录").head(RecordPoint.class).build();
            WriteSheet i18nSheet = EasyExcel.writerSheet( "国际化").head(I18n.class).build();

            List<Device> deviceData = new ArrayList<>(AppTableDataManager.DEVICES);
            List<AppBlockPoint> blockData = new ArrayList<>(AppTableDataManager.BLOCKS);
            List<AppYxPoint> yxData = new ArrayList<>(AppTableDataManager.YX_POINTS);
            List<AppYcPoint> ycData = new ArrayList<>(AppTableDataManager.YC_POINTS);
            List<AppYkPoint> ykData = new ArrayList<>(AppTableDataManager.YK_POINTS);
            List<RecordPoint> recordData = new ArrayList<>(AppTableDataManager.RECORDS);
            List<I18n> i18nData = new ArrayList<>(CommonDataManager.I_18_NS);

            excelWriter.write(deviceData, deviceSheet);
            excelWriter.write(blockData, blockSheet);
            excelWriter.write(yxData, yxSheet);
            excelWriter.write(ycData, ycSheet);
            excelWriter.write(ykData, ykSheet);
            excelWriter.write(i18nData, i18nSheet);
            excelWriter.write(recordData, recordSheet);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    public Boolean checkFile(File file) {
        Set<String> set = new HashSet<>();
        List<ReadSheet> sheets = EasyExcel.read(file).build().excelExecutor().sheetList();
        for(ReadSheet sheet : sheets){
            set.add(sheet.getSheetName());
        }
        return set.contains("设备") && set.contains("查询指令")&&set.contains("YX") &&set.contains("YC")&&
                set.contains("YK");
    }

    public void exportZip() {
        checkBlocks();
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        GridPane grid = UIFactory.createBaseGridPane();

        List<String> types = DictUtil.getDictByType("语言").keySet().stream().map(Object::toString).collect(Collectors.toList());

        HBox hBox = new HBox();
        for(String s : types){

            CheckBox checkBox = new CheckBox(s);
            checkBox.setStyle("-fx-padding: 3px 8px;");
            hBox.getChildren().add(checkBox);
            if("中文".equals(s)){
                checkBox.setSelected(true);
                checkBox.setDisable(true);
            }
        }

        grid.add(new Label("请选择xml导出的语言"), 0, 0);
        grid.add(hBox, 0, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Optional result = dialog.showAndWait();

        if (result.get() == ButtonType.OK) {
            String fileName = AppTableDataManager.DEVICES.get(0).getProtocolName();
            File file = UIFactory.saveFile(fileName + ".zip");
            if (file == null) return;
            try {
                String locals = getLocals(hBox);
                if (StringUtils.isBlank(locals) || locals.split(",").length == 0) {
                    AlertFactory.createAlert("国际化语言不能为空");
                    return;
                }
                i18nService.checkLocals(locals);
                exportZip(file, locals);
                AlertFactory.createAlert("导出成功");
            } catch (Exception e) {
                AlertFactory.createAlert(e.getMessage());
            }
        }

    }

    private void checkBlocks(){
        List<AppBlockPoint> blockPoints = new ArrayList<>(AppTableDataManager.BLOCKS);

        checkBlock(blockPoints);
    }

    private void checkBlock(List<AppBlockPoint> blockPoints) {
        blockPoints.forEach(x -> {
            if(StringUtils.isBlank(x.getModule()) || StringUtils.isBlank(x.getUserType())){
                throw new CheckException("查询指令模块或者用户类型不能为空");
            }
        });
    }

    private String getLocals(HBox hBox) {
        StringBuilder sb = new StringBuilder();
        for (Node box : hBox.getChildren()) {
            CheckBox checkBox = (CheckBox) box;
            if (checkBox.isSelected()) {
                sb.append(DictUtil.getDictByKey("语言", checkBox.getText()));
                sb.append(",");
            }
        }
        return sb.length() == 0 ? "" : sb.deleteCharAt(sb.length() - 1).toString();
    }


    private void exportZip(File zipFile,String locals) throws IOException {
        try {
            File file = zipFile.getParentFile();
            List<String> urls = new ArrayList<>();
            i18nService.cacheI18n();

            for(String local : locals.split(",")){
                urls.add(exportXml(file, local));
            }

            FileUtil.zipFiles(zipFile, urls);
            for(String url : urls){
                FileUtil.deleteFile(url);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("文件导出错误");
        }finally {
            i18nService.clearCache();
        }
    }

    private String exportXml(File file,String local) throws IOException {
        Document doc = DocumentHelper.createDocument();
        Element root = doc.addElement("ROOT");
        Element device = root.addElement("device");
        Element block = root.addElement("BLOCK");
        Element yx = root.addElement("YX");
        Element yc = root.addElement("YC");
        Element yk = root.addElement("YK");
        Element group = root.addElement("GROUP");
        Element record = root.addElement("RECORD");

        List<Device> deviceData = new ArrayList<>(AppTableDataManager.DEVICES);
        List<AppBlockPoint> blockData = new ArrayList<>(AppTableDataManager.BLOCKS);
        List<AppYxPoint> yxData = new ArrayList<>(AppTableDataManager.YX_POINTS);
        List<AppYcPoint> ycData = new ArrayList<>(AppTableDataManager.YC_POINTS);
        List<AppYkPoint> ykData = new ArrayList<>(AppTableDataManager.YK_POINTS);
        List<Group> groups = new ArrayList<>(AppTableDataManager.GROUPS);
        List<RecordPoint> recordData = new ArrayList<>(AppTableDataManager.RECORDS);

        buildDevice(device, deviceData.get(0), local);
        buildBlock(block, blockData);
        buildYx(yx, yxData, local);
        buildYc(yc, ycData, local);
        buildYk(yk, ykData, local);
        buildGroup(group, groups, local);
        buildRecord(record, recordData, local);

        return writeToXml(doc, file, local);
    }

    private void buildGroup(Element group, List<Group> groupPoints, String local) {
        groupPoints.forEach(x -> {
            Element point1 = group.addElement("point");
            point1.addAttribute("key", x.getName());
            point1.addAttribute("name",i18nService.getLocal(x.getKey(), local));
            point1.addAttribute("sort",x.getSort());
        });
    }

    private void buildRecord(Element record, List<RecordPoint> recordData, String local) {
        Map<String, List<RecordPoint>> recordType = recordData.stream().collect(Collectors.groupingBy(RecordPoint :: getRecordType));
        recordType.forEach((k,v) -> {
            Element point = record.addElement(k);
            buildRecordType(point, v, local);
        });

    }

    private void buildRecordType(Element point, List<RecordPoint> v, String local) {
        for (RecordPoint data : v) {
            Element point1 = point.addElement("point");
            point1.addAttribute("code",data.getCode());
            point1.addAttribute("regType",data.getRegType());
            point1.addAttribute("ratio",data.getRatio());
            point1.addAttribute("remark",i18nService.getLocal(data.getRemark(), local));
            point1.addAttribute("unit",data.getUnit() == null? "" : data.getUnit());
            point1.addAttribute("userDefined1",data.getUserDefined1() == null? "" : i18nService.getLocal(data.getUserDefined1(), local));
            point1.addAttribute("userDefined2",data.getUserDefined2() == null? "" : data.getUserDefined2());
        }

    }

    private static String writeToXml(Document doc,File file, String local) throws IOException {
        String fileName = AppTableDataManager.DEVICES.get(0).getProtocolName();
        File xmlfile = new File(file.getAbsolutePath() + "/" + fileName + "_" +local + ".xml");
        if(!xmlfile.exists()){
            xmlfile.createNewFile();
        }
        try(FileOutputStream fos = new FileOutputStream(xmlfile)){
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("utf-8");
            XMLWriter writer =  new XMLWriter(fos,format);
            writer.write(doc);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return xmlfile.getAbsolutePath();
    }

    private void buildDevice( Element device, Device deviceData, String local) {
        device.addAttribute("templetCode", deviceData.getTemplateCode() == null? "" : deviceData.getTemplateCode());
        device.addAttribute("protocolType", deviceData.getProtocolType() == null? "": deviceData.getProtocolType());
        device.addAttribute("language", local);
        device.addAttribute("protocolName", i18nService.getLocal(deviceData.getProtocolName(),local));
        device.addAttribute("describe", deviceData.getDescribe() == null? "" : deviceData.getDescribe());
        device.addAttribute("devType", deviceData.getDevType() == null? "" : deviceData.getDevType());
        device.addAttribute("maker", deviceData.getMaker() == null? "" : deviceData.getMaker());
        device.addAttribute("model", deviceData.getModel() == null? "" : deviceData.getModel());

    }

    private void buildBlock(Element block, List<AppBlockPoint> blockData) {
        for (AppBlockPoint block1 : blockData) {
            Element b = block.addElement("point");
            b.addAttribute("funcode", block1.getFuncode());
            b.addAttribute("addr", block1.getAddr().toString());
            b.addAttribute("regCount", block1.getRegCount().toString());
            b.addAttribute("queryType", block1.getQueryType());
            b.addAttribute("module", block1.getModule());
            b.addAttribute("userType", block1.getUserType());
        }
    }

    private void buildYx(Element yx, List<AppYxPoint> yxList, String local) {
        Collections.sort(yxList);
        for (AppYxPoint data : yxList) {
            Element point = yx.addElement("point");
            point.addAttribute("funcode",data.getFunCode());
            point.addAttribute("addr", String.valueOf(data.getAddr()));
            point.addAttribute("regType",data.getRegType());
            point.addAttribute("regParam",data.getRegParam());
            point.addAttribute("endian",data.getEndian());
            point.addAttribute("ratio",data.getRatio());
            point.addAttribute("offset",data.getOffset());
            point.addAttribute("property","YX" + data.getAddr() + "_" + data.getRegParam().split(",")[0]);
            point.addAttribute("remark",i18nService.getLocal(data.getRemark(), local));
            point.addAttribute("levelNo", data.getLevelNo());
            point.addAttribute("levelType",data.getLevelType());
            point.addAttribute("userDefined1",data.getUserDefined1() == null? "" : i18nService.getLocal(data.getUserDefined1(), local));
            point.addAttribute("userDefined2",data.getUserDefined2() == null? "" : data.getUserDefined2());
            point.addAttribute("sort",data.getSort() == null? "" : data.getSort());
            point.addAttribute("userType",data.getUserType());
            point.addAttribute("group",data.getGroup() == null? "" : data.getGroup());
        }
    }

    private void buildYc(Element yc, List<AppYcPoint> ycList, String local) {

        Collections.sort(ycList);
        for (AppYcPoint data : ycList) {
            String unit = data.getUnit() == null? "" : "(" + data.getUnit() +")";

            if(data.getUserDefined1() != null && data.getUserDefined1().split(",").length > 1){
                int i = 1;
                for(String s : data.getUserDefined1().split(",")){
                    Element point = yc.addElement("point");
                    point.addAttribute("funcode",data.getFunCode());
                    point.addAttribute("addr", String.valueOf(data.getAddr()));
                    point.addAttribute("regType",data.getRegType());
                    point.addAttribute("regParam", StringUtils.isBlank(data.getRegParam())? "" : data.getRegParam());
                    point.addAttribute("endian",data.getEndian());
                    point.addAttribute("ratio",data.getRatio());
                    point.addAttribute("offset",data.getOffset());
                    point.addAttribute("property","YC" + data.getAddr() + "_" +i);
                    point.addAttribute("remark",i18nService.getLocal(data.getRemark(), local) + unit);
                    point.addAttribute("levelNo", data.getLevelNo());
                    point.addAttribute("levelType",data.getLevelType());
                    point.addAttribute("userDefined1", s);
                    point.addAttribute("userDefined2",data.getUserDefined2() == null? "" : data.getUserDefined2());
                    point.addAttribute("sort",data.getSort() == null? "" : data.getSort());
                    point.addAttribute("userType",data.getUserType());
                    point.addAttribute("group",data.getGroup() == null? "" : data.getGroup());
                    i++;
                }
            }else {
                Element point = yc.addElement("point");
                point.addAttribute("funcode",data.getFunCode());
                point.addAttribute("addr", String.valueOf(data.getAddr()));
                point.addAttribute("regType",data.getRegType());
                point.addAttribute("regParam",StringUtils.isBlank(data.getRegParam())? "" : data.getRegParam());
                point.addAttribute("endian",data.getEndian());
                point.addAttribute("ratio",data.getRatio());
                point.addAttribute("offset",data.getOffset());
                point.addAttribute("property","YC" + data.getAddr());
                point.addAttribute("remark",i18nService.getLocal(data.getRemark(), local) + unit);
                point.addAttribute("levelNo",data.getLevelNo());
                point.addAttribute("levelType",data.getLevelType());
                point.addAttribute("userDefined1",data.getUserDefined1() == null? "" : data.getUserDefined1());
                point.addAttribute("userDefined2",data.getUserDefined2() == null? "" : data.getUserDefined2());
                point.addAttribute("sort",data.getSort() == null? "" : data.getSort());
                point.addAttribute("userType",data.getUserType());
                point.addAttribute("group",data.getGroup() == null? "" : data.getGroup());
            }
        }
    }

    private void buildYk(Element yk, List<AppYkPoint> ykList, String local) {

        List<Element> blocks = new ArrayList<>();
        List<AppYkPoint> copyYkList = new ArrayList<>(ykList);
        int index = 0;
        boolean flag = false;
        for (AppYkPoint data : ykList) {
            String unit = data.getUnit() == null? "" : "(" + data.getUnit() +")";
            if(data.getPointNum() != null){
                flag = true;
                List<AppYkPoint>  blockList = copyYkList.subList(index, index + Integer.parseInt(data.getPointNum())+1);
                blocks.add(buildBlock(blockList,local));
                index--;
            }else if(!flag){
                Element point = yk.addElement("point");
                point.addAttribute("funcode",data.getFunCode());
                point.addAttribute("addr", String.valueOf(data.getAddr()));
                point.addAttribute("regType",data.getRegType());
                point.addAttribute("regParam",StringUtils.isBlank(data.getRegParam())? "" : data.getRegParam());
                point.addAttribute("endian",data.getEndian());
                point.addAttribute("ratio",data.getRatio());
                point.addAttribute("offset",data.getOffset());
                point.addAttribute("property","YK" + data.getAddr());
                point.addAttribute("remark",i18nService.getLocal(data.getRemark(), local) + unit);
                point.addAttribute("userDefined1",data.getUserDefined1() == null? "" : i18nService.getLocal(data.getUserDefined1(), local));
                point.addAttribute("userDefined2",data.getUserDefined2() == null? "" : data.getUserDefined2());
                point.addAttribute("rangeData",data.getRangeData());
                point.addAttribute("nextProp",data.getNextProp()== null? "" : data.getNextProp());
                point.addAttribute("defaultVal",data.getDefaultVal()== null? "" : data.getDefaultVal());
                point.addAttribute("sort",data.getSort() == null? "" : data.getSort());
                point.addAttribute("userType",data.getUserType());
                point.addAttribute("group",data.getGroup() == null? "" : data.getGroup());
            }
            index++;
        }
        for(Element e : blocks) {
            yk.add(e);
        }
    }


    private Element buildBlock(List<AppYkPoint> blockList,String local) {
        Element block = DocumentHelper.createElement("block");
        AppYkPoint blockData = blockList.remove(0);
        block.addAttribute("property", blockData.getUserDefined2() == null? "BLOCK" + blockData.getAddr() : blockData.getUserDefined2());
        block.addAttribute("funcode", blockData.getFunCode());
        block.addAttribute("addr", String.valueOf(blockData.getAddr()));
        block.addAttribute("regCount", blockData.getRegNum());
        block.addAttribute("remark", i18nService.getLocal(blockData.getRemark(), local));
        block.addAttribute("nextProp", blockData.getNextProp() == null? "" : blockData.getNextProp());
        block.addAttribute("sort",blockData.getSort() == null? "" : blockData.getSort());
        block.addAttribute("userType",blockData.getUserType());
        block.addAttribute("group",blockData.getGroup() == null? "" : blockData.getGroup());
        for(AppYkPoint data :blockList){
            Element point = block.addElement("point");
            point.addAttribute("addr",data.getAddr().toString());
            point.addAttribute("regType",data.getRegType());
            point.addAttribute("regParam",StringUtils.isBlank(data.getRegParam())? "" : data.getRegParam());
            point.addAttribute("endian",data.getEndian());
            point.addAttribute("ratio",data.getRatio());
            point.addAttribute("offset",data.getOffset());
            point.addAttribute("property","YK" + data.getAddr());
            point.addAttribute("remark",i18nService.getLocal(data.getRemark(), local));
            point.addAttribute("userDefined1",data.getUserDefined1() == null? "" : i18nService.getLocal(data.getUserDefined1(), local));
            point.addAttribute("userDefined2",data.getUserDefined2() == null? "" : data.getUserDefined2());
            point.addAttribute("rangeData", StringUtils.isBlank(data.getRangeData())? "" : data.getRangeData());
            point.addAttribute("defaultVal",data.getDefaultVal() == null? "" : data.getDefaultVal());
            point.addAttribute("sort",data.getSort() == null? "" : data.getSort());
            point.addAttribute("userType",data.getUserType());
            point.addAttribute("group",data.getGroup() == null? "" : data.getGroup());
        }
        return block;
    }

    public Timeline createBackupFileTask(int seconds){
        Timeline fiveSecondsWonder = new Timeline(new KeyFrame(Duration.seconds(seconds), event -> backFile()));
        fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
        return fiveSecondsWonder;
    }

    private void backFile() {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filePath = PathUtil.getBackPath(GloableSetting.workPath) + date + ".xlsx";

        if(AppTableDataManager.DEVICES.size() != 0){
            String protocol = AppTableDataManager.DEVICES.get(0).getProtocolName();
            filePath = PathUtil.getBackPath(GloableSetting.workPath) + protocol + "_" + date + ".xlsx";
        }
        File file = new File(filePath);

        saveFile(file);

        CommonDataManager.BACK_MANAGER.add(file);
        if (CommonDataManager.BACK_MANAGER.size() > 10) {
            File deleteFile = CommonDataManager.BACK_MANAGER.poll();
            FileUtil.deleteFile(deleteFile.getAbsolutePath());
        }
    }


}
