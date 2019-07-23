package com.neo.springlisten;

import java.io.Serializable;

/**
 * @author aa
 */
public class MicroService implements Serializable {
    private static final long serialVersionUID = -1513079165016L;
    private String name;
    private String className;
    private Integer status;
    private String statusDesc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusDesc() {
        if (status == null) {
            statusDesc = "";
        } else {
            switch (status.intValue()) {
                case AbstractService.CREATED:
                    statusDesc = "已创建";
                    break;
                case AbstractService.INITIALIZED:
                    statusDesc = "已初始化";
                    break;
                case AbstractService.RUNNING:
                    statusDesc = "运行中";
                    break;
                case AbstractService.PAUSED:
                    statusDesc = "已暂停";
                    break;
                case AbstractService.STOPED:
                    statusDesc = "已停止";
                    break;
                case AbstractService.DESTROYED:
                    statusDesc = "已销毁";
                    break;
            }
        }
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
