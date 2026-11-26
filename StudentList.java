package com.mycompany.studentadvisingsystemclean;

// هذا الكلاس مسؤول عن تخزين الطلاب في قائمة مرتبة حسب الرقم الجامعي
public class StudentList implements IStudentList {
    
    // القائمة اللي فيها كل الطلاب
    public LinkedList <IStudent> students = new LinkedList <> ();
    
    
    //=============
    // ------------------------------------------------------
    // إضافة طالب جديد في المكان الصحيح (مرتب حسب الرقم)
    // ------------------------------------------------------
    @Override
    public boolean add(IStudent student)
    {
        // إذا القائمة فاضية، حط الطالب وخلصنا
        if ( students.empty())
        {
            students.insert(new Student (student));  // نضيف نسخة منه عشان نفكه من المين
            return true;
        }
        
        // نشوف أول طالب في القائمة
        students.findFirst();
        
        // إذا الرقم الجديد أصغر من أول طالب، حطه في البداية
        if (student.compareTo(students.retrieve()) < 0)
        {
            students.insertAtBegin(student);
            return true;
        }
        
        // نلف لين نلاقي المكان المناسب
        while ( ! students.last() && (student.compareTo(students.retrieve()) >= 0))
            students.findNext();

        // إذا لقينا نفس الرقم، نرجع false (ما نضيف)
        if (student.getStudentId() == students.retrieve().getStudentId())
            return false;
        
        // نحدد مكان الإضافة: قبل current أو بعده
        int comp = student.compareTo(students.retrieve());
        if (comp > 0 )
            students.insert(student);        // نحطه بعد current
        else 
            students.insertBefore(student);   // نحطه قبل current
        return true;
    }

    // ------------------------------------------------------
    // البحث عن طالب بالرقم الجامعي
    // ------------------------------------------------------
    @Override
    public IStudent findById(int studentId)
    {
        IStudent student = null;
        boolean Found = false;
        
        if ( ! students.empty())
        {
            students.findFirst();
        
            // نلف على القائمة لين نلقاه أو نخلص
            while ( ! students.last() && ! Found)
            {
                if ( studentId == students.retrieve().getStudentId())
                {
                    Found = true;
                    student = students.retrieve();
                }
                else   
                    students.findNext();
            }
            // نشوف آخر عنصر بعد
            if ( ! Found && (studentId == students.retrieve().getStudentId()))
            {
                Found = true;
                student = students.retrieve();
            }
        }
        return student;  // ترجع الطالب إذا لقته، null إذا ما لقته
    }

    // ------------------------------------------------------
    // البحث عن طلاب بالاسم (يطابق الاسم بالكامل)
    // ------------------------------------------------------
    @Override
    public LinkedList<IStudent> findByName(String fullName)
    {
        LinkedList<IStudent> data = new LinkedList<IStudent>();
        if ( ! students.empty())
        {
            students.findFirst();
            while ( ! students.last())
            {
                if ( students.retrieve().getName().equalsIgnoreCase(fullName))
                    data.insert(new Student(students.retrieve()));  // نضيف نسخة
                students.findNext();
            }
            // آخر عنصر
            if ( students.retrieve().getName().equalsIgnoreCase(fullName))
                data.insert(new Student(students.retrieve()));
        }
        return data;//يرجع لست فيها الاسما 
    }

    // ------------------------------------------------------
    // البحث عن طلاب يحتوي اسمهم على نص معين (بحث جزئي)
    // ------------------------------------------------------
    @Override
    public LinkedList<IStudent> findByNameContains(String partialName)
    {
        LinkedList<IStudent> data = new LinkedList<IStudent>();
        if ( ! students.empty())
        {
            students.findFirst();
            while ( ! students.last())
            {
                if ( students.retrieve().getName().contains(partialName))//من ضمن 
                    data.insert(students.retrieve());
                students.findNext();
            }
            if ( students.retrieve().getName().contains(partialName))
                data.insert(students.retrieve());
        }
        return data;//يرجع لست فيها الاسما 
    }

    // ------------------------------------------------------
    // البحث عن طالب بالإيميل
    // ------------------------------------------------------
    @Override
    public IStudent findByEmail(String email)
    {
        IStudent student = null;
        boolean Found = false;
        
        if ( ! students.empty())
        {
            students.findFirst();
            while ( ! students.last() && ! Found)
            {
                if ( email.compareToIgnoreCase(students.retrieve().getEmail()) == 0)
                {
                    Found = true;
                    student = students.retrieve();
                }
                else   
                    students.findNext();
            }
        }
        // نشوف آخر عنصر
        if ( email.compareToIgnoreCase(students.retrieve().getEmail()) == 0)
        {
            Found = true;
            student = students.retrieve();
        }
        return student;
    }

    // ------------------------------------------------------
    // ترجع كل الطلاب في تخصص معين
    // ------------------------------------------------------
    @Override
    public LinkedList<IStudent> findByMajor(String major)
    {
        LinkedList<IStudent> data = new LinkedList<IStudent>();
        if ( ! students.empty())
        {
            students.findFirst();
            while ( ! students.last())
            {
                if ( students.retrieve().getMajor().compareToIgnoreCase(major) == 0)
                    data.insert(students.retrieve());
                students.findNext();
            }
            if ( students.retrieve().getMajor().compareToIgnoreCase(major) == 0)
                data.insert(students.retrieve());
        }
        return data;
    }

    // ------------------------------------------------------
    // ترجع كل الطلاب في مستوى معين 
    // ------------------------------------------------------
    @Override
    public LinkedList<IStudent> findByYearLevel(int yearLevel)
    {
        LinkedList<IStudent> data = new LinkedList<IStudent>();
        if ( ! students.empty())
        {
            students.findFirst();
            while ( ! students.last())
            {
                if ( students.retrieve().getYearLevel() == yearLevel)
                    data.insert(students.retrieve());
                students.findNext();
            }
            if ( students.retrieve().getYearLevel() == yearLevel)
                data.insert(students.retrieve());
        }
        return data;
    }

    // ------------------------------------------------------
    // ترجع كل الطلاب في القائمة
    // ------------------------------------------------------
    @Override
    public LinkedList<IStudent> getAll()
    {
        return students;  // القائمة نفسها، ما نعمل نسخة جديدة
    }

    // ------------------------------------------------------
    // تحذف طالب بالرقم الجامعي
    // ------------------------------------------------------
    @Override
    public boolean deleteById(int studentId)
    {
        boolean Delete = false;
        if ( ! students.empty())
        {
            students.findFirst();
            while ( ! students.last() && ! Delete)
            {
                if ( studentId == students.retrieve().getStudentId())
                {
                    Delete = true;
                    students.remove();  // نحذف العنصر اللي واقفين عليه
                }
                else   
                    students.findNext();
            }
            // نشوف آخر عنصر
            if ( ! Delete && studentId == students.retrieve().getStudentId())
            {
                Delete = true;
                students.remove();
            }
        }
        return Delete;
    }

    // ------------------------------------------------------
    // كم عدد الطلاب في القائمة
    // ------------------------------------------------------
    @Override
    public int size()
    {
        return students.getSize();
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

    // هذي دالة زيادة بالغلط، ما نحتاجها
    public boolean add(Object student) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}