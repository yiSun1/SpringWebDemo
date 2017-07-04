package com.yisun.web.domain;
import java.io.Serializable;
import java.util.Date;


/**
 * 所有domain对象的基类
 *
 * @author <a href="mailto:winstonvip@gmail.com">winston</a>
 * @version Created by IntelliJ IDEA.
 *          Date: 2010-3-17 21:31
 * @since 1.0
 */
public abstract class BaseObject implements Serializable {

    /**
     * 创建时间
     */
    protected Date createTime;

    /**
     * 最后修改时间
     */
    protected Date lastModifyTime;

    /**
     * 版本号。每次更新+1
     */
    protected int version;

    /**
     * Returns a multi-line String with key=value pairs.
     *
     * @return a String representation of this class.
     */
    public abstract String toString();

    /**
     * Compares object equality. When using Hibernate, the primary key should
     * not be a part of this comparison.
     *
     * @param o object to compare to
     * @return true/false based on equality tests
     */
    public abstract boolean equals(Object o);

    /**
     * When you override equals, you should override hashCode. See "Why are
     * equals() and hashCode() importation" for more information:
     * http://www.hibernate.org/109.html
     *
     * @return hashCode
     */
    public abstract int hashCode();

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}

