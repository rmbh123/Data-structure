package com.mycompany.studentadvisingsystemclean;

// هذا الكلاس مسؤول عن التاريخ والوقت
// يستخدم في جدولة الاجتماعات وورش العمل
public class DateTime implements IDateTime {
    
    // هذي هي البيانات الأساسية
    int day;     // اليوم
    int month;   // الشهر
    int year;    // السنة
    int hour;    // الساعة
    int minute;  // الدقيقة

    // ------------------------------------------------------
    // أول كونستركتر: يقرأ التاريخ من نص
    // ------------------------------------------------------
    public DateTime (String dt)//نص كامل
    {
        dt = dt.trim();  // أشيل المسافات الزايدة من البداية والنهاية
        
        // أدور على أماكن الشرطات والمسافات والنقطتين
        int first_S  = dt.indexOf('/');        // أول شرطة
        int second_S  = dt.lastIndexOf('/');   // آخر شرطة
        int space = dt.lastIndexOf(' ');       // آخر مسافة (بين التاريخ والوقت)
        int first_dots = dt.indexOf(':');      // أول نقطتين

        // أستخرج الشهر
        this.month = Integer.parseInt(dt.substring(0,first_S));
        if (this.month >12  || this.month < 1)
            this.month = 1;  // إذا كان خطأ، خليه 1 (يناير)
            
        // أستخرج اليوم
        this.day = Integer.parseInt(dt.substring(first_S+1, second_S));
        if (this.day >31  || this.day < 1)
            this.day = 1;   // إذا كان خطأ، خليه 1

        // أستخرج السنة
        this.year = Integer.parseInt(dt.substring( second_S+1, space));

        // أستخرج الساعة
        this.hour = Integer.parseInt(dt.substring( space+1 , first_dots));
        if (this.hour >23  || this.hour < 0)
            this.hour = 0;  // إذا كان خطأ، خليه 0

        // أستخرج الدقيقة
        this.minute = Integer.parseInt(dt.substring( first_dots+1));
        if (this.minute > 59  || this.minute < 0)
            this.minute = 0;  // إذا كان خطأ، خليه 0
    }
    
    // ------------------------------------------------------
    // ثاني كونستركتر: يدخل التاريخ أرقام مباشرة
    // ------------------------------------------------------
    public DateTime ( int year1, int month1, int day1 , int hour1, int mintue1)
    {
        this.year = year1;
        this.month = month1;
        this.day = day1;
        this.hour = hour1;
        this.minute = mintue1;
    }

    // ------------------------------------------------------
    // ثالث كونستركتر: ينسخ من DateTime ثاني
    // ------------------------------------------------------
    public DateTime ( IDateTime dt)
    {
        this.year = dt.getYear();
        this.month = dt.getMonth();
        this.day = dt.getDay();
        this.hour = dt.getHour();
        this.minute = dt.getMinute();
    }
    
    // ------------------------------------------------------
    // دوال جلب البيانات (Getters)
    // ------------------------------------------------------
    
    @Override
    public int getYear()
    {
        return year;   // أرجع السنة
    }

    @Override
    public int getMonth()
    {
        return month;  // أرجع الشهر
    }

    @Override
    public int getDay()
    {
        return day;    // أرجع اليوم
    }

    @Override
    public int getHour()
    {
        return hour;   // أرجع الساعة
    }

    @Override
    public int getMinute()
    {
        return minute; // أرجع الدقيقة
    }

    // ------------------------------------------------------
    // ترجع التاريخ بشكل مرتب للعرض
    // مثال: "08/24/2026 12:30"
    // ------------------------------------------------------
    @Override
    public String format()
    {
        String str = "";
        str += String.format("%02d" , month) + "/";   // شهر برقمين (01, 02, 12)
        str += String.format("%02d" , day) + "/";     // يوم برقمين
        str += String.format("%04d" , year) + " ";    // سنة بأربع أرقام
        str += String.format("%02d" , hour) + ":";    // ساعة برقمين
        str += String.format("%02d" , minute);        // دقيقة برقمين
        return str;
    }
    
    // ------------------------------------------------------
    // مقارنة تاريخين: أي واحد أقدم؟
    // تستخدم لاكتشاف التعارض بين الأحداث
    // ------------------------------------------------------
    @Override
    public int compareTo(IDateTime other)
    {
        // أقارن السنة أول شيء
        if (this.year != other.getYear())
            return (this.year - other.getYear());     // إذا اختلفت، الفرق يحدد
        // إذا السنة وحدة، أقارن الشهر
        else if (this.month != other.getMonth())
            return (this.month - other.getMonth());
        // إذا الشهر وحدة، أقارن اليوم
        else if (this.day != other.getDay())
            return (this.day - other.getDay());
        // إذا اليوم واحد، أقارن الساعة
        else if (this.hour != other.getHour())
            return (this.hour - other.getHour());
        // ، أقارن الدقيقة
        return (this.minute - other.getMinute());
    }
}