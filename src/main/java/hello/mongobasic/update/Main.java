package hello.mongobasic.update;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import hello.mongobasic.model.Product;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Filters.regex;
import static com.mongodb.client.model.Projections.fields;
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
        MongoCollection<Product> productCollection = database.getCollection("products", Product.class);

        UpdateResult updateResult =
                productCollection.updateMany(
                        fields(regex("name", "shoes"), // filter shoes
                                gt("price", 10000)), // filter price > 10000
                        mul("price", 0.9) // 10% discount
                );
        if (updateResult.wasAcknowledged()) {
            System.out.println("modified: " + updateResult.getModifiedCount());
        }
    }

}
