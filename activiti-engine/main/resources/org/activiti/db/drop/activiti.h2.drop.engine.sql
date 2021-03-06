drop table if exists ACT_GE_PROPERTY cascade constraints;
drop table if exists ACT_GE_BYTEARRAY cascade constraints;
drop table if exists ACT_RE_DEPLOYMENT cascade constraints;
drop table if exists ACT_RE_MODEL cascade constraints;
drop table if exists ACT_RU_EXECUTION cascade constraints;
drop table if exists ACT_RU_JOB cascade constraints;
drop table if exists ACT_RE_PROCDEF cascade constraints;
drop table if exists ACT_RU_TASK cascade constraints;
drop table if exists ACT_RU_IDENTITYLINK cascade constraints;
drop table if exists ACT_RU_VARIABLE cascade constraints;
drop table if exists ACT_RU_EVENT_SUBSCR cascade constraints;

drop index if exists ACT_IDX_EXEC_BUSKEY;
drop index if exists ACT_IDX_TASK_CREATE;
drop index if exists ACT_IDX_IDENT_LNK_USER;
drop index if exists ACT_IDX_IDENT_LNK_GROUP;
drop index if exists ACT_IDX_VARIABLE_TASK_ID;
drop index if exists ACT_IDX_EVENT_SUBSCR_CONFIG_;
drop index if exists ACT_IDX_ATHRZ_PROCEDEF;