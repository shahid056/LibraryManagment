import entity.Book;
import enums.BookCategory;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import servicesImpl.BookServicesImpl;
import userInterface.Home;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        BookData.populatedBookData("The Great Adventure", "John Doe", "FICTION", 12);
        BookData.populatedBookData("The Truth About Science", "Jane Austen", "SCIENCE", 8);
        BookData.populatedBookData("World War II", "Mark Twain", "HISTORY", 15);
        BookData.populatedBookData("The Detective's Casebook", "Arthur Conan Doyle", "MYSTERY", 6);
        BookData.populatedBookData("Wizards of the World", "J.R.R. Tolkien", "FANTASY", 20);
        BookData.populatedBookData("Children's Fairytales", "Brothers Grimm", "CHILDREN", 25);
        BookData.populatedBookData("Mastering Cooking", "Gordon Ramsay", "COOKING", 18);
        BookData.populatedBookData("Philosophical Musings", "Socrates", "PHILOSOPHY", 5);
        BookData.populatedBookData("The Psychology of Motivation", "Daniel Goleman", "PSYCHOLOGY", 10);

        new Home().HomeScreen();
    }
}