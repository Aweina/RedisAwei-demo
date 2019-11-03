import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

public class Hash {
    public static void main(String[] args){
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        Post post = new Post();
        post.setTitle("hash blog");
        post.setAuthor("awei");
        post.setContent("我的文稿");
        Long postId = savePost(jedis, post);
        getPost(postId,jedis);
        System.out.println(post);

        System.out.println("修改后的标题如下");

        boolean s=updateTitle(jedis,postId,"awei");
        Post post1=getPost(postId,jedis);
        System.out.println(post1);
        deleteBlog(postId,jedis);




    }
//保存
     static Long savePost(Jedis jedis, Post post) {
         Long postId = jedis.incr("posts");
         Map<String, String> myPost = new HashMap<String, String>();
         myPost.put("title", post.getTitle());
         myPost.put("content", post.getContent());
         myPost.put("author", post.getAuthor());
         jedis.hmset("post:" + postId, myPost);
         return postId;
    }
//获取
    static Post getPost(Long postId, Jedis jedis){
        Map<String,String>myBlog=jedis.hgetAll("post:"+postId);
        Post post=new Post();
        post.setTitle(myBlog.get("title"));
        post.setAuthor(myBlog.get("author"));
        post.setContent(myBlog.get("content"));

        return post;
    }
//修改
    static boolean updateTitle(Jedis jedis,Long postId ,String title){
        Long s=jedis.hset("post:"+postId,"title",title);
        if (s==0){
            return true;
        }
        return  false;
    }

    static void deleteBlog(Long postId, Jedis jedis) {
        jedis.hdel("post:" + postId,"title","author","content");
        System.out.println("del成功");
    }

}
