
Create a new repository on the command line

touch README.md
git init
git add README.md
git commit -m "first commit"
git remote add origin https://github.com/yin-bp/activiti-engine-5.12.git
git push -u origin master

Push an existing repository from the command line

git remote add origin https://github.com/yin-bp/activiti-engine-5.12.git
git push -u origin master

自由流改造分析：
org.activiti.engine.impl.TaskServiceImpl
    complete(String taskId,String destinationTaskKey) 
       --call->org.activiti.engine.impl.interceptor.CommandContextInterceptor
                                                                  public <T> T execute(Command<T> command)
                      execute(new CompleteTaskCmd(taskId, null,destinationTaskKey));