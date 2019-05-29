package cn.com.wosuo.taskrecorder.di.components;

import cn.com.wosuo.taskrecorder.di.UserScope;
import cn.com.wosuo.taskrecorder.di.modules.BigkeerModule;
import cn.com.wosuo.taskrecorder.repository.TaskRepository;
import dagger.Component;

@UserScope
@Component(dependencies = NetComponent.class, modules = BigkeerModule.class)
public interface BigkeerComponent {
    void inject(TaskRepository taskRepository);
}
