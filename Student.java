package com.mycompany.studentadvisingsystemclean;

// هذا الكلاس يمثل طالب في نظام الإرشاد
public class Student implements IStudent {
    
    // الرقم الجامعي - نهائي ما يتغير أبداً
    final int STUDENTID;
    String studentName;      // اسم الطالب
    String email;            // الإيميل
    String major;            // التخصص
    int yearLevel;           // المستوى (سنة أولى، ثاني، إلخ)
    String notes;            // ملاحظات
    LinkedList<IEvent> sEvents;  // جدول الطالب لست (الأحداث المسجل فيها)

    // ------------------------------------------------------
    // كونستركتر 1: ينشئ طالب بالرقم فقط
    // ------------------------------------------------------
    public Student(int ID)
    {
        STUDENTID = ID;                    // أحط الرقم
        sEvents = new LinkedList<IEvent> ();  // جدوله فاضي
    }
     
    // ------------------------------------------------------
    // كونستركتر 2: ينسخ طالب من طالب ثاني (Copy Constructor)
    // ينسخ كل شيء، حتى الأحداث اللي في جدوله
    // ------------------------------------------------------
    public Student(IStudent std)
    {
        STUDENTID = std.getStudentId();     // أنسخ الرقم
        studentName = std.getName();        // أنسخ الاسم
        email = std.getEmail();             // أنسخ الإيميل
        major = std.getMajor();             // أنسخ التخصص
        yearLevel = std.getYearLevel();     // أنسخ المستوى
        notes = std.getNotes();             // أنسخ الملاحظات

        sEvents = new LinkedList<IEvent> ();  // جدول جديد
        IEvent event;
        
        // إذا كان الطالب الأصلي عنده أحداث في جدوله كثيره ، أنسخها كلها
        if (! std.getSchedule().empty())
        {
            std.getSchedule().findFirst();
            while ( ! std.getSchedule().last())
            {
                // إذا كان الحدث Meeting، أنسخه كـ Meeting
                if (std.getSchedule().retrieve() instanceof Meeting)
                    event = new Meeting ((Meeting)(std.getSchedule().retrieve()));
                else
                    event = new Workshop ((Workshop)(std.getSchedule().retrieve()));
                
                sEvents.insert(event);  // حط الحدث الجديد في الجدول
                std.getSchedule().findNext();
            }
            // آخر عنصر بروح ننسخه
            if (std.getSchedule().retrieve() instanceof Meeting)
                event = new Meeting ((Meeting)(std.getSchedule().retrieve()));
            else
                event = new Workshop ((Workshop)(std.getSchedule().retrieve()));
            sEvents.insert(event);
        }
    }
    
    // ------------------------------------------------------
    // جلب الاسم
    // ------------------------------------------------------
    @Override
    public String getName()
    {
        return studentName;
    }

    // تغيير الاسم (مع إزالة المسافات الزايدة)
    @Override
    public void setName(String name)
    {
        studentName = name.trim();
    }

    //  الرقم الجامعي
    @Override
    public int getStudentId()
    {
        return STUDENTID;
    }

    //  الإيميل
    @Override
    public String getEmail()
    {
        return email;
    }

    // تغيير الإيميل
    @Override
    public void setEmail(String email)
    {
        this.email = email;
    }

    //  التخصص
    @Override
    public String getMajor()
    {
        return major;
    }

    // تغيير التخصص
    @Override
    public void setMajor(String major)
    {
        this.major = major;
    }

    //  المستوى
    @Override
    public int getYearLevel()
    {
        return yearLevel;
    }

    // تغيير المستوى
    @Override
    public void setYearLevel(int yearLevel)
    {
        this.yearLevel = yearLevel;
    }

    //  الملاحظات
    @Override
    public String getNotes()
    {
        return notes;
    }

    // تغيير الملاحظات
    @Override
    public void setNotes(String notes)
    {
        this.notes = notes;
    }

    // ------------------------------------------------------
    // تجيب الاسم الأول فقط (أول كلمة في الاسم)
    // ------------------------------------------------------
    @Override
    public String getFirstName()
    {
        return studentName.substring(0, studentName.indexOf(" "));
    }

    // ترجع جدول الطالب (كل الأحداث المسجل فيها)
    @Override
    public LinkedList<IEvent> getSchedule()
    {
        return sEvents;
    }

    // ------------------------------------------------------
    // ترجع بيانات الطالب كـ نص مرتب
    // ------------------------------------------------------
    @Override
    public String toString()
    {
        String str = "" ;
        
        str += "Name: " + this.studentName + "\n";
        str += "Student ID: " + this.STUDENTID + "\n";
        str += "Email Address: " + this.email + "\n";
        str += "Major: " + this.major + "\n";
        str += "Year Level:" + this.yearLevel + "\n";
        str += "Notes: " + this.notes + "\n";
        
        str += "Student events Schedules : " + "\n";
        if (sEvents.empty())
            str += "No Scheduled Events. " + "\n";
        else
        {
            sEvents.findFirst();
            while ( ! sEvents.last())
            {
                str += sEvents.retrieve().getTitle() + "  (" + sEvents.retrieve().getEventId() + ")  \n";
                sEvents.findNext();
            }
            str += sEvents.retrieve().getTitle() + "  (" + sEvents.retrieve().getEventId() + ")  \n";
        }
        return str;
    }

    // ------------------------------------------------------
    // مقارنة طالبين حسب الرقم الجامعي
    // تستخدم للترتيب
    // ------------------------------------------------------
    @Override
    public int compareTo(IStudent other)
    {
        if (this.getStudentId() == other.getStudentId())
            return 0;    // نفس الرقم
        
        if (this.getStudentId() < other.getStudentId())
            return -1;   // هذا أصغر
        
        return 1;        // هذا أكبر
    }
}