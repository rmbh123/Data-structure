package com.mycompany.studentadvisingsystemclean;

// هذا الكلاس مسؤول عن تخزين وإدارة كل الأحداث
// (الاجتماعات وورش العمل) في قائمة مرتبة أبجدياً
public class EventList implements IEventList {

    // هذي القائمة اللي فيها كل الأحداث
    LinkedList <IEvent > events = new LinkedList <IEvent > ();

    // ------------------------------------------------------
    // إضافة حدث جديد
    // الفكرة: نخلي القائمة مرتبة أبجدياً حسب العنوان
    // ------------------------------------------------------
    @Override
    public boolean addEvent(IEvent event)
    {
        // إذا كانت القائمة فاضية، حط الحدث وخلصنا
        if (events.empty())
        {
             events.insert(event);
             return true;
        }
        
        // أول شيء: نشوف إذا الحدث موجود مسبقاً ولا لأ
        boolean found = false;

        events.findFirst();
        while ( !events.last() && ! found)
        {
            if ( events.retrieve().getEventId() == event.getEventId())
                found = true;   // لقيناه موجود
            else
                events.findNext();
        }
        if ( !found && events.retrieve().getEventId() == event.getEventId())
            found = true;

        boolean added = false;

        // إذا الحدث غير موجود نضيفه في المكان المناسب (مرتب أبجدياً)
        if ( !found ) 
        {
            events.findFirst();
            // إذا الجديد أصغر من أول عنصر، حطه في البداية
            if (events.retrieve().compareTo(event) > 0)
                events.insertAtBegin(event);
            else 
            {
                // نلف لين نلاقي المكان المناسب
                while ( ! events.last() && events.retrieve().compareTo(event) <= 0)
                        events.findNext();
                
                int comp = events.retrieve().compareTo(event);
                if (comp <= 0 )
                    events.insert(event);      // نحط الحدث بعد الحالي
                else 
                    events.insertBefore(event); // نحطه قبل الحالي
            }
            added = true;
        }
        // إذا الحدث موجود وكان ورشة عمل (Workshop) نضيف الطالب الجديد للورشة الموجودة
        else if ( event instanceof Workshop ) 
        {
            IEvent wevent = events.retrieve();  // نمسك الورشة القديمة
            
            // نجيب الطالب الجديد اللي تبغى تضيفه
            LinkedList <IStudent> stds = ((Workshop) event).getParticipants();
            stds.findFirst();
            int new_std = stds.retrieve().getStudentId();

            // إذا الطالب مو موجود في الورشة، نضيفه
            if (! ((Workshop) wevent).hasStudent(new_std))
            {
                ((Workshop)wevent).addParticipant(stds.retrieve());
                events.update(wevent);  // نحدث الورشة في القائمة
                added = true;
            }
        }
        return added;
    }
    
    // ------------------------------------------------------
    // حذف حدث بواسطة رقمه
    // ------------------------------------------------------
    @Override
    public boolean removeEventById(int eventId)
    {
        boolean removed = false;
        
        if ( ! events.empty())
        {
            events.findFirst();
            while ( ! events.last() )
            {
                if (events.retrieve().getEventId() == eventId)
                {
                    events.remove();    // إذا لقيناه، نشيله
                    removed = true;
                }
                else
                    events.findNext();
            }
            // نشوف آخر عنصر بعد
            if (events.retrieve().getEventId() == eventId)
            {
                events.remove();
                removed = true;
            }
        }
        return removed;
    }

    // ------------------------------------------------------
    // ترجع كل الأحداث مرتبة أبجدياً
    // ------------------------------------------------------
    @Override
    public LinkedList<IEvent> getAllAlphabetically()
    {
         return events;  // القائمة أصلاً مرتبة، نرجعها زي ما هي
    }

    // ------------------------------------------------------
    // تدور على الأحداث اللي عنوانها مطابق
    // ------------------------------------------------------
    @Override
    public LinkedList<IEvent> findByTitle(String title)
    {
        LinkedList<IEvent> result = new LinkedList<IEvent>();
        if ( ! events.empty())
        {
            events.findFirst();
            while ( ! events.last() )
            {
                if (events.retrieve().getTitle().compareToIgnoreCase(title) == 0)
                    result.insert(events.retrieve());  // إذا العنوان طابق، نضيفه للنتيجة
                events.findNext();
            }
            if (events.retrieve().getTitle().compareToIgnoreCase(title) == 0)
                result.insert(events.retrieve());
        }
        return result;
    }

    // ------------------------------------------------------
    // تدور على الأحداث اللي فيها طالب معين (باسمه)
    // ------------------------------------------------------
    @Override
    public LinkedList<IEvent> findByStudentName(String studentFullName)
    {
        LinkedList <IEvent> result = new LinkedList<IEvent>();
        
        if ( ! events.empty())
        {
            events.findFirst();
            while ( ! events.last() )
            {
                // إذا كان الحدث اجتماع (Meeting)
                if (events.retrieve() instanceof Meeting)
                 {
                    if ( ((Meeting) events.retrieve()).getStudent().getName().equalsIgnoreCase(studentFullName))
                        result.insert(events.retrieve());
                } 
                else  // إذا كان ورشة عمل (Workshop)
                {
                    // نجيب كل المشاركين في الورشة
                    LinkedList<IStudent> stds = ((Workshop) events.retrieve()).getParticipants();
                    if (! stds.empty())
                    {
                        boolean found = false;
                        stds.findFirst();
                        while ( ! stds.last() && ! found)
                        {
                            if (stds.retrieve().getName().equalsIgnoreCase(studentFullName))
                            {
                                result.insert(events.retrieve());  // نضيف الورشة للنتيجة
                                found = true;
                            }
                            stds.findNext();
                        }
                        if ( !found && stds.retrieve().getName().equalsIgnoreCase(studentFullName))
                        {
                            result.insert(events.retrieve());
                        }
                    }
                }
                events.findNext();
            }
            // نفس الشي لآخر عنصر
            if (events.retrieve() instanceof Meeting)
             {
                if ( ((Meeting) events.retrieve()).getStudent().getName().equalsIgnoreCase(studentFullName))
                    result.insert(events.retrieve());
            } 
            else
            {
                LinkedList<IStudent> stds = ((Workshop) events.retrieve()).getParticipants();
                if (! stds.empty())
                {
                    boolean found = false;
                    stds.findFirst();
                    while ( ! stds.last() && ! found)
                    {
                        if (stds.retrieve().getName().equalsIgnoreCase(studentFullName))
                        {
                            result.insert(events.retrieve());
                            found = true;
                        }
                        stds.findNext();
                    }
                    if ( !found && stds.retrieve().getName().equalsIgnoreCase(studentFullName) )
                        result.insert(events.retrieve());
                }
            }
        }
        return result;
    }

    // ------------------------------------------------------
    // كم عدد الأحداث في القائمة؟
    // ------------------------------------------------------
    @Override
    public int size()
    {
         return events.getSize();
    }
}