package jedis;
import redis.clients.jedis.Jedis;
import java.util.HashMap;
import java.util.Map;

public class hash {
    public static void main(String[] args) {
        //连接本地的 Redis 服务
        Jedis jedis = new Jedis("localhost");
        Article post =new Article();
        post.setAuthor("zbq");
        post.setContent("blog");
        post.setTitle("my blog");
        Long postId = Save(post, jedis);
        System.out.println("保存成功");
        Article myPost=Get(postId,jedis);
        System.out.println(myPost);
    }
    //存
    static Long Save(Article article,Jedis jedis){
        Long postID = jedis.incr("posts");
        Map<String, String> blog = new HashMap<String, String>();
        blog.put("title",article.getTitle());
        blog.put("author",article.getAuthor());
        blog.put("content",article.getContent());
        jedis.hmset("post"+postID+"data",blog);
        return postID;
    }
    //取
    static Article Get(Long postId,Jedis jedis) {
        Map<String, String> myblog = jedis.hgetAll("article:" + postId + ":data");
        Article article=new Article();
        article.setTitle(myblog.get("title"));
        article.setAuthor(myblog.get("author"));
        article.setContent(myblog.get("content"));
        return article;
    }
    //改
     static Long update(Article article,Jedis jedis,Long id){
        Long postId=id;
        Map<String,String> map=new HashMap<String, String>();
        map.put("title",article.getTitle());
        map.put("author",article.getAuthor());
        map.put("content",article.getContent());
        jedis.hmset("post:"+id+":data",map);
        return postId;
    }
   //删
    static Long delete(Jedis jedis,Long id){
        long postId=jedis.hdel("post:"+id+":data");
        return postId;
    }

}

