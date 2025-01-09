package hello.mongobasic.create;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertManyResult;
import hello.mongobasic.model.Product;
import org.bson.Document;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class Main {
    public static void main(String[] args) {
        CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
        CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));

        String uri = "mongodb://localhost:27017";
        MongoClient mongoClient = MongoClients.create(uri);
        MongoDatabase database = mongoClient.getDatabase("de-mongodb").withCodecRegistry(pojoCodecRegistry);
        MongoCollection<Product> productCollection = database.getCollection("products", Product.class);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<Product> products = new ArrayList<>();
        products.add(new Product(null, "shoes1", LocalDateTime.parse("2022-08-01 01:00:00", formatter),
                "This is shoes1", 10000));
        products.add(new Product(null, "shoes2", LocalDateTime.parse("2022-08-01 02:00:00", formatter),
                "This is shoes2", 20000));
        products.add(new Product(null, "shoes3", LocalDateTime.parse("2022-08-01 03:00:00", formatter),
                "This is shoes3", 30000));
        products.add(new Product(null, "shoes4", LocalDateTime.parse("2022-08-01 04:00:00", formatter),
                "This is shoes4", 40000));
        products.add(new Product(null, "shoes5", LocalDateTime.parse("2022-08-01 05:00:00", formatter),
                "This is shoes5", 50000));
        products.add(new Product(null, "shoes6", LocalDateTime.parse("2022-08-01 06:00:00", formatter),
                "This is shoes6", 60000));
        products.add(new Product(null, "backpack", LocalDateTime.parse("2022-08-02 04:00:00", formatter),
                "This is backpack", 50000));
        products.add(new Product(null, "shirt", LocalDateTime.parse("2022-08-03 05:00:00", formatter),
                "This is shirt", 20000));
        products.add(new Product(null, "glasses", LocalDateTime.parse("2022-08-04 06:00:00", formatter),
                "This is glasses", 10000));

        InsertManyResult insertManyResult = productCollection.insertMany(products);
        if (insertManyResult.wasAcknowledged()) {
            System.out.println("inserted ids: " + insertManyResult.getInsertedIds());
        }
    }
}
