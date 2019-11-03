import com.alibaba.fastjson.JSON;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.security.Key;

public class App {
    @Test
    public void test1(){
        Jedis jedis = new Jedis("127.0.0.1",6379);
        Post post = new Post();

        post.setAuthor("awei");
        post.setContent("我的设计文稿");
        post.setTitle("文稿");
        Long postId = SavePost(post,jedis);
        GetPost(postId,jedis);
        Post post1 = updateTitle(postId, jedis);
        System.out.println(post1);
        deleteBlog(postId,jedis);
        jedis.close();
    }


    public Long SavePost(Post post,Jedis jedis){
        Long postId = jedis.incr("posts");
        String myPost = JSON.toJSONString(post);
        jedis.set("post:"+postId+":data",myPost);

        return postId;
    }


    public Post GetPost(Long postId,Jedis jedis){
        String getPost = jedis.get("post:" + postId + ":data");
        jedis.incr("post:" + postId + ":page.view");
        Post parseObject = JSON.parseObject(getPost, Post.class);

        return parseObject;
    }


    public Post updateTitle(Long postId,Jedis jedis){
        Post post = GetPost(postId, jedis);
        post.setTitle("修改后的文稿");
        String myPost = JSON.toJSONString(post);
        jedis.set("post:"+postId+":data",myPost);
        System.out.println("修改完成");
        return post;
    }

    public void deleteBlog(Long postId,Jedis jedis){
        jedis.del("post:" + postId + ":data");
        jedis.del("post:"+postId+":page.view");
        System.out.println("del成功");
    }
}