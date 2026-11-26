package com.mycompany.studentadvisingsystemclean;

public class AdvisingSystem implements IAdvisingSystemPhase1 {

    private StudentList students;  // هذي فيها كل الطلاب
    private EventList events;      // هذي فيها الاجتماعات وورش العمل

    // لمن نفتح البرنامج، نسوي قائمة طلاب فاضية وقائمة أحداث فاضية
    public AdvisingSystem() {
        students = new StudentList();
        events = new EventList();
    }
    
    // ------------------------ إدارة الطلاب ------------------------
    
    // أضف طالب جديد
    public boolean addStudent(IStudent student) {
        if (student == null) {
            return false;  // إذا كان فاضي، وش تبي تضبط؟
        }
        return students.add(student);  // حطه في القائمة
    }

    // دور على طالب برقمه الجامعي
    public IStudent searchStudentById(int studentId) {
        return students.findById(studentId);  // أسأل القائمة عنه
    }

    // دور على طالب بإيميله
    public IStudent searchStudentByEmail(String email) {
        return students.findByEmail(email);
    }

    // ------------------------ طباعة الطلاب ------------------------
    
    // هذي دالة خاصه، تساعدني اطبع أي قائمة طلاب
    private void printStudentList(LinkedList<IStudent> list) {
        if (list.empty()) {
            System.out.println("ما فيه طلاب.");
            return;
        }

        list.findFirst();
        while (true) {
            System.out.println(list.retrieve());  // اطبع اللي واقف عنده
            if (list.last()) {
                break;  // إذا هو آخر واحد، خلاص طلع
            }
            list.findNext();  // انتقل للي بعده
        }
    }
    
    // اطبع الطلاب حسب تخصصهم
    public void printStudentsByMajor(String major) {
        LinkedList<IStudent> result = students.findByMajor(major);
        printStudentList(result);
    }

    // اطبع الطلاب حسب مستواهم (سنة أولى، ثاني، ثالث، رابع)
    public void printStudentsByYearLevel(int yearLevel) {
        LinkedList<IStudent> result = students.findByYearLevel(yearLevel);
        printStudentList(result);
    }

    // اطبع طالب بالاسم بالضبط
    public void printStudentsByName(String fullName) {
        LinkedList<IStudent> result = students.findByName(fullName);
        printStudentList(result);
    }

    // اطبع الطلاب اللي اسمهم فيه كلمة معينه (مثل "محمد" يطلع لك كل من اسمه محمد)
    public void printStudentsByPartialName(String partialName) {
        LinkedList<IStudent> result = students.findByNameContains(partialName);
        printStudentList(result);
    }

    // اطبع كل الطلاب اللي في النظام
    public void printAllStudents() {
        LinkedList<IStudent> result = students.getAll();
        printStudentList(result);
    }
//===============
    // ------------------------ حذف طالب (وهذي أهم وحده) ------------------------
    
    // لمن أحذف طالب، لازم أشيله من كل مكان:
    // - أشيله من الاجتماعات
    // - أشيله من ورش العمل
    // - إذا الورشة صارت فاضية، أشيل الورشة بعد
    public boolean deleteStudent(int studentId) {
        IStudent student = students.findById(studentId);
        if (student == null) {
            return false;  // الطالب أصلاً مو موجود
        }

        // جيب كل الأحداث اللي في الدنيا
        LinkedList<IEvent> allEvents = events.getAllAlphabetically();

        if (!allEvents.empty()) {
            allEvents.findFirst();
            while (true) {
                IEvent event = allEvents.retrieve();

                // إذا كان الطالب مشترك في هذا الحدث
                if (event.hasStudent(studentId)) {
                    
                    if (event instanceof Meeting) {
                        // إذا كان اجتماع، أرميه بالكامل
                        events.removeEventById(event.getEventId());
                    } 
                    else if (event instanceof Workshop) {
                        // إذا كان ورشة، أشيل الطالب منها
                        Workshop workshop = (Workshop) event;
                        workshop.removeParticipantById(studentId);
                        
                        // إذا الورشة بقيت فاضية (ما فيها أحد)، أشيلها بعد
                        if (workshop.isEmpty()) {
                            events.removeEventById(workshop.getEventId());
                        }
                    }
                }

                if (allEvents.empty() || allEvents.last()) {
                    break;
                }
                allEvents.findNext();
            }
        }

        // أخيراً، أشيل الطالب من قائمة الطلاب
        return students.deleteById(studentId);
    }

