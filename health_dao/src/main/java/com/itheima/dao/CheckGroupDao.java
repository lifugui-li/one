package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CheckGroupDao {
    /**
     * 添加检查组
     * @param checkGroup
     */
    void add(CheckGroup checkGroup);

    /**
     * 设置检查组与检查项的关系
     * @param checkGroupId
     * @param checkitemId
     */
    void setCheckGroupAndCheckItem(@Param("checkGroupId") Integer checkGroupId, @Param("checkitemId") Integer checkitemId);

    /**
     * 条件查询
     * @param queryString
     * @return
     */
    Page<CheckGroup> findByCondition(String queryString);

    /**
     * 通过编号查询检查组
     * @param id
     * @return
     */
    CheckGroup findById(int id);

    /**
     * 通过检查组的编号查询选中的检查项编号
     * @param id
     * @return
     */
    List<Integer> getCheckItemIds(int id);

    /**
     * 更新检查组
     * @param checkGroup
     */
    void update(CheckGroup checkGroup);

    /**
     * 通过检查组的编号删除检查组与检查项的关系
     * @param checkGroupId
     */
    void deleteCheckGroupCheckItemByCheckGroupId(int checkGroupId);

    /**
     * 获取检查组所有数据
     * @return
     */
    List<CheckGroup> findAll();
}
