package com.rentian.newoa.common.constant;

import android.os.Environment;

public interface Const {


    public static interface MessageType {

        //用户之间的普通消息
        public static final String TYPE_0 = "0";

        //用户之间的普通消息
        public static final String TYPE_1 = "1";

        //用户之间的群聊消息
        public static final String TYPE_2 = "2";

        //刷新发言人列表
        public static final String TYPE_3 = "3";

        //刷新todo列表
        public static final String TYPE_5 = "5";

        //刷新任务详情
        public static final String TYPE_6 = "6";


        //下线类型
        String TYPE_999 = "999";
    }


    public static interface MessageStatus {

        //消息未读
        public static final String STATUS_0 = "0";
        //消息已经读取
        public static final String STATUS_1 = "1";
    }


    public static class RTOA {

        //服务端IP地址hah
        public static String CIM_SERVER_HOST = "192.168.1.121"; //"118.190.67.170";


        //注意，这里的端口不是tomcat的端口，CIM端口在服务端spring-cim.xml中配置的，没改动就使用默认的23456
        public static int CIM_SERVER_PORT = 34567;


        // 错误测试
        // public static String URL_BASE =
        // "http://192.168.1.137:8081/qiyehe_hbrt/";

        // 上线
        public static String URL_BASE = "http://192.168.1.121:9090/worklist-team/";
        // 测试
        public static String  URL_TEST_BASE= "http://thz.likelai.com.cn:80/";
        // 测试不在线的人
        public static String URL_TEST_OFFLINE = URL_BASE + "client/chat/off.htm";

        // 下载路径
        public static final String PATH_DOWNLOAD = Environment
                .getExternalStorageDirectory() + "/hbrt/download";

        // 用户登录
        public static String URL_LOGIN = URL_BASE + "appLoginValidator.htm";

        // 天气
        public static String URL_WEATHER = "http://wthrcdn.etouch.cn/weather_mini?city=";

        // 公告通知
        public static String URL_INFORMATION = URL_BASE
                + "client/sxy/inform/list.htm?uid=";


        // 日报评论
        public static String URL_REPROT_PL = URL_BASE + "client/sxy/addComment.htm";

        // 通知详情
        public static String URL_INFORMATION_DETAIL = URL_BASE
                + "client/sxy/inform/detail.htm";

        // 规章制度
        public static String URL_RULES = URL_BASE + "client/rule/list.htm";

        public static String URL_RULES_DETAIL = URL_BASE
                + "client/rule/detail.htm";

        // 查看日报详情
        public static String URL_REPROT_DETAIL = URL_BASE + "client/sxy/note/detail.htm";

        // 发送日报
        public static String URL_REPROT_SEND = URL_BASE + "client/sxy/note/add.htm";

        public static String URL_VOTE_DETAIL = URL_BASE
                + "client/vote/detail.htm";

        public static String URL_APPLY = URL_BASE + "client/fee/myExam.htm";

        public static String URL_MYPROJECTS = URL_BASE
                + "client/myproject/index.htm";

        // 办公经费 参数：tiqing=&shiji=&reason=&uid=
        public static String URL_OFFICE_EXPENSES = URL_BASE
                + "client/fee/createOffice.htm";

        // 物资经费 参数:(tiqing fukuan_type reason uid)(tiqing fukuan_type company
        // bank account reason uid)
        public static String URL_MATERAILS_AND_FUNDS = URL_BASE
                + "client/fee/createWZJF.htm";

        // 发送日报 参数："today_work=" "&summy=" "&tomorrow=" "&date=" "&parkinfo="
        // "&uid="
        public static String URL_SEND_DAILY = URL_BASE
                + "client/report/createDaily.htm";
        //日报緩存
        public static String URL_SAVE_DAILY = URL_BASE
                + "client/report/getDaily.htm";

        // 发送周报
        public static String URL_SEND_WEEKLY = URL_BASE
                + "client/report/createWeekly.htm";

        // 日报点赞
        public static String URL_REPROT_DZ = URL_BASE + "client/sxy/lk.htm";

        // 工作任務 我接收的
        public static String URL_WORK_TASK = URL_BASE
                + "client/task/listMyReceive.htm";

        // 工作任務 我发送的
        public static String URL_WORK_TASK_SEND = URL_BASE
                + "client/task/listMySend.htm";

        // 工作任务详情 ?id=
        public static String URL_CONCRETE_WORK_TASK_YANSHOU = URL_BASE
                + "client/task/changeWorkTaskStatus.htm";

