package com.mysl.api.schedule;

import com.mysl.api.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Ivan Su
 * @date 2022/9/15
 */
@Component
public class MyScheduleTask {

    @Autowired
    private ClassService classService;

    // 每天0点1分执行，让已申请但未完成学习的课程失效
    @Async
    @Scheduled(cron = "0 1 0 * * ?")
//    @Scheduled(cron = "0 0/5 * * * ?")
    public void expireClassCourse() {
        classService.expireClassCourse();
    }
}
