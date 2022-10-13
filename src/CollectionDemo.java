/*
The text file students.txt is given, each line containing the student's name and the symbol of the course he or she is attending.
A student may be enrolled in several subjects (i.e. his / her name may appear many times in the file.)
Based on the data contained in the file, a structure based on one of the Map collections was created,
the keys of which are the symbols of the subjects and the values of the list of students attending a given subject.

Following methods have been implemented to test the structure:

int numberOfClasses() - returns the number of items
LinkedList<String> studentsAttendingClass(String classCode) - returns an alphabetical list of students attending the course
int bigClasses(int limit) - returns the number of subjects attended by more than the student limit
LinkedList<String> bigClassesList(int limit) - returns a list of subjects attended by more than the student limit
int classNumber(String student) - returns the number of subjects attended by the given student
LinkedList<String> classList(String student) - returns a list of subjects attended by a given student

*/

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CollectionDemo {

    private static final Map<String, LinkedList<String>> studentsMap = new TreeMap<>();

    private static void addListToMap(String ClassCode, String name) {
        studentsMap.computeIfAbsent(ClassCode, employeeList -> new LinkedList<>());
        Collections.addAll(studentsMap.get(ClassCode), name);
    }

    /*
        private static void printMap() {  // display map
            studentsMap.forEach((ClassCode, students) -> System.out.println(ClassCode + " : " + students));
            System.out.println();
        }
    */
    private static int numberOfClasses() {
        return studentsMap.size();
    }

    private static LinkedList<String> studentsAttendingClass(String classCode) {
        LinkedList<String> sortedList = studentsMap.get(classCode);
        Collections.sort(sortedList);
        return sortedList;
    }

    private static int bigClasses(int limit) {
        int bigCounter = 0;
        // Obtaining a TreeMap key or TreeMap value using an array.
        Set<Map.Entry<String, LinkedList<String>>> studentsSet
                = studentsMap.entrySet();

        Map.Entry<String, LinkedList<String>>[] studentsArray
                = studentsSet.toArray(
                new Map.Entry[studentsSet.size()]);

        for (int i = 0; i < studentsArray.length; i++) {
            if (studentsArray[i].getValue().size() > limit) {
                bigCounter++;
            }
        }
        return bigCounter;
    }

    private static LinkedList<String> bigClassesList(int limit) {
        LinkedList<String> bigList = new LinkedList<>();
        Set<Map.Entry<String, LinkedList<String>>> studentsSet
                = studentsMap.entrySet();

        Map.Entry<String, LinkedList<String>>[] studentsArray
                = studentsSet.toArray(
                new Map.Entry[studentsSet.size()]);

        for (int i = 0; i < studentsArray.length; i++) {
            if (studentsArray[i].getValue().size() > limit) {
                bigList.add(studentsArray[i].getKey());
            }
        }
        return bigList;
    }

    static int classNumber(String student) {
        AtomicInteger atom = new AtomicInteger();
        studentsMap.forEach((attendedClass, students) -> {
            students.forEach(atendee -> {
                if (atendee.equals(student)) {
                    atom.getAndIncrement();
                }
            });
        });
        return atom.get();
    }

    public static LinkedList<String> classList(String student) {
        LinkedList<String> chosenClassList = new LinkedList<String>();
        studentsMap.forEach((attendedClass, students) -> {
            students.forEach(atendee -> {
                if (atendee.equals(student)) {
                    chosenClassList.add(attendedClass);
                }
            });
        });
        return chosenClassList;
    }

    public static void main(String[] args) {

        Path path = FileSystems.getDefault().getPath("", "students.txt");
        String file_data = "";
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);

            for (int i = 0; i < lines.size(); i++) {
                file_data += lines.get(i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] strings = file_data.split(" [A-Z][0-9][0-9]"); // regular expression finding students
        String[] strings2 = file_data.split("[A-Z][a-z]+ "); // regular expression finding symbols of a course

        for (int i = 0, j = 1; i < strings.length; i++, j++) {
            addListToMap(strings2[j], strings[i]);
        }
        //printMap();
        System.out.println();
        System.out.println("Number of subjects: " + numberOfClasses());

        System.out.println();
        // Obtaining a TreeMap key or TreeMap value using an array.
        Set<Map.Entry<String, LinkedList<String>>> studentsSet
                = studentsMap.entrySet();

        Map.Entry<String, LinkedList<String>>[] studentsArray
                = studentsSet.toArray(
                new Map.Entry[studentsSet.size()]);

        System.out.println("Alphabetical list of students attending a given class: ");

        for (int i = 0; i < studentsArray.length; i++) {
            System.out.println(studentsArray[i].getKey() + ": " + studentsAttendingClass(studentsArray[i].getKey()));
        }
        System.out.println();
        System.out.println("Number of courses attended by more than 13 students: " + bigClasses(13));
        System.out.println("List of courses attended by more than 13 students: " + bigClassesList(13));
        System.out.println("Number of courses that student Harry is attending: " + classNumber("Harry"));
        System.out.println("List of courses that student Harry is attending: " + classList("Harry"));
    }
}
