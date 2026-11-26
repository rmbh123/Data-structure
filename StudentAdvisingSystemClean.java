package com.mycompany.studentadvisingsystemclean;

import java.util.Scanner;

// هذا هو المدخل الرئيسي للبرنامج
public class StudentAdvisingSystemClean {

    public static void main(String[] args) {
        
        Scanner input = new Scanner(System.in);      // جهاز قراءة المدخلات من المستخدم
        AdvisingSystem system = new AdvisingSystem(); // نشغل النظام

        int choice;  // خيار المستخدم

        // نرحب بالمستخدم
        System.out.println("Welcome to the Student Advising System!");

        // ------------------------------------------------------
        // القائمة الرئيسية: تتكرر لين يختار Exit (11)
        // ------------------------------------------------------
        do {
            // نعرض الخيارات
            System.out.println("\nPlease choose an option:");
            System.out.println("1. Add a student");
            System.out.println("2. Search for a student");
            System.out.println("3. Delete a student");
            System.out.println("4. Schedule a meeting/workshop");
            System.out.println("5. Print meeting/workshop details");
            System.out.println("6. Print students");
            System.out.println("7. Print all meetings/workshops alphabetically");
            System.out.println("8. Print workshop students");
            System.out.println("9. Load students from CSV");
            System.out.println("10. Load events from CSV");
            System.out.println("11. Exit");
            System.out.print("Enter your choice: ");

            choice = Integer.parseInt(input.nextLine());  // نقرأ الخيار

            // ------------------------------------------------------
            // حالة 1: إضافة طالب جديد
            // ------------------------------------------------------
            switch (choice) {
                case 1:
                    System.out.print("Enter the student's name: ");
                    String name = input.nextLine();

                    System.out.print("Enter the student's ID: ");
                    int id = Integer.parseInt(input.nextLine());

                    System.out.print("Enter the student's email address: ");
                    String email = input.nextLine();

                    System.out.print("Enter the student's major: ");
                    String major = input.nextLine();

                    System.out.print("Enter the student's year level: ");
                    int yearLevel = Integer.parseInt(input.nextLine());

                    System.out.print("Enter any notes for the student: ");
                    String notes = input.nextLine();

                    // نسوي طالب جديد
                    Student student = new Student(id);
                    student.setName(name);
                    student.setEmail(email);
                    student.setMajor(major);
                    student.setYearLevel(yearLevel);
                    student.setNotes(notes);

                    if (system.addStudent(student)) {
                        System.out.println("Student added successfully!");
                    } else {
                        System.out.println("Student could not be added.");
                    }
                    break;

                // ------------------------------------------------------
                // حالة 2: البحث عن طالب
                // ------------------------------------------------------
                case 2:
                    System.out.println("Enter search criteria:");
                    System.out.println("1. Name");
                    System.out.println("2. Student ID");
                    System.out.println("3. Email");
                    System.out.print("Enter your choice: ");
                    int searchChoice = Integer.parseInt(input.nextLine());

                    if (searchChoice == 1) {
                        System.out.print("Enter the student's name: ");
                        system.printStudentsByName(input.nextLine());

                    } else if (searchChoice == 2) {
                        System.out.print("Enter the student's ID: ");
                        int searchId = Integer.parseInt(input.nextLine());

                        IStudent found = system.searchStudentById(searchId);

                        if (found != null) {
                            System.out.println("Student found!");
                            System.out.println(found);
                        } else {
                            System.out.println("Student not found.");
                        }

                    } else if (searchChoice == 3) {
                        System.out.print("Enter the student's email: ");
                        String searchEmail = input.nextLine();

                        IStudent found = system.searchStudentByEmail(searchEmail);

                        if (found != null) {
                            System.out.println("Student found!");
                            System.out.println(found);
                        } else {
                            System.out.println("Student not found.");
                        }
                    }
                    break;

                // ------------------------------------------------------
                // حالة 3: حذف طالب
                // ------------------------------------------------------
                case 3:
                    System.out.print("Enter student ID to delete: ");
                    int deleteId = Integer.parseInt(input.nextLine());

                    if (system.deleteStudent(deleteId)) {
                        System.out.println("Student deleted successfully!");
                    } else {
                        System.out.println("Student not found.");
                    }
                    break;

                // ------------------------------------------------------
                // حالة 4: جدولة اجتماع أو ورشة عمل
                // ------------------------------------------------------
                case 4:
                    System.out.println("Enter type:");
                    System.out.println("1. Meeting");
                    System.out.println("2. Workshop");
                    System.out.print("Enter your choice: ");
                    int typeChoice = Integer.parseInt(input.nextLine());

                    System.out.print("Enter title: ");
                    String title = input.nextLine();

                    System.out.print("Enter start date and time (MM/DD/YYYY HH:MM): ");
                    IDateTime start = new DateTime(input.nextLine());

                    System.out.print("Enter end date and time (MM/DD/YYYY HH:MM): ");
                    IDateTime end = new DateTime(input.nextLine());

                    System.out.print("Enter location: ");
                    String location = input.nextLine();

                    if (typeChoice == 1) {
                        // اجتماع: طالب واحد
                        System.out.print("Enter student ID: ");
                        int meetingStudentId = Integer.parseInt(input.nextLine());

                        if (system.scheduleMeeting(title, start, end, location, meetingStudentId)) {
                            System.out.println("Meeting scheduled successfully!");
                        } else {
                            System.out.println("Meeting could not be scheduled.");
                        }

                    } else if (typeChoice == 2) {
                        // ورشة عمل: عدة طلاب
                        System.out.print("Enter student IDs separated by comma: ");
                        String[] idsText = input.nextLine().split(",");

                        int[] studentIds = new int[idsText.length];

                        for (int i = 0; i < idsText.length; i++) {
                            studentIds[i] = Integer.parseInt(idsText[i].trim());
                        }

                        if (system.scheduleWorkshop(title, start, end, location, studentIds)) {
                            System.out.println("Workshop scheduled successfully!");
                        } else {
                            System.out.println("Workshop could not be scheduled.");
                        }
                    }
                    break;

                // ------------------------------------------------------
                // حالة 5: طباعة تفاصيل حدث
                // ------------------------------------------------------
                case 5:
                    System.out.println("Enter search criteria:");
                    System.out.println("1. Student name");
                    System.out.println("2. Workshop/Meeting title");
                    System.out.print("Enter your choice: ");
                    int eventSearch = Integer.parseInt(input.nextLine());

                    if (eventSearch == 1) {
                        System.out.print("Enter student name: ");
                        system.printEventDetailsByStudentName(input.nextLine());

                    } else if (eventSearch == 2) {
                        System.out.print("Enter event title: ");
                        system.printEventDetailsByTitle(input.nextLine());
                    }
                    break;

                // ------------------------------------------------------
                // حالة 6: طباعة الطلاب
                // ------------------------------------------------------
                case 6:
                    System.out.println("Print students by:");
                    System.out.println("1. Name");
                    System.out.println("2. Partial name");
                    System.out.println("3. Major");
                    System.out.println("4. Year level");
                    System.out.println("5. All students");
                    System.out.print("Enter your choice: ");
                    int printChoice = Integer.parseInt(input.nextLine());

                    if (printChoice == 1) {
                        System.out.print("Enter full name: ");
                        system.printStudentsByName(input.nextLine());

                    } else if (printChoice == 2) {
                        System.out.print("Enter partial name: ");
                        system.printStudentsByPartialName(input.nextLine());

                    } else if (printChoice == 3) {
                        System.out.print("Enter major: ");
                        system.printStudentsByMajor(input.nextLine());

                    } else if (printChoice == 4) {
                        System.out.print("Enter year level: ");
                        system.printStudentsByYearLevel(Integer.parseInt(input.nextLine()));

                    } else if (printChoice == 5) {
                        system.printAllStudents();
                    }
                    break;

                // ------------------------------------------------------
                // حالة 7: طباعة كل الأحداث أبجدياً
                // ------------------------------------------------------
                case 7:
                    system.printAllEventsAlphabetically();
                    break;

                // ------------------------------------------------------
                // حالة 8: طباعة طلاب ورشة معينة
                // ------------------------------------------------------
                case 8:
                    System.out.print("Enter workshop title: ");
                    system.printWorkshopStudents(input.nextLine());
                    break;

                // ------------------------------------------------------
                // حالة 9: تحميل الطلاب من ملف CSV
                // ------------------------------------------------------
               
    case 9:
    if (system.loadStudentsFromCSV("/Users/raghad/Desktop/students_100.csv")) {
        System.out.println("Students loaded successfully!");
    } else {
        System.out.println("Students could not be loaded.");
    }
    break;

case 10:
    if (system.loadEventsFromCSV("/Users/raghad/Desktop/events_40.csv")) {
        System.out.println("Events loaded successfully!");
    } else {
        System.out.println("Events could not be loaded.");
    }
    break;

                // ------------------------------------------------------
                // حالة 11: خروج من البرنامج
                // ------------------------------------------------------
                case 11:
                    System.out.println("Goodbye!");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 11);  // نكرر لين يختار Exit

        input.close();  // نقفل جهاز القراءة
    }
}