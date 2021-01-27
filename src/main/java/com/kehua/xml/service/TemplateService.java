package com.kehua.xml.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.kehua.xml.data.CommonDataManager;
import com.kehua.xml.data.GloableSetting;
import com.kehua.xml.data.TableDataManager;
import com.kehua.xml.exception.CheckException;
import com.kehua.xml.model.*;
import com.kehua.xml.utils.DictUtil;
import com.kehua.xml.utils.SimpleBooleanPropertyConvert;
import com.kehua.xml.utils.SimpleStringPropertyConvert;
import com.kehua.xml.utils.UIFactory;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TemplateService {

    private final static Map<String,Store> STORE = new HashMap<>();
    private final static Map<String,I18n> I18N = new HashMap<>();
    private final static Map<String,Event> EVENT = new HashMap<>();

    @Autowired
    private FileService fileService;
    @Autowired
    private TableDataService tableDataService;


    /**
     * 提取配置
     */
    public void extractSetting(){
        checkTemplate();

        List<Device> device = new ArrayList<>(TableDataManager.DEVICES);
        List<YxPoint> yx = new ArrayList<>(TableDataManager.YX_POINTS);
        List<YcPoint> yc = new ArrayList<>(TableDataManager.YC_POINTS);
        List<YkPoint> yk = new ArrayList<>(TableDataManager.YK_POINTS);

        //提取国际化
        Set<String> i18n = new HashSet<>();

        if(device.size() > 0){
            i18n.add(device.get(0).getProtocolName());
        }
        yx.forEach(x -> {
            if("83".equals(x.getRegType())){
                i18n.add(x.getUserDefined1());
            }
            i18n.add(x.getRemark());
        });
        yc.forEach(x -> {
            if("83".equals(x.getRegType())){
                i18n.add(x.getUserDefined1());
            }
            i18n.add(x.getRemark());
        });
        yk.forEach(x -> {
            if("83".equals(x.getCollectRegType())){
                i18n.add(x.getUserDefined1());
            }
            i18n.add(x.getRemark());
        });

        backupSetting();

        CommonDataManager.I_18_NS.clear();
        TableDataManager.EVENTS.clear();
        TableDataManager.STORES.clear();

        for(String s : i18n){
            if(I18N.get(s) != null){
                CommonDataManager.I_18_NS.add(I18N.get(s));
            }else {
                I18n i18n1 = new I18n();
                i18n1.setRemark(s);
                CommonDataManager.I_18_NS.add(i18n1);
            }
        }

        //提取事件
        yx.forEach(x -> {
            if(!"83".equals(x.getRegType())){
                if(EVENT.get(x.getRemark()) != null){
                    TableDataManager.EVENTS.add(EVENT.get(x.getRemark()));
                }else {
                    Event event = new Event();
                    event.setRemark(x.getRemark());
                    TableDataManager.EVENTS.add(event);
                }
            }
        });

        //提取存储配置
        yc.forEach(x -> {
            String remark = x.getRemark();
            if(StringUtils.isNotBlank(x.getUnit())){
                remark= remark + "(" + x.getUnit() + ")";
            }

            if(STORE.get(remark) != null){
                TableDataManager.STORES.add(STORE.get(remark));
            }else {
                Store store = new Store();
                store.setRemark(remark);

                TableDataManager.STORES.add(store);
            }

        });

        clearBackUp();
    }

    private void clearBackUp() {
        I18N.clear();
        STORE.clear();
        EVENT.clear();
    }

    private void backupSetting() {
        List<I18n> yx = new ArrayList<>(CommonDataManager.I_18_NS);
        yx.forEach(x -> I18N.put(x.getRemark(),x));
        List<Event> yc = new ArrayList<>(TableDataManager.EVENTS);
        yc.forEach(x -> EVENT.put(x.getRemark(),x));
        List<Store> yk = new ArrayList<>(TableDataManager.STORES);
        yk.forEach(x -> STORE.put(x.getRemark(),x));
    }

    /**
     * 生成block
     * @return
     */
    public void createBlocks() {
        checkTemplate();

        List<Block> result = new ArrayList<>();

        List<YxPoint> yx = new ArrayList<>(TableDataManager.YX_POINTS);
        List<YcPoint> yc = new ArrayList<>(TableDataManager.YC_POINTS);
        List<YkPoint> yk = new ArrayList<>(TableDataManager.YK_POINTS);

        List<BlockPoint> realtime = new ArrayList<>();
        List<BlockPoint> period = new ArrayList<>();
        List<BlockPoint> onetime = new ArrayList<>();

        yx2BlockPoint(yx, realtime);
        // yx + yk 过滤掉不采集，不实时上送的点位 ,所有变位上送点位
        List<YkPoint> collectYk = yk.stream()
                .filter(x -> !x.isNoCollect())
                .filter(x -> !x.isNoRealTime())
                .filter(x -> !"16".equals(x.getFunCode()))
                .collect(Collectors.toList());
        yk2BlockPoint(collectYk, realtime);

        //yc 过滤掉一次采集的的点位 + yk 的不实时上送点位
        List<YcPoint> periodYc = yc.stream().filter(x -> !x.getOneTime())
                .collect(Collectors.toList());
        List<YkPoint> periodYk = yk.stream().filter(YkPoint::isNoRealTime)
                .filter(x -> !"16".equals(x.getFunCode()))
                .collect(Collectors.toList());
        yk2BlockPoint(periodYk, period);
        yc2BlockPoint(periodYc, period);

        // 过滤出yc中一次上送的点位
        List<YcPoint> onetimeYc = yc.stream().filter(YcPoint::getOneTime)
                .collect(Collectors.toList());
        yc2BlockPoint(onetimeYc, onetime);

        filterLockedPoint(realtime, period, onetime);

        Map<String,List<BlockPoint>> realtime1 = realtime.stream().collect(Collectors.groupingBy(BlockPoint :: getFunCode));
        Map<String,List<BlockPoint>> period1 = period.stream().collect(Collectors.groupingBy(BlockPoint :: getFunCode));
        Map<String,List<BlockPoint>> onetime1 = onetime.stream().collect(Collectors.groupingBy(BlockPoint :: getFunCode));

        createBlocks(result, realtime1,"1");
        createBlocks(result, period1,"0");
        createBlocks(result, onetime1,"2");

        TableDataManager.BLOCKS.removeIf(blockPoint -> !blockPoint.isIsLocked());
        TableDataManager.BLOCKS.addAll(result);
    }

    private void filterLockedPoint(List<BlockPoint> realtime, List<BlockPoint> period, List<BlockPoint> onetime) {
        List<Block> blocks = new ArrayList<>(TableDataManager.BLOCKS);
        List<Block> lockedBlock = blocks.stream().filter(Block :: isIsLocked).collect(Collectors.toList());
        lockedBlock.forEach(x -> {
            int min = Integer.parseInt(x.getAddr());
            int max = Integer.parseInt(x.getAddr()) + Integer.parseInt(x.getRegCount());

            realtime.removeIf(blockPoint -> min <= blockPoint.getAddr() && blockPoint.getAddr() < max);
            period.removeIf(blockPoint -> min <= blockPoint.getAddr() && blockPoint.getAddr() < max);
            onetime.removeIf(blockPoint -> min <= blockPoint.getAddr() && blockPoint.getAddr() < max);
        });
    }


    public void checkTemplate(){
        List<Device> device = new ArrayList<>(TableDataManager.DEVICES);
        List<YxPoint> yxData = new ArrayList<>(TableDataManager.YX_POINTS);
        List<YcPoint> ycData = new ArrayList<>(TableDataManager.YC_POINTS);
        List<YkPoint> ykData = new ArrayList<>(TableDataManager.YK_POINTS);

        List<YxPoint> yxPoints = fileService.collectYx(yxData,ycData,ykData);
        List<YcPoint> ycPoints = fileService.collectYc(ycData,ykData);

        checkDevice(device);
        //校验遥信
        checkYx(yxPoints);
        //校验遥测
        checkYc(ycPoints);
        //校验遥控
        checkYk(ykData);

        //2.校验点位重复，地址，remark
        checkRepeat(yxPoints,ycPoints);
    }

    private void checkDevice(List<Device> device) {
        if(device.size() != 1){
            throw new CheckException("设备信息未填写，请检查");
        }
        Device device1 = device.get(0);
        if(StringUtils.isBlank(device1.getProtocolName())
                || StringUtils.isBlank(device1.getLanguage())
                || StringUtils.isBlank(device1.getDevType())){
            throw new CheckException("协议名称,设备类型,或者语言未填写");
        }
    }

    private void checkRepeat(List<YxPoint> yxPoints, List<YcPoint> ycPoints) {
        Map<String,YxPoint> yxMap = new HashMap<>();
        yxPoints.forEach(x ->{
            if(yxMap.get(x.getRemark()) != null){
                if(yxMap.get(x.getRemark()).getRegParam() != null) {
                    if(yxMap.get(x.getRemark()).getRegParam().equals(x.getRegParam())){
                        throw new CheckException("地址为 " + x.getAddr() + " 的点位重复");
                    }
                }else {
                    throw new CheckException("地址为 " + x.getAddr() + " 的点位重复");
                }

            }else {
                yxMap.put(x.getRemark(),x);
            }
        });
        Map<String,YcPoint> ycMap = new HashMap<>();
        ycPoints.forEach(x -> {
            if(ycMap.get(x.getRemark()) != null){
                if(ycMap.get(x.getRemark()).getUserDefined1() != null) {
                    if(ycMap.get(x.getRemark()).getUserDefined1().equals(x.getUserDefined1())){
                        throw new CheckException("地址为 " + x.getAddr() + " 的点位重复");
                    }
                }else {
                    throw new CheckException("地址为 " + x.getAddr() + " 的点位重复");
                }
            }else {
                ycMap.put(x.getRemark(),x);
            }
        });
    }

    private void checkYk(List<YkPoint> ykData) {
        Collections.sort(ykData);
        //单点校验
        ykData.forEach(this::checkYk);
        //多点校验
        checkBlocks(ykData);
    }
    private void checkBlocks(List<YkPoint> ykData) {
        int index = 0;
        for(YkPoint ykPoint : ykData){
            if("16".equals(ykPoint.getCollectFuncode())){
                List<YkPoint>  blockList = ykData.subList(index, index + Integer.parseInt(ykPoint.getPointNum())+1);
                blockList.forEach(x -> {
                    if (StringUtils.isNotBlank(x.getPointNum()) || StringUtils.isNotBlank(x.getRegNum())){
                        throw new CheckException("多点遥控点位 " + ykPoint.getAddr() + " 配置错误");
                    }
                    if(StringUtils.isNotBlank(x.getFunCode())){
                        throw new CheckException("多点遥控点位 " + ykPoint.getAddr() + " 功能码填写错误");
                    }
                });
            }
            index++;
        }
    }


    private void checkYk(YkPoint ykData) {
        if(ykData.getAddr() == null){
            throw new CheckException("遥控存在地址为空的点位");
        }
        checkCommonFiled(ykData);
        if("5".equals(ykData.getFunCode()) || "6".equals(ykData.getFunCode())){
            if(StringUtils.isBlank(ykData.getUserDefined1()) && StringUtils.isBlank(ykData.getRangeData())){
                throw new CheckException("遥控点位 " + ykData.getAddr() + " 范围未配置");
            }
        }
        if(!ykData.isNoCollect() && !"16".equals(ykData.getFunCode())){
            if(StringUtils.isBlank(ykData.getCollectFuncode()) || StringUtils.isBlank(ykData.getCollectRegType())){
                throw new CheckException("遥控点位 " + ykData.getAddr() + " 查询功能码或者采集器类型未配置");
            }
        }
    }

    private void checkYc(List<YcPoint> ycPoints) {
        ycPoints.forEach(this :: checkYc);
    }


    private void checkYc(YcPoint ycPoint){
        if(ycPoint.getAddr() == null){
            throw new CheckException("遥测存在地址为空的点位");
        }
        checkCommonFiled(ycPoint);
        if(StringUtils.isBlank(ycPoint.getFunCode())){
            throw new CheckException("遥测点位 " + ycPoint.getAddr() + " 功能码配置错误");
        }
        if("9".equals(ycPoint.getRegType())){
            if(StringUtils.isBlank(ycPoint.getRegParam()) || ycPoint.getRegParam().contains("，")){
                throw new CheckException("遥测点位 " + ycPoint.getAddr() + " 寄存器参数配置错误");
            }
        }
        if("83".equals(ycPoint.getRegType())){
            if(StringUtils.isBlank(ycPoint.getRegParam())|| StringUtils.isBlank(ycPoint.getUserDefined1()) || ycPoint.getUserDefined1().contains("；")){
                throw new CheckException("遥测点位" + ycPoint.getAddr() + " UserDefined1 配置错误");
            }
        }
    }


    private void checkYx(List<YxPoint> yxPoints) {
        yxPoints.forEach(this::checkYx);
    }

    private void checkYx(YxPoint yxPoint) {
        if(yxPoint.getAddr() == null){
            throw new CheckException("遥信存在地址为空的点位");
        }
        checkCommonFiled(yxPoint);
        if(StringUtils.isBlank(yxPoint.getFunCode())){
            throw new CheckException("遥信点位 " + yxPoint.getAddr() + " 功能码未配置");
        }
        if((!"81".equals(yxPoint.getRegType()) && !"83".equals(yxPoint.getRegType()))){
            throw new CheckException("遥信点位 " + yxPoint.getAddr() + " 寄存器类型配置错误");
        }
        if(StringUtils.isBlank(yxPoint.getRegParam()) || yxPoint.getRegParam().contains("，")){
            throw new CheckException("遥信点位 " + yxPoint.getAddr() + " 寄存器参数配置存在非法字符");
        }
        if("83".equals(yxPoint.getRegType())){
            if(StringUtils.isBlank(yxPoint.getUserDefined1()) || yxPoint.getUserDefined1().contains("；")){
                throw new CheckException("遥信点位" + yxPoint.getAddr() + " UserDefined1 配置错误");
            }
        }
    }

    private void checkCommonFiled(BasePoint basePoint){
        if(StringUtils.isBlank(basePoint.getEndian())
                || StringUtils.isBlank(basePoint.getRatio())
                || StringUtils.isBlank(basePoint.getOffset())
                || StringUtils.isBlank(basePoint.getRemark())
        ) {
            throw new CheckException("点位 " + basePoint.getAddr() + "必填项为空");
        }
    }


    private void yc2BlockPoint(List<YcPoint> periodYc, List<BlockPoint> realtime) {
        periodYc.forEach(x -> {
            BlockPoint blockPoint = new BlockPoint();
            blockPoint.setAddr(Integer.valueOf(x.getAddr()));
            blockPoint.setFunCode(x.getFunCode());
            blockPoint.setRegType(x.getRegType());
            blockPoint.setRegParam(x.getRegParam());
            realtime.add(blockPoint);
        });
    }

    private void yk2BlockPoint(List<YkPoint> collectYk, List<BlockPoint> realtime) {
        collectYk.forEach(x -> {
            BlockPoint blockPoint = new BlockPoint();
            blockPoint.setAddr(Integer.valueOf(x.getAddr()));
            blockPoint.setFunCode(x.getCollectFuncode());
            blockPoint.setRegType(x.getRegType());
            blockPoint.setRegParam(x.getRegParam());
            realtime.add(blockPoint);
        });
    }

    private void yx2BlockPoint(List<YxPoint> yx, List<BlockPoint> realtime) {
        yx.forEach(x -> {
            BlockPoint blockPoint = new BlockPoint();
            blockPoint.setAddr(Integer.valueOf(x.getAddr()));
            blockPoint.setFunCode(x.getFunCode());
            blockPoint.setRegType(x.getRegType());
            blockPoint.setRegParam(x.getRegParam());
            realtime.add(blockPoint);
        });
    }

    private void createBlocks(List<Block> result, Map<String, List<BlockPoint>> realtime1, String queryType) {
        realtime1.forEach((k,v) -> {
            Collections.sort(v);
            LinkedList<BlockPoint> tem = new LinkedList<>(v);
            gengrateBlock2(result, tem, queryType);
        });

    }

    private void gengrateBlock2(List<Block> result, LinkedList<BlockPoint> v, String queryType) {
        BlockPoint now  = v.pollFirst();
        BlockPoint next = v.pollFirst();
        for(;;){
            if(now != null && next != null){
                int start = now.getAddr() ,num = 0;
                for(;;) {
                    //如果步长大于10，独立封装一个block
                    //超过110个寄存器另起一个block
                    if (getMutiAdddr(now, next) > GloableSetting.blockIntervalNum || num > GloableSetting.blockMaxNum) {
                        num += getRegNum(now);
                        result.add(createBlock(start, num , now.getFunCode(), queryType));
                        break;
                    } else { //如果不足10则继续封装，总共不超过128个寄存器，保险起不超110
                        num += getMutiAdddr(now, next);
                    }
                    //取出下一个进行比较
                    //此处比较失败不满足条件需要回退一个量
                    now = next;
                    next = v.pollFirst();
                    //走到此处说明当前List全部为一个block
                    if(next == null) {
                        num += getRegNum(now);
                        result.add(createBlock(start, num, now.getFunCode(), queryType));
                        break;
                    }
                }
                if(next == null){
                    break;
                }
                now = next;
                next = v.pollFirst();
                //完成退出
                if(next == null){
                    result.add(createBlock(now.getAddr(), getRegNum(now), now.getFunCode(), queryType));
                    break;
                }
            }else if(next == null){
                result.add(createBlock(now.getAddr(), getRegNum(now), now.getFunCode(), queryType));
                break;
            }
        }
    }


    private static Integer getMutiAdddr(BlockPoint now, BlockPoint next) {
        return next.getAddr() - now.getAddr();
    }

    private static int getRegNum(BlockPoint now) {
        if ("5".equals(now.getRegType()) || "6".equals(now.getRegType())) {
            return 2;
        } else if ("9".equals(now.getRegType())) {
            return Integer.parseInt(now.getRegParam().split(",")[0]) / 2;
        } else {
            return 1;
        }
    }

    private Block createBlock(int start, int num, String funcode, String queryType) {
        Block b = new Block();
        b.setFuncode(funcode);
        b.setAddr(Integer.toString(start));
        b.setRegCount(Integer.toString(num));
        b.setQueryType(queryType);
        return b;
    }

    public void exportI18n(File file) {
        ExcelWriter excelWriter = null;
        try {
            // 这里 指定文件
            excelWriter = EasyExcel.write(file)
                    .registerConverter(new SimpleStringPropertyConvert())
                    .registerConverter(new SimpleBooleanPropertyConvert()).build();

            WriteSheet i18nSheet = EasyExcel.writerSheet( "国际化").head(I18n.class).build();

            List<I18n> i18nData = new ArrayList<>(CommonDataManager.I_18_NS);

            excelWriter.write(i18nData, i18nSheet);

        } finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    public void importI18n(File file) {
        List<I18n> i18ns = EasyExcel.read(file).registerConverter(new SimpleStringPropertyConvert()).registerConverter(new SimpleBooleanPropertyConvert()).head(I18n.class).sheet("国际化").doReadSync();

        CommonDataManager.I_18_NS.clear();

        CommonDataManager.I_18_NS.addAll(i18ns);
    }

    public void importEvent(File file) {
        List<Event> events = EasyExcel.read(file).registerConverter(new SimpleStringPropertyConvert()).registerConverter(new SimpleBooleanPropertyConvert()).head(Event.class).sheet(0).doReadSync();

        TableDataManager.EVENTS.clear();

        TableDataManager.EVENTS.addAll(events);
    }

    public void importStore(File file) {
        List<Store> stores = EasyExcel.read(file).registerConverter(new SimpleStringPropertyConvert()).registerConverter(new SimpleBooleanPropertyConvert()).head(Store.class).sheet(0).doReadSync();

        TableDataManager.STORES.clear();

        TableDataManager.STORES.addAll(stores);
    }

    public Boolean checkI18nFile(File file) {
        Set<String> set = new HashSet<>();
        List<ReadSheet> sheets = EasyExcel.read(file).build().excelExecutor().sheetList();
        for(ReadSheet sheet : sheets){
            set.add(sheet.getSheetName());
        }
        return set.size() == 1 && set.contains("国际化");
    }

    public void convertTemplate(File file) throws FileNotFoundException, DocumentException {
        Document document = new SAXReader().read(new FileInputStream(file));
        Element rootElement = document.getRootElement();
        Element device = rootElement.element("device");
        Element yx = rootElement.element("YX");
        Element yc = rootElement.element("YC");
        Element yk = rootElement.element("YK");

        tableDataService.clearAllData();
        CommonDataManager.currentFile = null;

        createDevice(device);
        createYx(yx);
        createYc(yc);
        createYk(yk);
    }

    private void createYk(Element yk) {
        List<Element> points = yk.elements("point");
        for(Element point : points){
            String funcode = point.attributeValue("funcode");
            String addr = point.attributeValue("addr");
            String ratio = point.attributeValue("ratio");
            String regType = point.attributeValue("regType");
            String regParam = point.attributeValue("regParam");
            String remark = point.attributeValue("remark");
            String userDefined1 = point.attributeValue("userDefined1");
            String rangeData = point.attributeValue("rangeData");
            YkPoint ykPoint = new YkPoint();
            ykPoint.setFunCode(funcode);
            ykPoint.setAddr(addr);
            ykPoint.setRatio(ratio);
            ykPoint.setRegType(regType);
            ykPoint.setRegParam(regParam);
            ykPoint.setRemark(remark);
            if(remark.endsWith(")")){
                int start = remark.indexOf("(");
                int end = remark.indexOf(")");
                ykPoint.setUnit(remark.substring(start + 1, end));
            }
            ykPoint.setUserDefined1(userDefined1);
            ykPoint.setRangeData(rangeData);
            TableDataManager.YK_POINTS.add(ykPoint);
        }

        List<Element> blocks = yk.elements("block");

        for(Element block : blocks){
            String funcode = block.attributeValue("funcode");
            String addr = block.attributeValue("addr");
            String regCount = block.attributeValue("regCount");
            String remark = block.attributeValue("remark");
            YkPoint ykPoint = new YkPoint();
            ykPoint.setFunCode(funcode);
            ykPoint.setAddr(addr);
            ykPoint.setRegNum(regCount);
            ykPoint.setRemark(remark);
            TableDataManager.YK_POINTS.add(ykPoint);

            List<Element> blockPoints = block.elements("point");
            for(Element blockPoint : blockPoints){
                String addr1 = blockPoint.attributeValue("addr");
                String ratio = blockPoint.attributeValue("ratio");
                String regType = blockPoint.attributeValue("regType");
                String regParam = blockPoint.attributeValue("regParam");
                String remark1 = blockPoint.attributeValue("remark");
                String rangeData = blockPoint.attributeValue("rangeData");
                YkPoint ykBlockPoint = new YkPoint();
                ykBlockPoint.setAddr(addr1);
                ykBlockPoint.setRatio(ratio);
                ykBlockPoint.setRegType(regType);
                ykBlockPoint.setRegParam(regParam);
                ykBlockPoint.setRemark(remark1);
                if(remark.endsWith(")")){
                    int start = remark.indexOf("(");
                    int end = remark.indexOf(")");
                    ykBlockPoint.setUnit(remark.substring(start + 1, end));
                }
                ykBlockPoint.setRangeData(rangeData);
                TableDataManager.YK_POINTS.add(ykBlockPoint);
            }
        }

    }

    private void createYc(Element yc) {
        List<Element> points = yc.elements("point");
        for(Element point : points){
            String funcode = point.attributeValue("funcode");
            String addr = point.attributeValue("addr");
            String ratio = point.attributeValue("ratio");
            String regType = point.attributeValue("regType");
            String regParam = point.attributeValue("regParam");
            String remark = point.attributeValue("remark");
            String userDefined1 = point.attributeValue("userDefined1");
            YcPoint ycPoint = new YcPoint();
            ycPoint.setFunCode(funcode);
            ycPoint.setAddr(addr);
            ycPoint.setRatio(ratio);
            ycPoint.setRegType(regType);
            ycPoint.setRegParam(regParam);
            ycPoint.setRemark(remark);
            if(remark.endsWith(")")){
                int start = remark.indexOf("(");
                int end = remark.indexOf(")");
                ycPoint.setUnit(remark.substring(start + 1 , end));
            }
            ycPoint.setUserDefined1(userDefined1);
            TableDataManager.YC_POINTS.add(ycPoint);
        }
    }

    private void createYx(Element yx) {
        List<Element> points = yx.elements("point");
        for(Element point : points){
            String funcode = point.attributeValue("funcode");
            String addr = point.attributeValue("addr");
            String regType = point.attributeValue("regType");
            String regParam = point.attributeValue("regParam");
            String remark = point.attributeValue("remark");
            String userDefined1 = point.attributeValue("userDefined1");
            YxPoint yxPoint = new YxPoint();
            yxPoint.setFunCode(funcode);
            yxPoint.setAddr(addr);
            yxPoint.setRegType(regType);
            yxPoint.setRegParam(regParam);
            yxPoint.setRemark(remark);
            yxPoint.setUserDefined1(userDefined1);
            TableDataManager.YX_POINTS.add(yxPoint);
        }
    }

    private void createDevice(Element device) {
        String templetCode = device.attributeValue("templetCode");
        String devType = device.attributeValue("devType");
        String language = device.attributeValue("language");
        String protocolName = device.attributeValue("protocolName");
        String maker = device.attributeValue("maker");
        String model = device.attributeValue("model");
        String describe = device.attributeValue("describe");
        String protocolType = device.attributeValue("protocolType");
        Device device1 = new Device();
        device1.setTemplateCode(templetCode);
        device1.setDevType(devType);
        device1.setLanguage(language);
        device1.setProtocolName(protocolName);
        device1.setMaker(maker);
        device1.setModel(model);
        device1.setDescribe(describe);
        device1.setProtocolType(protocolType);
        TableDataManager.DEVICES.add(device1);
    }
}
