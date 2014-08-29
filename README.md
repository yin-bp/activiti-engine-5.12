流程驳回到已处理节点再跳转该驳回节点功能开发
td_wf_rejectlog
rejectnode
rejecttaskid
newtaskid

2014-05-16 解决多实例任务多出路分支条件不起作用导致每个分支都产生任务的问题修改
2014-05-16 ServiceTask中express类型的服务也支持javadelegate和activitibehavier类型的表达式类运算
2014-05-16 增加实例升级处理服务

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

org.activiti.engine.impl.cmd.CompleteTaskCmd
	protected Void execute(CommandContext commandContext, TaskEntity task)                      
创建任务(关联Assignment)：
org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior        
 public void execute(ActivityExecution execution) throws Exception {
    TaskEntity task = TaskEntity.createAndInsert(execution);  
    handleAssignments(task, execution);
 protected void handleAssignments(TaskEntity task, ActivityExecution execution)   
    
建立用户和任务关系Canidate
org.activiti.engine.impl.persistence.entity.ExecutionEntity
 public IdentityLinkEntity addIdentityLink(String userId, String type) {
    IdentityLinkEntity identityLinkEntity = IdentityLinkEntity.createAndInsert();
                
任务分配相关：
org.activiti.engine.impl.bpmn.behavior.BpmnActivityBehavior
	 protected void performOutgoingBehavior(ActivityExecution execution, 
          boolean checkConditions, boolean throwExceptionIfExecutionStuck, List<ActivityExecution> reusableExecutions)  
        选择路径  

org.activiti.engine.impl.pvm.runtime.AtomicOperationTransitionDestroyScope
		 public void execute(InterpretableExecution execution)

org.activiti.engine.impl.pvm.runtime.AtomicOperationTransitionNotifyListenerTake
	public void execute(InterpretableExecution execution)		
	
	
驳回改造：                      
会签
 org.activiti.engine.impl.bpmn.behavior.ParallelMultiInstanceBehavior(并行 )
 
单实例任务：
org.activiti.engine.impl.bpmn.behavior.BpmnActivityBehavior 
单实例任务驳回时，需要判断任务对应的流程实例中还有别的任务在运行，如果有，则需要检测驳回到 的节点是不是这些其他任务的直接或者
间接前置节点，如果是则不允许驳回，如果不是则允许驳回。

并行gateway                      
 org.activiti.engine.impl.bpmn.behavior.ParallelGatewayActivityBehavior
 org.activiti.engine.impl.bpmn.behavior.InclusiveGatewayActivityBehavior	
 
获取任务可以跳转节点列表
自动驳回到上一个处理环节
配置流程处理环节
会签串并行改造  
	如果下流程中存在多实例任务，那么可以通过流程变量在运行时或者在发起流程的时候设置相关多实例任务执行的方式为串行还是并行
   * 变量的命名规范为：
   * taskkey.bpmn.behavior.multiInstance.mode
   * 取值范围为：
   * 	parallel
   * 	sequential
   * 说明：taskkey为对应的任务的定义id
   * 
   * 这个变量可以在设计流程时统一配置，可以启动流程实例时动态修改，也可以在上个活动任务完成时修改
增加UserInfoMap组件，用来在流程引擎中获取用户相关属性（根据userAccount或者根据用户ID获取用户属性）

流程实例和任务升级改造要点：
需要修改的表结构：
act_ru_task
act_ru_job
act_ru_identitylink
act_ru_execution
act_hi_taskinst
act_hi_procinst
act_hi_actinst

主要是对这些表中的流程定义id进行统一的变更，对于定时任务的处理以及内存中的缓存数据的处理，还需要时间进一步检验
变更完成后需要刷新缓存数据
子流程删除，父流程的处理