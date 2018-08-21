package com.rentian.newoa.common.view.timeline.bean;

/**
 * 时间轴子项点的类型
 */
public class TimeLineItemType {
    //正常
    public final static int NORMAL = 0;
    //开始
    public final static int START = 1;
    //结束
    public final static int END = 2;
    //只有一条数据,那么beginLine和endLine都没有
    public final static int ATOM = 3;
}
