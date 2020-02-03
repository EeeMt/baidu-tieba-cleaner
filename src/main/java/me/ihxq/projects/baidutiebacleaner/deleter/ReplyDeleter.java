package me.ihxq.projects.baidutiebacleaner.deleter;

import me.ihxq.projects.baidutiebacleaner.model.Reply;
import me.ihxq.projects.baidutiebacleaner.runner.Deleter;
import org.springframework.stereotype.Service;

/**
 * @author xq.h
 * 2020/2/1 16:29
 **/
@Service
public class ReplyDeleter implements Deleter<Reply> {
    @Override
    public boolean delete(Reply e) {
        return false;
    }
}
