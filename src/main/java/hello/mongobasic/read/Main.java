package hello.mongobasic.read;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import hello.mongobasic.model.Product;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.regex;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;
import static com.mongodb.client.model.Sorts.descending;
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

        MongoCursor<Product> cursor = productCollection.find().cursor();
        while (cursor.hasNext()) {
            System.out.println(cursor.next());
        }

        MongoCursor<Product> cursor1 = productCollection.find(eq("price", 10000)).cursor();
        while (cursor1.hasNext()) {
            System.out.println("cursor1 : " + cursor1.next());
        }

        MongoCursor<Product> cursor2 = productCollection.find(regex("name", "shoes")).cursor();
        while (cursor2.hasNext()) {
            System.out.println("cursor2 : " + cursor2.next());
        }

        MongoCursor<Product> cursor3 = productCollection.find(regex("name", "shoes"))
                .sort(descending("price"))
                .cursor();
        while (cursor3.hasNext()) {
            System.out.println("cursor3 : " + cursor3.next());
        }

        MongoCursor<Product> cursor4 = productCollection.find(regex("name", "shoes"))
                .projection(fields(include("name", "price")
                ))
                .sort(descending("price"))
                .cursor();
        while (cursor4.hasNext()) {
            System.out.println("cursor4 : " + cursor4.next());
        }
    }

}