        // 工作任务详情 ?id=
        public static String URL_CONCRETE_WORK_TASK = URL_BASE
                + "client/task/myReceive.htm";

        // 通讯
        public static String URL_CONTACTS = URL_BASE
                + "client/address/list.htm";

        // 部门编号 参数 uid
        public static String URL_DEPT_INFO = URL_BASE
                + "client/report/daily/acl.htm";

        // 部门日报内容 参数 ?deptid=4&date=2014-09-03
        public static String URL_DEPT_PREVIOUS_DAILYS = URL_BASE
                + "client/report/listNoticeDaily.htm";

        // 周报审批 参数 deptid=4&date=2014-09-3
        public static String URL_DEPT_PREVIOUS_WEEKLY = URL_BASE
                + "client/report/listNoticeWeekly.htm";

        // 检查更新

        public static String URL_CHECK_UPDATE = URL_BASE
                + "v1.json";

        // 发送申请 参数 data data data ... typeid uid

        public static String URL_SEND_APPLY = URL_BASE
                + "client/apply/create.htm";

        // 接受工作任务 ?tid=
        public static String URL_ACCEPT_ASSIGNMENTS = URL_BASE
                + "client/task/confirmWorkTask.htm";

        // 申报完成client/task/applyCompleteWorkTask.htm ?id=&notice=
        public static String URL_APPLY_COMPLETE_TASK = URL_BASE
                + "client/task/applyCompleteWorkTask.htm";

        // 下载附件 ?dfid=
        public static String URL_DOWNLOAD_ATTACHMENTS = URL_BASE
                + "client/download.htm";

        // 提交日报审批?
        public static String URL_SUBMIT_EXAMINE = URL_BASE
                + "client/report/noticeDaily.htm";

        // 提交周报审批
        public static String URL_SUBMIT_WEEKLY_EXAMINE = URL_BASE
                + "client/report/noticeWeekly.htm";

        //申报审批?uid=
        public static String URL_APPLY_LIST = URL_BASE + "client/apply/myExamine.htm";

        // 审批详情client/apply/detail.htm?id=
        public static String URL_EXECUTE_EXAMINE = URL_BASE
                + "client/apply/detail.htm";

        // notice&uid&res&id res=1 yes res=2 no
        public static String URL_SEND_EXAMINE = URL_BASE
                + "client/apply/apply.htm";

        // 我的审批（三种经费）?uid=
        public static String URL_FUNDS_EXAMINE = URL_BASE
                + "client/fee/myExam.htm";

        // 消息?uid=
        public static String URL_MESSAGE_LIST = URL_BASE
                + "client/message/list.htm";

        // 消息详情?id=
        public static String URL_CONCRETE_MESSAGE = URL_BASE
                + "client/message/detail.htm";

        // 办公经费进行审批?mid=?&feeid=?
        public static String URL_EXECUTE_OFE_FUNDS_EXAMINE = URL_BASE
                + "client/fee/detailOffice.htm";

        // 办公经费审批同意?osid=?&mid=?&notice=?&uid=?
        public static String URL_AGREE_OFE_FUNDS_APPLY = URL_BASE
                + "client/fee/applyOffice.htm";

        // 办公经费审批打回?osid=?&mid=?&notice=?&uid=?
        public static String URL_DISAGREE_OFE_FUNDS_APPLY = URL_BASE
                + "client/fee/backOffice.htm";

        // 物资经费进行审批?mid=?&feeid=?
        public static String URL_EXECUTE_MF_FUNDS_EXAMINE = URL_BASE
                + "client/fee/detailWZJF.htm";

        // 物资经费审批同意?feeid=?&mid=?&notice=?&uid=?
        public static String URL_AGREE_MF_APPLY = URL_BASE
                + "client/fee/applyWZJF.htm";

        // 物资经费审批打回?feeid=?&mid=?&notice=?&uid=?
        public static String URL_DISAGREE_MF_APPLY = URL_BASE
                + "client/fee/backWZJF.htm";

        // 物业协调费进行审批?mid=?&feeid=?
        public static String URL_EXECUTE_WYXT_FUNDS_EXAMINE = URL_BASE
                + "client/fee/detailXieTiao.htm";
        // 物业协调费审批同意//?mid=?&feeid=?&uid=?&notice=?
        public static String URL_AGREE_WYXTF_APPLY = URL_BASE
                + "client/fee/applyXieTiao.htm";
        // 物业协调费审批打回?mid=?&feeid=?&uid=?&notice=?
        public static String URL_DISAGREE_WYXTF_APPLY = URL_BASE
                + "/client/fee/backXieTiao.htm";

