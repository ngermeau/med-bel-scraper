package be.nicolasgermeau;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;


/**
 * Hello world!
 *
 */
public class App 
{

    public static void main( String[] args ) throws IOException
    {
        Document doctorsListPage = Jsoup.connect("https://www.medecinbelgique.com/medecin/rechercher-un-medecin.htm?data[Doctor][specialties]=&data[Doctor][city_id]=Bruxelles&data[Doctor][nom]=&data[Doctor][prenom]=&data[Doctor][codepostal]=").get();
        Elements doctorsLink = doctorsListPage.select("div.strip_list > a");
        for(Element doctorLink:doctorsLink) {
            String link = doctorLink.attr("href");
            Document doctorDetailPage = Jsoup.connect("https://www.medecinbelgique.com" + link).get();
            String specialities = doctorDetailPage.select("div.profile small").first().text();
            System.out.println(specialities);
        }
    }
}
