package be.nicolasgermeau;

import com.opencsv.CSVWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * Hello world!
 */
public class App {
    private final static Integer STARTING_PAGE = 1;
    private final static Integer ENDING_PAGE = 418;
    private final static String filename = "doctors";
    private final static File file = new File(filename);
    private static final FileWriter outputfile = new FileWriter(file);
    CSVWriter writer = new CSVWriter(outputfile);

    public static void write(String[] doctorData) throws Exception {
//        Paths.ge
        String[] ar = {"hello","world"};
        writer.writeNext(ar);
        writer.close();
    }

    public App() {
    }

    public static void main(String[] args) throws Exception {
        App.write();
        for (int currentPage = STARTING_PAGE; currentPage <= ENDING_PAGE; currentPage++) {
            Document doctorsListPage = Jsoup.connect("https://www.medecinbelgique.com/medecin/rechercher-un-medecin.htm/page:" + currentPage + "?data[Doctor][specialties]=&data[Doctor][city_id]=Bruxelles&data[Doctor][nom]=&data[Doctor][prenom]=&data[Doctor][codepostal]=").get();
            Elements doctorsLink = doctorsListPage.select("div.strip_list > a");
            for (Element doctorLink : doctorsLink) {
                String link = doctorLink.attr("href");
                Document doctorDetailPage = Jsoup.connect("https://www.medecinbelgique.com" + link).get();
                String specialities = doctorDetailPage.select("div.profile small").first().text();
                String name = doctorDetailPage.select("div.profile h1").first().text();
                String address = doctorDetailPage.select("ul.contacts li").first().text().replace("Adresse principale", "");
                String telephone = doctorDetailPage.select("ul.contacts li").get(1).text().replace("Téléphone : ", "");
                String[] doctorData = {String.valueOf(currentPage),specialities,name,address,telephone};
                write(doctorData);
            }
        }
    }
}
