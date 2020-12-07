package model;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;


public class Application {


    public static void main(String[] args) {


        System.out.println("\n\n\n\t\t\t========= ~ WELCOME TO THE WORLD'S #1 TUTORING MANAGEMENT SYSTEM ~ =========");



        Application app = new Application();
        app.studentMenu();

    }


    //------------------------------------------------------------------------------------------------------------


    public static SessionFactory getSessionFactory() {

        Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties());
        SessionFactory sessionFactory = configuration
                .buildSessionFactory(builder.build());
        return sessionFactory;
    }


    //------------------------------------------------------------------------------------------------------------


    public void studentMenu()  {

        int choice = 0;
        int s_id = 0;
        Scanner input = new Scanner(System.in);


        System.out.println("\n\n****** STUDENT MENU ******\n");
        System.out.println("    1. ADD STUDENT & COURSE");
        System.out.println("    2. VIEW ALL STUDENTS");
        System.out.println("    3. EDIT STUDENT RECORD");
        System.out.println("    4. REMOVE STUDENT RECORD");
        System.out.println("    5. EXIT");
        System.out.print("\n Enter number here: ");

        try {
            choice = Integer.parseInt(input.nextLine());

        } catch (Exception e) {
            e.printStackTrace();
        }

        switch (choice) {
            case 1:
                addStudent();
                studentMenu();
                break;
            case 2:
                viewStudents();
                studentMenu();
                break;
            case 3:
                System.out.println("Enter StudentID to edit record");
                s_id = input.nextInt();
                editStudentRecord(s_id);
                studentMenu();
                break;
            case 4:
                removeStudentRecord();
                break;
            case 5:
                System.out.println("Exiting program....");
                break;

            default:
                System.out.println("Access denied, enter number between 1-5");
                studentMenu();
                break;
        }
    }

    // ------------------------------------------------------------------------------------------------------------



    public static void addStudent() {


        Scanner input = new Scanner(System.in);

        System.out.println("\n\n");
        System.out.println("\t\t\t\t\tADD STUDENT INFORMATION");
        System.out.println("---------------------------------------------------------------------");


        Student student = new Student();

        System.out.println("Enter student first name: ");
        String studentName = input.nextLine();
        student.setStudent_name(studentName);

        System.out.println("Available student courses: \n"
                + " \t Java \n " + " \t Python \n " + " \t SQL \n ");
        System.out.println("Enter student course name: ");
        String studentCourse = input.nextLine();
        student.setName(studentCourse);
        System.out.println("---------------------------------------------------------------------");


        Course course = new Course(student.getName());

        student.setCourses(Collections.singletonList(course));

        Session session = getSessionFactory().openSession();
        session.beginTransaction();
        session.save(student);
        session.save(course);

        session.getTransaction().commit();
        session.close();
        System.out.println("\n\nStudent record was added successfully\n");
    }

    //------------------------------------------------------------------------------------------------------------


    public void viewStudents() {

        Session session = getSessionFactory().openSession();
        Transaction tx = null;

        tx = session.beginTransaction();

        List students = session.createQuery("FROM Student").list();

        System.out.println("\n\n");
        System.out.println("\t\t\t\t\tALL STUDENT RECORDS");
        for (Object o : students) {
            Student student = (Student) o;
            System.out.println("---------------------------------------------------------------------");
            System.out.println("StudentID:                " + student.getId());
            System.out.println("Student Name:             " + student.getStudent_name());
            System.out.println("Student Course:           " + student.getName());
            System.out.println("---------------------------------------------------------------------");
        }
        session.getTransaction();
        tx.commit();
    }


    //------------------------------------------------------------------------------------------------------------


    public void editStudentRecord(int id)  {

        Session session = getSessionFactory().openSession();
        Scanner input = new Scanner(System.in);

        System.out.println("\n\n");
        System.out.println("\t\t\t\t\tEDIT STUDENT RECORD");
        System.out.println("---------------------------------------------------------------------");

        int option = 0;

        System.out.println("How would you like to edit student record? ");
        System.out.println("    1. Edit student name");
        System.out.println("    2. Update student course");
        System.out.println("    3. Return to Student Menu");
        System.out.print("\n Enter number: ");

        try {
            option = input.nextInt();
        } catch (Exception e) {
            e.printStackTrace();
        }

        switch (option) {

            // user clicks 1 - student name can be edited
            case 1:
                try {
                    Transaction tx = session.beginTransaction();
                    System.out.println("Enter new student name to update");
                    input.nextLine();
                    String newStudentName = input.nextLine();
                    Student stu = (Student) session.get(Student.class, id);
                    if(stu == null)
                        System.out.println("StudentID does not exist, re-enter correct id");
                    else {
                        stu.setStudent_name(newStudentName);
                        session.update(stu);
                        System.out.println("\nUpdated Student Details: ");
                        System.out.println("\tStudent Name - " + stu.getStudent_name());

                        tx.commit();
                    }
                }
                catch(Exception e) {
                    System.out.println(e);
                }
                finally {
                    System.out.println("Student name updated successfully");
                }
                studentMenu();
                break;

            // user clicks 2 - student course can be updated
            case 2:
                try {
                    Transaction tx = session.beginTransaction();
                    System.out.println("Enter new student course to update");
                    input.nextLine();
                    String newStudentCourse = input.nextLine();
                    Student stu = (Student) session.get(Student.class, id);
                    if(stu == null)
                        System.out.println("StudentID does not exist, re-enter correct id");
                    else {
                        stu.setName(newStudentCourse);
                        session.update(stu);
                        System.out.println("\nUpdated Student Details: ");
                        System.out.println("\tStudent Course - " + stu.getName());

                        tx.commit();
                    }
                }
                catch(Exception e) {
                    System.out.println(e);
                }
                finally {
                    System.out.println("Student course updated successfully");
                }
                studentMenu();
                break;

            // user clicks 3 - redirected back to Student Menu
            case 3:
                System.out.println("Returning to Student Menu...");
                studentMenu();
                break;
            default:
                System.out.println("Access denied, enter number between 1-3");
                studentMenu();
                break;
        }
    }




    //------------------------------------------------------------------------------------------------------------

    public void removeStudentRecord()  {

        Session session = getSessionFactory().openSession();
        Transaction tx = null;
        Scanner input = new Scanner(System.in);

        int x = 0;

        System.out.println("\n\n");
        System.out.println("\t\t\t\t\tREMOVE STUDENT RECORD");
        System.out.println("----------------------------------------------------------------------");

        session.beginTransaction();

        System.out.println("Enter StudentID to delete record ");
        int s_id = Integer.parseInt(input.nextLine());

        Query query = session.createQuery("delete from Student where id= :id");
        query.setInteger("id", s_id);

        query.executeUpdate();

        System.out.println("Deleted Student Details");

        session.getTransaction().commit();
        studentMenu();
    }



    //------------------------------------------------------------------------------------------------------------




}
