package jedis;
import com.alibaba.fastjson.JSON;
import redis.clients.jedis.Jedis;

public class jedis {
    public static void main(String[] args) {
        //连接本地的 Redis 服务
        Jedis jedis = new Jedis("localhost");
        System.out.println("连接成功");
        //查看服务是否运行
        System.out.println("服务正在运行: "+jedis.ping());

    }
    public Long Save(Article article,Jedis jedis){
        Long Id = jedis.incr("articles");
        String myPost = JSON.toJSONString(article);
        jedis.set("post:"+Id+":data",myPost);
        return Id;
    }
    //获取
    public Article Get(Long Id,Jedis jedis){
        String get = jedis.get("article:" + Id + ":data");
        jedis.incr("post:" + Id + ":page.view");
        Article parseObject = JSON.parseObject(get, Article.class);
        System.out.println("这是第"+Id+"篇文章"+parseObject);
        return parseObject;
    }
    //修改
    public Article update(Long Id,Jedis jedis){
        Article article = Get(Id, jedis);
        article.setTitle("update title");
        String myPost = JSON.toJSONString(article);
        jedis.set("post:"+Id+":data",myPost);
        System.out.println("修改完成");
        return article;
    }
    //删除
    public void delete(Long Id,Jedis jedis){
        jedis.del("post:" + Id + ":data");
        jedis.del("post:"+Id+":page.view");
        System.out.println("删除成功");
    }
}
