package com.kehua.xml.service;

import com.kehua.xml.data.AppTableDataManager;
import com.kehua.xml.data.GloableSetting;
import com.kehua.xml.exception.CheckException;
import com.kehua.xml.model.BasePoint;
import com.kehua.xml.model.BlockPoint;
import com.kehua.xml.model.Device;
import com.kehua.xml.model.app.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AppTemplateService {

    /**
     * 生成block
     * @return
     */
    public void createAppBlocks() {
        checkTemplate();
        List<AppBlockPoint> result = new ArrayList<>();

        List<AppYxPoint> yx = new ArrayList<>(AppTableDataManager.YX_POINTS);
        List<AppYcPoint> yc = new ArrayList<>(AppTableDataManager.YC_POINTS);
        List<AppYkPoint> yk = new ArrayList<>(AppTableDataManager.YK_POINTS);

        List<BlockPoint> period = new ArrayList<>();
        List<BlockPoint> onetime = new ArrayList<>();

        List<AppYcPoint> periodYc = yc.stream().filter(x -> !x.isOneTime())
                .collect(Collectors.toList());

        appYx2BlockPoint(yx, period);
        appYk2BlockPoint(yk, period);
        appYc2BlockPoint(periodYc, period);

        // 过滤出yc中一次上送的点位
        List<AppYcPoint> onetimeYc = yc.stream().filter(AppYcPoint::isOneTime)
                .collect(Collectors.toList());
        appYc2BlockPoint(onetimeYc, onetime);

        filterLockedPoint(new ArrayList<>(), period, onetime);

        Map<String,List<BlockPoint>> period1 = period.stream().collect(Collectors.groupingBy(BlockPoint :: getFunCode));
        Map<String,List<BlockPoint>> onetime1 = onetime.stream().collect(Collectors.groupingBy(BlockPoint :: getFunCode));

        createAppBlocks(result, period1,"0");
        createAppBlocks(result, onetime1,"2");

        AppTableDataManager.BLOCKS.removeIf(blockPoint -> !blockPoint.isIsLocked());
        AppTableDataManager.BLOCKS.addAll(result);
    }

    private void checkTemplate() {
        List<Device> device = new ArrayList<>(AppTableDataManager.DEVICES);
        List<AppYxPoint> yxData = new ArrayList<>(AppTableDataManager.YX_POINTS);
        List<AppYcPoint> ycData = new ArrayList<>(AppTableDataManager.YC_POINTS);
        List<AppYkPoint> ykData = new ArrayList<>(AppTableDataManager.YK_POINTS);
        List<RecordPoint> recordPoints = new ArrayList<>(AppTableDataManager.RECORDS);

        checkDevice(device);
        //校验遥信
        checkYx(yxData);
        //校验遥测
        checkYc(ycData);
        //校验遥控
        checkYk(ykData);

        checkRecord(recordPoints);
    }

    private void checkRecord(List<RecordPoint> recordPoints) {
        recordPoints.forEach(x -> {
            if(StringUtils.isBlank(x.getCode())
                    || StringUtils.isBlank(x.getRemark())
                    || StringUtils.isBlank(x.getRegType())
                    || StringUtils.isBlank(x.getRatio())
                    || StringUtils.isBlank(x.getRecordType())){
                throw new CheckException("记录必填项未配置");
            }
        });
    }

    private void checkYk(List<AppYkPoint> ykData) {
        Collections.sort(ykData);
        //单点校验
        ykData.forEach(this::checkYk);
    }

    private void checkYk(AppYkPoint ykData) {
        if(ykData.getAddr() == null){
            throw new CheckException("遥控存在地址为空的点位");
        }
        if(StringUtils.isBlank(ykData.getUserType())){
            throw new CheckException("遥测点位 " + ykData.getAddr() + " 用户类型未配置");
        }
        checkCommonFiled(ykData);
        if("5".equals(ykData.getFunCode()) || "6".equals(ykData.getFunCode())){
            if(StringUtils.isBlank(ykData.getUserDefined1()) && StringUtils.isBlank(ykData.getRangeData())){
                throw new CheckException("遥控点位 " + ykData.getAddr() + " 范围未配置");
            }
        }
    }

    private void checkYc(List<AppYcPoint> ycData) {
        ycData.forEach(this :: checkYc);
    }

    private void checkYc(AppYcPoint ycPoint){
        if(ycPoint.getAddr() == null){
            throw new CheckException("遥测存在地址为空的点位");
        }
        checkCommonFiled(ycPoint);
        if(StringUtils.isBlank(ycPoint.getFunCode())){
            throw new CheckException("遥测点位 " + ycPoint.getAddr() + " 功能码配置错误");
        }
        if(StringUtils.isBlank(ycPoint.getUserType())){
            throw new CheckException("遥测点位 " + ycPoint.getAddr() + " 用户类型未配置");
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

    private void checkYx(List<AppYxPoint> yxData) {
        yxData.forEach(this::checkYx);
    }

    private void checkYx(AppYxPoint yxPoint) {
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
        if(StringUtils.isBlank(yxPoint.getUserType())){
            throw new CheckException("遥信点位 " + yxPoint.getAddr() + " 用户类型未配置");
        }
        if("83".equals(yxPoint.getRegType())){
            if(StringUtils.isBlank(yxPoint.getUserDefined1()) || yxPoint.getUserDefined1().contains("；")){
                throw new CheckException("遥信点位" + yxPoint.getAddr() + " UserDefined1 配置存在非法字符");
            }
        }
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

    private void checkCommonFiled(BasePoint basePoint){
        if(StringUtils.isBlank(basePoint.getEndian())
                || StringUtils.isBlank(basePoint.getRatio())
                || StringUtils.isBlank(basePoint.getOffset())
                || StringUtils.isBlank(basePoint.getRemark())
        ) {
            throw new CheckException("点位 " + basePoint.getAddr() + "必填项为空");
        }
    }

    private void appYx2BlockPoint(List<AppYxPoint> yx, List<BlockPoint> realtime) {
        yx.forEach(x -> {
            BlockPoint blockPoint = new BlockPoint();
            blockPoint.setAddr(Integer.valueOf(x.getAddr()));
            blockPoint.setFunCode(x.getFunCode());
            blockPoint.setRegType(x.getRegType());
            blockPoint.setRegParam(x.getRegParam());
            blockPoint.setUserType(x.getUserType());
            realtime.add(blockPoint);
        });
    }

    private void appYk2BlockPoint(List<AppYkPoint> collectYk, List<BlockPoint> realtime) {
        collectYk.forEach(x -> {
            BlockPoint blockPoint = new BlockPoint();
            blockPoint.setAddr(Integer.valueOf(x.getAddr()));
            blockPoint.setFunCode(x.getCollectFuncode());
            blockPoint.setRegType(x.getRegType());
            blockPoint.setRegParam(x.getRegParam());
            blockPoint.setUserType(x.getUserType());
            realtime.add(blockPoint);
        });
    }

    private void appYc2BlockPoint(List<AppYcPoint> periodYc, List<BlockPoint> realtime) {
        periodYc.forEach(x -> {
            BlockPoint blockPoint = new BlockPoint();
            blockPoint.setAddr(Integer.valueOf(x.getAddr()));
            blockPoint.setFunCode(x.getFunCode());
            blockPoint.setRegType(x.getRegType());
            blockPoint.setRegParam(x.getRegParam());
            blockPoint.setUserType(x.getUserType());
            realtime.add(blockPoint);
        });
    }

    private void createAppBlocks(List<AppBlockPoint> result, Map<String, List<BlockPoint>> blocks, String queryType) {
        blocks.forEach((k,v) -> {
            Map<String, List<BlockPoint>> userTypeBlocks = v.stream().collect(Collectors.groupingBy(BlockPoint :: getUserType));
            userTypeBlocks.forEach((userType, userBlocks) -> {
                Collections.sort(userBlocks);
                LinkedList<BlockPoint> tem = new LinkedList<>(userBlocks);
                gengrateAppBlock(result, tem, queryType, userType);
            });
        });
    }

    private void gengrateAppBlock(List<AppBlockPoint> result, LinkedList<BlockPoint> v, String queryType, String userType) {
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
                        result.add(createAppBlock(start, num , now.getFunCode(), queryType, userType));
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
                        result.add(createAppBlock(start, num, now.getFunCode(), queryType, userType));
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
                    result.add(createAppBlock(now.getAddr(), getRegNum(now), now.getFunCode(), queryType, userType));
                    break;
                }
            }else if(next == null){
                result.add(createAppBlock(now.getAddr(), getRegNum(now), now.getFunCode(), queryType, userType));
                break;
            }
        }
    }

    private AppBlockPoint createAppBlock(int start, int num, String funcode, String queryType,String userType) {
        AppBlockPoint b = new AppBlockPoint();
        b.setFuncode(funcode);
        b.setAddr(Integer.toString(start));
        b.setRegCount(Integer.toString(num));
        b.setQueryType(queryType);
        b.setUserType(userType);
        return b;
    }


    private void filterLockedPoint(List<BlockPoint> realtime, List<BlockPoint> period, List<BlockPoint> onetime) {
        List<AppBlockPoint> blocks = new ArrayList<>(AppTableDataManager.BLOCKS);
        List<AppBlockPoint> lockedBlock = blocks.stream().filter(AppBlockPoint :: isIsLocked).collect(Collectors.toList());
        lockedBlock.forEach(x -> {
            int min = Integer.parseInt(x.getAddr());
            int max = Integer.parseInt(x.getAddr()) + Integer.parseInt(x.getRegCount());

            realtime.removeIf(blockPoint -> min <= blockPoint.getAddr() && blockPoint.getAddr() < max);
            period.removeIf(blockPoint -> min <= blockPoint.getAddr() && blockPoint.getAddr() < max);
            onetime.removeIf(blockPoint -> min <= blockPoint.getAddr() && blockPoint.getAddr() < max);
        });
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


}
