package com.mycompany.studentadvisingsystemclean;

// هذي العقدة (Node): كل عنصر في القائمة
// فيها البيانات ومرجع للعنصر اللي بعده
class Node<T> {
    public T data;        // البيانات
    public Node<T> next;  // إشارة للعقدة اللي بعدها

    public Node(T val) {
        data = val;
        next = null;
    }
}

// هذا الكلاس مسؤول عن القائمة المرتبطة
public class LinkedList<T> implements List<T> {
    
    private Node<T> head;     // أول عنصر في القائمة
    private Node<T> current;  // العنصر اللي أنا واقف عليه حالياً

    // لمن ننشئ قائمة جديدة، تكون فاضية
    public LinkedList() {
        head = current = null;
    }

    // القائمة فاضية؟ إذا head == null يعني ما فيه عناصر
    public boolean empty() {
        return head == null;
    }

    // هل أنا واقف على آخر عنصر؟
    public boolean last() {
        return current.next == null;
    }

    // القائمة مليانة؟ لا، أبداً! نقدر نضيف لين نشبع
    public boolean full() {
        return false;
    }

    // روح لأول عنصر في القائمة
    public void findFirst() {
        current = head;
    }

    // روح للعنصر اللي بعدي
    public void findNext() {
        current = current.next;
    }

    // وش البيانات اللي عندي حالياً؟
    public T retrieve() {
        return current.data;
    }

    // غير البيانات اللي عندي
    public void update(T val) {
        current.data = val;
    }

    // ------------------------------------------------------
    // أضف عنصر جديد بعد العنصر اللي أنا واقف عليه
    // ------------------------------------------------------
    public void insert(T val) {
        Node<T> tmp;
        if (empty()) {
            // إذا كانت القائمة فاضية، العنصر الجديد يكون أول عنصر وآخر عنصر
            current = head = new Node<T>(val);
        } else {
            // خزّن اللي بعدي في مكان مؤقت
            tmp = current.next;
            // حط العنصر الجديد بعدي
            current.next = new Node<T>(val);
            // انقل للعنصر الجديد
            current = current.next;
            // ربطه باللي كان بعدي أول
            current.next = tmp;
        }
    }

    // أضف عنصر في أول القائمة
    public void insertAtBegin(T val) {
        Node<T> tmp = new Node<T>(val);
        tmp.next = head;   // العنصر الجديد يشير لأول عنصر قديم
        head = current = tmp;  // العنصر الجديد صار هو الأول
    }

    // أضف عنصر قبل العنصر اللي أنا واقف عليه
    public void insertBefore(T val) {
        if (empty() || current == head) {
            // إذا القائمة فاضية أو أنا في البداية، أضف في البداية
            insertAtBegin(val);
        } else {
            // أدور على العنصر اللي قبلي
            Node<T> tmp = head;
            while (tmp.next != current) {
                tmp = tmp.next;
            }
            // أسوي عقدة جديدة
            Node<T> newNode = new Node<T>(val);
            // أربطها
            newNode.next = current;
            tmp.next = newNode;
            current = newNode;
        }
    }

    // ------------------------------------------------------
    // أحذف العنصر اللي أنا واقف عليه
    // ------------------------------------------------------
    @Override
    public void remove() {
        if (current == head) {
            // إذا أنا أول عنصر، خلاص أول عنصر يصير اللي بعده
            head = head.next;
        } else {
            // أدور على العنصر اللي قبلي
            Node<T> tmp = head;
            while (tmp.next != current) {
                tmp = tmp.next;
            }
            // أشيل العنصر الحالي
            tmp.next = current.next;
        }
        // أحرك current لمكان مناسب
        if (current.next == null) {
            current = head;  // إذا كنت آخر عنصر، روح لأول عنصر
        } else {
            current = current.next;  // غير ذلك، روح للعنصر اللي بعدي
        }
    }

    // ------------------------------------------------------
    // كم عدد العناصر في القائمة؟
    // ------------------------------------------------------
    public int getSize() {
        int count = 0;
        Node<T> tmp = head;
        while (tmp != null) {
            count++;
            tmp = tmp.next;
        }
        return count;
    }
}