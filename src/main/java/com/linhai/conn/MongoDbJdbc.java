package com.linhai.conn;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 * Created by linhai on 2018/12/9.
 */
public class MongoDbJdbc {

    public static void main( String args[] ){
        try{
            // 连接到 mongodb 服务
            MongoClient mongoClient = new MongoClient( "47.99.138.3" , 27777 );

            // 连接到数据库
            MongoDatabase mongoDatabase = mongoClient.getDatabase("DataSource");
            System.out.println("Connect to database successfully");
            System.out.println("MongoDatabase inof is : "+mongoDatabase.getName());

            System.out.println("当前数据库中的所有集合是：");
            for (String name : mongoDatabase.listCollectionNames()) {
                System.out.println(name);
            }

            MongoCollection<Document> collection = mongoDatabase.getCollection("carrier");
            System.out.println("集合 carrier 选择成功");
            //检索所有文档
            /**
             * 1. 获取迭代器FindIterable<Document>
             * 2. 获取游标MongoCursor<Document>
             * 3. 通过游标遍历检索出的文档集合
             * */
            FindIterable<Document> findIterable = collection.find();
            MongoCursor<Document> mongoCursor = findIterable.iterator();
            while(mongoCursor.hasNext()){
                System.out.println("文档："+mongoCursor.next());
            }
        }catch(Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }
}
