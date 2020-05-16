import salon.Service;
import salon.Visit;

import java.util.ArrayList;

import static java.lang.System.out;

public class Main {
    public static void main(String[] args) {
        Service s = new Service("manicure", 1000, 2, 2000);
        out.println("Service state before serialization: " + s);
        Service.writeFile("ServiceSer.txt", s);
        Service newS = Service.readFile("ServiceSer.txt");
        out.println("Service state after serialization: " + newS);

        ArrayList<Service> arr = new ArrayList<Service>();
        arr.add(s);
        arr.add(new Service("pedicure", 1500, 2, 3000));
        arr.add(new Service("pedicure", 1500, 2, 3000));
        arr.add(new Service("haircut", 2000, 1, 2000));
        arr.add(new Service("washing", 500, 2, 1000));

        Visit v = new Visit(1, arr);
        v.writeFile("VisitSer.txt");
        Visit newV = new Visit(2);
        newV.readFile("VisitSer.txt");
        out.println("---------Visit state before serialization---------\n" + v);
        out.println("---------Visit state after serialization---------\n" + newV);

        v.removeService(0);
        out.println("---------The first element was deleted---------\n" + v);

        v.addService(0, s);
        out.println("---------The first element was returned---------\n" + v);

        v.sortServices();
        out.println("---------Sorted Visit---------\n" + v);
    }
}
