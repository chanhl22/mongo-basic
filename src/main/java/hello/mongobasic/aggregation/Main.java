package hello.mongobasic.aggregation;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.result.UpdateResult;
import hello.mongobasic.model.Product;
import org.bson.Document;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.Arrays;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Filters.regex;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Sorts.descending;
import static com.mongodb.client.model.Updates.mul;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class Main {
    public static void main(String[] args) {
        CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
        CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));

        String uri = "mongodb://localhost:27017";
        MongoClient mongoClient = MongoClients.create(uri);
        MongoDatabase database = mongoClient.getDatabase("de-mongodb").withCodecRegistry(pojoCodecRegistry);
        MongoCollection<Document> productCollection = database.getCollection("products");

        MongoCursor<Document> cursor = productCollection.aggregate(
                Arrays.asList(
                        Aggregates.match(gt("price", 10000)),
                        Aggregates.group("$name", Accumulators.avg("avg_price", "$price")),
                        Aggregates.sort(descending("avg_price"))
                )
        ).iterator();

        while (cursor.hasNext()) {
            System.out.println(cursor.next());
        }
    }

}
