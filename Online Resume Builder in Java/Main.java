import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Name: ");
        String name = sc.nextLine();

        System.out.print("Email: ");
        String email = sc.nextLine();

        System.out.print("Phone: ");
        String phone = sc.nextLine();

        System.out.print("Summary: ");
        String summary = sc.nextLine();

        System.out.print("Education: ");
        String education = sc.nextLine();

        System.out.print("Experience: ");
        String experience = sc.nextLine();

        System.out.print("Skills (comma-separated): ");
        String skills = sc.nextLine();

        ResumeData data = new ResumeData(name, email, phone, summary, education, experience, skills);
        ResumeGenerator.generatePDF(data);
    }
}
