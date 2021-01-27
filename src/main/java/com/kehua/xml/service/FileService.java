package com.kehua.xml.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.kehua.xml.data.CommonDataManager;
import com.kehua.xml.data.GloableSetting;
import com.kehua.xml.data.TableDataManager;
import com.kehua.xml.model.*;
import com.kehua.xml.utils.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
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
public class FileService {

    @Autowired
    private TableDataService tableDataService;

    @Autowired
    private I18nService i18nService;


    public void newTemplate() {
        if(!beforeOpenCheck()) {
            return;
        }
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        GridPane grid = UIFactory.createBaseGridPane();

        List<String> types = DictUtil.getDictByType("协议类型").keySet().stream().map(Object::toString).collect(Collectors.toList());
        ChoiceBox<String> templateType = new ChoiceBox<>(FXCollections.observableArrayList(types));
        TextField version = new TextField();
        templateType.setPrefWidth(200);
        version.setPrefWidth(200);

        grid.add(new Label("协议类型"), 0, 0);
        grid.add(templateType, 1, 0);
        grid.add(new Label("协议版本"), 0, 1);
        grid.add(version, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Optional result = dialog.showAndWait();
        if (result.get() == ButtonType.OK) {
            if(StringUtils.isBlank(templateType.getValue()) || StringUtils.isBlank(version.getText())){
                throw new IllegalArgumentException("参数不能为空");
            }
            tableDataService.clearAllData();
            CommonDataManager.currentFile = null;

            Device device = new Device();
            device.setDevType(DictUtil.getDictByKey("协议类型", templateType.getValue()));
            device.setLanguage("zh-CN");
            device.setProtocolName(templateType.getValue() + "_" + version.getCharacters().toString().replace(".","_"));
            device.setProtocolType("0");
            TableDataManager.DEVICES.add(device);
        }
    }

    /**
     * 打开文件
     */
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
        if(TableDataManager.DEVICES.size() != 0){
            Optional<ButtonType> result = AlertFactory.createAlert("该操作会导致当前文件丢失，请确认保存", Alert.AlertType.CONFIRMATION);
            if(result.isPresent() && result.get().equals(ButtonType.CANCEL)){
                return false;
            }
        }
        return true;
    }

    public void openFile(File file){
        List<Device> device = EasyExcel.read(file).registerConverter(new SimpleStringPropertyConvert()).registerConverter(new SimpleBooleanPropertyConvert()).registerConverter(new SimpleIntegerPropertyConvert()).head(Device.class).sheet("设备").doReadSync();
        List<Block> blocks = EasyExcel.read(file).registerConverter(new SimpleStringPropertyConvert()).registerConverter(new SimpleBooleanPropertyConvert()).registerConverter(new SimpleIntegerPropertyConvert()).head(Block.class).sheet("查询指令").doReadSync();
        List<YxPoint> yxList = EasyExcel.read(file).registerConverter(new SimpleStringPropertyConvert()).registerConverter(new SimpleBooleanPropertyConvert()).registerConverter(new SimpleIntegerPropertyConvert()).head(YxPoint.class).sheet("YX").doReadSync();
        List<YcPoint> ycList = EasyExcel.read(file).registerConverter(new SimpleStringPropertyConvert()).registerConverter(new SimpleBooleanPropertyConvert()).registerConverter(new SimpleIntegerPropertyConvert()).head(YcPoint.class).sheet("YC").doReadSync();
        List<YkPoint> ykList = EasyExcel.read(file).registerConverter(new SimpleStringPropertyConvert()).registerConverter(new SimpleBooleanPropertyConvert()).registerConverter(new SimpleIntegerPropertyConvert()).head(YkPoint.class).sheet("YK").doReadSync();
        List<Event> events = EasyExcel.read(file).registerConverter(new SimpleStringPropertyConvert()).registerConverter(new SimpleBooleanPropertyConvert()).registerConverter(new SimpleIntegerPropertyConvert()).head(Event.class).sheet("事件配置").doReadSync();
        List<I18n> i18ns = EasyExcel.read(file).registerConverter(new SimpleStringPropertyConvert()).registerConverter(new SimpleBooleanPropertyConvert()).registerConverter(new SimpleIntegerPropertyConvert()).head(I18n.class).sheet("国际化").doReadSync();
        List<Store> stores = EasyExcel.read(file).registerConverter(new SimpleStringPropertyConvert()).registerConverter(new SimpleBooleanPropertyConvert()).registerConverter(new SimpleIntegerPropertyConvert()).head(Store.class).sheet("存储配置").doReadSync();

        tableDataService.clearAllData();

        TableDataManager.DEVICES.addAll(device);
        TableDataManager.BLOCKS.addAll(blocks);
        TableDataManager.YX_POINTS.addAll(yxList);
        TableDataManager.YC_POINTS.addAll(ycList);
        TableDataManager.YK_POINTS.addAll(ykList);
        TableDataManager.EVENTS.addAll(events);
        CommonDataManager.I_18_NS.addAll(i18ns);
        TableDataManager.STORES.addAll(stores);
    }

