package com.kehua.xml.analysis;

import com.kehua.xml.analysis.parser.*;

public class ParserFactory {
    private static final ParserFactory INSTANCE = new ParserFactory();

    private ParserFactory(){}

    public static ParserFactory getInstance(){
        return INSTANCE;
    }

    public Parser getParser(String regType, String regParam,double ratio,String funCode){
        Parser parser;

        switch (regType){
            case "4":
            case "6":
                parser =  new IntUnSignedParser(ratio);
                break;
            case "81":
                if("1".equals(funCode) || "2".equals(funCode)){
                    parser =  new IntSignedParser(ratio);
                }else {
                    parser =  new BitParser(Integer.parseInt(regParam.split(",")[0]));
                }
                break;
            case "3":
            case "5":
            case "83":
                parser =  new IntSignedParser(ratio);
                break;
            case "9":
                String type = regParam.split(",")[1];
                if("1".equals(type)){
                    parser =  new StringAsciiParser();
                }else if("0".equals(type)){
                    parser =  new StringUTF8Parser();
                }else if("3".equals(type)){
                    parser =  new StringHexParser();
                } else if("4".equals(type)){
                    parser = new StringTime6Parser();
                } else if("5".equals(type)){
                    parser =  new StringTimeParser();
                } else if("6".equals(type)){
                    parser =  new StringBinaryParser();
                }else {
                    parser =  new StringBCDParser();
                }
                break;
            default:
                parser =  new StringBCDParser();
        }
        return parser;
    }
}
