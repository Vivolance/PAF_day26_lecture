package IBF.day26.lecture.repository;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class ShowsRepository {

    public static final String C_TVSHOWS = "tvshows";

    @Autowired
    private MongoTemplate mongoTemplate;

    //In test, you need to Write the native mongo query:
    //db.tvshows.find({name: "name of movie"})
    public List<Document> findTvShowByName(String title) {

        //Create the filter -> the criteria
        //Pass in field name parameter
        // {name: "name of movie"}
        Criteria criterial = Criteria.where("name").is(title);

        //Create a query
        Query query = Query.query(criterial);

        //Execute a query
        List<Document> results = mongoTemplate.find(query, Document.class, C_TVSHOWS);

        return results;

    }
    /*
     * db.tvshows.find({
     * { "rating.average": { $gte: 6.5 } },
     * { "runtime": {$lte: 30 }}"}
     * })
     */
    
     public List<Document> findshowTimeLessThan(int mins) {

        Criteria criteria = Criteria.where("rating.avergae").gte(6.5).andOperator(
            Criteria.where("runtime").lte(mins));

        Query query = Query.query(criteria);


        return mongoTemplate.find(query, Document.class, C_TVSHOWS);
        
     }
    
     /* 
      * db.tvshows 
            .find({ status: "Ended"})
            .sort({ "rating.avergae": -1, name: 1 })
      */
      public List<Document> findMoviesByStatus(String status) {

        Criteria criteria = Criteria.where("status").is(status)
                .andOperator(Criteria.where("rating.average").not().isNullValue());

        Query query = Query.query(criteria)
            .with(
                Sort.by(Direction.DESC, "rating.average")
                    .and(Sort.by(Direction.ASC, "name"))
                );
        query.fields().include("name", "rating.average").exclude("_id");

        return mongoTemplate.find(query, Document.class, C_TVSHOWS);
    }
}