        //我的项目?uid=65
        public static String URL_MY_PROJECT_LIST = URL_BASE + "client/project/my.htm";

        //项目详情?id=571
        public static String URL_CONCRETE_PROJECT = URL_BASE + "client/project/detail.htm";

        //用户反馈?uid=?&content=?
        public static String URL_FEED_BACK = URL_BASE + "client/fee/addAdvice.htm";

        // 部门编号 参数 uid
        public static String URL_ALL_REPORT_INFO = URL_BASE + "client/sxy/note/list.htm";

        //
        public static String URL_ALL_REPORT_DETAILS = URL_BASE + "client/report/detail.htm";

        //按日期查看日报
        public static String URL_DEPT_REPORT_DETAILS = URL_BASE + "client/report/user/date/get.htm";

        public static String URL_DEPTS = URL_BASE + "client/report/dept/list.htm";

        //通讯录
        public static String URL_TEST_CONTACTS = URL_BASE + "client/sxy/book/list.htm";

        //日报 参数pn：页数；deptid：部门id
        public static String URL_DEPART_REPORT_INFO = URL_BASE + "client/report/dept/date/list.htm";

        // 通知中心
        public static String URL_TEST_INFORM = URL_BASE + "client/index.htm";

        // 提交测试日报审批?
        public static String URL_SUBMIT_EXAMINE2 = URL_BASE
                + "client/report/noticeDaily.htm";

        // 定位
        public static String URL_FIELD_SEND_LOCATION = URL_BASE + "client/location/recordLocation.htm";

        // 定位-上传图片
        public static String URL_FIELD_SEND_IMAGE = URL_BASE + "client/oauth/putimg.htm";

        // 上传文件
        public static String URL_SUBMIT_FILE = URL_BASE + "client/upload.htm";

        // 申请列表
        public static String URL_APPLY_LIST_TEST = URL_BASE + "client/sxy/bpm/business/list.htm";

        // 申请表单
        public static String URL_APPLY_FORM_TEST = URL_BASE + "client/bpm/business/createView.htm";

        // 提交申请表单
        public static String URL_SUBMIT_APPLY_FORM = URL_BASE + "client/bpm/business/create.htm";

        // 审批列表
        public static String URL_EXAMINATION_LIST = URL_BASE + "client/bpm/task/list.htm";

        // 审批历史列表
        public static String URL_EXAMINATIONHISTORY_LIST = URL_BASE + "client/bpm/task/history.htm";

        // 申请列表
        public static String URL_MYAPPLY_LIST = URL_BASE + "client/bpm/apply/list.htm";

        // 申请历史列表
        public static String URL_MYAPPLYHISTORY_LIST = URL_BASE + "client/bpm/event/history.htm";

        // 审批详情
        public static String URL_EXAMINATION_DETAIL = URL_BASE + "client/bpm/task/detail2.htm";

        // 查看申请详情
        public static String URL_CHECK_APPLY_DETAIL = URL_BASE + "client/bpm/task/detail3.htm";

        // 物资审批详情
        public static String URL_EXAMINATION_DETAIL2 = URL_BASE + "client/bpm/task/detail.htm";

        // 物资申请详情
        public static String URL_APPLY_DETAIL2 = URL_BASE + "client/bpm/task/detail4.htm";

        // 审批 同意
        public static String URL_EXAMINATION_AGREE = URL_BASE + "client/bpm/task/ok.htm";//?uid=?&notice=?&taskid=?

        // 审批 打回
        public static String URL_EXAMINATION_BACK = URL_BASE + "client/bpm/task/back.htm";//?uid=?&notice=?&taskid=?

        // 会议 查看成员
        public static String URL_MEETING_ROOM_ON = URL_BASE + "client/chat/room/on.htm";

        // 会议 群组发送会话
        public static String URL_MEETING_SEND_MESSAGE = URL_BASE + "client/chat/im/send.htm";

        // 会议 查看会话信息
        public static String URL_MEETING_CHECK_MESSAGE = URL_BASE + "client/chat/message/send.htm";

        // 会议 添加成员
        public static String URL_MEETING_ROOM_ADD = URL_BASE + "client/chat/room/add.htm";

        // 会议 删除成员
        public static String URL_MEETING_ROOM_DEL = URL_BASE + "client/chat/room/del.htm";

        // 会议 查看历史消息
        public static String URL_MEETING_ON = URL_BASE + "client/chat/on.htm";

        // 会议 查看发言人
        public static String URL_MEETING_LIST_SPEAKER = URL_BASE + "client/chat/im/speaker.htm";

