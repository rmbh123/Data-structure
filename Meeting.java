package com.mycompany.studentadvisingsystemclean;

// هذا الكلاس مسؤول عن الاجتماع بين المرشد والطالب
public class Meeting implements IMeeting {

    IStudent student;              // الطالب اللي حضر الاجتماع
    final int EventId;             // رقم الاجتماع (ما يتغير أبداً)
    String title;                  // عنوان الاجتماع
    IDateTime startDateTime;       // وقت البداية
    IDateTime endDateTime;         // وقت النهاية
    String location;               // المكان

    // ------------------------------------------------------
    // كونستركتر 1: أقل شيء، بس الرقم ووقت البداية والنهاية
    // ------------------------------------------------------
    public Meeting(int eID, IDateTime sDT , IDateTime eDT )
    {
        EventId = eID;
        startDateTime = new DateTime (sDT);  // نسخة جديدة
        endDateTime = new DateTime (eDT);    // نسخة جديدة
    }

    // ------------------------------------------------------
    // كونستركتر 2: مع العنوان والمكان
    // ------------------------------------------------------
    public Meeting(int eID, String t, IDateTime sDT , IDateTime eDT, String loc )
    {
        EventId = eID;
        title = t;
        startDateTime = new DateTime (sDT);
        endDateTime = new DateTime (eDT);
        location = loc;
    }

    // ------------------------------------------------------
    // كونستركتر 3: كل شيء مع الطالب
    // ------------------------------------------------------
    public Meeting(int eID, String t, IDateTime sDT , IDateTime eDT, String loc, IStudent std )
    {
        EventId = eID;
        title = t;
        startDateTime = new DateTime (sDT);
        endDateTime = new DateTime (eDT);
        location = loc;
        student = new Student(std);  // نسخة جديدة من الطالب
    }

    // ------------------------------------------------------
    // كونستركتر 4: نسخ (Copy Constructor)
    // ينسخ كل شيء من اجتماع ثاني
    // ------------------------------------------------------
    public Meeting(Meeting M )
    {
        EventId = M.EventId;
        title = M.title;
        startDateTime = new DateTime (M.startDateTime);
        endDateTime = new DateTime (M.endDateTime);
        location = M.location;
        student = new Student( M.getStudent()); 
    }

    // ------------------------------------------------------
    // دوال جلب البيانات (Getters)
    // ------------------------------------------------------
    
    public int getEventId()
    {
        return EventId;  // رقم الاجتماع
    }

    public String getTitle()
    {
        return title;    // عنوان الاجتماع
    }

    // ------------------------------------------------------
    // دوال تعديل البيانات (Setters)
    // ------------------------------------------------------
    
    public void setTitle(String title)
    {
        this.title = title;   // تغيير العنوان
    }

    public IDateTime getStartDateTime()
    {
        return startDateTime;   // وقت البداية
    }

    public IDateTime getEndDateTime()
    {
        return endDateTime;     // وقت النهاية
    }

    public String getLocation()
    {
        return location;        // المكان
    }

    public void setLocation(String location)
    {
        this.location = location;  // تغيير المكان
    }

    // ------------------------------------------------------
    // تفحص إذا كان طالب معين مشارك في هذا الاجتماع
    // ------------------------------------------------------
    public boolean hasStudent(int studentId)
    {
        boolean hasStd = false;
        if (student != null)
            if (student.getStudentId() == studentId )
                hasStd = true;
        return hasStd;    
    }

    // ------------------------------------------------------
    // ترجع الاجتماع كـ نص مرتب للعرض
    // ------------------------------------------------------
    @Override
    public String toString()
    {
        String str = "\n";
        str +=  "Meeting ID: " + this.getEventId() + "\n";
        str +=  "Meeting title: " + this.getTitle() + "\n";
        str +=  "Meeting start date and time : " + this.getStartDateTime().format() + "\n";
        str +=  "Meeting end date and time : " + this.getEndDateTime().format() + "\n";
        str +=  "Meeting location : " + this.getLocation() + "\n";
        return str;
    }

    // ------------------------------------------------------
    // مقارنة الأحداث حسب العنوان (أبجدياً)
    // تستخدم في ترتيب القوائم
    // ------------------------------------------------------
    @Override
    public int compareTo(IEvent other)
    {
        return title.compareToIgnoreCase(other.getTitle());
    }

    // يرجع الطالب المخصص للاجتماع
    public IStudent getStudent()
    {
        return student;
    }

    // يخصص طالب للاجتماع
    public void setStudent(IStudent student)
    {
        this.student = student;
    }
}