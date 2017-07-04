package com.yisun.web.common.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhangxs7 on 2017/7/4.
 */
public class DateFormatUtils extends org.apache.commons.lang.time.DateFormatUtils {
    private static final Log log = LogFactory.getLog(DateFormatUtils.class);

    public static final String format = "yyyy-MM-dd HH:mm:ss";


    /**
     * 功能：按照日期格式，将字符串解析为日期对象
     * 默认format为yyyy-MM-dd HH:mm:ss
     *
     * @param date 一个按format格式排列的日期的字符串描述
     * @return Date 对象
     * @throws ParseException
     * @throws ParseException
     * @see java.text.SimpleDateFormat
     */
    public static final Date parseDate(String date) {
        return parseDate(date, format);
    }


    /**
     * 功能：按照日期格式，将字符串解析为日期对象
     *
     * @param date   一个按format格式排列的日期的字符串描述
     * @param format 输入字符串的格式
     * @return Date 对象
     * @throws ParseException
     * @throws ParseException
     * @see java.text.SimpleDateFormat
     */
    public static final Date parseDate(String date, String format) {

        SimpleDateFormat df = new SimpleDateFormat(format);

        try {
            return df.parse(date);
        } catch (ParseException e) {
            if (log.isErrorEnabled()) {
                log.error("parseDate() ParseException", e);
            }
        }

        return null;
    }

    /**
     * 功能：按照日期格式，将Date解析为日期对象
     *
     * @param date 一个Date类型的date
     * @return Date 对象
     * @throws ParseException
     * @throws ParseException
     * @see java.text.SimpleDateFormat
     */
    public static Date parseDate(Date date) {
        return parseDate(date, format);
    }

    /**
     * 功能：按照日期格式，将字符串解析为日期对象
     *
     * @param date   一个Date类型的date
     * @param format 输入字符串的格式
     * @return Date 对象
     * @throws ParseException
     * @throws ParseException
     * @see java.text.SimpleDateFormat
     */
    public static Date parseDate(Date date, String format) {
        SimpleDateFormat inDf = new SimpleDateFormat(format);
        SimpleDateFormat outDf = new SimpleDateFormat(format);
        String reDate = "";
        try {
            reDate = inDf.format(date);
            return outDf.parse(reDate);
        } catch (ParseException e) {
            if (log.isErrorEnabled()) {
                log.error("parseDate() ParseException", e);
            }
        }
        return date;
    }

    public static String format(Date date) {
        return format(date,format);
    }

}
