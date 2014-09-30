create table ACT_HI_PROCINST (
    ID_ varchar(64) not null,
    PROC_INST_ID_ varchar(64) not null,
    BUSINESS_KEY_ varchar(255),
    PROC_DEF_ID_ varchar(64) not null,
    START_TIME_ datetime not null,
    END_TIME_ datetime,
    DURATION_ bigint,
    START_USER_ID_ varchar(255),
    START_ACT_ID_ varchar(255),
    END_ACT_ID_ varchar(255),
    SUPER_PROCESS_INSTANCE_ID_ varchar(64),
    DELETE_REASON_ varchar(4000),
    primary key (ID_),
    unique (PROC_INST_ID_),
    unique ACT_UNIQ_HI_BUS_KEY (PROC_DEF_ID_, BUSINESS_KEY_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

-- Create table
create table td_wf_rejectlog
(
  newtaskid    varchar(64) not null,
  rejecttaskid varchar(64) not null,
  rejectnode   varchar(100) not null,
   primary key (newtaskid),
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

create table ACT_HI_ACTINST (
    ID_ varchar(64) not null,
    PROC_DEF_ID_ varchar(64) not null,
    PROC_INST_ID_ varchar(64) not null,
    EXECUTION_ID_ varchar(64) not null,
    ACT_ID_ varchar(255) not null,
    TASK_ID_ varchar(64),
    CALL_PROC_INST_ID_ varchar(64),
    ACT_NAME_ varchar(255),
    ACT_TYPE_ varchar(255) not null,
    ASSIGNEE_ varchar(64),
    START_TIME_ datetime not null,
    END_TIME_ datetime,
    DURATION_ bigint,
    primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

create table ACT_HI_TASKINST (
    ID_ varchar(64) not null,
    PROC_DEF_ID_ varchar(64),
    TASK_DEF_KEY_ varchar(255),
    PROC_INST_ID_ varchar(64),
    EXECUTION_ID_ varchar(64),
    NAME_ varchar(255),
    PARENT_TASK_ID_ varchar(64),
    DESCRIPTION_ varchar(4000),
    OWNER_ varchar(255),
    ASSIGNEE_ varchar(255),
    START_TIME_ datetime not null,
    CLAIM_TIME_ datetime,
    END_TIME_ datetime,
    DURATION_ bigint,
    DELETE_REASON_ varchar(4000),
    PRIORITY_ integer,
    DUE_DATE_ datetime,
    FORM_KEY_ varchar(255),
    primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

create table ACT_HI_VARINST (
    ID_ varchar(64) not null,
    PROC_INST_ID_ varchar(64),
    EXECUTION_ID_ varchar(64),
    TASK_ID_ varchar(64),
    NAME_ varchar(255) not null,
    VAR_TYPE_ varchar(100),
    REV_ integer,
    BYTEARRAY_ID_ varchar(64),
    DOUBLE_ double,
    LONG_ bigint,
    TEXT_ varchar(4000),
    TEXT2_ varchar(4000),
    primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

create table ACT_HI_DETAIL (
    ID_ varchar(64) not null,
    TYPE_ varchar(255) not null,
    PROC_INST_ID_ varchar(64),
    EXECUTION_ID_ varchar(64),
    TASK_ID_ varchar(64),
    ACT_INST_ID_ varchar(64),
    NAME_ varchar(255) not null,
    VAR_TYPE_ varchar(255),
    REV_ integer,
    TIME_ datetime not null,
    BYTEARRAY_ID_ varchar(64),
    DOUBLE_ double,
    LONG_ bigint,
    TEXT_ varchar(4000),
    TEXT2_ varchar(4000),
    primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

create table ACT_HI_COMMENT (
    ID_ varchar(64) not null,
    TYPE_ varchar(255),
    TIME_ datetime not null,
    USER_ID_ varchar(255),
    TASK_ID_ varchar(64),
    PROC_INST_ID_ varchar(64),
    ACTION_ varchar(255),
    MESSAGE_ varchar(4000),
    FULL_MSG_ LONGBLOB,
    primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

create table ACT_HI_ATTACHMENT (
    ID_ varchar(64) not null,
    REV_ integer,
    USER_ID_ varchar(255),
    NAME_ varchar(255),
    DESCRIPTION_ varchar(4000),
    TYPE_ varchar(255),
    TASK_ID_ varchar(64),
    PROC_INST_ID_ varchar(64),
    URL_ varchar(4000),
    CONTENT_ID_ varchar(64),
    primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

create index ACT_IDX_HI_PRO_INST_END on ACT_HI_PROCINST(END_TIME_);
create index ACT_IDX_HI_PRO_I_BUSKEY on ACT_HI_PROCINST(BUSINESS_KEY_);
create index ACT_IDX_HI_ACT_INST_START on ACT_HI_ACTINST(START_TIME_);
create index ACT_IDX_HI_ACT_INST_END on ACT_HI_ACTINST(END_TIME_);
create index ACT_IDX_HI_DETAIL_PROC_INST on ACT_HI_DETAIL(PROC_INST_ID_);
create index ACT_IDX_HI_DETAIL_ACT_INST on ACT_HI_DETAIL(ACT_INST_ID_);
create index ACT_IDX_HI_DETAIL_TIME on ACT_HI_DETAIL(TIME_);
create index ACT_IDX_HI_DETAIL_NAME on ACT_HI_DETAIL(NAME_);
create index ACT_IDX_HI_DETAIL_TASK_ID on ACT_HI_DETAIL(TASK_ID_);
create index ACT_IDX_HI_PROCVAR_PROC_INST on ACT_HI_VARINST(PROC_INST_ID_);
create index ACT_IDX_HI_PROCVAR_NAME_TYPE on ACT_HI_VARINST(NAME_, VAR_TYPE_);
create index ACT_IDX_HI_ACT_INST_PROCINST on ACT_HI_ACTINST(PROC_INST_ID_, ACT_ID_);
create index ACT_IDX_HI_ACT_INST_EXEC on ACT_HI_ACTINST(EXECUTION_ID_, ACT_ID_);



ALTER TABLE ACT_RU_TASK
ADD COLUMN DURATION_NODE decimal(19)   NULL DEFAULT 0;

ALTER TABLE ACT_RU_TASK
ADD COLUMN  ADVANCESEND decimal(1)  NULL DEFAULT 0;

ALTER TABLE ACT_RU_TASK
ADD COLUMN OVERTIMESEND decimal(1) NULL DEFAULT 0;

ALTER TABLE ACT_RU_TASK
ADD COLUMN (ALERTTIME TIMESTAMP   NULL DEFAULT NULL);

ALTER TABLE ACT_RU_TASK
ADD COLUMN (OVERTIME TIMESTAMP   NULL DEFAULT NULL);

ALTER TABLE ACT_HI_ACTINST
ADD COLUMN DURATION_NODE decimal(19) NULL DEFAULT 0;

ALTER TABLE ACT_HI_ACTINST
ADD COLUMN ADVANCESEND decimal(1) NULL DEFAULT 0;

ALTER TABLE ACT_HI_ACTINST
ADD COLUMN OVERTIMESEND decimal(1) NULL DEFAULT 0;

 

ALTER TABLE ACT_HI_ACTINST
ADD COLUMN (ALERTTIME TIMESTAMP   NULL DEFAULT NULL);

ALTER TABLE ACT_HI_ACTINST
ADD COLUMN (OVERTIME TIMESTAMP   NULL DEFAULT NULL);

 

ALTER TABLE ACT_HI_TASKINST
ADD COLUMN DURATION_NODE decimal(19) NULL DEFAULT 0;

ALTER TABLE ACT_HI_TASKINST
ADD COLUMN ADVANCESEND decimal(1) NULL DEFAULT 0;

ALTER TABLE ACT_HI_TASKINST
ADD COLUMN OVERTIMESEND decimal(1) NULL DEFAULT 0;

ALTER TABLE ACT_HI_TASKINST
ADD COLUMN (ALERTTIME TIMESTAMP   NULL DEFAULT NULL);
ALTER TABLE ACT_HI_TASKINST
ADD COLUMN (OVERTIME TIMESTAMP   NULL DEFAULT NULL);

ALTER TABLE ACT_HI_ACTINST
ADD COLUMN (NOTICERATE decimal(8));

ALTER TABLE ACT_HI_TASKINST
ADD COLUMN (NOTICERATE decimal(8));

ALTER TABLE ACT_RU_TASK
ADD COLUMN (NOTICERATE decimal(8)); 

ALTER TABLE ACT_HI_ACTINST
ADD COLUMN (IS_CONTAIN_HOLIDAY decimal(1));
ALTER TABLE ACT_HI_TASKINST
ADD COLUMN (IS_CONTAIN_HOLIDAY decimal(1));
ALTER TABLE ACT_RU_TASK
ADD COLUMN (IS_CONTAIN_HOLIDAY decimal(1) );

ALTER TABLE ACT_HI_ACTINST
ADD COLUMN IS_AUTO_COMPLETE decimal(1)  NULL DEFAULT 0;

ALTER TABLE ACT_HI_ACTINST
ADD COLUMN (AUTO_HANDLER  varchar(255));

ALTER TABLE ACT_HI_TASKINST
 ADD COLUMN (BUSSINESS_OP  varchar(255));

ALTER TABLE ACT_HI_TASKINST
 ADD COLUMN (BUSSINESS_REMARK  varchar(2000));
 
ALTER TABLE ACT_HI_ACTINST

 ADD COLUMN (BUSSINESS_OP  NVARCHAR2(255));

ALTER TABLE ACT_HI_ACTINST

 ADD COLUMN (BUSSINESS_REMARK  NVARCHAR2(2000)); 

ALTER TABLE ACT_HI_ACTINST
 ADD COLUMN (DELETE_REASON_  NVARCHAR2(2000)); 
ALTER TABLE ACT_HI_ACTINST
 ADD COLUMN (OWNER_  NVARCHAR2(255)); 
 
   ALTER TABLE ACT_HI_ACTINST
 ADD COLUMN (CLAIM_TIME_  TIMESTAMP   NULL DEFAULT NULL); 
    ALTER TABLE td_wf_rejectlog
 ADD COLUMN (optype  decimal(1)  NULL DEFAULT 0); 