        // 会议 添加发言人
        public static String URL_MEETING_ADD = URL_BASE + "client/chat/add.htm";

        // 会议 添加发言人
        public static String URL_MEETING_SPEAKER_ADD = URL_BASE + "client/chat/im/goMic.htm";

        // 会议 删除发言人
        public static String URL_MEETING_SPEAKER_DEL = URL_BASE + "client/chat/im/downMic.htm";

        // 会议 删除发言人
        public static String URL_MEETING_DEL = URL_BASE + "client/chat/del.htm";

        //车辆列表
        public static String URL_TRAFFIC_LIST = URL_BASE + "client/traffic/record/card.htm";

        //车辆 提交使用记录
        public static String URL_TRAFFIC_SUBMIT = URL_BASE + "client/traffic/record/create.htm";

        //库存
        public static String URL_STOCK_LIST = URL_BASE + "client/inventory/wh/list.htm";

        //库存详情   ?id=1&pno=1"
        public static String URL_STOCK_DETAIL = URL_BASE + "client/inventory/wh/detailWh.htm";

        //月计划列表
        public static String URL_MONTH_PLAN = URL_BASE + "client/workplan/month/my.htm";

        //月计划详情  //?wid=2151
        public static String URL_MONTH_PLAN_DETAILS = URL_BASE + "client/workplan/month/my/detail.htm";

        //周计划列表
        public static String URL_WEEK_PLAN = URL_BASE + "client/workplan/week/my.htm";

        //周计划详情  //?wid=2151
        public static String URL_WEEK_PLAN_DETAILS = URL_BASE + "client/workplan/week/my/detail.htm";

        //月计划审批页 //?uid=65&pno=1&type=1
        public static String URL_MONTH_PLAN_APPLY = URL_BASE + "client/workplan/month/apply/list.htm";

        //月计划审批 通过或打回接口 //wid type status uid
        public static String URL_MONTH_PLAN_AGREE = URL_BASE + "client/workplan/month/apply.htm";

        //周计划审批页 //?uid=65&pno=1&type=1
        public static String URL_WEEK_PLAN_APPLY = URL_BASE + "client/workplan/week/apply/list.htm";

        //周计划审批 通过或打回接口 //wid  status uid
        public static String URL_WEEK_PLAN_AGREE = URL_BASE + "client/workplan/week/apply.htm";

        //工程列表
        public static String URL_PROJECT_FEAT = URL_BASE + "client/project/feat/list.htm";

        //众筹工程列表
        public static String URL_ZC_PROJECT = URL_BASE + "client/zc/project/list.htm";

        //众筹工程详情  //uid id
        public static String URL_ZC_PROJECT_DETAIL = URL_BASE + "client/zc/project/detail.htm";

        //众筹主页信息 //uid
        public static String URL_ZC_MAIN = URL_BASE + "client/zc/project/index.htm";

        //众筹账单明细 //pno
        public static String URL_ZC_BILL = URL_BASE + "client/zc/project/apply/list.htm";

        //众筹收益列表 uid
        public static String URL_ZC_INCOME = URL_BASE + "client/zc/user/sy/list.htm";

        //众筹动态列表
        public static String URL_ZC_NEWS = URL_BASE + "client/zc/news/list.htm";

        //打卡详情
        public static String URL_DK_DETAILS = URL_BASE + "client/check/index.htm";

        //确认打卡
        public static String URL_DK_ADD = URL_BASE + "client/check/add.htm";

        //打卡历史
        public static String URL_DK_HISTORY_LIST = URL_BASE + "client/check/history.htm";

        //车俩位置
        public static String URL_CAR_LOCATION_LIST = URL_BASE + "client/car/online.htm";

        //车俩轨迹
        public static String URL_CAR_GUIJI = URL_BASE + "client/car/history.htm";

        //车俩列表
        public static String URL_CAR_LIST = URL_BASE + "client/car/list.htm";

        //日报保存
        public static String URL_SAVE_REPORT = URL_BASE + "client/report/daily/sendDailyReportAuto.htm";

        //密码修改
        public static String URL_CHANGE_PASSWORD = URL_BASE + "client/av/avd.htm";

        //私车公用 开始上传
        public static String URL_CAR_PERSONAL_START = URL_BASE + "client/car/personal/start.htm";

        //私车公用 上传坐标
        public static String URL_CAR_PERSONAL_UPLOAD = URL_BASE + "client/car/personal/record.htm";

