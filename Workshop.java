package com.mycompany.studentadvisingsystemclean;

// هذا الكلاس يمثل ورشة عمل - فيها عدة طلاب مشاركين
public class Workshop implements IWorkshop {
    
    public IStudentList students;      // قائمة الطلاب المشاركين في الورشة
    final int EventId;                 // رقم الورشة (ما يتغير)
    String title;                      // عنوان الورشة
    IDateTime startDateTime;           // وقت البداية
    IDateTime endDateTime;             // وقت النهاية
    String location;                   // المكان

    // ------------------------------------------------------
    // كونستركتر 1: أقل شيء، بس الرقم ووقت البداية والنهاية
    // ------------------------------------------------------
    public Workshop(int eID, IDateTime sDT , IDateTime eDT )
    {
        students = new StudentList();              // قائمة طلاب فاضية
        EventId = eID;
        startDateTime = new DateTime (sDT);       // نسخة جديدة
        endDateTime = new DateTime (eDT);         // نسخة جديدة
    }

    // ------------------------------------------------------
    // كونستركتر 2: مع العنوان والمكان
    // ------------------------------------------------------
    public Workshop(int eID, String t, IDateTime sDT , IDateTime eDT, String loc )
    {
        EventId = eID;
        title = t;
        startDateTime = new DateTime (sDT);
        endDateTime = new DateTime (eDT);
        location = loc;
        students = new StudentList();
    }

    // ------------------------------------------------------
    // كونستركتر 3: مع طالب واحد
    // ------------------------------------------------------
    public Workshop(int eID, String t, IDateTime sDT , IDateTime eDT, String loc, IStudent std )
    {
        EventId = eID;
        title = t;
        startDateTime = new DateTime (sDT);
        endDateTime = new DateTime (eDT);
        location = loc;
        students = new StudentList();
        students.add(std);  // نضيف الطالب للقائمة
    }

    // ------------------------------------------------------
    // كونستركتر 4: ينسخ ورشة عمل كاملة
    // ينسخ كل شيء، حتى الطلاب المشاركين
    // ------------------------------------------------------
    public Workshop(Workshop workshop )
    {
        EventId = workshop.EventId;
        title = workshop.title;
        startDateTime = new DateTime (workshop.startDateTime);
        endDateTime = new DateTime (workshop.endDateTime);
        location = workshop.location;

        students = new StudentList();
        // ننسخ كل الطلاب من الورشة القديمة للجديدة
        if (! workshop.getParticipants().empty())
        {
            workshop.getParticipants().findFirst();
            while ( ! workshop.getParticipants().last())
            {
                students.add(workshop.getParticipants().retrieve());
                workshop.getParticipants().findNext();
            }
            students.add(workshop.getParticipants().retrieve());
        }
    }
    
    // ------------------------------------------------------
    // جلب رقم الورشة
    // ------------------------------------------------------
    public int getEventId()
    {
        return EventId;
    }

    // جلب عنوان الورشة
    public String getTitle()
    {
        return title;
    }

    // تغيير عنوان الورشة
    public void setTitle(String title)
    {
        this.title = title ; 
    }

    // جلب وقت البداية
    public IDateTime getStartDateTime()
    {
        return startDateTime;
    }

    // جلب وقت النهاية
    public IDateTime getEndDateTime()
    {
        return endDateTime;
    }

    // جلب المكان
    public String getLocation()
    {
        return location;
    }

    // تغيير المكان
    public void setLocation(String location)
    {
        this.location = location;
    }

    // ------------------------------------------------------
    // تفحص إذا كان طالب معين مشارك في الورشة
    // ------------------------------------------------------
    public boolean hasStudent(int studentId)
    {
        return (students.findById(studentId) != null);  // إذا لقيناه يرجع true
    }

    // ------------------------------------------------------
    // ترجع الورشة كـ نص مرتب للعرض
    // ------------------------------------------------------
    @Override
    public String toString()
    {
        String str = "\n";
        str +=  "Workshop ID: " + this.getEventId() + "\n";
        str +=  "Workshop title: " + this.getTitle() + "\n";
        
        LinkedList<IStudent> stds = this.students.getAll();
        str += "Student name(s): " + "\n";
        if ( ! stds.empty())
        {
            stds.findFirst();
            while (! stds.last())
            {
                str += "    " + stds.retrieve().getName() + "("+ stds.retrieve().getStudentId() + "), \n";
                stds.findNext();
            }
            str += "    " + stds.retrieve().getName() + "("+ stds.retrieve().getStudentId() + ") "+ "\n";
        }
        str += "Workshop start date and time : " + this.getStartDateTime().format() + "\n";
        str += "Workshop end date and time : " + this.getEndDateTime().format() + "\n";
        str += "Workshop location : " + this.getLocation() + "\n";
        return str;
    }

    // ------------------------------------------------------
    // مقارنة الأحداث حسب العنوان (للترتيب الأبجدي)
    // ------------------------------------------------------
    @Override
    public int compareTo(IEvent other)
    {
        return title.compareToIgnoreCase(other.getTitle());
    }

    // ------------------------------------------------------
    // ترجع قائمة المشاركين في الورشة
    // ------------------------------------------------------
    @Override
    public LinkedList<IStudent> getParticipants()
    {
        return students.getAll();
    }

    // ------------------------------------------------------
    // تضيف طالب للورشة
    // ------------------------------------------------------
    @Override
    public boolean addParticipant(IStudent student)
    {
        return students.add(student);  // StudentList يمنع التكرار تلقائياً
    }

    // ------------------------------------------------------
    // تشيل طالب من الورشة باستخدام رقمه
    // ------------------------------------------------------
    @Override
    public boolean removeParticipantById(int studentId)
    {
        return students.deleteById(studentId);
    }

    // ------------------------------------------------------
    // تفحص إذا الورشة فاضية (ما فيها طلاب)
    // ------------------------------------------------------
    @Override
    public boolean isEmpty()
    {
        return (students.size() == 0);
    }
}