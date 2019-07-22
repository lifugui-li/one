package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;

import java.util.List;

public interface CheckGroupService {
    /**
     * 添加检查组
     * @param checkGroup
     * @param checkitemIds
     */
    void add(CheckGroup checkGroup, Integer[] checkitemIds);

    /**
     * 分页条件查询
     * @param queryPageBean
     * @return
     */
    PageResult<CheckGroup> findPage(QueryPageBean queryPageBean);

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
     * 修改检查组
     * @param checkGroup
     * @param checkitemIds
     */
    void update(CheckGroup checkGroup, Integer[] checkitemIds);

    /**
     * 获取检查组所有数据
     * @return
     */
    List<CheckGroup> findAll();
}