    public Boolean checkFile(File file) {
        Set<String> set = new HashSet<>();
        List<ReadSheet> sheets = EasyExcel.read(file).build().excelExecutor().sheetList();
        for(ReadSheet sheet : sheets){
            set.add(sheet.getSheetName());
        }
        return set.contains("设备") && set.contains("查询指令")&&set.contains("YX") &&set.contains("YC")&&
                set.contains("YK") && set.contains("事件配置") && set.contains("国际化") && set.contains("存储配置");
    }

    /**
     * 保存文件
     */
    public void save(){
        File file = CommonDataManager.currentFile;
        if (file == null) {
            String fileName = "新建文件";
            if(TableDataManager.DEVICES.size() != 0) {
                fileName = TableDataManager.DEVICES.get(0).getProtocolName();
            }
            file = UIFactory.saveFile(fileName);
            if (file == null) return;
            CommonDataManager.currentFile = file;
        }
        saveFile(file);
    }

    /**
     * 另存为文件
     */
    public void saveAs(){
        String fileName = "新建文件";
        if(TableDataManager.DEVICES.size() != 0) {
            fileName = TableDataManager.DEVICES.get(0).getProtocolName();
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
            WriteSheet blockSheet = EasyExcel.writerSheet( "查询指令").head(Block.class).build();
            WriteSheet yxSheet = EasyExcel.writerSheet( "YX").head(YxPoint.class).build();
            WriteSheet ycSheet = EasyExcel.writerSheet( "YC").head(YcPoint.class).build();
            WriteSheet ykSheet = EasyExcel.writerSheet( "YK").head(YkPoint.class).build();
            WriteSheet eventSheet = EasyExcel.writerSheet( "事件配置").head(Event.class).build();
            WriteSheet i18nSheet = EasyExcel.writerSheet( "国际化").head(I18n.class).build();
            WriteSheet storeSheet = EasyExcel.writerSheet( "存储配置").head(Store.class).build();

            List<Device> deviceData = new ArrayList<>(TableDataManager.DEVICES);
            List<Block> blockData = new ArrayList<>(TableDataManager.BLOCKS);
            List<YxPoint> yxData = new ArrayList<>(TableDataManager.YX_POINTS);
            List<YcPoint> ycData = new ArrayList<>(TableDataManager.YC_POINTS);
            List<YkPoint> ykData = new ArrayList<>(TableDataManager.YK_POINTS);
            List<Event> eventData = new ArrayList<>(TableDataManager.EVENTS);
            List<I18n> i18nData = new ArrayList<>(CommonDataManager.I_18_NS);
            List<Store> storeData = new ArrayList<>(TableDataManager.STORES);

            excelWriter.write(deviceData, deviceSheet);
            excelWriter.write(blockData, blockSheet);
            excelWriter.write(yxData, yxSheet);
            excelWriter.write(ycData, ycSheet);
            excelWriter.write(ykData, ykSheet);
            excelWriter.write(eventData, eventSheet);
            excelWriter.write(i18nData, i18nSheet);
            excelWriter.write(storeData, storeSheet);

        }catch (Exception e){
            throw e;
        }finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    private String exportStore(File file) throws IOException {
        String fileName = TableDataManager.DEVICES.get(0).getProtocolName();
        File storefile = new File(file.getAbsolutePath() + "/" + fileName + "_store" + ".xlsx");

        if(!storefile.exists()){
            storefile.createNewFile();
        }
        ExcelWriter excelWriter = null;
        List<Store> storeData = new ArrayList<>(TableDataManager.STORES);

        try {

            excelWriter = EasyExcel.write(storefile).registerConverter(new SimpleStringPropertyConvert()).build();
            WriteSheet storeSheet = EasyExcel.writerSheet( "存储配置").head(Store.class).build();
            excelWriter.write(storeData, storeSheet);
        }finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
        return storefile.getAbsolutePath();
    }

    private String exportEvent(File file) throws IOException {
        String fileName = TableDataManager.DEVICES.get(0).getProtocolName();

        File eventfile = new File(file.getAbsolutePath() + "/" + fileName + "_event" + ".xlsx");
        if(!eventfile.exists()){
            eventfile.createNewFile();
        }
        ExcelWriter excelWriter = null;
        List<Event> eventData = new ArrayList<>(TableDataManager.EVENTS);
        try {
            excelWriter = EasyExcel.write(eventfile).registerConverter(new SimpleStringPropertyConvert()).build();
            WriteSheet eventSheet = EasyExcel.writerSheet( "事件配置").head(Event.class).build();
            excelWriter.write(eventData, eventSheet);
        }finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
        return eventfile.getAbsolutePath();
    }

    private String exportXml(File file,String local) throws IOException {
        Document doc = DocumentHelper.createDocument();
        Element root = doc.addElement("ROOT");
        Element device = root.addElement("device");
        Element block = root.addElement("BLOCK");
        root.addElement("SUB-DEVICE");
        Element yx = root.addElement("YX");
        Element yc = root.addElement("YC");
        Element yk = root.addElement("YK");

        List<Device> deviceData = new ArrayList<>(TableDataManager.DEVICES);
        List<Block> blockData = new ArrayList<>(TableDataManager.BLOCKS);
        List<YxPoint> yxData = new ArrayList<>(TableDataManager.YX_POINTS);
        List<YcPoint> ycData = new ArrayList<>(TableDataManager.YC_POINTS);
        List<YkPoint> ykData = new ArrayList<>(TableDataManager.YK_POINTS);

        List<YxPoint> yxPoints = collectYx(yxData,ycData,ykData);
        List<YcPoint> ycPoints = collectYc(ycData,ykData);

        buildDevice(device, deviceData.get(0), local);
        buildBlock(block, blockData);
        buildYx(yx, yxPoints, local);
        buildYc(yc, ycPoints, local);
        buildYk(yk, ykData, local);

        return writeToXml(doc, file, local);
    }

    protected List<YcPoint> collectYc(List<YcPoint> ycData, List<YkPoint> ykData) {

        List<YcPoint> yc = ycData.stream().filter(x -> !"83".equals(x.getRegType())).collect(Collectors.toList());

        List<YkPoint> yk = ykData.stream().filter(x -> !x.isNoCollect())
                .filter(x ->!"83".equals(x.getCollectRegType()))
                .filter(x -> !"16".equals(x.getFunCode()))
                .filter(x -> !x.isNoCollect())
                .collect(Collectors.toList());

        List<YcPoint> yk2Ycc = convertYk2Yc(yk);

        List<YcPoint> result = new ArrayList<>(yc);
        result.addAll(yk2Ycc);

        return result;
    }

    private List<YcPoint> convertYk2Yc(List<YkPoint> yk) {
        List<YcPoint> ycPoints = new ArrayList<>();
        YcPoint point;
        for(YkPoint ykPoint : yk){
            point = new YcPoint();
            point.setFunCode(ykPoint.getCollectFuncode());
            point.setAddr(ykPoint.getAddr());
            point.setRegType(ykPoint.getCollectRegType());
            point.setRegParam(ykPoint.getCollectRegParam());
            point.setRatio(ykPoint.getRatio());
            point.setEndian(ykPoint.getEndian());
            point.setOffset(ykPoint.getOffset());
            point.setProperty("YC" + ykPoint.getAddr());
            point.setRemark(ykPoint.getRemark());
            point.setUnit(ykPoint.getUnit());
            point.setLevelType("-1");
            point.setLevelNo("-1");
            point.setUserDefined1("");
            point.setUserDefined2("");
            ycPoints.add(point);
        }
        return ycPoints;
    }

    protected List<YxPoint> collectYx(List<YxPoint> yxData, List<YcPoint> ycData, List<YkPoint> ykData) {

        List<YxPoint> result = new ArrayList<>(yxData);

        List<YcPoint> yc = ycData.stream().filter(x -> "83".equals(x.getRegType())).collect(Collectors.toList());
        List<YkPoint> yk = ykData.stream()
                .filter(x -> "83".equals(x.getCollectRegType()))
                .filter(x -> !x.isNoCollect())
                .collect(Collectors.toList());

        List<YxPoint> yc2Yx = convertYc2Yx(yc);
        List<YxPoint> yk2Yx = convertYk2Yx(yk);
        result.addAll(yc2Yx);
        result.addAll(yk2Yx);

        return result;
    }

    private List<YxPoint> convertYk2Yx(List<YkPoint> yk) {
        List<YxPoint> ycPoints = new ArrayList<>();
        YxPoint point;
        for(YkPoint ykPoint : yk){
            point = new YxPoint();
            point.setFunCode(ykPoint.getCollectFuncode());
            point.setAddr(ykPoint.getAddr());
            point.setRegType("83");
            point.setRegParam("0,16");
            point.setRatio(ykPoint.getRatio());
            point.setEndian(ykPoint.getEndian());
            point.setOffset(ykPoint.getOffset());
            point.setProperty("YX" + ykPoint.getAddr());
            point.setRemark(ykPoint.getRemark());
            point.setLevelType("-1");
            point.setLevelNo("-1");
            point.setUserDefined1(ykPoint.getUserDefined1());
            point.setUserDefined2(ykPoint.getUserDefined2());
            ycPoints.add(point);
        }
        return ycPoints;
    }

    private List<YxPoint> convertYc2Yx(List<YcPoint> yc) {
        List<YxPoint> ycPoints = new ArrayList<>();
        YxPoint point;
        for(YcPoint ycPoint : yc){
            point = new YxPoint();
            point.setFunCode(ycPoint.getFunCode());
            point.setAddr(ycPoint.getAddr());
            point.setRegType("83");
            point.setRegParam("0,16");
            point.setRatio(ycPoint.getRatio());
            point.setEndian(ycPoint.getEndian());
            point.setOffset(ycPoint.getOffset());
            point.setProperty("YX" + ycPoint.getAddr());
            point.setRemark(ycPoint.getRemark());
            point.setLevelType(ycPoint.getLevelType());
            point.setLevelNo(ycPoint.getLevelNo());
            point.setUserDefined1(ycPoint.getUserDefined1());
            point.setUserDefined2(ycPoint.getUserDefined2());
            ycPoints.add(point);
        }
        return ycPoints;
    }

    private void buildYk(Element yk, List<YkPoint> ykList, String local) {

        List<Element> blocks = new ArrayList<>();
        List<YkPoint> copyYkList = new ArrayList<>(ykList);
        int index = 0;
        for (YkPoint data : ykList) {
            String unit = data.getUnit() == null? "" : "(" + data.getUnit() +")";
            if(data.getPointNum() != null){
                List<YkPoint>  blockList = copyYkList.subList(index, index + Integer.parseInt(data.getPointNum())+1);
                blocks.add(buildBlock(blockList,local));
                index--;
            }else if(StringUtils.isBlank(data.getFunCode())){
                index++;
                continue;
            }else {
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
            }
            index++;
        }
        for(Element e : blocks) {
            yk.add(e);
        }
    }

    private void buildYc(Element yc, List<YcPoint> ycList, String local) {

        Collections.sort(ycList);
        for (YcPoint data : ycList) {
            String unit = data.getUnit() == null? "" : "(" + data.getUnit() +")";

            if(data.getUserDefined1() != null && data.getUserDefined1().split(",").length > 1){
                int i = 1;
                for(String s : data.getUserDefined1().split(",")){
                    Element point = yc.addElement("point");
                    point.addAttribute("funcode",data.getFunCode());
                    point.addAttribute("addr", String.valueOf(data.getAddr()));
                    point.addAttribute("regType",data.getRegType());
                    point.addAttribute("regParam",StringUtils.isBlank(data.getRegParam())? "" : data.getRegParam());
                    point.addAttribute("endian",data.getEndian());
                    point.addAttribute("ratio",data.getRatio());
                    point.addAttribute("offset",data.getOffset());
                    point.addAttribute("property","YC" + data.getAddr() + "_" +i);
                    point.addAttribute("remark",i18nService.getLocal(data.getRemark(), local) + unit);
                    point.addAttribute("levelNo", data.getLevelNo());
                    point.addAttribute("levelType",data.getLevelType());
                    point.addAttribute("userDefined1", s);
                    point.addAttribute("userDefined2",data.getUserDefined2() == null? "" : data.getUserDefined2());
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
            }
        }
    }

    private void buildYx(Element yx, List<YxPoint> yxList, String local) {
        Collections.sort(yxList);
        for (YxPoint data : yxList) {
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
        }
    }

    private void buildBlock(Element block, List<Block> blockData) {
        for (Block block1 : blockData) {
            Element b = block.addElement("point");
            b.addAttribute("funcode", block1.getFuncode());
            b.addAttribute("addr", block1.getAddr().toString());
            b.addAttribute("regCount", block1.getRegCount().toString());
            b.addAttribute("queryType", block1.getQueryType());
        }
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


    private static String writeToXml(Document doc,File file, String local) throws IOException {
        String fileName = TableDataManager.DEVICES.get(0).getProtocolName();
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



    private Element buildBlock(List<YkPoint> blockList,String local) {
        Element block = DocumentHelper.createElement("block");
        YkPoint blockData = blockList.remove(0);
        block.addAttribute("property", blockData.getDefaultVal() == null? "BLOCK" + blockData.getAddr() : blockData.getDefaultVal());
        block.addAttribute("funcode", blockData.getFunCode());
        block.addAttribute("addr", String.valueOf(blockData.getAddr()));
        block.addAttribute("regCount", blockData.getRegNum());
        block.addAttribute("remark", i18nService.getLocal(blockData.getRemark(), local));
        block.addAttribute("nextProp", blockData.getNextProp() == null? "" : blockData.getNextProp());
        for(YkPoint data :blockList){
            Element point = block.addElement("point");
            point.addAttribute("addr",data.getAddr().toString());
            point.addAttribute("regType",data.getRegType());
            point.addAttribute("regParam",StringUtils.isBlank(data.getRegParam())? "" : data.getRegParam());
            point.addAttribute("endian",data.getEndian());
            point.addAttribute("ratio",data.getRatio());
            point.addAttribute("offset",data.getOffset());
            point.addAttribute("property", data.getDefaultVal() == null? "YK" + data.getAddr() : data.getDefaultVal());
            point.addAttribute("remark",i18nService.getLocal(data.getRemark(), local));
            point.addAttribute("userDefined1",data.getUserDefined1() == null? "" : i18nService.getLocal(data.getUserDefined1(), local));
            point.addAttribute("userDefined2",data.getUserDefined2() == null? "" : data.getUserDefined2());
            point.addAttribute("rangeData", StringUtils.isBlank(data.getRangeData())? "" : data.getRangeData());
            point.addAttribute("defaultVal","");
        }
        return block;
    }

    /**
     * 导出xml协议
     */
    public void exportZip() {

        Dialog<Pair<String, String>> dialog = new Dialog<>();
        GridPane grid = UIFactory.createBaseGridPane();

        List<String> types = DictUtil.getDictByType("语言").keySet().stream().map(Object::toString).collect(Collectors.toList());

        HBox hBox = new HBox();
        for(String s : types){

            CheckBox checkBox = new CheckBox(s);
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
            if(TableDataManager.DEVICES.size() == 0){
                throw new RuntimeException("协议配置错误");
            }
            String fileName = TableDataManager.DEVICES.get(0).getProtocolName();
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

    private void exportZip(File zipFile,String locals) throws IOException {
        try {
            File file = zipFile.getParentFile();
            List<String> urls = new ArrayList<>();
            i18nService.cacheI18n();

            for(String local : locals.split(",")){
                urls.add(exportXml(file, local));
            }

            urls.add(exportEvent(file));
            urls.add(exportStore(file));

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

    private String getLocals(HBox hBox) {
        StringBuilder sb = new StringBuilder();
        for (Node box : hBox.getChildren()) {
            CheckBox checkBox = (CheckBox) box;
            checkBox.setStyle("-fx-padding: 3px 8px;");
            if (checkBox.isSelected()) {
                sb.append(DictUtil.getDictByKey("语言", checkBox.getText()));
                sb.append(",");
            }
        }
        return sb.length() == 0 ? "" : sb.deleteCharAt(sb.length() - 1).toString();
    }

    public Timeline createBackupFileTask(int seconds){
        Timeline fiveSecondsWonder = new Timeline(new KeyFrame(Duration.seconds(seconds), event -> backFile()));
        fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
        return fiveSecondsWonder;
    }

    private void backFile() {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filePath = PathUtil.getBackPath(GloableSetting.workPath) + date + ".xlsx";

        if(TableDataManager.DEVICES.size() != 0){
            String protocol = TableDataManager.DEVICES.get(0).getProtocolName();
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
