package me.ihxq.projects.baidutiebacleaner.deleter;

import me.ihxq.projects.baidutiebacleaner.model.Post;
import me.ihxq.projects.baidutiebacleaner.runner.Deleter;
import org.springframework.stereotype.Service;

/**
 * @author xq.h
 * 2020/2/1 16:28
 **/
@Service
public class PostDeleter implements Deleter<Post> {
    @Override
    public boolean delete(Post e) {
        return false;
    }
}
