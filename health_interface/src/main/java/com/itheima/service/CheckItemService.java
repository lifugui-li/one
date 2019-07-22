package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckItem;

import java.util.List;

/**
 * 检查项接口
 */
public interface CheckItemService {
    /**
     * 新增检查项
     * @param checkItem
     */
    void add(CheckItem checkItem);

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    PageResult<CheckItem> findPage(QueryPageBean queryPageBean);

    /**
     * 通过编号删除
     * @param id
     */
    void deleteById(int id);

    /**
     * 通过编号查询
     * @param id
     * @return
     */
    CheckItem findById(int id);

    /**
     * 更新
     * @param checkItem
     */
    void update(CheckItem checkItem);

    /**
     * 查询所有检查项列表
     * @return
     */
    List<CheckItem> findAll();
}