    // ------------------------ قراءة ملفات CSV ------------------------
    
    // أقرأ ملف الطلاب من CSV
   public boolean loadStudentsFromCSV(String studentsFilePath) 
   {
    try {
        java.io.File file = new java.io.File(studentsFilePath);
        java.util.Scanner input = new java.util.Scanner(file);

        // أضيفي هذا السطر عشان يتخطى أول سطر (العناوين)
        if (input.hasNextLine()) {
            input.nextLine();  // نتخطى سطر headers
        }

        while (input.hasNextLine()) {
            String line = input.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] data = line.split(",");

            int id = Integer.parseInt(data[0].trim());
            Student student = new Student(id);
            
            student.setName(data[1].trim());
            student.setEmail(data[2].trim());
            student.setMajor(data[3].trim());
            student.setYearLevel(Integer.parseInt(data[4].trim()));

            if (data.length > 5) {
                student.setNotes(data[5].trim());
            }

            addStudent(student);
        }

        input.close();
        return true;

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}

    // أقرأ ملف الأحداث من CSV
  public boolean loadEventsFromCSV(String eventsFilePath) {
    try {
        java.io.File file = new java.io.File(eventsFilePath);
        java.util.Scanner input = new java.util.Scanner(file);

        // تخطي سطر العناوين
        if (input.hasNextLine()) {
            input.nextLine();
        }

        while (input.hasNextLine()) {
            String line = input.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] data = line.split(",");
            
            // تخطي السطور الناقصة
            if (data.length < 6) {
                continue;
            }

            String type = data[0].trim();
            String title = data[1].trim();
            
            IDateTime start = null;
            IDateTime end = null;
            
            try {
                String startStr = data[2].trim();
                String endStr = data[3].trim();
                
                if (!startStr.isEmpty() && !endStr.isEmpty()) {
                    start = new DateTime(startStr);
                    end = new DateTime(endStr);
                } else {
                    continue;
                }
            } catch (Exception ex) {
                continue;
            }
            
            String location = data[4].trim();

            if (type.equalsIgnoreCase("meeting")) {
                int studentId = Integer.parseInt(data[5].trim());
                scheduleMeeting(title, start, end, location, studentId);
            }
            else if (type.equalsIgnoreCase("workshop")) {
                String[] ids = data[5].split(";");
                int[] studentIds = new int[ids.length];
                for (int i = 0; i < ids.length; i++) {
                    studentIds[i] = Integer.parseInt(ids[i].trim());
                }
                scheduleWorkshop(title, start, end, location, studentIds);
            }
        }

        input.close();
        return true;

    } catch (Exception e) {
        return false;
    }
}
    // ------------------------ طباعة طلاب ورشة معينة ------------------------
    
    public void printWorkshopStudents(String workshopTitle) {
        LinkedList<IEvent> result = events.findByTitle(workshopTitle);

        if (result.empty()) {
            System.out.println("الورشة مو موجودة.");
            return;
        }

        result.findFirst();
        while (true) {
            IEvent event = result.retrieve();

            if (event instanceof Workshop) {
                LinkedList<IStudent> students = ((Workshop) event).getParticipants();

                if (!students.empty()) {
                    students.findFirst();
                    while (true) {
                        System.out.println(students.retrieve());
                        if (students.last()) break;
                        students.findNext();
                    }
                }
            }

            if (result.last()) break;
            result.findNext();
        }
    }

    // ------------------------ جدولة اجتماع ------------------------
    
    public boolean scheduleMeeting(String title, IDateTime startDateTime, IDateTime endDateTime, 
                                   String location, int studentId) {
        IStudent student = students.findById(studentId);
        if (student == null) {
            return false;  // الطالب مو موجود
        }

        // تفضل، شف جدول الطالب، فيه شيء في هذا الوقت؟
        LinkedList<IEvent> schedule = student.getSchedule();
        if (!schedule.empty()) {
            schedule.findFirst();
            while (true) {
                IEvent event = schedule.retrieve();
                
                // إذا الوقت الجديد يزاحم وقت قديم، هذا تعارض
                boolean conflict = startDateTime.compareTo(event.getEndDateTime()) < 0
                                && endDateTime.compareTo(event.getStartDateTime()) > 0;
                
                if (conflict) {
                    return false;  // تعارض، ما نقدر نضيف
                }

                if (schedule.last()) break;
                schedule.findNext();
            }
        }

        int eventId = events.size() + 1;  // رقم جديد للحدث
        Meeting meeting = new Meeting(eventId, title, startDateTime, endDateTime, location, student);

        events.addEvent(meeting);               // حط الاجتماع في القائمة العامة
        student.getSchedule().insert(meeting);  // حطه في جدول الطالب

        return true;
    }

    // ------------------------ جدولة ورشة عمل ------------------------
    
    public boolean scheduleWorkshop(String title, IDateTime startDateTime, IDateTime endDateTime,
                                    String location, int[] studentIds) {
        if (studentIds == null || studentIds.length == 0) {
            return false;  // ولا طالب واحد! وش تبي تسوي ورشة؟
        }

        // تفضل، تأكد إن كل الطلاب اللي أدخلتهم موجودين
        IStudent[] workshopStudents = new IStudent[studentIds.length];
        for (int i = 0; i < studentIds.length; i++) {
            workshopStudents[i] = students.findById(studentIds[i]);
            if (workshopStudents[i] == null) {
                return false;  // هذا الطالب مو موجود
            }
        }

        // تأكد إن كل طالب فاضي في هذا الوقت
        for (int i = 0; i < workshopStudents.length; i++) {
            LinkedList<IEvent> schedule = workshopStudents[i].getSchedule();
            if (!schedule.empty()) {
                schedule.findFirst();
                while (true) {
                    IEvent event = schedule.retrieve();
                    
                    boolean conflict = startDateTime.compareTo(event.getEndDateTime()) < 0
                                    && endDateTime.compareTo(event.getStartDateTime()) > 0;
                    
                    if (conflict) {
                        return false;  // تعارض مع هذا الطالب
                    }

                    if (schedule.last()) break;
                    schedule.findNext();
                }
            }
        }

        int eventId = events.size() + 1;
        Workshop workshop = new Workshop(eventId, title, startDateTime, endDateTime, location);

        // حط كل الطلاب في الورشة
        for (int i = 0; i < workshopStudents.length; i++) {
            workshop.addParticipant(workshopStudents[i]);
            workshopStudents[i].getSchedule().insert(workshop);  // حط الورشة في جدول الطالب
        }

        events.addEvent(workshop);  // حط الورشة في القائمة العامة
        return true;
    }

    // ------------------------ طباعة الأحداث ------------------------
    
    // دالة خاصه تطبع أي قائمة أحداث
    private void printEventList(LinkedList<IEvent> list) {
        if (list.empty()) {
            System.out.println("ما فيه أحداث.");
            return;
        }

        list.findFirst();
        while (true) {
            System.out.println(list.retrieve());
            if (list.last()) break;
            list.findNext();
        }
    }

    public void printEventDetailsByTitle(String title) {
        LinkedList<IEvent> result = events.findByTitle(title);
        printEventList(result);
    }

    public void printEventDetailsByStudentName(String studentName) {
        LinkedList<IEvent> result = events.findByStudentName(studentName);
        printEventList(result);
    }

    public void printAllEventsAlphabetically() {
        LinkedList<IEvent> result = events.getAllAlphabetically();
        printEventList(result);
    }
}