package util;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.commons.lang.time.DateUtils;
import play.libs.F;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.Date;

/**
 * 常用工具类
 * User: wenzhihong
 * Date: 12-11-10
 * Time: 下午1:26
 */
public abstract class CommonUtils {
    //-999 表示全部. 定义在 keyValueOptions.js 里. 所有的都用这个值
    public static final int SELECT_ALL_OPTION_VALUE = -999;

    public static final String[] DATE_FORMAT_STR_ARR = {"yyyy-MM-dd", "yyyy-MM-dd hh:mm:ss"};

    /**
     * 把字符串解析成Date. 支持的字符串格式(yyyy-MM-dd)跟(yyyy-MM-dd hh:mm:ss)
     */
    public static Date parseDate(String d) {
        try {
            return DateUtils.parseDate(d, DATE_FORMAT_STR_ARR);
        } catch (ParseException e) {
            throw new NestableRuntimeException(e);
        }
    }

    /**
     * 返回int数组里的最小值, 最大值.
     * _1 最小值
     * _2 最大值
     */
    public static F.T2<Number, Number> minMax(int[] arr) {
        if (arr.length == 1) {
            new F.T2(arr[0], arr[0]);
        }

        int minVal = Integer.MAX_VALUE;
        int maxVal = Integer.MIN_VALUE;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] < minVal) {
                minVal = arr[i];
            }
            if (arr[i] > maxVal) {
                maxVal = arr[i];
            }
        }

        return new F.T2(minVal, maxVal);
    }

    /**
     * 返回long数组里的最小值, 最大值.
     * _1 最小值
     * _2 最大值
     */
    public static F.T2<Number, Number> minMax(long[] arr) {
        if (arr.length == 1) {
            new F.T2(arr[0], arr[0]);
        }

        long minVal = Long.MAX_VALUE;
        long maxVal = Long.MIN_VALUE;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] < minVal) {
                minVal = arr[i];
            }
            if (arr[i] > maxVal) {
                maxVal = arr[i];
            }
        }

        return new F.T2(minVal, maxVal);
    }

    /**
     * 返回float数组里的最小值, 最大值.
     * _1 最小值
     * _2 最大值
     */
    public static F.T2<Number, Number> minMax(float[] arr) {
        if (arr.length == 1) {
            new F.T2(arr[0], arr[0]);
        }

        float minVal = Float.MAX_VALUE;
        float maxVal = Float.MIN_VALUE;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] < minVal) {
                minVal = arr[i];
            }
            if (arr[i] > maxVal) {
                maxVal = arr[i];
            }
        }

        return new F.T2(minVal, maxVal);
    }

    /**
     * 返回float数组里的最小值, 最大值.
     * _1 最小值
     * _2 最大值
     */
    public static F.T2<Number, Number> minMax(double[] arr) {
        if (arr.length == 1) {
            new F.T2(arr[0], arr[0]);
        }

        double minVal = Double.MAX_VALUE;
        double maxVal = Double.MIN_VALUE;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] < minVal) {
                minVal = arr[i];
            }
            if (arr[i] > maxVal) {
                maxVal = arr[i];
            }
        }

        return new F.T2(minVal, maxVal);
    }

    /**
     * 读取json配制文件,去掉注释. 以 //, /*, #, var  开头的, 都认为是注释. 注意,这里的注释是行注释
     * @param input
     * @return
     */
    public static String readJsonConfigFile2String(InputStream input){
        StringWriter writer = new StringWriter();
        try {
            LineIterator it = IOUtils.lineIterator(input, "UTF-8");
            while (it.hasNext()){
                String line = it.nextLine();
                String linePack = line.trim();
                if(linePack.startsWith("//") || linePack.startsWith("/*") || linePack.startsWith("#")
                        || linePack.startsWith("var ")){ //认为是注释,跳过
                    continue;
                }else{
                    writer.write(line);
                    writer.write(IOUtils.LINE_SEPARATOR);
                }
            }
        } catch (IOException e) {}
        finally {
            IOUtils.closeQuietly(input);
        }

        return writer.toString();
    }

    /**
     * 按 HighChart 的数据类型转化成 json. 主要是用于画图的
     */
    public static String toJsonWithHighChartDataType(Object o) {
        return HighChartDataType.toJsonWithHighChartDataType(o);
    }
}
