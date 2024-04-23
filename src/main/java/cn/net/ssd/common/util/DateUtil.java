package cn.net.ssd.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {

	public static Date parseDate(String fmt, String time) {
		try {
		    if(time==null || "".equals(time)){
		        return null;
		    }
			SimpleDateFormat smf = new SimpleDateFormat(fmt);
			return smf.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;

	}

	public static String formatDate(Date date, String fmt) {
		try {
			SimpleDateFormat smf = new SimpleDateFormat(fmt);
			return smf.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static synchronized String formatDate(String fmt) {
		try {
			Thread.sleep(1000);
			Date date = new Date();
			SimpleDateFormat smf = new SimpleDateFormat(fmt);
			return smf.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取系统当前时间 yyyy-MM-dd hh:mm:ss
	 * 
	 * @return
	 */
	public static String getCurrentTime() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);

	}

	/**
	 * 获取系统当前时间 yyyy-MM-dd hh:mm:ss
	 * 
	 * @return
	 */
	public static String getTime() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(date);

	}
	
	/**
	 * 获取系统当前时间 yyyy-MM-dd
	 * 
	 * @return
	 */
	public static String getTimeFormat() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);

	}
	
	/**
	 * 获取下一天日期 yyyy-MM-dd
	 * 
	 * @return
	 */
	public static String getNextFormat() {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		Date date = calendar.getTime();
		return sdf.format(date);

	}

	/**
	 * 获取系统当前时间 yyyy-MM-dd hh:mm:ss
	 * 
	 * @return
	 */
	public static String getCurrentTime(String parten) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(parten);
		return sdf.format(date);

	}

	/**
	 * 系统当前时间增加指定小时
	 * 
	 * @return
	 */
	public static String addAnyDay(int hourCount) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR, hourCount);

		return sdf.format(cal.getTime());

	}
	
	/**
     * 系统当前时间增加指定分钟
     * 
     * @return
     */
    public static String addAnyMIn(int minCount) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, minCount);

        return sdf.format(cal.getTime());

    }

	public static String getDifferenceTime(String startTime, String endTime,
			String format, String str) {
		// 按照传入的格式生成一个simpledateformate对象
		SimpleDateFormat sd = new SimpleDateFormat(format);
		long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
		long nh = 1000 * 60 * 60;// 一小时的毫秒数
		long nm = 1000 * 60;// 一分钟的毫秒数
		long ns = 1000;// 一秒钟的毫秒数
		long diff;
		long day = 0;
		long hour = 0;
		long min = 0;
		long sec = 0;
		// 获得两个时间的毫秒时间差异
		try {
			diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();
			day = diff / nd;// 计算差多少天
			hour = diff % nd / nh + day * 24;// 计算差多少小时
			min = diff % nd % nh / nm + day * 24 * 60;// 计算差多少分钟
			sec = diff % nd % nh % nm / ns;// 计算差多少秒
			// 输出结果
			// System.out.println("时间相差：" + day + "天" + (hour - day * 24) + "小时"
			// + (min - day * 24 * 60) + "分钟" + sec + "秒。");
			// System.out.println("hour=" + hour + ",min=" + min);
			// if (str.equals("h")) {
			// return hour;
			// } else {
			// return min;
			// }
			String time = ((hour - day * 24) < 10 ? "0" + (hour - day * 24)
					: (hour - day * 24))
					+ "小时"
					+ ((min - day * 24 * 60) < 10 ? "0" + (min - day * 24 * 60)
							: (min - day * 24 * 60))
					+ "分钟"
					+ (sec < 10 ? "0" + sec : sec) + "秒";
			return time;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// if (str.equalsIgnoreCase("h")) {
		// return hour;
		// } else {
		// return min;
		// }
		return null;
	}

	/**
	 * 
	 *
	 * @Title: dateDiff 
	 * @Description: 获取任意两个日期间中不包含星期六、星期日的总天数
	 * @param @param startDate
	 * @param @param endDate
	 * @param @return    设定文件 
	 * @return long    返回类型 
	 * @throws
	 */
	public static long dateDiff(String startDate, String endDate) {
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		GregorianCalendar endGC = new GregorianCalendar();
		long times, days1 = 0l;
		try {
			times = sd.parse(endDate).getTime() - sd.parse(startDate).getTime();
			long days = times / (1000 * 24 * 60 * 60);
			days1 = (days / 7) * 5;
			long days2 = days % 7;
			endGC.setTime(sd.parse(endDate));
			int weekDay = endGC.get(Calendar.DAY_OF_WEEK);
			if (weekDay == 1) {
				days1 += days2 > 2 ? days2 - 2 : 0;
			} else if (weekDay == 7) {
				days1 += days2 > 1 ? days2 - 1 : 0;
			} else if (weekDay - 1 < days2) {
				days1 += days2 - 2;
			} else if (weekDay - 1 > days2) {
				days1 += days2;
			} else if (weekDay - 1 == days2) {
				days1 += weekDay - 1;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return days1;
	}
	
	/**
	 * 
	 *
	 * @Title: secToTime 
	 * @Description: 将总秒变化为 xx小时xx分钟xx秒
	 * @param @param time
	 * @param @return    设定文件 
	 * @return String    返回类型 
	 * @throws
	 */
	public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00小时00分钟00秒";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr ="00小时"+ unitFormat(minute) + "分钟" + unitFormat(second)+"秒";
            } else {
                hour = minute / 60;
               /* if (hour > 99)
                    return "99:59:59";*/
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + "小时" + unitFormat(minute) + "分钟" + unitFormat(second)+"秒";
            }
        }
        return timeStr;
    }
    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

	//比较指定日期大小
	public static boolean compareDate(String sendTime, int n){
	    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowtime = addAnyMIn(n);
        try{
            Date d1 = format.parse(sendTime);
            Date d2 = format.parse(nowtime);
            //System.out.println(nowtime);
            return d1.after(d2);
        }catch(ParseException e){
            e.printStackTrace();
        }
        return false;
    }
	
	//指定日期加上指定小时
	public static String addHourDate(String sendTime, int n){
	    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
        	Date date = format.parse(sendTime);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.HOUR, n);

            return format.format(cal.getTime());
        }catch(ParseException e){
            e.printStackTrace();
            return "";
        }
    }
	
	//指定日期加上指定小时 fmt:yyyyMM,yyyyMMdd,yyyy-MM,...
	public static String addMonDate(String sendTime, int n, String fmt, String flag){
	    SimpleDateFormat format = new SimpleDateFormat(fmt);
        try{
        	Date date = format.parse(sendTime);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            if("Y".equals(flag))
            	cal.add(Calendar.YEAR, n);
            else if("M".equals(flag))
            	cal.add(Calendar.MONTH, n);
            else if("D".equals(flag))
            	cal.add(Calendar.DATE, n);
            else if("H".equals(flag))
            	cal.add(Calendar.HOUR, n);
            else if("MI".equals(flag))
            	cal.add(Calendar.MINUTE, n);
            else
            	cal.add(Calendar.SECOND, n);

            return format.format(cal.getTime());
        }catch(ParseException e){
            e.printStackTrace();
            return "";
        }
    }
	/* 
     * 将时间转换为时间戳
     */    
    public static long dateToStamp(String s) throws ParseException{
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        //res = String.valueOf(ts);
        return ts;
    }
    
    /** 
     * @Title: getSpaceMonthTime 
     * @Description: 获取任意时间的指定间隔月的时间,默认是往后推迟N月，往前推算就将spaceMonth设置为负数即可
     * @param @param time
     * @param @param spaceMonth
     * @param @return     
     * @return String     
     * @throws 
     */
     public static String getSpaceMonthTime(String time,int spaceMonth) {
     	try {
         	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
         	Date now = sdf.parse(time);
         	Calendar calendar = Calendar.getInstance();
         	calendar.setTime(now);
         	calendar.add(Calendar.MONTH, spaceMonth);
         	return sdf.format(calendar.getTime());
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
     	return time;
     }
     
     public static String getSpaceMonthTimeh(String time,int spaceMonth) {
      	try {
          	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
          	Date now = sdf.parse(time);
          	Calendar calendar = Calendar.getInstance();
          	calendar.setTime(now);
          	calendar.add(Calendar.MONTH, spaceMonth);
          	return sdf.format(calendar.getTime());
  		} catch (Exception e) {
  			e.printStackTrace();
  		}
      	return time;
      }
     /** 
 	* @Title: isValidDate 
 	* @Description: 判断参数的格式是否为“yyyy/MM/dd”格式的合法日期字符串
 	* @param @param str
 	* @param @return     
 	* @return boolean     
 	* @throws 
 	*/
 	public static boolean isValidDate(String str) {
 		try {
 			if (str != null && !str.equals("")) {
 				if (str.length() == 10) {
 					// 闰年标志
 					boolean isLeapYear = false;
 					String[] dateArr = str.split("/");
 					String year = dateArr[0];
 					String month = dateArr[1];
 					String day = dateArr[2];
 					int vYear = Integer.parseInt(year);
 					// 判断年份是否合法
 					if (vYear < 1900 || vYear > 2200) {
 						return false;
 					}
 					// 判断是否为闰年
 					if (vYear % 4 == 0 && vYear % 100 != 0 || vYear % 400 == 0) {
 						isLeapYear = true;
 					}
 					// 判断月份
 					// 1.判断月份
 					if (month.startsWith("0")) {
 						String units4Month = month.substring(1, 2);
 						int vUnits4Month = Integer.parseInt(units4Month);
 						if (vUnits4Month == 0) {
 							return false;
 						}
 						if (vUnits4Month == 2) {
 							// 获取2月的天数
 							int vDays4February = Integer.parseInt(day);
 							if (isLeapYear) {
 								if (vDays4February > 29)
 									return false;
 							} else {
 								if (vDays4February > 28)
 									return false;
 							}

 						}
 					} else {
 						// 2.判断非0打头的月份是否合法
 						int vMonth = Integer.parseInt(month);
 						if (vMonth != 10 && vMonth != 11 && vMonth != 12) {
 							return false;
 						}
 					}
 					// 判断日期
 					// 1.判断日期
 					if (day.startsWith("0")) {
 						String units4Day = day.substring(1, 2);
 						int vUnits4Day = Integer.parseInt(units4Day);
 						if (vUnits4Day == 0) {
 							return false;
 						}
 					} else {
 						// 2.判断非0打头的日期是否合法
 						int vDay = Integer.parseInt(day);
 						if (vDay < 10 || vDay > 31) {
 							return false;
 						}
 					}
 					return true;
 				} else {
 					return false;
 				}
 			} else {
 				return false;
 			}
 		} catch (Exception e) {
 			e.printStackTrace();
 			return false;
 		}
 	}
 	
 	
 	/** 
 	 * @Title: isValidDate 
 	 * @Description: 判断参数的格式是否为“yyyy-MM-dd”格式的合法日期字符串
 	 * @param @param str
 	 * @param @return     
 	 * @return boolean     
 	 * @throws 
 	 */
 	public static boolean isValidDateh(String str) {
 		try {
 			if (str != null && !str.equals("")) {
 				if (str.length() == 10) {
 					// 闰年标志
 					boolean isLeapYear = false;
 					String[] dateArr = str.split("-");
 					String year = dateArr[0];
 					String month = dateArr[1];
 					String day = dateArr[2];
 					int vYear = Integer.parseInt(year);
 					// 判断年份是否合法
 					if (vYear < 1900 || vYear > 2200) {
 						return false;
 					}
 					// 判断是否为闰年
 					if (vYear % 4 == 0 && vYear % 100 != 0 || vYear % 400 == 0) {
 						isLeapYear = true;
 					}
 					// 判断月份
 					// 1.判断月份
 					if (month.startsWith("0")) {
 						String units4Month = month.substring(1, 2);
 						int vUnits4Month = Integer.parseInt(units4Month);
 						if (vUnits4Month == 0) {
 							return false;
 						}
 						if (vUnits4Month == 2) {
 							// 获取2月的天数
 							int vDays4February = Integer.parseInt(day);
 							if (isLeapYear) {
 								if (vDays4February > 29)
 									return false;
 							} else {
 								if (vDays4February > 28)
 									return false;
 							}
 							
 						}
 					} else {
 						// 2.判断非0打头的月份是否合法
 						int vMonth = Integer.parseInt(month);
 						if (vMonth != 10 && vMonth != 11 && vMonth != 12) {
 							return false;
 						}
 					}
 					// 判断日期
 					// 1.判断日期
 					if (day.startsWith("0")) {
 						String units4Day = day.substring(1, 2);
 						int vUnits4Day = Integer.parseInt(units4Day);
 						if (vUnits4Day == 0) {
 							return false;
 						}
 					} else {
 						// 2.判断非0打头的日期是否合法
 						int vDay = Integer.parseInt(day);
 						if (vDay < 10 || vDay > 31) {
 							return false;
 						}
 					}
 					return true;
 				} else {
 					return false;
 				}
 			} else {
 				return false;
 			}
 		} catch (Exception e) {
 			e.printStackTrace();
 			return false;
 		}
 	}
 	
 	 /** 
     * @Title: differentDays 
     * @Description: 两个日期相差天数
     * @param @param date1
     * @param @param date2
     * @param @return     
     * @return int     
     * @throws 
     */
     public static int differentDays(Date date1,Date date2) {
         Calendar cal1 = Calendar.getInstance();
         cal1.setTime(date1);
         Calendar cal2 = Calendar.getInstance();
         cal2.setTime(date2);
         int day1= cal1.get(Calendar.DAY_OF_YEAR);
         int day2 = cal2.get(Calendar.DAY_OF_YEAR);
         
         int year1 = cal1.get(Calendar.YEAR);
         int year2 = cal2.get(Calendar.YEAR);
         if(year1 != year2)   //同一年
         {
             int timeDistance = 0 ;
             for(int i = year1 ; i < year2 ; i ++) {
                 if(i%4==0 && i%100!=0 || i%400==0)    //闰年            
                 {
                     timeDistance += 366;
                 }
                 else    //不是闰年
                 {
                     timeDistance += 365;
                 }
             }
             return timeDistance + (day2-day1) ;
         }
         else    //不同年
         {
        	 //  System.out.println("判断day2 - day1 : " + (day2-day1));
             return day2-day1;
         }
     }

	//获取过去n天的日期
	public static  String  getBeforNDays(int n){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, - n);
		Date d = c.getTime();
		String day = format.format(d);
		return day;
	}

	//当前时间返回指定格式
	public static String getFrom(String formt) {
		Date d1 = new Date();
		SimpleDateFormat sdf0 = new SimpleDateFormat(formt);
		return sdf0.format(d1);
	}



	public static void main(String[] args){
		try{
			long i=getSeconds("2019-05-29 11:29:56","2019-05-29 11:29:13");
			if(i>=1){
				System.out.println("您传入的两个时间相差"+i+"miao！");
			}else{
				System.out.println("您传入的两个时间相差小于1分钟！");
			}
		}catch(Exception e){e.printStackTrace();}
	}

	// 获取两个时间相差分钟数
	public static long getTime(String oldTime,String newTime) throws ParseException {

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long NTime =df.parse(newTime).getTime();
		//从对象中拿到时间
		long OTime = df.parse(oldTime).getTime();
		long diff=(NTime-OTime)/1000/60;
		return Math.abs(diff);
	}

	// 获取两个时间相差分钟数
	public static long getSeconds(String oldTime,String newTime) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long NTime =df.parse(newTime).getTime();
		//从对象中拿到时间
		long OTime = df.parse(oldTime).getTime();
		long diff=(NTime-OTime)/1000;
		return Math.abs(diff);
	}

	/**
	 * 时间的加减
	 *
	 * @param date     传入时间
	 * @param dateType 计算时间的类型  yyyy:年 ;MM:月;dd:天 ;HH:小时;mm:分钟
	 * @param value    减去的值(正数为加,负数为减)
	 * @return
	 */
	public static Date calculateDate(Date date, String dateType, int value) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		if ("yyyy".equals(dateType)) {
			calendar.add(Calendar.YEAR, value);
		} else if ("MM".equals(dateType)) {
			calendar.add(Calendar.MONTH, value);
		} else if ("dd".equals(dateType)) {
			calendar.add(Calendar.DAY_OF_YEAR, value);
		} else if ("HH".equals(dateType)) {
			calendar.add(Calendar.HOUR_OF_DAY, value);
		} else if ("mm".equals(dateType)) {
			calendar.add(Calendar.MINUTE, value);
		} else if ("ss".equals(dateType)) {
			calendar.add(Calendar.SECOND, value);
		}
		return calendar.getTime();
	}
     
}