        //私车公用 记录列表
        public static String URL_CAR_PERSONAL_HISTORY_LIST = URL_BASE + "client/car/personal/history.htm?uid=";

        //私车公用 轨迹记录
        public static String URL_CAR_PERSONAL_HISTORY = URL_BASE + "client/car/personal/gps.htm";

        //私车公用 轨迹记录
        public static String URL_OFFLINE_MESSAGE_LIST = URL_BASE + "client/chat/im/offline/message.htm";

        //工作任务 打回
        public static String URL_WORKTAST_DISARGEE = URL_BASE + "client/task/backApply.htm";

        //众筹数据url
        public static String URL_ZHONGCHOU_LIST = URL_BASE + "client/yj/fgsyj.htm";

        //语音会议提醒
        public static String URL_MEETTING_TIP = URL_BASE + "client/apphello.htm";

        // 忘记密码
        public static String URL_FORGET_PASSWORD = URL_BASE + "client/sxy/user/upp.htm";

        // 修改信息
        public static String URL_UPDATE_INFO = URL_BASE + "client/sxy/user/info/update.htm";

        // 首页数据
        public static String URL_TODOLIST_DATA = URL_BASE + "client/team/index.htm";

        // 拖动
        public static String URL_TODOLIST_TUODONG = URL_BASE + "client/team/todo/sort/update.htm";

        // 任务详情
        public static String URL_TODO_TASKDETAIL = URL_BASE + "client/team/todo/detail.htm";

        // 修改子任务
        public static String URL_UPDATE_ZIRENWU = URL_BASE + "client/team/todo/sub/update.htm";

        // 修改子任务
        public static String URL_UPDATE_TITLE = URL_BASE + "client/team/todo/title/set.htm";

        // 增加子任务
        public static String URL_ADD_ZIRENWU = URL_BASE + "client/team/todo/sub/add.htm";

        // 修改非子任务
        public static String URL_UPDATE_FEIZIRENWU = URL_BASE +"client/team/todo/content/update.htm";

        // 设置取消子任务
        public static String URL_SET_ZIRENWU = URL_BASE +"client/team/todo/sub/set.htm";

        // 任务评论
        public static String URL_TASK_PINGLUN = URL_BASE +"client/team/todo/comment/add.htm";

        // 任务点赞
        public static String URL_TASK_DIANZAN = URL_BASE +"client/team/todo/like/add.htm";

        // 任务收藏
        public static String URL_TASK_SHOUCANG = URL_BASE +"client/team/todo/focus/add.htm";

        // 子任务提交
        public static String URL_TASK_SUBMIT = URL_BASE +"client/team/todo/sub/complete.htm";

        // 主任务提交
        public static String URL_ZHUTASK_SUBMIT = URL_BASE +"client/team/todo/complete.htm";

        // 获取应用列表
        public static String URL_LIST_APPLICATION = URL_BASE +"client/team/app/list.htm";

        // 获取搜索列表
        public static String URL_LIST_SEARCH = URL_BASE +"client/team/book/search.htm";

        // 创建主任务
        public static String URL_CREAT_TASK = URL_BASE +"client/team/todo/add.htm";

        // 设置任务级别
        public static String URL_SET_RANK = URL_BASE +"client/team/todo/rank/set.htm";

        // 删除评论
        public static String URL_DEL_PINGLUN = URL_BASE +"client/team/todo/comment/remove.htm";

        // 任务时间选择
        public static String URL_TIME_SELECTOR = URL_BASE +"client/team/todo/time/set.htm";

        // 个人设置
        public static String URL_PERSON_SETTING = URL_BASE +"client/team/person/panel.htm";

        // 图片列表
        public static String URL_LIST_URL = URL_BASE +"app/lbt.json";

        // 注册第一步
        public static String URL_REGISTER_1 = URL_BASE +"client/reg1.htm";

        // 注册第二步
        public static String URL_REGISTER_2 = URL_BASE +"client/reg2.htm";

        // 忘记密码第一步
        public static String URL_FORGET_1 = URL_BASE +"client/forget1.htm";

        // 忘记密码第二步
        public static String URL_FORGET_2 = URL_BASE +"client/forget2.htm";

        // 创建团队
        public static String URL_GREATTEAM = URL_BASE +"client/team/add.htm";

        // 加入团队
        public static String URL_ADD_TEAM = URL_BASE +"client/team/join.htm";

        // 搜索可以加入的团队列表
        public static String URL_LIST_TEAM = URL_BASE +"client/team/s.htm";

        // 用户信息
        public static String URL_USER_INFO = URL_BASE +"client/team/company/list.htm";
    }

}
