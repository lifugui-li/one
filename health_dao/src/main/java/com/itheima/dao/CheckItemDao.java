package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckItem;

import java.util.List;

public interface CheckItemDao {

    /**
     * 新增检查项
     * @param checkItem
     */
    void add(CheckItem checkItem);

    /**
     * 按条件查询
     * @param queryString
     * @return
     */
    Page<CheckItem> findByCondition(String queryString);

    /**
     * 通过检查项编号查询是否被检查组使用了
     * @param id
     * @return
     */
    int countByCheckItemId(int id);

    /**
     * 通过编号删除检查项
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
