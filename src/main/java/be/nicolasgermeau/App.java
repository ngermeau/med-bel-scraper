package be.nicolasgermeau;

import com.opencsv.CSVWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


/**
 * Hello world!
 */
public class App {
    private final static Integer STARTING_PAGE = 1;
    private final static Integer ENDING_PAGE = 418;
    private final static String filename = "doctors.csv";
    private CSVWriter writer;

    public void write(String[] doctorData) throws Exception {
        this.writer.writeNext(doctorData);
        this.writer.flush();
    }

    public App() throws IOException {
        File file = new File(filename);
        FileWriter outputFile = new FileWriter(file);
        writer = new CSVWriter(outputFile);
    }
    
    private void scrap() throws Exception {
        for (int currentPage = STARTING_PAGE; currentPage <= ENDING_PAGE; currentPage++) {
            Document doctorsListPage = Jsoup.connect("https://www.medecinbelgique.com/medecin/rechercher-un-medecin.htm/page:" + currentPage + "?data[Doctor][specialties]=&data[Doctor][city_id]=Bruxelles&data[Doctor][nom]=&data[Doctor][prenom]=&data[Doctor][codepostal]=").get();
            Elements doctorsLink = doctorsListPage.select("div.strip_list > a");
            for (Element doctorLink : doctorsLink) {
                String link = doctorLink.attr("href");
                try {
                    Document doctorDetailPage = Jsoup.connect("https://www.medecinbelgique.com" + link).get();
                    String specialities = doctorDetailPage.select("div.profile small").first().text();
                    String name = doctorDetailPage.select("div.profile h1").first().text();
                    String address = doctorDetailPage.select("ul.contacts li").first().text().replace("Adresse principale", "").replace("Voir sur la carte", "");
                    String telephone = doctorDetailPage.select("ul.contacts li").get(1).text().replace("Téléphone : ", "");
                    String[] doctorData = {String.valueOf(currentPage), specialities, name, address, telephone};
                    write(doctorData);
                }catch (Exception exception){
                    System.out.println("error while fetching url" + link);
                }
            }
        }
    }
    public static void main(String[] args) throws Exception {
        new App().scrap();
    }
}
